package org.gene.modules.database.SQLExecutor;

import java.io.IOException;
import java.sql.SQLException;

import org.gene.modules.check.Check;
import org.gene.modules.database.dbConnectionPool.DBConnectionPool;
import org.gene.modules.database.dbConnectionPool.dbSource.DBProperties;
import org.gene.modules.database.dbConnectionPool.dbSource.DBSource;
import org.gene.modules.database.dbUtils.Constants;
import org.gene.modules.database.dbUtils.DBUtils;





public class SQLExecutorFactory
{
	private SQLExecutorFactory(){}
	
	public static synchronized SQLExecutor getInstance(String URL, String username, String password) throws InterruptedException, ClassNotFoundException, SQLException
	{
		Check.notBlank(URL, username, password);
		
		DBConnectionPool connectionPoolAdaptor = DBConnectionPool.getInstance(DBUtils.getDriver(URL), URL, username, password);
		SQLExecutor sqlExecutor = new SQLExecutor();
		sqlExecutor.setDBConnectionPool(connectionPoolAdaptor);
		
		return sqlExecutor;
	}
	
	public static synchronized SQLExecutor getInstance(String driver, String URL, String username, String password) throws InterruptedException, ClassNotFoundException, SQLException
	{
		Check.notBlank(driver, URL, username, password);
		
		DBConnectionPool connectionPoolAdaptor = DBConnectionPool.getInstance(driver, URL, username, password);
		SQLExecutor sqlExecutor = new SQLExecutor();
		sqlExecutor.setDBConnectionPool(connectionPoolAdaptor);
		
		return sqlExecutor;
	}
	
	
	
	public static synchronized SQLExecutor getInstance(String dbPropertisFileName) throws InterruptedException, ClassNotFoundException, SQLException, IOException  
	{
		Check.notBlank(IllegalArgumentException.class, dbPropertisFileName);
		
		DBSource dbConnectionInfo = new DBProperties(dbPropertisFileName);
		SQLExecutor sqlExecutor = SQLExecutorFactory.getInstance(dbConnectionInfo.getDriver(), dbConnectionInfo.getURL(), dbConnectionInfo.getUsername(), dbConnectionInfo.getPassword());

		return sqlExecutor;
	}

	
	public static synchronized SQLExecutor getInstance() throws InterruptedException, ClassNotFoundException, SQLException, IOException 
	{
		return SQLExecutorFactory.getInstance(Constants.DB.Settings.getDBPropertiesPath());
	}
	

	
	
	
	public static void main(String[] args) throws InterruptedException, ClassNotFoundException, SQLException, IOException 
    {
		SQLExecutor db = SQLExecutorFactory.getInstance();
		
		try
		{
			db.execute("drop table temp1");
		}
		catch(Exception e){}
		
		Object result = null;
    	result = db.execute("create table temp1(col1 text primary key, col2 text)");
    	System.out.println(result);
    	result = db.execute("insert into temp1(col1, col2) values ('1','1')");
    	System.out.println(result);
    	result = db.execute("insert into temp1(col1, col2) values ('?', '?')", "aa", "bb");
    	System.out.println(result);
    	result = db.execute("select * from temp1");
    	System.out.println(result);
    	result = db.execute("drop table temp1");
    	System.out.println(result);
    	
    	db.getDBConnectionPool().close();
    	db.close();
    }
	

	
	
	
}
