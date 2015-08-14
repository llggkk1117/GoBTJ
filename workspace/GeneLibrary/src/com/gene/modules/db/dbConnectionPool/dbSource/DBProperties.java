package com.gene.modules.db.dbConnectionPool.dbSource;

import java.io.IOException;

import com.gene.modules.properties.Properties;

public class DBProperties implements DBSource
{
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
		this.driver = properties.getProperty(DRIVER);
		this.url = properties.getProperty(URL);
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
	
	public String getUserName()
	{
		return this.username;
	}
	
	public String getPassword()
	{
		return this.password;
	}
}
