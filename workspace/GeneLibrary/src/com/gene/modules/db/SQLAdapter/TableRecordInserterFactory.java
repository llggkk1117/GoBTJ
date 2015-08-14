package com.gene.modules.db.SQLAdapter;

import java.sql.SQLException;

import com.gene.modules.db.SQLExecutor.SQLExecutorFactory;

public class TableRecordInserterFactory
{
	private TableRecordInserterFactory(){}
	
	public static synchronized TableRecordInserter getInstance(String driver, String URL, String username, String password) throws InterruptedException, ClassNotFoundException, SQLException
	{
		TableRecordInserter tableRecordInserter = new TableRecordInserter();
		tableRecordInserter.setSQLExecutor(SQLExecutorFactory.getInstance(driver, URL, username, password));
		
		return tableRecordInserter;
	}
}
