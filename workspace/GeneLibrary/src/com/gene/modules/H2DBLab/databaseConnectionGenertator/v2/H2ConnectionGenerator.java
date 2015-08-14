package com.gene.modules.H2DBLab.databaseConnectionGenertator.v2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import com.gene.modules.exceptions.InstanceAlreadyExistException;
import com.gene.modules.exceptions.InvalidConnectionException;
import com.gene.modules.H2DBLab.databaseConnection.DatabaseConnection;
import com.gene.modules.H2DBLab.databaseConnection.H2Connection;



public class H2ConnectionGenerator implements DatabaseConnectionGenerator
{
	private static HashMap<String, H2ConnectionGenerator> instanceRegistry;
	private static Semaphore semaphore;
	static
	{
		instanceRegistry = new HashMap<String, H2ConnectionGenerator>();
		semaphore = new Semaphore();
	}
	
	private String URL;
	private String username;
	private String password;

	
	
	public H2ConnectionGenerator(String URL, String username, String password) throws ClassNotFoundException, InvalidConnectionException, InstanceAlreadyExistException, InterruptedException
	{
		H2ConnectionGenerator.semaphore.acquire();
		
		String key = URL+username+password;
		if(instanceRegistry.get(key) == null)
		{
			this.URL = URL;
			this.username = username;
			this.password = password;
			Class.forName("org.h2.Driver");
			String errorMessage = this.validateConnection();
			
			if(errorMessage == null)
			{
				instanceRegistry.put(key, this);
			}
			else
			{
				this.URL = null;
				this.username = null;
				this.password = null;
				
				throw new InvalidConnectionException(errorMessage);
			}
		}
		else
		{
			throw new InstanceAlreadyExistException("Instance already exists");
		}
		
		H2ConnectionGenerator.semaphore.release();
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
		Connection connection = DriverManager.getConnection(this.URL, this.username, this.password);
		H2Connection h2DatabaseConnection = new H2Connection(connection);
		return h2DatabaseConnection;
	}
	
	
	
	public static synchronized DatabaseConnectionGenerator getInstance(String URL, String username, String password) throws ClassNotFoundException, InvalidConnectionException, InstanceAlreadyExistException, InterruptedException
	{
		String key=URL+username+password;
		H2ConnectionGenerator instance = H2ConnectionGenerator.instanceRegistry.get(key); 
		if(instance == null)
		{
			H2ConnectionGenerator newInstance = new H2ConnectionGenerator(URL, username, password);
			H2ConnectionGenerator.instanceRegistry.put(key, newInstance);
			instance = newInstance;
		}
		return instance;
	}
	
	
	public void close() throws SQLException
	{
		String key=this.URL+this.username+this.password;
		H2ConnectionGenerator.instanceRegistry.remove(key);
	
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