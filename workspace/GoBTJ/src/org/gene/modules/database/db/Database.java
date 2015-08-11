package org.gene.modules.database.db;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.gene.modules.check.Check;
import org.gene.modules.database.SQLExecutor.SQLExecutor;
import org.gene.modules.database.SQLScriptExecutor.MyBatisSrciptRunner;
import org.gene.modules.database.dbConnectionPool.DBConnectionPool;
import org.gene.modules.database.dbConnectionPool.dbSource.DBProperties;
import org.gene.modules.database.dbConnectionPool.dbSource.DBSource;
import org.gene.modules.database.dbConnectionPool.dbSource.DefaultDBSources;
import org.gene.modules.database.dbUtils.DBUtils;
import org.gene.modules.database.dbUtils.Constants.DB;


public class Database
{
	final static Logger logger = Logger.getLogger(Database.class);
	
	private DBConnectionPool dbConnectionPool;
	private SQLExecutor sqlExecutor;
	private MyBatisSrciptRunner sqlScriptRunner;
	
	public Database(int dbType, String hostname, int port, String dbOrServiceName, String username, String password) throws ClassNotFoundException, SQLException
	{
		this.initialize(dbType, hostname, port, dbOrServiceName, username, password);
	}
	
	public Database(String URL, String username, String password) throws ClassNotFoundException, SQLException
	{
		this.initialize(URL, username, password);
	}
	
	public Database(DBSource dbSource) throws ClassNotFoundException, SQLException
	{
		this.initialize(dbSource);
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
		this.sqlScriptRunner = new MyBatisSrciptRunner();
		this.sqlScriptRunner.setDBConnectionPool(this.dbConnectionPool);
	}
	
	
	private synchronized void initialize(String URL, String username, String password) throws ClassNotFoundException, SQLException
	{
		String driver = DBUtils.getDriver(URL);
		this.initialize(driver, URL, username, password);
	}
	
	
	private synchronized void initialize(DBSource dbSource) throws ClassNotFoundException, SQLException
	{
		this.initialize(dbSource.getDriver(), dbSource.getURL(), dbSource.getUsername(), dbSource.getPassword());
	}
	
	
	private synchronized void initialize(String dbPropertisFileName) throws IOException, ClassNotFoundException, SQLException
	{
		DBSource dbConnectionInfo = new DBProperties(dbPropertisFileName);
		this.initialize(dbConnectionInfo);
	}
	
	
	private synchronized void initialize()
	{
		for(int i=0; i<DefaultDBSources.dbSources.size(); ++i)
		{
			try
			{
				DBSource dbSource = DefaultDBSources.dbSources.get(i);
				this.initialize(dbSource);
				if(logger.isInfoEnabled()){logger.info("Databasae connection to "+dbSource.getURL()+" was successfully set up.");}
				break;
			}
			catch(Throwable t)
			{
				logger.error(t);
			}
		}
	}
	
	
	private synchronized void initialize(int dbType, String hostname, int port, String dbOrServiceName, String username, String password) throws ClassNotFoundException, SQLException
	{
		String driver = DBUtils.getDriver(dbType);
		String URL = DBUtils.getURL(dbType, hostname, port, dbOrServiceName);
		this.initialize(driver, URL, username, password);
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
	
	public synchronized String[] executeScript(String sqlScriptFileName) throws ClassNotFoundException, SQLException, InterruptedException, IOException
	{
		return this.sqlScriptRunner.execute(sqlScriptFileName);
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
	
	public synchronized boolean checkTableExist(String tableName) throws ClassNotFoundException, SQLException, InterruptedException
	{
		Check.notBlank(tableName);
		
		int databaseType = DBUtils.getDBType(this.sqlExecutor.getDBConnectionPool().getURL());
		
		String tableExistCheckSQL = null;
		if(databaseType == DB.Type.postgresql)
		{
			tableExistCheckSQL = DB.SQL.Postgresql.tableExistCheckSQL;
		}
		else //if(databaseType == DB_CONSTANTS.DB_TYPE.ORACLE)
		{
			tableExistCheckSQL = DB.SQL.Oracle.tableExistCheckSQL;
		}
		
		@SuppressWarnings("unchecked")
		boolean tableExists = ((List<String[]>)sqlExecutor.execute(tableExistCheckSQL, tableName.toLowerCase())).size()>1;
		
		return tableExists;
	}
	
	
	public static void main(String[] args) throws ClassNotFoundException, InterruptedException, SQLException, IOException 
    {
//		Database db1 = new Database();
//		Database db2 = new Database();
//		
//		System.out.println(db1.dbConnectionPool.getDriver());
//		System.out.println(db2.dbConnectionPool.getDriver());
//		
//		try
//		{
//			db1.execute("drop table temp1");
//		}
//		catch(Exception e){}
//		
//		db1.openSession();
//		db2.openSession();
//		Object result = null;
//    	result = db1.execute("create table temp1(col1 text primary key, col2 text)");
//    	System.out.println(result);
//    	db1.rollBack();
//    	result = db1.execute("create table temp1(col1 text primary key, col2 text)");
//    	System.out.println(result);
//    	db1.closeSession();
//    	db1.openSession();
//    	result = db1.execute("create table temp1(col1 text primary key, col2 text)");
//    	System.out.println(result);
//    	result = db1.execute("insert into temp1(col1, col2) values ('1','1')");
//    	System.out.println(result);
//    	result = db1.execute("insert into temp1(col1, col2) values ('?', '?')", "aa", "bb");
//    	System.out.println(result);
//    	db1.commit();
//    	result = db1.execute("select * from temp1");
//    	DBUtils.showResult(result);
//    	db1.commit();
//    	db1.closeSession();
//    	db1.close();
//    	
//    	System.out.println("exists: "+db2.checkTableExist("temp1"));
//    	result = db2.execute("select * from temp1");
//    	DBUtils.showResult(result);
//    	
//    	result = db2.execute("select max(col1) as hello from temp1");
//    	DBUtils.showResult(result);
//    	
////    	result = db1.execute("drop table temp1");
////    	System.out.println(result);
//    	
//    	
//    	
//    	db2.close();
		
		Database db = new Database();
//		String[] result = db.executeScript("src/org/gene/database/setting/initialization.sql");
//		System.out.println(result[0]);
//		System.out.println(result[1]);
//		db.openSession();
//		@SuppressWarnings("unchecked")
//		List<String[]> result = (List<String[]>) db.execute("select date_joined from members");
//		db.closeSession();
//		System.out.println(result.get(1)[0]);

		
		String sql = "select id, board_id, member_id, article_number, title, contents, view_count, comment_of, last_updated_time from articles where id='?'";
		db.openSession();
		@SuppressWarnings("unchecked")
		List<String[]> result = (List<String[]>) db.execute(sql, 3);
		db.closeSession();
		for(int i=0; i<result.get(1).length; ++i)
		{
			System.out.print(result.get(1)[i]+" ");
		}
		
		
		
		
		
//		db.execute("insert into member (korean_name, english_name, birthday) values ('할렐루야', 'Gene Kwan Lee', to_date('Nov-17-1980', 'Mon-DD-YYYY'))");
//		db.commit();
//		Object result = db.execute("select * from member");
//		DBUtils.showResult(result);
//		db.closeSession();
		db.close();
    }
}
