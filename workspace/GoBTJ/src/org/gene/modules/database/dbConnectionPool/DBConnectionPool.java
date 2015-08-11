package org.gene.modules.database.dbConnectionPool;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.gene.modules.check.Check;
import org.gene.modules.database.dbConnectionPool.dbSource.DBProperties;
import org.gene.modules.database.dbConnectionPool.dbSource.DBSource;
import org.gene.modules.database.dbUtils.DBUtils;


public class DBConnectionPool
{
	private ConnectionPool connectionPool;
	private String instanceKey;

	private String driver;
	private String URL;
	private String username;
	private String password;

	private static HashMap<String, DBConnectionPool> instanceRegistry;
	private static HashMap<String, Integer> instanceBeingUsed;
	static
	{
		DBConnectionPool.instanceRegistry = new HashMap<String, DBConnectionPool>();
		DBConnectionPool.instanceBeingUsed = new HashMap<String, Integer>();
	}


	public DBConnectionPool(String driver, String URL, String username, String password) throws ClassNotFoundException, SQLException 
	{
		this.initialize(driver, URL, username, password);
	}
	
	
	public DBConnectionPool(String URL, String username, String password) throws ClassNotFoundException, SQLException 
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




	private synchronized void initialize(String driver, String URL, String username, String password) throws ClassNotFoundException, SQLException  
	{
		Check.notBlank(driver, URL, username, password);

		String instanceKey = DBConnectionPool.generateInstanceKey(URL, username);
		boolean instanceNotExist = DBConnectionPool.putInstanceToRegistryIfNotExist(instanceKey, this);
		Check.allTrue(this.getClass().getName()+" instance already exists", instanceNotExist);

		this.instanceKey = instanceKey;
		this.driver = driver;
		this.URL = URL;
		this.username = username;
		this.password = password;
		this.connectionPool = ConnectionPool.getInstance(this.driver, this.URL, this.username, this.password);
	}


	private static synchronized boolean putInstanceToRegistryIfNotExist(String instanceKey, DBConnectionPool dbConnectionPool)
	{
		boolean instanceNotExist = DBConnectionPool.instanceRegistry.get(instanceKey) == null;
		if(instanceNotExist)
		{
			DBConnectionPool.instanceRegistry.put(instanceKey, dbConnectionPool);
			DBConnectionPool.instanceBeingUsed.put(instanceKey, 1);
		}

		return instanceNotExist;
	}



	private static synchronized boolean removeInstanceFromRegistry(String instanceKey)
	{
		Check.notBlank(instanceKey);
		boolean notBeingUsed = DBConnectionPool.instanceBeingUsed.get(instanceKey) == 1;
		if(notBeingUsed)
		{
			DBConnectionPool.instanceRegistry.remove(instanceKey);
			DBConnectionPool.instanceBeingUsed.remove(instanceKey);
		}
		else
		{
			DBConnectionPool.instanceBeingUsed.put(instanceKey, DBConnectionPool.instanceBeingUsed.get(instanceKey)-1);
		}

		return notBeingUsed;
	}





	public static synchronized DBConnectionPool getInstance(DBSource dbConnectionInfo) throws ClassNotFoundException, SQLException   
	{
		Check.notNull(dbConnectionInfo);

		String driver = dbConnectionInfo.getDriver();
		String URL = dbConnectionInfo.getURL();
		String username = dbConnectionInfo.getUsername();
		String password = dbConnectionInfo.getPassword();

		DBConnectionPool instance = DBConnectionPool.getInstance(driver,URL, username, password);

		return instance;
	}



	public static synchronized DBConnectionPool getInstance(String driver, String URL, String username, String password) throws ClassNotFoundException, SQLException  
	{
		Check.notBlank(driver, URL, username, password);

		String instanceKey = DBConnectionPool.generateInstanceKey(URL, username);

		DBConnectionPool instance = DBConnectionPool.instanceRegistry.get(instanceKey);
		if(instance != null)
		{
			DBConnectionPool.instanceBeingUsed.put(instanceKey, DBConnectionPool.instanceBeingUsed.get(instanceKey)+1);
		}
		else
		{
			instance = new DBConnectionPool(driver, URL, username, password);
		}

		return instance;
	}
	
	
	public static synchronized DBConnectionPool getInstance(String URL, String username, String password) throws ClassNotFoundException, SQLException
	{
		return getInstance(DBUtils.getDriver(URL), URL, username, password);
	}


	private static String generateInstanceKey(String URL, String username)
	{
		Check.notBlank(URL, username);
		return URL+"|"+username;
	}





	public synchronized void close()
	{
		if(this.instanceKey != null)
		{
			boolean removed = DBConnectionPool.removeInstanceFromRegistry(this.instanceKey);
			if(removed)
			{
				if(this.connectionPool != null)
				{
					this.connectionPool.close();
				}
				this.driver = null;
				this.URL = null;
				this.username = null;
				this.password = null;
				this.connectionPool = null;
				this.instanceKey = null;
			}
		}
	}




	public synchronized DBConnection getConnection() throws ClassNotFoundException, SQLException, InterruptedException
	{
		Check.notNull(this.connectionPool);

		Connection connection = this.connectionPool.getConnection();
		DBConnection dbConnection = new DBConnection(connection, this.connectionPool);

		return dbConnection;
	}

	public synchronized String getUsername()
	{
		return this.username;
	}

	public String getDriver()
	{
		return this.driver;
	}

	public String getURL()
	{
		return this.URL;
	}

	public String getPassword()
	{
		return this.password;
	}

	public synchronized String getInstanceKey()
	{
		return this.instanceKey;
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, InterruptedException
	{
		DBSource dbConnectionInfo = new DBProperties("settings/database.properties");
		// PasswordEncryptor passwordEncryptor = new PasswordEncryptorByCrytoHelper();
		// String pass = passwordEncryptor.encrypt(dbConnectionInfo.getPassword());
		DBConnectionPool ca = DBConnectionPool.getInstance(dbConnectionInfo.getDriver(), dbConnectionInfo.getURL(), dbConnectionInfo.getUsername(), dbConnectionInfo.getPassword());
		DBConnection conn = ca.getConnection();
		// conn.executeUpdate("drop table temp2 cascade constraints");
		conn.executeUpdate("create table temp2 (col1 text, col2 text)");
		conn.commit();
		conn.close();
		ca.close();
	}
}
