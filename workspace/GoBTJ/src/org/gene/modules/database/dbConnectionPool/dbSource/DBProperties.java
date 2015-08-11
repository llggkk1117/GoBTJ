package org.gene.modules.database.dbConnectionPool.dbSource;

import java.io.IOException;

import org.gene.modules.check.Check;
import org.gene.modules.database.dbUtils.DBUtils;
import org.gene.modules.properties.Properties;



public class DBProperties implements DBSource
{
	private static String DB_TYPE = "db_type";
	private static String HOSTNAME = "hostname";
	private static String PORT = "port";
	private static String DB_OR_SERVICE_NAME = "db_or_service_name";
	
	private static String DRIVER = "driver";
	private static String URL = "url";
	private static String USERNAME = "username";
	private static String PASSWORD = "password";
	
	private String driver;
	private String url;
	private String username;
	private String password;
	
	public DBProperties(){}
	
	public DBProperties(String propertiesFileName) throws IOException 
	{
		this.setPropertiesFile(propertiesFileName);
	}
	
	public void setPropertiesFile(String propertiesFileName) throws IOException  
	{
		Properties properties = new Properties(propertiesFileName);
		
		String driver = properties.getProperty(DRIVER);
		String url = properties.getProperty(URL);
		
		Integer dbType = DBUtils.getDBType(properties.getProperty(DB_TYPE));
		String hostname = properties.getProperty(HOSTNAME);
		Integer port = null;
		try
		{
			port = Integer.parseInt(properties.getProperty(PORT));
		}
		catch(Throwable t){}
		String dbOrServiceName = properties.getProperty(DB_OR_SERVICE_NAME);

		
		this.driver = driver!=null ? driver : dbType !=null ? DBUtils.getDriver(dbType) : url!=null ? DBUtils.getDriver(url) : null;
		this.url = url!=null ? url : Check.isNotBlank(dbType, hostname, port, dbOrServiceName) ? DBUtils.getURL(dbType, hostname, port, dbOrServiceName) : null;
		this.username = properties.getProperty(USERNAME);
		this.password = properties.getProperty(PASSWORD);
	}
	
	public String getDriver()
	{
		return this.driver;
	}
	
	public String getURL()
	{
		return this.url;
	}
	
	public String getUsername()
	{
		return this.username;
	}
	
	public String getPassword()
	{
		return this.password;
	}
}
