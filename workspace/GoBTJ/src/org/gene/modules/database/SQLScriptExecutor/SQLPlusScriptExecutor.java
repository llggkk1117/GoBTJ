package org.gene.modules.database.SQLScriptExecutor;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;



public class SQLPlusScriptExecutor
{
	private static final String[] errorCode = new String[]{"SP2", "ORA"};
	private static final String file_extension = ".sql";
	private String[] log;
	
	private static synchronized File[] getFileList(String filePath)
	{
		if(StringUtils.isBlank(filePath))
		{
			throw new IllegalArgumentException();
		}
		
		File[] fileList = null;
		File tempFile = new File(filePath);
		if(tempFile.isDirectory())
		{
			FileFilter fileFilter = new FileFilter()
			{
				public boolean accept(File file)
				{
					if (file.getName().toLowerCase().endsWith(file_extension))
					{
						return true;
					}
					else
					{
						return false;
					}
				}
			};
	        		
			fileList= tempFile.listFiles(fileFilter);
		}
		else
		{
			if(tempFile.getName().toLowerCase().endsWith(file_extension))
			{
				fileList = new File[]{tempFile};
			}
		}
		
		return fileList;
	}
	

	
	
	private static synchronized Vector<String> singleExecute(File scriptFile) throws IOException
	{
		if(scriptFile == null)
		{
			throw new IllegalArgumentException();
		}
		
		
		String script_location = "@" + scriptFile.getAbsolutePath(); //ORACLE
		ProcessBuilder processBuilder = new ProcessBuilder("sqlplus", "odpair/odpair@localhost:1521/xe", script_location); //ORACLE
		//String script_location = "-i" + fileList[i].getAbsolutePath();
		//ProcessBuilder processBuilder = new ProcessBuilder("sqlplus", "-Udeep-Pdumbhead-Spc-de-deep\\sqlexpress-de_com",script_location);
		processBuilder.redirectErrorStream(true);
		Process process = processBuilder.start();
		
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
		bufferedWriter.write("\r\n");
		bufferedWriter.flush();
		bufferedWriter.write("quit\r\n");
		bufferedWriter.flush();

		BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
		
		Vector<String> vectorLog = new Vector<String>(); 

		String currentLine = null;
		while ((currentLine = in.readLine()) != null)
		{
			if(StringUtils.isNotBlank(currentLine.trim()))
			{
				while(currentLine.indexOf("SQL>") >= 0)
				{
					currentLine = currentLine.replace("SQL>", "").trim();
				}
				vectorLog.add(currentLine);
			}
		}

		
		return vectorLog;
	}
	
	
	

	
	public synchronized SQLPlusScriptExecutor execute(String filePath) throws IOException
	{
		if(StringUtils.isBlank(filePath))
		{
			throw new IllegalArgumentException();
		}
		File[] fileList = getFileList(filePath);
		if(fileList == null)
		{
			throw new IllegalArgumentException();
		}
		
		Vector<String> everyLog = new Vector<String>(); 
		
		for (int i = 0; i<fileList.length;i++)
		{
			Vector<String> singleLog = singleExecute(fileList[i]);
			everyLog.addAll(singleLog);
		}
		
		this.log = null;
		this.log = new String[everyLog.size()];
		everyLog.toArray(this.log);
		
		return this;
	}
	
	
	public synchronized String[] getLog()
	{
		return this.log;
	}
	
	
	public synchronized int getNumOfErrors()
	{
		return getNumberOfErrors(this.log);
	}
	
	
	private static synchronized int getNumberOfErrors(String[] logs)
	{
		if(logs == null)
		{
			throw new IllegalArgumentException();
		}
		
		int numOfError = 0;
		for(int i=0 ; i<logs.length; ++i)
		{
			for(int j=0; j<errorCode.length; ++j)
			{
				if(logs[i].toUpperCase().startsWith(errorCode[j]+"-"))
				{
					numOfError++;
					break;
				}
			}
		}
		return numOfError;
	}
	
	
	public static void main(String[] args) throws IOException
	{
		SQLPlusScriptExecutor s = new SQLPlusScriptExecutor();
		String[] logs = s.execute("d:/temp/cr40.sql").getLog();
		int numOfError = s.getNumOfErrors();
		System.out.println("numOfError: "+numOfError);
		for(int i=0 ; i<logs.length; ++i)
		{
			System.out.println(logs[i]);
		}
	}
}
