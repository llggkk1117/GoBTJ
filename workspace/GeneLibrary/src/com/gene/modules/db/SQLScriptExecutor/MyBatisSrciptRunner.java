package com.gene.modules.db.SQLScriptExecutor;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.SQLException;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;





import com.gene.modules.db.dbConnectionPool.DBConnection;
import com.gene.modules.db.dbConnectionPool.DBConnectionPool;
import com.gene.modules.db.dbConnectionPool.dbSource.DBProperties;
import com.gene.modules.db.dbConnectionPool.dbSource.DBSource;
import com.gene.modules.exceptions.MissingDependancyException;



public class MyBatisSrciptRunner
{
	private DBConnectionPool connectionPoolAdaptor;
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
	
	
	
	
	public synchronized void setConnectionPoolAdaptor(DBConnectionPool connectionPoolAdaptor)
	{
		if(connectionPoolAdaptor == null)
		{
			throw new IllegalArgumentException();
		}
		
		this.connectionPoolAdaptor = connectionPoolAdaptor;
	}
	
	
	
	
	public synchronized DBConnectionPool getConnectionPoolAdaptor()
	{
		return this.connectionPoolAdaptor;
	}

	
	
	
	public synchronized void close() throws SQLException
	{
		this.closeSession();
		this.connectionPoolAdaptor = null;
	}
	
	
	
	
	private synchronized void openSession() throws SQLException, InterruptedException, ClassNotFoundException
	{
		//Dependency
		if(this.connectionPoolAdaptor == null)
		{
			throw new MissingDependancyException();
		}

		if(this.dbConnection == null)
		{
			this.dbConnection = this.connectionPoolAdaptor.getConnection();
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
        	reader = Resources.getResourceAsReader(filePath[i]);
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
		s.setConnectionPoolAdaptor(DBConnectionPool.getInstance(dbConnectionInfo.getDriver(), dbConnectionInfo.getURL(), dbConnectionInfo.getUserName(), dbConnectionInfo.getPassword()));
		String[] result = s.execute("com/gene/resource/aaa.sql");
		System.out.println(result[0]);
		System.out.println(result[1]);
	}
}
