package com.gene.modules.db.SQLExecutor;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import com.gene.modules.exceptions.MissingDependancyException;
import com.gene.modules.check.Check;
import com.gene.modules.db.dbConnectionPool.DBConnection;
import com.gene.modules.db.dbConnectionPool.DBConnectionPool;
import com.gene.modules.textFile.TextFile;



public class OldSQLExecutor
{
	private DBConnectionPool dbConnectionPool;
	private DBConnection dbConnection;
	
    protected void finalize() throws Throwable
	{
	    try
	    {
	    	this.close();
	    }
	    finally
	    {
	        super.finalize();
	    }
	}
    
	public synchronized void setDBConnectionPool(DBConnectionPool dbConnectionPool)
	{
		Check.allTrue(dbConnectionPool != null);
		this.dbConnectionPool = dbConnectionPool;
	}
	
	public synchronized DBConnectionPool getDBConnectionPool()
	{
		return this.dbConnectionPool;
	}
	
	
	
	public synchronized void close() throws SQLException
	{
		this.closeSession();
		this.dbConnectionPool =  null;
	}
	
	public synchronized boolean isSessionOpen()
	{
		return (this.dbConnection != null);
	}
	
	public synchronized void openSession() throws InterruptedException, SQLException, ClassNotFoundException
	{
		Check.notNull(MissingDependancyException.class, this.dbConnectionPool);

		if(this.dbConnection == null)
		{
			this.dbConnection = this.dbConnectionPool.getConnection();
		}
	}
	

	public synchronized void closeSession() throws SQLException
	{
		if(this.dbConnection != null)
		{
			this.dbConnection.close();
			this.dbConnection = null;
		}
	}
	



	
	

	/**
	 * 
	 * @param sql
	 * @return never null
	 * @throws SQLException
	 * @throws InterruptedException 
	 * @throws ClassNotFoundException 
	 */
    public synchronized Object execute(final String sql) throws InterruptedException, ClassNotFoundException, SQLException 
    {
    	Check.notBlankWithMessage(IllegalArgumentException.class, sql+": the SQL statement is null or empty", sql);
    	
    	Object result = null;
    	boolean sessionClosed = (this.dbConnection == null);
    	boolean isSelectSql = sql.toUpperCase().startsWith("SELECT");
    	try
    	{
        	if(sessionClosed)
        	{
        		this.openSession();
        	}
        	
    		if(isSelectSql)
        	{
       			result = executeSelectSqlSaveToMemory(sql);
        	}
        	else
        	{
       			result = executeOtherSqlSaveToMemory(sql);
        	}
    		
    		if((sessionClosed)&&(!isSelectSql))
        	{
    			this.dbConnection.commit();
        	}
    	}
    	finally
    	{
    		if(sessionClosed)
        	{
        		this.closeSession();
        	}
    	}
	
    	return result;
    }
    

    
    public synchronized Object execute(String sql, Object... args) throws SQLException, InterruptedException, ClassNotFoundException
    {
    	return this.execute(SQLBuilder.buildSQL(sql, args));
    }
    


    public synchronized String executeToFile(final String sql) throws IOException, InterruptedException, SQLException, ClassNotFoundException 
    {
    	Check.notBlankWithMessage(IllegalArgumentException.class, sql+": the SQL statement is null or empty", sql);
    	
    	String resultDataFileName = null;
    	boolean sessionClosed = (this.dbConnection == null);
    	boolean isSelectSql = sql.toUpperCase().startsWith("SELECT");
    	try
    	{
    		if(sessionClosed)
        	{
        		this.openSession();
        	}
        	
    		if(isSelectSql)
        	{
    			resultDataFileName =  executeSelectSqlSaveToFile(sql);
        	}
        	else
        	{
        		resultDataFileName = executeOtherSqlSaveToFile(sql);
       		}
    		
    		if((sessionClosed)&&(!isSelectSql))
        	{
    			this.dbConnection.commit();
        	}
    	}
    	finally
    	{
    		if(sessionClosed)
        	{
        		this.closeSession();
        	}
    	}
    	
	
    	return resultDataFileName;
    }
    
    
    

    
    public synchronized String executeToFile(String sql, Object... args) throws IOException, InterruptedException, SQLException, ClassNotFoundException
    {
    	return this.executeToFile(SQLBuilder.buildSQL(sql, args));
    }

    

    
    
