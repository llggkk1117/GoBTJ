package org.gene.modules.database.SQLExecutor;

import java.io.IOException;
import java.sql.SQLException;

import org.gene.modules.database.dbConnectionPool.DBConnectionPool;
import org.gene.modules.database.dbConnectionPool.dbSource.DBProperties;
import org.gene.modules.database.dbConnectionPool.dbSource.DBSource;
import org.gene.modules.database.dbUtils.Constants;
import org.gene.modules.textFile.TextFile;

import junit.framework.TestCase;

public class SQLExecutorTest extends TestCase
{
	private SQLExecutor db;
	
	protected void setUp() throws IOException, ClassNotFoundException, InterruptedException, SQLException
	{
		this.db = new SQLExecutor();
		DBSource dbConnectionInfo = new DBProperties(Constants.DB.Settings.getDBPropertiesPath());
		DBConnectionPool connectionPoolAdaptor = DBConnectionPool.getInstance(dbConnectionInfo);
		this.db.setDBConnectionPool(connectionPoolAdaptor);
		
		System.out.println("");
		System.out.println("* driver: "+dbConnectionInfo.getDriver());
    	System.out.println("* URL: "+dbConnectionInfo.getURL());
    	System.out.println("* username: "+dbConnectionInfo.getUsername());
    	System.out.println("* password: "+dbConnectionInfo.getPassword());
    	System.out.println("");
	}
	
	protected void tearDown() throws SQLException
	{
		this.db.close();
	}
	
	
	private static void showResult(Object result)
	{
		System.out.println(result);
	}
	
	
	public void test00() // prepare
	{
		try
		{
			this.db.execute("drop table myTemp");
			this.db.commit();
		}
		catch (Exception e){}
		
		try
		{
			this.db.execute("drop table temptemp");
			this.db.commit();
		}
		catch (Exception e){}
	}
	
	
	public void test01() throws ClassNotFoundException, InterruptedException, SQLException
	{
		this.db.openSession();
		assertEquals(true, this.db.isSessionOpen());
    	Object result = null;
    	result = this.db.execute("create table myTemp (col1 text, col2 text)");
    	SQLExecutorTest.showResult(result);
    	result = this.db.execute("insert into myTemp (col1, col2) values ('aa', 'bb')");
    	SQLExecutorTest.showResult(result);
    	assertEquals(true, this.db.isSessionOpen());
    	this.db.commit();
    	this.db.closeSession();
    	assertEquals(false, this.db.isSessionOpen());
    	
	}
	
	
	public void test02() throws ClassNotFoundException, InterruptedException, SQLException
	{
		assertEquals(false, this.db.isSessionOpen());
    	Object result = null;
    	result = this.db.execute("select col1, col2 from myTemp");
    	SQLExecutorTest.showResult(result);
    	assertEquals(false, this.db.isSessionOpen());
	}
	
	
	public void test03() throws ClassNotFoundException, InterruptedException, SQLException
	{
		this.db.openSession();
		assertEquals(true, this.db.isSessionOpen());
		this.db.openSession();
    	assertEquals(true, this.db.isSessionOpen());
    	this.db.closeSession();
    	assertEquals(false, this.db.isSessionOpen());
    	this.db.closeSession();
    	assertEquals(false, this.db.isSessionOpen());
	}
	
	
	
