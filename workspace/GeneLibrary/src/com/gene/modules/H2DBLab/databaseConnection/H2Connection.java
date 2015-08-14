package com.gene.modules.H2DBLab.databaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class H2Connection implements DatabaseConnection
{
	private Connection connection;
	private Statement statement;
	
	public H2Connection(Connection connection) throws SQLException
	{
		this.connection = connection;
		connection.setAutoCommit(false);
		this.statement = this.connection.createStatement();
	}
	
	protected void finalize() throws Throwable
	{
	    try
	    {
	    	close();
	    }
	    finally
	    {
	        super.finalize();
	    }
	}

	public void close() throws SQLException
	{
		if(this.statement != null)
		{
			this.statement.close();
			this.statement = null;
		}
		
		if(this.connection != null)
		{
			this.connection.close();
			this.connection = null;
		}
	}

	
	public ResultSet executeQuery(String sql) throws SQLException
	{
		ResultSet result = null;
		if(statement != null)
		{
			result = statement.executeQuery(sql);
		}
		return result;
	}
	
	public Integer executeUpdate(String sql) throws SQLException
	{
		Integer result = null;
		if(statement != null)
		{
			result = statement.executeUpdate(sql);
		}
		return result;
	}
	
	public void commit() throws SQLException
    {
    	if(connection != null)
    	{
    		connection.commit();
    	}
    }
}

