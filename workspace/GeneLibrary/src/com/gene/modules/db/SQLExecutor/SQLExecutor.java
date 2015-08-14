package com.gene.modules.db.SQLExecutor;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.gene.modules.check.Check;
import com.gene.modules.db.dbConnectionPool.DBConnection;
import com.gene.modules.db.dbConnectionPool.DBConnectionPool;
import com.gene.modules.db.tableManagement.table.TableData;
import com.gene.modules.exceptions.MissingDependancyException;
import com.gene.modules.randomString.RandomString;
import com.gene.modules.textFile.TextFile;

public class SQLExecutor
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
		Check.notBlankWithMessage("The SQL statement is null or empty", sql);

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
		Check.notBlankWithMessage("The SQL statement is null or empty", sql);

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
	private synchronized TableData executeSelectSqlSaveToMemory(final String sql) throws SQLException
	{
		ResultSet resultSet = this.dbConnection.executeQuery(sql);
		ResultSetMetaData metadata = resultSet.getMetaData();
		int numOfColumns = metadata.getColumnCount();

		// retrieving column names
		String[] columnNames = new String[numOfColumns];
		for(int i=1; i<=columnNames.length; ++i)
		{
			columnNames[i-1] = metadata.getColumnLabel(i);
		}

		TableData tableData = new TableData(columnNames);

		// retrieving tuples
		while(resultSet.next())
		{
			String[] row = new String[numOfColumns];
			for(int i=1; i<=numOfColumns; ++i)
			{
				row[i-1] = resultSet.getString(i);
			}
			tableData.addTuple(row);
		}

		resultSet.close();
		resultSet = null;

		return tableData;
	}




	/**
	 * @param sql
	 * @return never be null
	 * @throws SQLException
	 * @throws InterruptedException 
	 */
	private synchronized int executeOtherSqlSaveToMemory(String sql) throws SQLException, InterruptedException
	{
		int result = this.dbConnection.executeUpdate(sql);
		return result;
	}    



	private static String generateRandomString()
    {
		RandomString random1 = new RandomString(10, RandomString.ALPHABET_LOWER);
		RandomString random2 = new RandomString(10, RandomString.NUMBER);
		String randomString = random1.nextString()+random2.nextString();
    	
    	return randomString;
    }
	

	private static synchronized String getRandomFileName(String prefix, String postfix)
    {
		if(prefix == null){prefix = "";}
		if(postfix == null){postfix = "";}
    	String fileName = null;
    	while(fileName == null)
    	{
    		fileName = prefix+generateRandomString()+postfix;
    		if(TextFile.exists(fileName))
    		{
    			fileName = null;
    		}
    	}
    	
    	return fileName;
    }



	private synchronized String executeSelectSqlSaveToFile(final String sql) throws IOException, InterruptedException, SQLException
	{
		String resultDataFileName = getRandomFileName("SQL_results_", ".txt");
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
		String resultDataFileName = getRandomFileName("SQL_results_", ".txt");
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
	
	
	public synchronized void rollBack() throws SQLException
	{
		if(this.dbConnection != null)
		{
			this.dbConnection.rollback();
		}
	}
}