	public void test04() throws ClassNotFoundException, InterruptedException, SQLException, IOException
	{
		this.db.openSession();
		assertEquals(true, this.db.isSessionOpen());
		String filename = null;
		filename = this.db.executeToFile("select col1, col2 from myTemp");
		assertNotNull(filename);
		TextFile file = TextFile.getInstance(filename);
		String line = file.readLine();
		System.out.println("Line: "+line);
		assertEquals("col1,col2", line);
		line = file.readLine();
		System.out.println("Line: "+line);
		assertEquals("aa,bb", line);
		TextFile.delete(filename);
		assertEquals(true, this.db.isSessionOpen());
		this.db.closeSession();
		assertEquals(false, this.db.isSessionOpen());
	}
	
	
	public void test05() throws ClassNotFoundException, InterruptedException, SQLException, IOException
	{
		assertEquals(false, this.db.isSessionOpen());
		String filename = null;
		filename = this.db.executeToFile("select col1, col2 from myTemp");
		assertNotNull(filename);
		TextFile file = TextFile.getInstance(filename);
		String line = file.readLine();
		System.out.println("Line: "+line);
		assertEquals("col1,col2", line);
		line = file.readLine();
		System.out.println("Line: "+line);
		assertEquals("aa,bb", line);
		TextFile.delete(filename);
		assertEquals(false, this.db.isSessionOpen());
	}
	
	
	public void test06() throws ClassNotFoundException, InterruptedException, SQLException, IOException
	{
		String filename = null;
		filename = this.db.executeToFile("insert into myTemp (col1, col2) values ('cc', 'dd')");
		assertNotNull(filename);
		TextFile file = TextFile.getInstance(filename);
		String line = file.readLine();
		System.out.println("Line: "+line);
		assertEquals("1", line);
		TextFile.delete(filename);
	}
	
	
	
	public void test07() throws ClassNotFoundException, InterruptedException, SQLException 
	{
		assertEquals(false, this.db.isSessionOpen());
		this.db.openSession();
		assertEquals(true, this.db.isSessionOpen());
		try
		{
			this.db.execute("drop table temptemp");
		}
		catch (Exception e){}
		assertEquals(true, this.db.isSessionOpen());
		this.db.closeSession();
		assertEquals(false, this.db.isSessionOpen());
	}
	
	
	public void test08() throws ClassNotFoundException, InterruptedException, SQLException 
	{
		assertEquals(false, this.db.isSessionOpen());
		try
		{
			this.db.execute("drop table temptemp");
		}
		catch (Exception e){}
		assertEquals(false, this.db.isSessionOpen());
	}
	
	
	public void test09() throws ClassNotFoundException, InterruptedException, SQLException 
	{
		assertEquals(false, this.db.isSessionOpen());
		this.db.openSession();
		assertEquals(true, this.db.isSessionOpen());
		try
		{
			this.db.executeToFile("drop table temptemp");
		}
		catch (Exception e){}
		assertEquals(true, this.db.isSessionOpen());
		this.db.closeSession();
		assertEquals(false, this.db.isSessionOpen());
	}
	
	
	public void test10() throws ClassNotFoundException, InterruptedException, SQLException 
	{
		assertEquals(false, this.db.isSessionOpen());
		try
		{
			this.db.executeToFile("drop table temptemp");
		}
		catch (Exception e){}
		assertEquals(false, this.db.isSessionOpen());
	}
	
	
	public void test11() throws ClassNotFoundException, InterruptedException, SQLException
	{
    	Object result = null;
    	result = this.db.execute("select ?, ? from myTemp", "col1", "col2");
    	SQLExecutorTest.showResult(result);
	}
	
	public void test12() throws ClassNotFoundException, InterruptedException, SQLException, IOException
	{
		String filename = null;
		filename = this.db.executeToFile("select ?, ? from myTemp", "col1", "col2");
		TextFile file = TextFile.getInstance(filename);
		String line = file.readLine();
		System.out.println("Line: "+line);
		assertEquals("col1,col2", line);
		line = file.readLine();
		System.out.println("Line: "+line);
		assertEquals("aa,bb", line);
		TextFile.delete(filename);
	}
	
	
	
	public void test99() throws ClassNotFoundException, InterruptedException, SQLException
	{
		Object result = null;
    	result = this.db.execute("drop table myTemp");
    	SQLExecutorTest.showResult(result);
    	assertEquals(false, this.db.isSessionOpen());
	}
}