    /**
     * 
     * @param sql
     * @return never null
     * @throws SQLException
     */
    private synchronized String[][] executeSelectSqlSaveToMemory(final String sql) throws SQLException
    {
		Vector<String[]> resultData = new Vector<String[]>();
    	
    	ResultSet resultSet = this.dbConnection.executeQuery(sql);
		ResultSetMetaData metadata = resultSet.getMetaData();
		int numOfColumns = metadata.getColumnCount();

		// adding column names
		String[] columnNames = new String[numOfColumns];
		for(int i=1; i<=columnNames.length; ++i)
		{
			columnNames[i-1] = metadata.getColumnLabel(i);
		}
		resultData.add(columnNames);
		
		// adding records
		while(resultSet.next())
		{
			String[] row = new String[numOfColumns];
			for(int i=1; i<=numOfColumns; ++i)
			{
				row[i-1] = resultSet.getString(i);
			}
			resultData.add(row);
		}
		
		resultSet.close();
		resultSet = null;
		
		String[][] result = new String[resultData.size()][numOfColumns];
		for(int i=0; i<result.length; ++i)
		{
			result[i] = resultData.elementAt(i);
		}
		resultData = null;

    	return result;
    }
    

    
    


    /**
     * @param sql
     * @return never be null
     * @throws SQLException
     * @throws InterruptedException 
     */
    private synchronized Integer executeOtherSqlSaveToMemory(String sql) throws SQLException, InterruptedException
    {
   		Integer result = this.dbConnection.executeUpdate(sql);
    	return result;
    }    

    
    
    
    
    

    
    private synchronized String executeSelectSqlSaveToFile(final String sql) throws IOException, InterruptedException, SQLException
    {
    	String resultDataFileName = this.getRandomFileName("temp_SQL_results_", ".txt");
    	TextFile resultDataFile = TextFile.getFile(resultDataFileName);
    	resultDataFile.clear();
    	
    	ResultSet resultSet = this.dbConnection.executeQuery(sql);
		ResultSetMetaData metadata = resultSet.getMetaData();
		int numOfColumns = metadata.getColumnCount();

		// adding column names
		for(int i=1; i<=numOfColumns; ++i)
		{
			String columnName = metadata.getColumnLabel(i);
			if(i != numOfColumns)
			{
				columnName +=",";
			}
			else
			{
				columnName +="\n";
			}
			resultDataFile.write(columnName);
		}
		
		// adding records
		while(resultSet.next())
		{
			for(int i=1; i<=numOfColumns; ++i)
			{
				String record = resultSet.getString(i);
				if(i != numOfColumns)
				{
					record +=",";
				}
				else
				{
					record +="\n";
				}
				resultDataFile.write(record);
			}
		}

    	resultSet.close();
		resultSet = null;
    	resultDataFile.close();
    	resultDataFile = null;

    	return resultDataFileName;
    }
    
  



    
    
    private synchronized String executeOtherSqlSaveToFile(final String sql) throws IOException, InterruptedException, SQLException
    {
		String resultDataFileName = this.getRandomFileName("temp_SQL_results_", ".txt");
    	TextFile resultDataFile = TextFile.getFile(resultDataFileName);
    	resultDataFile.clear();
    	
    	Integer resultValue = this.dbConnection.executeUpdate(sql);
    	resultDataFile.writeLine(resultValue+"");
    	
    	resultDataFile.close();
    	resultDataFile = null;
    	
    	return resultDataFileName;
    }
    
    
    
    public synchronized void commit() throws SQLException
    {
    	if(this.dbConnection != null)
		{
    		this.dbConnection.commit();
		}
    }
    
    
    
    private synchronized String generateRandomString(final String prefix, final String postfix)
    {
		String prefixString = prefix;
		String postfixString = postfix;
		if(prefixString == null)
		{
			prefixString = "";
		}
		if(postfixString == null)
		{
			postfixString = "";
		}
		
    	String instanceId = this.toString();
    	instanceId = instanceId.substring(instanceId.indexOf("@")+1, instanceId.length()); 
    	String randomString = prefixString+System.currentTimeMillis()+"_"+instanceId+postfixString;
    	
    	return randomString;
    }
    
    
    
    private synchronized String getRandomFileName(final String prefix, final String postfix)
    {
    	String fileName = null;
    	while(fileName == null)
    	{
    		fileName = this.generateRandomString(prefix, postfix);
    		if(TextFile.exists(fileName))
    		{
    			fileName = null;
    		}
    	}
    	
    	return fileName;
    }
    
    @SuppressWarnings("unused")
	private static void showResult(Object result)
	{
		if(result instanceof String[][])
		{
			String[][] res = (String[][]) result;
			for(int i=0; i<res.length; ++i)
	    	{
	    		for(int j=0; j<res[i].length; j++)
	    		{
	    			System.out.print(res[i][j]+"\t");
	    		}
	    		System.out.print("\n");
	    	}
		}
		else if(result instanceof Integer)
		{
			Integer res = (Integer) result;
			System.out.println(res);
		}
	}

}
