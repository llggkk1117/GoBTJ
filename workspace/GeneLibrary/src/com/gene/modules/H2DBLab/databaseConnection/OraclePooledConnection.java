package com.gene.modules.H2DBLab.databaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.PooledConnection;

public class OraclePooledConnection implements DatabaseConnection
{
	private PooledConnection pooledConnection;
	private Connection connection;
	private Statement statement;
	
	public OraclePooledConnection(PooledConnection pooledConnection) throws SQLException
	{
		this.pooledConnection = pooledConnection;
		this.connection = this.pooledConnection.getConnection();
		this.connection.setAutoCommit(false);
		this.statement = this.connection.createStatement();
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
		
		if(this.pooledConnection != null)
		{
			this.pooledConnection.close();
			this.pooledConnection = null;
		}
	}
}
