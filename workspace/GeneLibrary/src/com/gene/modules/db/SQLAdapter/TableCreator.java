package com.gene.modules.db.SQLAdapter;

import java.io.IOException;
import java.sql.SQLException;


import com.gene.modules.db.SQLExecutor.OldSQLExecutor;
import com.gene.modules.db.SQLExecutor.SQLExecutorFactory;





public class TableCreator
{
	private OldSQLExecutor sqlExecutor;

	
	public synchronized void setSQLExecutor(OldSQLExecutor sqlExecutor)
	{
		if(sqlExecutor == null)
		{
			throw new IllegalArgumentException();
		}
		
		this.sqlExecutor = sqlExecutor;
	}
	
	public synchronized OldSQLExecutor getSQLExecutor()
	{
		return this.sqlExecutor;
	}
	
	private synchronized boolean checktableAlreadyExists(TableInfo tableInfo) throws SQLException, InterruptedException, ClassNotFoundException
	{
		boolean tableExists = false;
		String sql_check = "SELECT TABLE_NAME FROM USER_TABLES WHERE TABLE_NAME = '"+tableInfo.getTableName().toUpperCase()+"'";
		
//		For test
//		System.out.println(sql_check);
		
		this.sqlExecutor.openSession();
		String[][] result = (String[][]) this.sqlExecutor.execute(sql_check);
		this.sqlExecutor.closeSession();
		
//		For test
//		System.out.println(result.length);
//		for(int i=0; i<result.length; ++i)
//		{
//			for(int j=0; j<result[i].length; ++j)
//			{
//				System.out.println(result[i][j]);
//			}
//		}
		
		if(result.length > 0)
		{
			tableExists = true;
		}
		
		return tableExists;
	}
	
	
	
	private static synchronized String generateCreateTableSQL(TableInfo tableInfo)
	{
		String tableName = tableInfo.getTableName().toUpperCase();
		String[] columnNames = tableInfo.getColumnNames();

		String sql_create = "CREATE TABLE "+tableName+" (";
		
		ColumnInfo tableColumn = null;
		for(int i=0; i<columnNames.length; ++i)
		{
			tableColumn = tableInfo.getColumnInfo(columnNames[i]);
			if(tableColumn != null)
			{
				sql_create += columnNames[i];
				sql_create += " "+ tableColumn.getDataType();
					
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
			sql_create += ", CONSTRAINT "+tableName.toUpperCase()+"_PK PRIMARY KEY (";
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
		
//		For test
		System.out.println(sql_create);
		
		return sql_create;
	}
	
	
	
	public synchronized int createTable(TableInfo tableInfo) throws SQLException, InterruptedException, NumberFormatException, ClassNotFoundException 
	{
		int result = -1;
		if(!this.checktableAlreadyExists(tableInfo))
		{
			this.sqlExecutor.openSession();
			String[][] restulTemp = (String[][]) this.sqlExecutor.execute(TableCreator.generateCreateTableSQL(tableInfo));
			this.sqlExecutor.commit();
			this.sqlExecutor.closeSession();
			result = Integer.parseInt(restulTemp[0][0]);
		}
		
		return result;
	}
	
	
	public static void main(String[] args) throws InterruptedException, ClassNotFoundException, SQLException, IOException  
	{
//		SQLExecutor sqlExecutor = new SQLExecutor();
//		String url = "jdbc:oracle:thin:@localhost:1521/xe";
//		String id = "sdc-local";
//		String pw = "1234";
//		OraclePooledConnectionGenerator cg = new OraclePooledConnectionGenerator(url, id, pw);
//		GeneralScriptExecuter se = new GeneralScriptExecuter();
//		sqlExecutor.setDatabaseConnectionGenerator(cg);
//		sqlExecutor.setScriptExecuter(se);
		
		TableCreator tc = new TableCreator();
		tc.setSQLExecutor(SQLExecutorFactory.getInstance());
		tc.createTable(SSDExceptionTableInfoGenerator.getSSDExceptionTableInfo());
//		System.out.println(tc.getSQL_ToCreatTable());
//		System.out.println(tc.tableAlreadyExists());
	}
}
