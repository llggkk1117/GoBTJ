package com.gene.modules.db.tableManagement;

import java.io.IOException;
import java.sql.SQLException;

import com.gene.modules.db.SQLExecutor.SQLExecutorFactory;

public class TableInfoLibraryFactory
{
	private TableInfoLibraryFactory(){}
	
	public static TableInfoLibrary getInstance() throws InterruptedException, ClassNotFoundException, SQLException, IOException 
	{
		// Setting up TableInfoLoader
		TableInfoLoader tableInfoLoader = new TableInfoLoader();
		tableInfoLoader.setSQLExecutor(SQLExecutorFactory.getInstance());
		
		TableInfoLibrary tableInfoLibrary = new TableInfoLibrary();
		tableInfoLibrary.setTableInfoLoader(tableInfoLoader);
		
		return tableInfoLibrary;
	}
	
	public static TableInfoLibrary getInstance(String driver, String URL, String username, String password) throws InterruptedException, ClassNotFoundException, SQLException 
	{
		// Setting up TableInfoLoader
		TableInfoLoader tableInfoLoader = new TableInfoLoader();
		tableInfoLoader.setSQLExecutor(SQLExecutorFactory.getInstance(driver, URL, username, password));
		
		TableInfoLibrary tableInfoLibrary = new TableInfoLibrary();
		tableInfoLibrary.setTableInfoLoader(tableInfoLoader);
		
		return tableInfoLibrary;
	}
}
