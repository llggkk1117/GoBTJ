package com.gene.modules.db.SQLScriptExecutor;


import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;

import com.gene.modules.exceptions.ErrorMessages;
import com.gene.modules.exceptions.InstanceAlreadyExistException;
import com.gene.modules.exceptions.InstanceNotExistException;
import com.gene.modules.db.dbConnectionPool.DBConnection;
import com.gene.modules.db.dbConnectionPool.DBConnectionPool;
import com.gene.modules.textFile.TextFile;




public class SQLScriptExecuter
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
	
	public synchronized void openSession() throws SQLException, InstanceNotExistException, InterruptedException, ClassNotFoundException
	{
		//Dependency
		if(this.connectionPoolAdaptor == null)
		{
			throw new InstanceNotExistException("databaseConnectionGenerator", ErrorMessages.instanceNotExist);
		}

		if(this.dbConnection == null)
		{
			this.dbConnection = this.connectionPoolAdaptor.getConnection();
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
	
	
	public synchronized void execute(final String scriptFileName) throws IOException, InstanceAlreadyExistException, InterruptedException, SQLException
	{
		SQLScriptExecuter.execute(scriptFileName, this.dbConnection);
	}
	
	
	
	private static synchronized String removeStarComment(String line)
	{
		String result = SQLScriptExecuter.substring(line, 0, line.indexOf("/*")-1);
		result += " ";
		result += SQLScriptExecuter.substring(line, line.indexOf("*/")+2, line.length()-1);
		
		return result;
	}
	
	
	private static synchronized String removeOpenStarComment(String line)
	{
		String result = SQLScriptExecuter.substring(line, 0, line.indexOf("/*")-1);
		
		return result;
	}
	
	
	private static synchronized String removeCloseStarComment(String line)
	{
		String result = SQLScriptExecuter.substring(line, line.indexOf("*/")+2, line.length()-1);
		
		return result;
	}
	
	
	private static synchronized String removeDashComment(String line)
	{
		String result = SQLScriptExecuter.substring(line, 0, line.indexOf("--")-1);
		
		return result;
	}
	
	private static synchronized int compare(int index1, int index2)
	{
		int result = 0;
		
		if((index1 >= 0)&&(index2 >= 0))
		{
			if(index1 < index2)
			{
				result = 1;
			}
			else if(index1 > index2) 
			{
				result = 2;
			}
			else
			{
				result = 0;
			}
		}
		else if(index1 >= 0)
		{
			result = 1;
		}
		else if(index2 >= 0)
		{
			result = 2;
		}
		else
		{
			result = -1;
		}
		
		
		return result;
	}
	
	
	
	private static synchronized String removeSpace(String line)
	{
		String result = line.replace("\t", " ").trim();
		while(result.indexOf("  ") >= 0)
		{
			result = result.replace("  ", " ");
		}
		
		return result;
	}
	
	
	private static synchronized String getCommentMarkFirstOccured(String line)
	{
		String result = null;
		int starMarkIndex = (((line.indexOf("/*") >= 0)&&(line.indexOf("/*") < line.indexOf("*/"))) ? line.indexOf("/*") : -1);
		int openStarMarkIndex = (((line.indexOf("/*") >= 0)&&(line.indexOf("*/") < 0)) ? line.indexOf("/*") : -1);
		int dashMarkIndex = line.indexOf("--");
		int remMarkIndex = ((line.trim().toUpperCase().indexOf("REM") == 0) ? 0 : -1);
		

		
		if(remMarkIndex == 0)
		{
			result = "REM";
		}
		else if((starMarkIndex >= 0)&&(compare(starMarkIndex, openStarMarkIndex) == 1)&&(compare(starMarkIndex, dashMarkIndex) == 1))
		{
			result = "/**/";
		}
		else if((openStarMarkIndex >= 0)&&(compare(openStarMarkIndex, starMarkIndex) == 1)&&(compare(openStarMarkIndex, dashMarkIndex) == 1))
		{
			result = "/*";
		}
		else if((dashMarkIndex >= 0)&&(compare(dashMarkIndex, starMarkIndex) == 1)&&(compare(dashMarkIndex, openStarMarkIndex) == 1))
		{
			result = "--";
		}
		else
		{
			result = "";
		}
		
		
		return result;
	}
	
	
	private static synchronized void execute(final String scriptFileName, final DBConnection dbConnection) throws IOException, InstanceAlreadyExistException, InterruptedException, SQLException
	{
		TextFile scriptFile = TextFile.getFile(scriptFileName);
		boolean commentOn = false;
		String tempLine = null;
		String buffer = "";

		
		while((tempLine = scriptFile.readLine()) != null)
		{
			tempLine =removeSpace(tempLine);
			
			if(commentOn)
			{
				if(tempLine.indexOf("*/") >= 0)
				{
					tempLine = removeCloseStarComment(tempLine);
					commentOn = false;
				}
				else
				{
					tempLine = "";
				}
			}

			
			if(!commentOn)
			{
				String firstCommentMark = null;
				
				boolean loop = true;
				while(loop)
				{
					firstCommentMark = getCommentMarkFirstOccured(tempLine);
					System.out.println("!!!!!!!!!!!"+firstCommentMark);
					
					if(firstCommentMark.equals("REM"))
					{
						tempLine = "";
					}
					else if(firstCommentMark.equals("/**/"))
					{
						tempLine = removeStarComment(tempLine);
					}
					else if(firstCommentMark.equals("/*"))
					{
						tempLine = removeOpenStarComment(tempLine);
						commentOn = true;
					}
					else if(firstCommentMark.equals("--"))
					{
						tempLine = removeDashComment(tempLine);
					}
					else
					{
						loop = false;
					}
				}
			}
				
			
			buffer += tempLine+" ";

			while(buffer.indexOf(";") >= 0)
			{
				String arrangedSQL = SQLScriptExecuter.substring(buffer, 0, buffer.indexOf(";")-1);
				arrangedSQL = removeSpace(arrangedSQL);
				buffer = SQLScriptExecuter.substring(buffer, buffer.indexOf(";")+1, buffer.length()-1);
				
				System.out.println("++"+arrangedSQL);
				
				if(arrangedSQL.toUpperCase().startsWith("SELECT"))
				{
					dbConnection.executeQuery(arrangedSQL);
				}
				else
				{
					dbConnection.executeUpdate(arrangedSQL);
				}
			}
		}
		
		System.out.println("buffer: "+buffer);

		
		scriptFile.close();
		scriptFile = null;
	}
	
	
	

	
	public static synchronized String[] parse(String scriptFileName, boolean toFile) throws IOException, InstanceAlreadyExistException, InterruptedException, SQLException
	{
		TextFile scriptFile = TextFile.getFile(scriptFileName);
		boolean commentOn = false;
		String tempLine = null;
		String buffer = "";
		
		Vector<String> memoryForSQLs = new Vector<String>();
		String fileForSQLsName = scriptFileName+"_temp";
		TextFile fileForSQLs = TextFile.getFile(fileForSQLsName);
		fileForSQLs.clear();

		
		while((tempLine = scriptFile.readLine()) != null)
		{
			tempLine =removeSpace(tempLine);
			
			if(commentOn)
			{
				if(tempLine.indexOf("*/") >= 0)
				{
					tempLine = removeCloseStarComment(tempLine);
					commentOn = false;
				}
				else
				{
					tempLine = "";
				}
			}

			
			if(!commentOn)
			{
				String firstCommentMark = null;
				
				boolean loop = true;
				while(loop)
				{
					firstCommentMark = getCommentMarkFirstOccured(tempLine);
//					System.out.println("firstCommentMark: "+firstCommentMark);
					
					if(firstCommentMark.equals("REM"))
					{
						tempLine = "";
					}
					else if(firstCommentMark.equals("/**/"))
					{
						tempLine = removeStarComment(tempLine);
					}
					else if(firstCommentMark.equals("/*"))
					{
						tempLine = removeOpenStarComment(tempLine);
						commentOn = true;
					}
					else if(firstCommentMark.equals("--"))
					{
						tempLine = removeDashComment(tempLine);
					}
					else
					{
						loop = false;
					}
				}
			}
				
			
			buffer += tempLine+" ";

			while(buffer.indexOf(";") >= 0)
			{
				String arrangedSQL = SQLScriptExecuter.substring(buffer, 0, buffer.indexOf(";")-1);
				arrangedSQL = removeSpace(arrangedSQL);
				buffer = SQLScriptExecuter.substring(buffer, buffer.indexOf(";")+1, buffer.length()-1);
				
//				System.out.println("arrangedSQL: "+arrangedSQL);
				
				if(!toFile)
				{
					memoryForSQLs.add(arrangedSQL);
				}
				else
				{
					fileForSQLs.writeLine(arrangedSQL);
				}
					
			}
		}
		

		
		scriptFile.close();
		scriptFile = null;
		
		
		String[] result = null;
		if(!toFile)
		{
			result = new String[memoryForSQLs.size()];
			memoryForSQLs.toArray(result);
			memoryForSQLs = null;
		}
		else
		{
			result = new String[1];
			result[0] = fileForSQLsName;
			fileForSQLs.close();
		}

		return result;
	}	
	
	
	
	
	

	
	
	
	private static synchronized String substring(final String str, int start, int end)
	{
		String result = null;
		if(end<=start)
		{
			result = "";
		}
		else if((end < 0 )||(start > str.length()-1))
		{
			result = "";
		}
		else
		{
			if(start < 0)
			{
				start = 0;
			}
			
			if(end > str.length()-1)
			{
				end = str.length()-1;
			}
			
			result = str.substring(start, end+1);
		}
			
		return result;
	}


	public static void main(String[] args) throws InterruptedException, ClassNotFoundException, SQLException, IOException, InstanceNotExistException
	{
//		SQLScriptExecuter_DEV se = new SQLScriptExecuter_DEV();
//		se.setConnectionPoolAdaptor(ConnectionPoolAdaptor.getInstance("settings/database.properties"));
//		se.openSession();
//		se.execute("d:/temp/cr40.sql");
//		se.closeSession();
		
		String[] result = SQLScriptExecuter.parse("d:/temp/cr40.sql", false);
		for(int i=0; i<result.length; ++i)
		{
			System.out.println(result[i]);
		}
	}
}

