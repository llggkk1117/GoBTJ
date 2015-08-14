package com.gene.modules.db.tableManagement;

import java.io.IOException;
import java.sql.SQLException;

import com.gene.modules.exceptions.InstanceAlreadyExistException;
import com.gene.modules.exceptions.InstanceNotExistException;
import com.gene.modules.exceptions.InvalidConnectionException;
import com.gene.modules.check.Check;
import com.gene.modules.db.SQLExecutor.SQLExecutor;
import com.gene.modules.db.SQLExecutor.SQLExecutorFactory;
import com.gene.modules.db.tableManagement.info.ColumnInfo;
import com.gene.modules.db.tableManagement.info.TableInfo;
import com.gene.modules.db.utils.Constants.DB;







public class TableInfoLoader
{
	private int databaseType;
	private SQLExecutor sqlExecutor;
	
	public TableInfoLoader(){}
	
	public TableInfoLoader(SQLExecutor sqlExecutor)
	{
		this.setSQLExecutor(sqlExecutor);
	}

	public synchronized TableInfo getTableInfo(String tableName) throws SQLException, InterruptedException, IOException, ClassNotFoundException
	{
		String columnInfoSQL = null;
		String primaryKeyCheckSQL = null;
		String schemeName = null;
		String nullableNo = null;
		if(this.databaseType == DB.Type.postgresql)
		{
			columnInfoSQL = DB.SQL.Postgresql.columnInfoSQL;
			primaryKeyCheckSQL = DB.SQL.Postgresql.primaryKeyCheckSQL;
			schemeName = "public";
			nullableNo = DB.Variable.Postgresql.nullableNo;
		}
		else //if(this.databaseType == DB_TYPE.ORACLE)
		{
			columnInfoSQL = DB.SQL.Oracle.columnInfoSQL;
			primaryKeyCheckSQL = DB.SQL.Oracle.primaryKeyCheckSQL;
			schemeName = this.sqlExecutor.getDBConnectionPool().getUsername();
			nullableNo = DB.Variable.Oracle.nullableNo;
		}
		

		
		final int COLUMN_NAME = 0;
		final int DATA_TYPE = 1;
		final int DATA_LENGTH = 2;
		final int NULLABLE = 3;
		
		tableName = tableName.toLowerCase();
		String[][] columns = (String[][]) this.sqlExecutor.execute(columnInfoSQL, schemeName, tableName);

		TableInfo tableInfo = null;
		if(columns.length > 1)
		{
			tableInfo = new TableInfo(tableName);
			
			for(int i=1; i<columns.length; ++i)
			{
				String columnName = columns[i][COLUMN_NAME].toLowerCase();
				String dataType = columns[i][DATA_TYPE].toLowerCase();
				if("varchar2".equalsIgnoreCase(dataType) &&(!Check.isBlank(columns[i][DATA_LENGTH])))
				{
					dataType += " ("+columns[i][DATA_LENGTH]+" byte)";
				}
				
				boolean isPrimaryKey = (((String[][]) this.sqlExecutor.execute(primaryKeyCheckSQL, schemeName, tableName, columnName)).length > 1);
				boolean isNotNull = isPrimaryKey || nullableNo.equalsIgnoreCase(columns[i][NULLABLE]);
				
				tableInfo.addColumnInfo(columnName, dataType, isNotNull, isPrimaryKey);
				
				System.out.println(columnName);
				System.out.println(dataType);
				System.out.println(isNotNull);
				System.out.println(isPrimaryKey);
			}
		}
		
		return tableInfo;
	}
	
	
	
	public synchronized boolean checkTableExist(String tableName) throws SQLException, InterruptedException, ClassNotFoundException
	{
		Check.notBlank(tableName);
		
		String tableExistCheckSQL = null;
		if(this.databaseType == DB.Type.postgresql)
		{
			tableExistCheckSQL = DB.SQL.Postgresql.tableExistCheckSQL;
		}
		else if(this.databaseType == DB.Type.oracle)
		{
			tableExistCheckSQL = DB.SQL.Oracle.tableExistCheckSQL;
		}
		
		boolean tableExists = (((String[][])this.sqlExecutor.execute(tableExistCheckSQL, tableName.toLowerCase())).length > 1);
		
		return tableExists;
	}
	
	
	
	public void setSQLExecutor(SQLExecutor sqlExecutor)
	{
		this.sqlExecutor = sqlExecutor;
		String driver = this.sqlExecutor.getDBConnectionPool().getDriver().toLowerCase();
		if(driver.contains("postgresql"))
		{
			this.databaseType = DB.Type.postgresql;
		}
		else //if(driver.contains("oracle"))
		{
			this.databaseType = DB.Type.oracle;
		}
	}
	
	
	public SQLExecutor getSQLExecutor()
	{
		return this.sqlExecutor;
	}
	
	
	
	public static void main(String[] args) throws IOException, InstanceAlreadyExistException, InvalidConnectionException, SQLException, InterruptedException, ClassNotFoundException, InstanceNotExistException
	{
		TableInfoLoader t = new TableInfoLoader(SQLExecutorFactory.getInstance());
		TableInfo info = t.getTableInfo("temp1");
		
		System.out.println(info.getTableName()+"===============");
		
		String[] everyColumn = info.getColumnNames();
		System.out.println(everyColumn.length);
		
		
		for(int i=0; i<everyColumn.length; ++i)
		{
			ColumnInfo columnInfo = info.getColumnInfo(everyColumn[i]);
			System.out.println(columnInfo.getColumnName()+": "+columnInfo.getDataType()+": "+columnInfo.isNotNull()+": "+columnInfo.isPrimaryKey());
		}
	}
	
	@SuppressWarnings("unused")
	private static void showResult(Object result)
	{
		if(result instanceof String[][])
		{
			String[][] res = (String[][]) result;
			for(int i=0; i<res.length; ++i)
	    	{
	    		for(int j=0; j<res[i].length; j++)
	    		{
	    			System.out.print(res[i][j]+"\t");
	    		}
	    		System.out.print("\n");
	    	}
		}
		else if(result instanceof Integer)
		{
			Integer res = (Integer) result;
			System.out.println(res);
		}
	}
}
