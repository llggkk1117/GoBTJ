package com.gene.modules.db.dbConnectionPool;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import com.gene.modules.check.Check;
import com.gene.modules.db.dbConnectionPool.dbSource.DBProperties;
import com.gene.modules.db.dbConnectionPool.dbSource.DBSource;
import com.gene.modules.db.utils.DBUtils;
import com.gene.modules.exceptions.InstanceAlreadyExistException;
import com.gene.modules.exceptions.InvalidConnectionException;


public class ConnectionFactory
{
    private String URL;
    private String driver;
    private String username;
    private String password;
    private String instanceKey;
    
    private static HashMap<String, ConnectionFactory> instanceRegistry;
    private static HashMap<String, Integer> instanceBeingUsed;
	static
	{
		instanceRegistry = new HashMap<String, ConnectionFactory>();
		instanceBeingUsed = new HashMap<String, Integer>();
	}
	

	public ConnectionFactory(String driver, String URL, String username, String password)
	{
		this.initialize(driver, URL, username, password);
	}
	
	public ConnectionFactory(String URL, String username, String password)
	{
		this(DBUtils.getDriver(URL), URL, username, password);
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
	
	
    
	private synchronized void initialize(String driver, String URL, String username, String password) 
	{
		Check.notBlank(driver, URL, username, password);
		
		String instanceKey = ConnectionFactory.generateInstanceKey(URL, username);
		boolean instanceNotExist = ConnectionFactory.putInstanceToRegistryIfNotExist(instanceKey, this);
		Check.allTrue(InstanceAlreadyExistException.class, this.getClass().getName()+" instance already exists", instanceNotExist);
		
		String errorMessage = ConnectionFactory.getErrorMessageFromTestConnection(driver, URL, username, password);
		Check.allTrue(InvalidConnectionException.class, errorMessage, errorMessage == null);
		
		this.driver = driver;
		this.URL = URL;
		this.username = username;
		this.password = password;
		this.instanceKey = instanceKey;
    }
	
	
	
	private static synchronized boolean putInstanceToRegistryIfNotExist(String instanceKey, ConnectionFactory connectionFactory)
	{
		boolean instanceNotExist = ConnectionFactory.instanceRegistry.get(instanceKey) == null;
		if(instanceNotExist)
		{
			ConnectionFactory.instanceRegistry.put(instanceKey, connectionFactory);
			ConnectionFactory.instanceBeingUsed.put(instanceKey, 1);
		}
		
		return instanceNotExist;
	}
	
	
	private static synchronized boolean removeInstanceFromRegistry(String instanceKey)
	{
		Check.notBlank(instanceKey);
		boolean notBeingUsed = ConnectionFactory.instanceBeingUsed.get(instanceKey) == 1;
		if(notBeingUsed)
		{
			ConnectionFactory.instanceRegistry.remove(instanceKey);
			ConnectionFactory.instanceBeingUsed.remove(instanceKey);
		}
		else
		{
			ConnectionFactory.instanceBeingUsed.put(instanceKey, ConnectionFactory.instanceBeingUsed.get(instanceKey)-1);
		}
		
		return notBeingUsed;
	}
	
	
    public static synchronized ConnectionFactory getInstance(String driver, String URL, String username, String password) 
    {
    	Check.notBlank(driver, URL, username, password);
    	
    	String instanceKey = ConnectionFactory.generateInstanceKey(URL, username);
    	ConnectionFactory instance = ConnectionFactory.instanceRegistry.get(instanceKey);
		if(instance != null)
		{
			ConnectionFactory.instanceBeingUsed.put(instanceKey, instanceBeingUsed.get(instanceKey)+1);
		}
		else
		{
			instance = new ConnectionFactory(driver, URL, username, password);
		}
		
		return instance;    	
    }
    
    
    public static synchronized ConnectionFactory getInstance(String URL, String username, String password)
    {
    	return getInstance(DBUtils.getDriver(URL), URL, username, password);
    }
    
    
    
    private static String generateInstanceKey(String URL, String username)
    {
    	Check.notBlank(URL, username);
    	return URL+"|"+username;
    }


    
	private static String getErrorMessageFromTestConnection(String driver, String URL, String username, String password)
	{
		Check.notBlank(driver, URL, username, password);
		
		String errorMessage = null;
		try
		{
			Connection connection = ConnectionFactory.createConnection(driver, URL, username, password);
			connection.close();
		}
		catch (Exception e)
		{
			errorMessage = e.getMessage();
		}

		return errorMessage;
	}
	
	
	
	private static Connection createConnection(String driver, String URL, String username, String password) throws ClassNotFoundException, SQLException
	{
		Check.notBlank(driver, URL, username, password);
		
		Class.forName(driver);
        return DriverManager.getConnection(URL, username, password);
	}
	

	
    public synchronized Connection createConnection() throws ClassNotFoundException, SQLException
    {
    	return ConnectionFactory.createConnection(this.driver, this.URL, this.username, this.password);
    }
    

    
    
    
    public synchronized void close()
    {
    	if(this.instanceKey != null)
    	{
    		boolean removed = ConnectionFactory.removeInstanceFromRegistry(this.instanceKey);
    		if(removed)
    		{
    			this.instanceKey = null;
    			this.driver = null;
    	    	this.URL = null;
    	    	this.username = null;
    	    	this.password = null;
    		}
    	}
    }
    
    
    
    


    
    
    
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException, SQLException
    {
    	DBSource dbConnectionInfo = new DBProperties("settings/database.properties");
    	ConnectionFactory cf = new ConnectionFactory(dbConnectionInfo.getDriver(), dbConnectionInfo.getURL(), dbConnectionInfo.getUserName(), dbConnectionInfo.getPassword());
    	Connection connection = cf.createConnection();
    	Statement st = connection.createStatement();
    	st.executeUpdate("drop table temp11 cascade constraints");
    	st.executeUpdate("create table temp11(col1 number)");
    }
}
