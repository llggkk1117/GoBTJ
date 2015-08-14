package com.gene.modules.db.db;

import java.io.IOException;
import java.sql.SQLException;

import com.gene.modules.check.Check;
import com.gene.modules.db.SQLExecutor.SQLExecutor;
import com.gene.modules.db.dbConnectionPool.DBConnectionPool;
import com.gene.modules.db.dbConnectionPool.dbSource.DBProperties;
import com.gene.modules.db.dbConnectionPool.dbSource.DBSource;
import com.gene.modules.db.utils.Constants;
import com.gene.modules.db.utils.DBUtils;


public class Database
{
	private DBConnectionPool dbConnectionPool;
	private SQLExecutor sqlExecutor;
	
	public Database(String driver, String URL, String username, String password) throws ClassNotFoundException, SQLException
	{
		this.initialize(driver, URL, username, password);
	}
	
	public Database(String URL, String username, String password) throws ClassNotFoundException, SQLException
	{
		this.initialize(URL, username, password);
	}
	
	public Database(String dbPropertisFileName) throws ClassNotFoundException, IOException, SQLException
	{
		this.initialize(dbPropertisFileName);
	}
	
	public Database() throws ClassNotFoundException, IOException, SQLException
	{
		this.initialize();
	}
	
	private synchronized void initialize(String driver, String URL, String username, String password) throws ClassNotFoundException, SQLException
	{
		Check.notBlank(driver, URL, username, password);
		this.dbConnectionPool = DBConnectionPool.getInstance(driver, URL, username, password);
		this.sqlExecutor = new SQLExecutor();
		this.sqlExecutor.setDBConnectionPool(this.dbConnectionPool); 
	}
	
	private synchronized void initialize(String URL, String username, String password) throws ClassNotFoundException, SQLException
	{
		this.initialize(DBUtils.getDriver(URL), URL, username, password);
	}
	
	private synchronized void initialize(String dbPropertisFileName) throws IOException, ClassNotFoundException, SQLException
	{
		Check.notBlank(dbPropertisFileName);
		DBSource dbConnectionInfo = new DBProperties(dbPropertisFileName);
		String driver = dbConnectionInfo.getDriver();
		if(driver == null)
		{
			driver = DBUtils.getDriver(dbConnectionInfo.getURL());
		}
		this.initialize(driver, dbConnectionInfo.getURL(), dbConnectionInfo.getUserName(), dbConnectionInfo.getPassword());
	}
	
	private synchronized void initialize() throws ClassNotFoundException, IOException, SQLException
	{
		this.initialize(Constants.DB.Settings.getDBPropertiesPath());
	}
	
	
	public synchronized void close() throws SQLException
	{
		if(this.sqlExecutor != null)
		{
			this.sqlExecutor.close();
		}
		
		if(this.dbConnectionPool != null)
		{
			this.dbConnectionPool.close();
		}
		
		this.sqlExecutor = null;
		this.dbConnectionPool = null;
	}
	
	public synchronized Object execute(final String sql) throws ClassNotFoundException, InterruptedException, SQLException
	{
		return this.sqlExecutor.execute(sql);
	}
	
	public synchronized Object execute(String sql, Object... args) throws ClassNotFoundException, SQLException, InterruptedException
	{
		return this.sqlExecutor.execute(sql, args);
	}
	
	public synchronized String executeToFile(final String sql) throws ClassNotFoundException, IOException, InterruptedException, SQLException
	{
		return this.sqlExecutor.executeToFile(sql);
	}
	
	public synchronized String executeToFile(String sql, Object... args) throws ClassNotFoundException, IOException, InterruptedException, SQLException
	{
		return this.sqlExecutor.executeToFile(sql, args);
	}
	
	public synchronized void commit() throws SQLException
	{
		this.sqlExecutor.commit();
	}
	
	public synchronized void rollBack() throws SQLException
	{
		this.sqlExecutor.rollBack();
	}
	
	public synchronized boolean isSessionOpen()
	{
		return this.sqlExecutor.isSessionOpen();
	}
	
	public synchronized void openSession() throws ClassNotFoundException, InterruptedException, SQLException
	{
		this.sqlExecutor.openSession();
	}

	public synchronized void closeSession() throws SQLException
	{
		this.sqlExecutor.closeSession();
	}
	
	
	
	public static void main(String[] args) throws ClassNotFoundException, InterruptedException, SQLException, IOException 
    {
		Database db1 = new Database();
		Database db2 = new Database();
		
		System.out.println(db1.dbConnectionPool.getDriver());
		System.out.println(db2.dbConnectionPool.getDriver());
		
		try
		{
			db1.execute("drop table temp1");
		}
		catch(Exception e){}
		
		db1.openSession();
		db2.openSession();
		Object result = null;
    	result = db1.execute("create table temp1(col1 text primary key, col2 text)");
    	System.out.println(result);
    	db1.rollBack();
    	result = db1.execute("create table temp1(col1 text primary key, col2 text)");
    	System.out.println(result);
    	db1.closeSession();
    	db1.openSession();
    	result = db1.execute("create table temp1(col1 text primary key, col2 text)");
    	System.out.println(result);
    	result = db1.execute("insert into temp1(col1, col2) values ('1','1')");
    	System.out.println(result);
    	result = db1.execute("insert into temp1(col1, col2) values ('?', '?')", "aa", "bb");
    	System.out.println(result);
    	db1.commit();
    	result = db1.execute("select * from temp1");
    	System.out.println(result);
    	db1.commit();
    	db1.closeSession();
    	db1.close();
    	
    	result = db2.execute("select * from temp1");
    	System.out.println(result);
    	
    	result = db2.execute("select max(col1) as hello from temp1");
    	System.out.println(result);
    	
//    	result = db1.execute("drop table temp1");
//    	System.out.println(result);
    	
    	
    	
    	db2.close();
    }
}
