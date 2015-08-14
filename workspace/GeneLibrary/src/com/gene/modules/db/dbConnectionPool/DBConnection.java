package com.gene.modules.db.dbConnectionPool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.gene.modules.check.Check;
import com.gene.modules.exceptions.MissingDependancyException;


public class DBConnection
{
	private Connection connection;
	private Statement statement;
	private ConnectionPool connectionPool;
	private boolean autoCommitDefault;
	
	public DBConnection(Connection connection, ConnectionPool connectionPool) throws SQLException
	{
		Check.notNull(connection, connectionPool);
		this.connection = connection;
		this.autoCommitDefault = this.connection.getAutoCommit();
		this.connection.setAutoCommit(false);
		this.statement = this.connection.createStatement();
		this.connectionPool = connectionPool;
	}
	
	
	protected void finalize() throws Throwable
	{
	    try
	    {
	    	this.close();
	    }
	    finally
	    {
	        super.finalize();
	    }
	}
	
	
	
	/**
	 * 
	 * @param sql
	 * @return never be null
	 * @throws SQLException
	 */
	public synchronized ResultSet executeQuery(String sql) throws SQLException
	{
		Check.notBlank(sql);
		Check.notNull(MissingDependancyException.class, this.statement);
			
		return this.statement.executeQuery(sql);
	}
	
	
	

	
	public synchronized int executeUpdate(String sql) throws SQLException
	{
		Check.notBlank(IllegalArgumentException.class, sql);
		Check.notNull(MissingDependancyException.class, this.statement);
		
		return this.statement.executeUpdate(sql);
	}
	
	
	
	
	
	public synchronized void commit() throws SQLException
    {
		Check.notNull(MissingDependancyException.class, this.connection);
		
		this.connection.commit();
    }
	
	public synchronized void rollback() throws SQLException
    {
		Check.notNull(MissingDependancyException.class, this.connection);
		
		this.connection.rollback();
    }
	
	
	
	public synchronized void close() throws SQLException
	{
		this.rollback();
		
		if(this.statement != null)
		{
			this.statement.close();
			this.statement = null;
		}
		
		if((this.connection != null)&&(this.connectionPool != null))
		{
			this.connection.setAutoCommit(this.autoCommitDefault);
			this.connectionPool.releaseConnection(this.connection);
		}
		else if(this.connection != null)
		{
			this.connection.close();
			this.connection = null;
		}
		else if(this.connectionPool != null)
		{
			this.connectionPool = null;
		}
	}
	
	
	public synchronized void terminate() throws SQLException
	{
		if(this.statement != null)
		{
			this.statement.close();
			this.statement = null;
		}
		
		this.connectionPool.destroyConnection(this.connection);
		this.connection = null;
		
		this.connectionPool = null;
	}
	
	
	public synchronized Connection getConnection()
	{
		return this.connection;
	}
	

	public synchronized Statement getStatement()
	{
		return this.statement;
	}
}
