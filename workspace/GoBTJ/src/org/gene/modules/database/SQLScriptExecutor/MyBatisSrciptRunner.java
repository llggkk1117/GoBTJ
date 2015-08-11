package org.gene.modules.database.SQLScriptExecutor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.SQLException;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.gene.modules.check.Check;
import org.gene.modules.database.dbConnectionPool.DBConnection;
import org.gene.modules.database.dbConnectionPool.DBConnectionPool;
import org.gene.modules.database.dbConnectionPool.dbSource.DBProperties;
import org.gene.modules.database.dbConnectionPool.dbSource.DBSource;


public class MyBatisSrciptRunner
{
	private DBConnectionPool dbConnectionPool;
	private DBConnection dbConnection;


	protected void finalize() throws Throwable
	{
	    try
	    {
	    	close();
	    }
	    finally
	    {
	        super.finalize();
	    }
	}




	public synchronized void setDBConnectionPool(DBConnectionPool dbConnectionPool)
	{
		Check.notNull(dbConnectionPool);
		this.dbConnectionPool = dbConnectionPool;
	}




	public synchronized DBConnectionPool getDBConnectionPool()
	{
		return this.dbConnectionPool;
	}




	public synchronized void close() throws SQLException
	{
		this.closeSession();
		this.dbConnectionPool = null;
	}




	private synchronized void openSession() throws SQLException, InterruptedException, ClassNotFoundException
	{
		//Dependency
		Check.notNull(this.dbConnectionPool);

		if(this.dbConnection == null)
		{
			this.dbConnection = this.dbConnectionPool.getConnection();
		}
	}




	private synchronized void closeSession() throws SQLException
	{
		if(this.dbConnection != null)
		{
			this.dbConnection.close();
			this.dbConnection = null;
		}
	}




	public synchronized String[] execute(final String... filePath) throws SQLException, InterruptedException, ClassNotFoundException, IOException
	{
		this.openSession();

        ScriptRunner runner = new ScriptRunner(this.dbConnection.getConnection());
        Writer log = new StringWriter();
        Writer errorLog = new StringWriter();
        runner.setLogWriter(new PrintWriter(log));
        runner.setErrorLogWriter(new PrintWriter(errorLog));
        //runner.setDelimiter("]");

        Reader reader = null;
        for(int i=0; i<filePath.length; ++i)
        {
        	reader = null;
        	//reader = Resources.getResourceAsReader(filePath[i]);
        	reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath[i]), "UTF8"));
            runner.runScript(reader);
            reader.close();
            this.dbConnection.commit();
        }

        this.closeSession();

        String[] resultLog = new String[]{log.toString(), errorLog.toString()};
//        System.out.println();
//        System.out.println();

        return resultLog;
	}




	public static void main(String[] args) throws InterruptedException, ClassNotFoundException, SQLException, IOException
	{
		String dbPropertisFileName = "settings/database.properties";
		DBSource dbConnectionInfo = new DBProperties(dbPropertisFileName);

		MyBatisSrciptRunner s = new MyBatisSrciptRunner();
		s.setDBConnectionPool(DBConnectionPool.getInstance(dbConnectionInfo.getDriver(), dbConnectionInfo.getURL(), dbConnectionInfo.getUsername(), dbConnectionInfo.getPassword()));
		String[] result = s.execute("com/gene/resource/aaa.sql");
		System.out.println(result[0]);
		System.out.println(result[1]);
	}
}
