package org.gene.modules.database.dbConnectionPool.dbSource;

import org.gene.modules.database.dbUtils.DBUtils;

public class DBSourceBean implements DBSource
{
	private String driver;
	private String URL;
	private String username;
	private String password;
	
	public DBSourceBean(){}
	
	public DBSourceBean(String driver, String URL, String username, String password)
	{
		this.setDriver(driver);
		this.setURL(URL);
		this.setUsername(username);
		this.setPassword(password);
	}
	
	public DBSourceBean(String URL, String username, String password)
	{
		this.setURL(URL);
		this.setUsername(username);
		this.setPassword(password);
	}
	
	public String getDriver()
	{
		return driver;
	}
	
	public void setDriver(String driver)
	{
		this.driver = driver;
	}
	
	public String getURL()
	{
		return URL;
	}
	
	public void setURL(String URL)
	{
		this.URL = URL;
		if(this.driver==null)
		{
			this.driver = DBUtils.getDriver(this.URL);
		}
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}

}
