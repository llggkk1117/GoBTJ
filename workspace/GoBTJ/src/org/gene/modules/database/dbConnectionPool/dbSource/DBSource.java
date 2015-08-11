package org.gene.modules.database.dbConnectionPool.dbSource;

public interface DBSource
{
	public String getDriver();
	public String getURL();
	public String getUsername();
	public String getPassword();
}
