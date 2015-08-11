package org.gene.modules.database.dbConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import org.gene.modules.check.Check;
import org.gene.modules.database.dbUtils.DBUtils;


public class ConnectionPool
{
	private static final int INITIAL_NUM_OF_CONNECTION = 1;
	private static final int MAX_NUM_OF_CONNECTION = 4;
	
	private ConnectionFactory connectionFactory;
	private HashSet<Connection> connectionBuffer;
	private HashSet<Connection> connectionRegistry;
	private String instanceKey;
	
	private static HashMap<String, ConnectionPool> instanceRegistry;
	private static HashMap<String, Integer> instanceBeingUsed;
	static
	{
		ConnectionPool.instanceRegistry = new HashMap<String, ConnectionPool>();
		ConnectionPool.instanceBeingUsed = new HashMap<String, Integer>();
	}
	
	
	public ConnectionPool(String driver, String URL, String username, String password) throws ClassNotFoundException, SQLException  
	{
		this.initialize(driver, URL, username, password);
	}
	
	
	public ConnectionPool(String URL, String username, String password) throws ClassNotFoundException, SQLException  
	{
		this(DBUtils.getDriver(URL), URL, username, password);
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
	


	
	private synchronized void initialize(String driver, String URL, String username, String password) throws ClassNotFoundException, SQLException  
	{
		Check.notBlank(driver, URL, username, password);
		
		String instanceKey = ConnectionPool.generateInstanceKey(URL, username);
		boolean instanceNotExist = ConnectionPool.putInstanceToRegistryIfNotExist(instanceKey, this);
		Check.allTrue(this.getClass().getName()+" instance already exists", instanceNotExist);

		this.instanceKey = instanceKey;
		this.connectionFactory = ConnectionFactory.getInstance(driver, URL, username, password);
		this.connectionBuffer = new HashSet<Connection>();
		this.connectionRegistry = new HashSet<Connection>();
		this.generateConnection(INITIAL_NUM_OF_CONNECTION);
    }
	
	
	
	private synchronized void generateConnection(int numOfConnection) throws ClassNotFoundException, SQLException 
	{
		Check.allTrue(numOfConnection>0);
		
		System.out.println(numOfConnection+" connections are now being generated");
		for(int i=0; i<numOfConnection; ++i)
		{
			Connection connectiontemp = this.connectionFactory.createConnection();
			// System.out.println(connectiontemp+" is created");
			this.connectionBuffer.add(connectiontemp);
			this.connectionRegistry.add(connectiontemp);
		}
	}
	
	
	
	private static synchronized boolean putInstanceToRegistryIfNotExist(String instanceKey, ConnectionPool connectionPool)
	{
		boolean instanceNotExist = ConnectionPool.instanceRegistry.get(instanceKey) == null;
		if(instanceNotExist)
		{
			ConnectionPool.instanceRegistry.put(instanceKey, connectionPool);
			ConnectionPool.instanceBeingUsed.put(instanceKey, 1);
		}
		
		return instanceNotExist;
	}
	
	

	
	private static synchronized boolean removeInstanceFromRegistry(String instanceKey)
	{
		Check.notBlank(instanceKey);
		boolean notBeingUsed = ConnectionPool.instanceBeingUsed.get(instanceKey) == 1;
		if(notBeingUsed)
		{
			ConnectionPool.instanceRegistry.remove(instanceKey);
			ConnectionPool.instanceBeingUsed.remove(instanceKey);
		}
		else
		{
			ConnectionPool.instanceBeingUsed.put(instanceKey, ConnectionPool.instanceBeingUsed.get(instanceKey)-1);
		}
		
		return notBeingUsed;
	}
	
	

	
	public static synchronized ConnectionPool getInstance(String driver, String URL, String username, String password) throws ClassNotFoundException, SQLException    
	{
		Check.notBlank(driver, URL, username, password);

    	String instanceKey = ConnectionPool.generateInstanceKey(URL, username);
    	ConnectionPool instance = ConnectionPool.instanceRegistry.get(instanceKey);
		if(instance != null)
		{
			ConnectionPool.instanceBeingUsed.put(instanceKey, ConnectionPool.instanceBeingUsed.get(instanceKey)+1);
		}
		else
		{
			instance = new ConnectionPool(driver, URL, username, password);
		}
		
		return instance;		
	}
	
	
	public static synchronized ConnectionPool getInstance(String URL, String username, String password) throws ClassNotFoundException, SQLException
	{
		return getInstance(DBUtils.getDriver(URL), URL, username, password);
	}
	
	

	
	
	
	private static String generateInstanceKey(String URL, String username)
    {
		Check.notBlank(URL, username);
    	return URL.toLowerCase()+"|"+username.toLowerCase();
    }
	
	

	
	
	
	
	public synchronized void close()
	{
		if(this.instanceKey != null)
		{
			boolean removed = ConnectionPool.removeInstanceFromRegistry(this.instanceKey);
			if(removed)
			{
				if(this.connectionRegistry != null)
				{
					Iterator<Connection> crIterator = this.connectionRegistry.iterator();
					while(crIterator.hasNext())
					{
						Connection connection = crIterator.next();
						try
						{
							connection.close();
						}
						catch(Exception e){}
					}
					this.connectionRegistry.clear();
				}
				
				if(this.connectionBuffer != null)
				{
					this.connectionBuffer.clear();
				}
				
				if(this.connectionFactory != null)
				{
					this.connectionFactory.close();
				}
				
				this.connectionRegistry = null;
				this.connectionBuffer = null;
				this.connectionFactory = null;
				this.instanceKey = null;
			}
		}
	}
	
	
	

    public synchronized Connection getConnection() throws ClassNotFoundException, SQLException, InterruptedException
    {
    	Check.notNull(IllegalArgumentException.class, this.connectionBuffer, this.connectionRegistry);
		
    	// System.out.println("numOfConnectionGenerated: "+this.connectionRegistry.size());
    	// System.out.println("buffer size: "+this.connectionBuffer.size());
    	
    	while(this.connectionBuffer.size() == 0)
    	{
    		if(this.connectionRegistry.size() < MAX_NUM_OF_CONNECTION)
    		{
    			this.generateConnection(1);
    		}
    		else
    		{
    			System.out.println("waiting");
    			this.wait();
    		}
    	}
    	
    	Connection connection = this.connectionBuffer.iterator().next();
    	this.connectionBuffer.remove(connection);
        // System.out.println(connection+" is now being used");

        return connection;
    }

    
    
    public synchronized void releaseConnection(Connection connection)
    {
    	Check.notNull(connection, this.connectionBuffer, this.connectionRegistry);
    	
    	// checking the connection instance is registered to the registry
    	// and check the connection is already returned back
    	if((this.connectionRegistry.contains(connection))&&(!this.connectionBuffer.contains(connection)))
    	{
    		// System.out.println(connection+" is now being returned");
    		this.connectionBuffer.add(connection);
            this.notifyAll();
    	}
    }
    
    
    public synchronized void destroyConnection(Connection connection)
    {
    	Check.notNull(connection, this.connectionBuffer, this.connectionRegistry);
    	
    	this.connectionBuffer.remove(connection);
    	this.connectionRegistry.remove(connection);
    	try
    	{
			connection.close();
		}
    	catch (Exception e){}
    }
    
    

    
    
    
    public static void main(String[] args) throws ClassNotFoundException, SQLException, InterruptedException 
    {
    	String driver = "org.postgresql.Driver";
    	String URL = "jdbc:postgresql://localhost:5432/test";
    	String username = "gene";
    	String password = "1234";
    			
    	ConnectionPool connectionPool = ConnectionPool.getInstance(driver, URL, username, password);
    	Vector<Connection> buffer = new Vector<Connection>(); 
    	for(int i=0; i<4; ++i)
    	{
    		buffer.add(connectionPool.getConnection());
    	}
    	Connection c1 = buffer.elementAt(0);
    	Connection c2 = buffer.elementAt(0);
    	System.out.println("c1.equals(c2): "+(c1.equals(c2)));
    	System.out.println("c1 == c2: "+(c1 == c2));
    	connectionPool.releaseConnection(c1);
    	connectionPool.releaseConnection(c2);
    	
    	
//    	Statement stm = connection.createStatement();
//    	stm.executeUpdate("drop table temp11 cascade constraints");
//    	stm.executeUpdate("create table temp1 (col1 number, col2 varchar2(2char))");
//    	stm.close();
    	
//    	for(int i=0; i<buffer.size(); ++i)
//    	{
//    		connectionPool.releaseConnection(buffer.elementAt(i));
//    	}
    }
}