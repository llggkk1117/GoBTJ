package com.gene.modules.H2DBLab.databaseConnectionGenertator.v1;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import org.h2.jdbcx.JdbcConnectionPool;

import com.gene.modules.exceptions.InstanceAlreadyExistException;
import com.gene.modules.exceptions.InvalidConnectionException;
import com.gene.modules.H2DBLab.databaseConnection.DatabaseConnection;
import com.gene.modules.H2DBLab.databaseConnection.H2PooledConnection;




public class H2PooledConnectionGenerator implements DatabaseConnectionGenerator
{
	private static HashMap<String, H2PooledConnectionGenerator> instanceRegistry;
	static
	{
		instanceRegistry = new HashMap<String, H2PooledConnectionGenerator>();
	}
	
	private JdbcConnectionPool connectionPool;
	private String URL;
	private String username;
	private String password;

	
	public H2PooledConnectionGenerator(String URL, String username, String password) throws InvalidConnectionException, InstanceAlreadyExistException
	{
		String key = URL+username+password;
		//if(instanceRegistry.get(key) == null)
		if(H2PooledConnectionGenerator.getInstanceFromRegistry(key) == null)
		{
			connectionPool = JdbcConnectionPool.create(URL, username, password);
			
			String errorMessage = this.validateConnection();
			if(errorMessage == null)
			{
				this.URL = URL;
				this.username = username;
				this.password = password;
				//instanceRegistry.put(key, this);
				H2PooledConnectionGenerator.putInstanceToRegistry(key, this);
			}
			else
			{
				connectionPool.dispose();
				connectionPool = null;
				throw new InvalidConnectionException(errorMessage);
			}
		}
		else
		{
			throw new InstanceAlreadyExistException("Instance already exists");
		}
	}

	
	private static synchronized H2PooledConnectionGenerator getInstanceFromRegistry(String key)
	{
		return instanceRegistry.get(key);
	}
	
	private static synchronized void putInstanceToRegistry(String key, H2PooledConnectionGenerator instance)
	{
		instanceRegistry.put(key, instance);
	}
	
	private static synchronized void removeInstanceFromRegistry(String key)
	{
		instanceRegistry.remove(key);
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
	
	private String validateConnection()
	{
		String errorMessage = null;
		try
		{
			DatabaseConnection connection = getConnection();
			connection.close();
		}
		catch (SQLException e)
		{
			errorMessage = e.getMessage();
		}

		return errorMessage;
	}
	
	
	public DatabaseConnection getConnection() throws SQLException
	{
		Connection connection = this.connectionPool.getConnection();
		H2PooledConnection h2PooledConnection = new H2PooledConnection(connection);
		return h2PooledConnection;
	}
	
	
	public static synchronized DatabaseConnectionGenerator getInstance(String URL, String username, String password) throws InvalidConnectionException, InstanceAlreadyExistException
	{
		String key=URL+username+password;
		H2PooledConnectionGenerator instance = H2PooledConnectionGenerator.instanceRegistry.get(key); 
		if(instance == null)
		{
			H2PooledConnectionGenerator newInstance = new H2PooledConnectionGenerator(URL, username, password);
			H2PooledConnectionGenerator.instanceRegistry.put(key, newInstance);
			instance = newInstance;
		}
		return instance;
	}
	
	
	public void close() throws SQLException
	{
		String key=this.URL+this.username+this.password;
		//H2PooledConnectionGenerator.instanceRegistry.remove(key);
		H2PooledConnectionGenerator.removeInstanceFromRegistry(key);
		
		if(this.connectionPool != null)
		{
			this.connectionPool.dispose();
			this.connectionPool = null;
		}
		this.URL = null;
		this.username = null;
		this.password = null;
	}
	
	public String getURL()
	{
		return this.URL;
	}
	
	public String getUsername()
	{
		return this.username;
	}
}

