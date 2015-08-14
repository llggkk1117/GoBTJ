package com.gene.modules.db.SQLProcessor;


import java.io.IOException;
import java.sql.SQLException;

import com.gene.modules.check.Check;
import com.gene.modules.db.SQLAdapter.TableCreator;
import com.gene.modules.db.SQLAdapter.TableCreatorFactory;
import com.gene.modules.db.SQLAdapter.TableRecordInserter;
import com.gene.modules.db.SQLAdapter.TableRecordInserterFactory;
import com.gene.modules.db.SQLExecutor.OldSQLExecutor;
import com.gene.modules.db.SQLExecutor.SQLExecutorFactory;
import com.gene.modules.db.connectionPoolAdaptor.DBSource.DBProperties;
import com.gene.modules.db.connectionPoolAdaptor.DBSource.DBSource;
import com.gene.modules.db.table.tableInfoLibrary.TableInfoLibrary;
import com.gene.modules.db.table.tableInfoLibrary.TableInfoLibraryFactory;

public class SQLProcessorFactory
{
	private static final String DEFAULT_DB_PROPERTIES_FILE_PATH = "settings/database.properties";
	
	private SQLProcessorFactory(){}
	
	public static synchronized SQLProcessor getInstance(String driver, String URL, String username, String password) throws InterruptedException, ClassNotFoundException, SQLException  
	{
		if(Check.anyBlankExists(driver, URL, username, password))
		{
			throw new IllegalArgumentException();
		}
		
		OldSQLExecutor sqlExecutor = SQLExecutorFactory.getInstance(driver, URL, username, password);
    	TableInfoLibrary tableInfoLibrary = TableInfoLibraryFactory.getInstance(driver, URL, username, password);
    	TableCreator tableCreator = TableCreatorFactory.getInstance(driver, URL, username, password);
    	TableRecordInserter tableRecordInserter = TableRecordInserterFactory.getInstance(driver, URL, username, password);
    	
		SQLProcessor sqlProcessor = new SQLProcessor();
		sqlProcessor.setSQLExecutor(sqlExecutor);
		sqlProcessor.setTableCreator(tableCreator);
		sqlProcessor.setTableInfoLibrary(tableInfoLibrary);
		sqlProcessor.setTableRecordInserter(tableRecordInserter);
		
		return sqlProcessor;
	}
	
	
	public static synchronized SQLProcessor getInstance(String dbPropertisFileName) throws IOException, InterruptedException, ClassNotFoundException, SQLException
	{
		if(Check.anyBlankExists(dbPropertisFileName))
		{
			throw new IllegalArgumentException();
		}
		
		DBSource dbConnectionInfo = new DBProperties(dbPropertisFileName);
		SQLProcessor sqlProcessor = SQLProcessorFactory.getInstance(dbConnectionInfo.getDriver(), dbConnectionInfo.getURL(), dbConnectionInfo.getUserName(), dbConnectionInfo.getPassword());
		
		return sqlProcessor;
	}
	
	
	public static synchronized SQLProcessor getInstance() throws IOException, InterruptedException, ClassNotFoundException, SQLException
	{
		return SQLProcessorFactory.getInstance(DEFAULT_DB_PROPERTIES_FILE_PATH);
	}
	
	
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException, SQLException
	{
		SQLProcessor sqlProcessor = SQLProcessorFactory.getInstance();
		sqlProcessor.dropTable("YO_JON").execute();
		sqlProcessor.createTable("YO_JON").createColumn("col1", "varchar2(10 char)", false, true).createColumn("col2", "varchar2(15 char)", false, false).execute();
		sqlProcessor.insert("YO_JON").columnAndValue("col1", "aa").columnAndValue("col2", "bb").execute();
		sqlProcessor.insert("YO_JON").columnAndValue("col1", "aa").columnAndValue("col2", "cc").force().execute();
		System.out.println(sqlProcessor.getTableInfoLibrary().checkTableAlreadyExists("YO_JON"));
	}
}
