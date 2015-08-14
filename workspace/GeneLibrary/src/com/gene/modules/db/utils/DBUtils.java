package com.gene.modules.db.utils;

import java.io.IOException;
import java.sql.SQLException;

import com.gene.modules.check.Check;
import com.gene.modules.db.SQLExecutor.SQLExecutor;
import com.gene.modules.db.tableManagement.info.ColumnInfo;
import com.gene.modules.db.tableManagement.info.TableInfo;
import com.gene.modules.db.tableManagement.table.TableData;
import com.gene.modules.db.utils.Constants.DB;


public class DBUtils
{
	public static boolean checkTableExist(SQLExecutor sqlExecutor, String tableName) throws SQLException, InterruptedException, ClassNotFoundException
	{
		Check.notBlank(tableName);
		
		int databaseType = DBUtils.getDBType(sqlExecutor.getDBConnectionPool().getURL());
		
		String tableExistCheckSQL = null;
		if(databaseType == DB.Type.postgresql)
		{
			tableExistCheckSQL = DB.SQL.Postgresql.tableExistCheckSQL;
		}
		else //if(databaseType == DB_CONSTANTS.DB_TYPE.ORACLE)
		{
			tableExistCheckSQL = DB.SQL.Oracle.tableExistCheckSQL;
		}
		
		boolean tableExists = !((TableData)sqlExecutor.execute(tableExistCheckSQL, tableName.toLowerCase())).isEmpty();
		
		return tableExists;
	}
	
	public static int getDBType(String URL)
	{
		Check.notBlank(URL);
		
		URL = URL.toLowerCase();
		int databaseType = 0;
		if(URL.contains("jdbc:oracle"))
		{
			databaseType = DB.Type.oracle;
		}
		else //if(URL.contains("jdbc:postgresql"))
		{
			databaseType = DB.Type.postgresql;
		}
		
		return databaseType;
	}
	
	
	public static String getCreatTableSQL(TableInfo tableInfo)
	{
		String tableName = tableInfo.getTableName();
		String[] columnNames = tableInfo.getColumnNames();

		String sql_create = "CREATE TABLE "+tableName+" (";
		
		ColumnInfo tableColumn = null;
		for(int i=0; i<columnNames.length; ++i)
		{
			tableColumn = tableInfo.getColumnInfo(columnNames[i]);
			if(tableColumn != null)
			{
				sql_create += columnNames[i];
				String dataType = tableColumn.getDataType();
				sql_create += " "+ ((dataType != null) ? dataType.toLowerCase() : "VARCHAR2 (20 CHAR)");
					
				if(tableColumn.isNotNull())
				{
					sql_create += " NOT NULL ENABLE";
				}
				
				if(i < columnNames.length-1)
				{
					sql_create += ", ";
				}
			}
		}
		
		
		//find out how many primary keys exist
		int numOfPrimaryKeys = 0;
		for(int i=0; i<columnNames.length; ++i)
		{
			tableColumn = tableInfo.getColumnInfo(columnNames[i]);
			if(tableColumn != null)
			{
				if(tableColumn.isPrimaryKey())
				{
					numOfPrimaryKeys++;
				}
			}
		}
		

		if(numOfPrimaryKeys > 0)
		{
			int numOfPrimaryKeys_temp = numOfPrimaryKeys;
			sql_create += ", CONSTRAINT "+tableName+"_pk PRIMARY KEY (";
			for(int i=0; i<columnNames.length; ++i)
			{
				tableColumn = tableInfo.getColumnInfo(columnNames[i]);
				if(tableColumn != null)
				{
					if(tableColumn.isPrimaryKey())
					{
						sql_create += columnNames[i];
						numOfPrimaryKeys_temp--;
						if(numOfPrimaryKeys_temp > 0)
						{
							sql_create += ", ";
						}
					}
				}
			}
			sql_create+=")";
		}
		
		sql_create += ")";
		
		
		return sql_create;
	}
	
	
	public static TableInfo getTableInfo(SQLExecutor sqlExecutor, String tableName) throws SQLException, InterruptedException, IOException, ClassNotFoundException
	{
		String columnInfoSQL = null;
		String primaryKeyCheckSQL = null;
		String schemeName = null;
		String nullableNo = null;
		int databaseType = DBUtils.getDBType(sqlExecutor.getDBConnectionPool().getURL());
		if(databaseType == DB.Type.postgresql)
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
			schemeName = sqlExecutor.getDBConnectionPool().getUsername();
			nullableNo = DB.Variable.Oracle.nullableNo;
		}
		
		final int COLUMN_NAME = 0;
		final int DATA_TYPE = 1;
		final int DATA_LENGTH = 2;
		final int NULLABLE = 3;
		
		tableName = tableName.toLowerCase();
		String[][] columns = ((TableData) sqlExecutor.execute(columnInfoSQL, schemeName, tableName)).getAllTuples();

		TableInfo tableInfo = null;
		if(columns.length > 0)
		{
			tableInfo = new TableInfo(tableName);
			
			for(int i=0; i<columns.length; ++i)
			{
				String columnName = columns[i][COLUMN_NAME].toLowerCase();
				String dataType = columns[i][DATA_TYPE].toLowerCase();
				if("VARCHAR2".equalsIgnoreCase(dataType) &&(!Check.isBlank(columns[i][DATA_LENGTH])))
				{
					dataType += " ("+columns[i][DATA_LENGTH]+" BYTE)";
				}
				
				boolean isPrimaryKey = !((TableData) sqlExecutor.execute(primaryKeyCheckSQL, schemeName, tableName, columnName)).isEmpty();
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
	
	
	public static String getDriver(String URL)
	{
		Check.notBlank(URL);
		
		int dbType = getDBType(URL);
		String driver = null;
		if(dbType == DB.Type.oracle)
		{
			driver = DB.Driver.oracle;
		}
		else if(dbType == DB.Type.postgresql)
		{
			driver = DB.Driver.postgresql;
		}
		
		return driver;
	}
	
	
	public static void showResult(Object result)
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
