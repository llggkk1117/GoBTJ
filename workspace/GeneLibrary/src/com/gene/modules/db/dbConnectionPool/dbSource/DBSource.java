package com.gene.modules.db.dbConnectionPool.dbSource;

public interface DBSource
{
	public String getDriver();
	public String getURL();
	public String getUserName();
	public String getPassword();
}
