package com.gene.modules.db.SQLAdapter;

import java.sql.SQLException;

import com.gene.modules.db.SQLExecutor.SQLExecutorFactory;


public class TableCreatorFactory
{
	private TableCreatorFactory(){}
	
	public static synchronized TableCreator getInstance(String driver, String URL, String username, String password) throws InterruptedException, ClassNotFoundException, SQLException
	{
		TableCreator tableCreator = new TableCreator();
		tableCreator.setSQLExecutor(SQLExecutorFactory.getInstance(driver, URL, username, password));
		
		return tableCreator;
	}
}
