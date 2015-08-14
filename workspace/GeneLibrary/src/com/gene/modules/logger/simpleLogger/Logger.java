package com.gene.modules.logger.simpleLogger;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Vector;

import com.gene.modules.check.Check;
import com.gene.modules.exceptions.InstanceAlreadyExistException;
import com.gene.modules.semaphore.Semaphore;
import com.gene.modules.textFile.TextFile;




public class Logger 
{
	private static HashMap<String, Logger> instanceRegistry;
	private static Semaphore semaphore;
	static
	{
		instanceRegistry = new HashMap<String, Logger>();
		semaphore = new Semaphore();
	}
	
	private String logfileName;
	private TextFile logfile; 
	private Integer logLevel;
	
	
	public static class LoggingLevel
	{
		public static final int DEBUG = 1; 
		public static final int INFO  = 2;
		public static final int WARN = 3;
		public static final int ERROR = 4;
		public static final int FATAL = 5;
		public static final int DEFAULT = Logger.LoggingLevel.INFO;
	}
	
	
	
	public Logger(final String fileName) throws InterruptedException, InstanceAlreadyExistException, IOException  
	{
		if(Check.anyBlankExists(fileName))
		{
			throw new IllegalArgumentException();
		}
		
		Logger.semaphore.acquire();

		if(Logger.instanceRegistry.get(fileName.toLowerCase()) == null)
		{
			this.logfile = TextFile.getFile(fileName);
			this.logfileName = fileName;
			this.setLogLeve(Logger.LoggingLevel.DEFAULT);
			
			Logger.instanceRegistry.put(fileName.toLowerCase(), this);
		}
		else
		{
			throw new InstanceAlreadyExistException("Logger instance of "+fileName+" already exists.");
		}
		
		Logger.semaphore.release();
	}
	
	
	protected synchronized void finalize() throws Throwable
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
	
	
	private synchronized static int adjustLogLeve(final int logLevel)
	{
		int log_level = logLevel; 
		if(log_level < Logger.LoggingLevel.DEBUG)
		{
			log_level = Logger.LoggingLevel.DEBUG;
		}
		else if(log_level > Logger.LoggingLevel.FATAL)
		{
			log_level = Logger.LoggingLevel.FATAL;
		}
		
		return log_level;
	}

	
	public synchronized void setLogLeve(final int logLevel)
	{
		this.logLevel = Logger.adjustLogLeve(logLevel);
	}
	
	
	public synchronized void close() throws IOException
	{
		if(logfile != null)
		{
			this.logfile.close();
		}
		this.logfile = null;
		this.logfileName = null;
		Logger.instanceRegistry.remove(this.logfileName.toLowerCase());
	}
	
	
	public synchronized void writeLog(final String message, final int logLevel)
	{
		String msg = message;
		if(msg == null)
		{
			msg = "";
		}
		
		if(logLevel >= this.logLevel)
		{
			try
			{
				String now = (new GregorianCalendar()).getTime().toString();
				String logMessage = "["+now+"] "+msg;
				this.logfile.writeLine(logMessage);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	
	
	public static synchronized Logger getInstance(String fileName) throws InstanceAlreadyExistException, InterruptedException, IOException 
	{
		Logger instance = Logger.instanceRegistry.get(fileName); 
		if(instance == null)
		{
			Logger newInstance = new Logger(fileName);
			instance = newInstance;
		}
		
		return instance;
	}

	
	public static synchronized Integer getNumOfInstance() throws IOException
	{
		return Logger.instanceRegistry.size();
	}

	
	public static synchronized void removeInstance(String fileName) throws IOException
	{
		Logger temp = Logger.instanceRegistry.get(fileName);
		if(temp != null)
		{
			temp.close();
		}
	}

	
	public static synchronized void removeEveryInstance() throws IOException
	{
		String[] everyFileName = Logger.getEveryKey();
		
		for(int i=0; i<everyFileName.length; ++i)
		{
			Logger.instanceRegistry.get(everyFileName[i]).close();
		}
		
		Logger.instanceRegistry.clear();
	}
	
	
	public static synchronized String[] getEveryKey() throws IOException
	{
		Vector<String> everyKey = new Vector<String>(Logger.instanceRegistry.keySet());
		String[] everyKeyArray = new String[everyKey.size()];
		everyKey.toArray(everyKeyArray);
		
		return everyKeyArray;
	}
	
	

	public static synchronized boolean checkIfInstanceExist(final String fileName)
	{
		if(Check.anyBlankExists(fileName))
		{
			throw new IllegalArgumentException();
		}
		
		return (Logger.instanceRegistry.get(fileName.toLowerCase()) != null); 
	}
	
	
	
	
	public static void main(String[] args) throws IOException, InstanceAlreadyExistException, InterruptedException
	{
		Logger log1 = Logger.getInstance("log.txt");
		Logger log2 = Logger.getInstance("log.txt");
		Logger log3 = Logger.getInstance("log.txt");
		Logger log4 = Logger.getInstance("log.txt");
		Logger log5 = Logger.getInstance("abc.txt");
		System.out.println(log1.toString());
		System.out.println(log2.toString());
		System.out.println(log3.toString());
		System.out.println(log4.toString());
		System.out.println(log5.toString());	
		
		System.out.println(log1.logfile.toString());
		System.out.println(log2.logfile.toString());
		System.out.println(log3.logfile.toString());
		System.out.println(log4.logfile.toString());
		
//		System.out.println(Logger.getNumOfInstance());
//		Logger.removeInstance("abc2.txt");
//		System.out.println(Logger.getNumOfInstance());
//		Logger.removeEveryInstance();
//		System.out.println(Logger.getNumOfInstance());
	}
}