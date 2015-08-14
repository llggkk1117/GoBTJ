package com.gene.modules.H2DBLab.databaseConnectionGenertator.v2;

import java.sql.SQLException;
import java.util.HashMap;
import javax.sql.PooledConnection;
import oracle.jdbc.pool.OracleConnectionPoolDataSource;

import com.gene.modules.exceptions.InstanceAlreadyExistException;
import com.gene.modules.exceptions.InvalidConnectionException;
import com.gene.modules.H2DBLab.databaseConnection.DatabaseConnection;
import com.gene.modules.H2DBLab.databaseConnection.OraclePooledConnection;


public class OraclePooledConnectionGenerator implements DatabaseConnectionGenerator
{
	private static HashMap<String, OraclePooledConnectionGenerator> instanceRegistry;
	private static Semaphore semaphore;
	static
	{
		instanceRegistry = new HashMap<String, OraclePooledConnectionGenerator>();
		semaphore = new Semaphore();
	}
	
	private OracleConnectionPoolDataSource ocpds;
	private String URL;
	private String username;
	private String password;
	
	public OraclePooledConnectionGenerator(String URL, String username, String password) throws InstanceAlreadyExistException, InvalidConnectionException, SQLException, InterruptedException 
	{
		OraclePooledConnectionGenerator.semaphore.acquire();
		String key = URL+username+password;
		
		if(instanceRegistry.get(key) == null)
		{
			ocpds = new OracleConnectionPoolDataSource();
			ocpds.setURL(URL);
			ocpds.setUser(username);
			ocpds.setPassword(password);
			
			String errorMessage = this.validateConnection();
			if(errorMessage == null)
			{
				this.URL = URL;
				this.username = username;
				this.password = password;
				instanceRegistry.put(key, this);
			}
			else
			{
				ocpds.close();
				ocpds = null;
				throw new InvalidConnectionException(errorMessage);
			}
		}
		else
		{
			throw new InstanceAlreadyExistException("Instance already exists");
		}
		OraclePooledConnectionGenerator.semaphore.release();
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
	
	private synchronized String validateConnection()
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
	
	
	public synchronized DatabaseConnection getConnection() throws SQLException
	{
		PooledConnection pooledConnection = ocpds.getPooledConnection();
		OraclePooledConnection oraclePooledConnection = new OraclePooledConnection(pooledConnection);
		return oraclePooledConnection;
	}
	
	
	public static synchronized DatabaseConnectionGenerator getInstance(String URL, String username, String password) throws InstanceAlreadyExistException, InvalidConnectionException, SQLException, InterruptedException
	{
		String key=URL+username+password;
		OraclePooledConnectionGenerator instance = OraclePooledConnectionGenerator.instanceRegistry.get(key); 
		if(instance == null)
		{
			OraclePooledConnectionGenerator newInstance = new OraclePooledConnectionGenerator(URL, username, password);
			OraclePooledConnectionGenerator.instanceRegistry.put(key, newInstance);
			instance = newInstance;
		}
		return instance;
	}
	
	
	
	public synchronized void close() throws SQLException
	{
		String key=this.URL+this.username+this.password;
		OraclePooledConnectionGenerator.instanceRegistry.remove(key);
		
		if(this.ocpds != null)
		{
			this.ocpds.close();
			this.ocpds = null;
		}
		this.URL = null;
		this.username = null;
		this.password = null;
	}
	
	
	public synchronized String getURL()
	{
		return this.URL;
	}
	
	public synchronized String getUsername()
	{
		return this.username;
	}
	
	public static void main(String[] args) throws SQLException, InstanceAlreadyExistException, InvalidConnectionException, InterruptedException
	{
		String url = "jdbc:oracle:thin:@localhost:1521/xe";
		String id = "odpair";
		String pw = "odpair";
		OraclePooledConnectionGenerator a = new OraclePooledConnectionGenerator(url, id, pw);
//		OracleConnectionGenerator b = new OracleConnectionGenerator(url, id, pw);
		System.out.println(OraclePooledConnectionGenerator.instanceRegistry.size());
		a.close();
		System.out.println(OraclePooledConnectionGenerator.instanceRegistry.size());
	}
}