package com.gene.modules.logger.previousVersion;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.gene.modules.exceptions.InstanceAlreadyExistException;
import com.gene.modules.features.StacktraceToStringFeature;
import com.gene.modules.textFile.TextFile;



public class Logger 
{
	private static HashMap<String, Logger> loggers = new HashMap<String, Logger>();
	
	private String logfileName;
	private TextFile logfile; 
	private Integer logLevel;
	private boolean logfileOpen;
	private static StacktraceToStringFeature ss = new StacktraceToStringFeature();
	
	public static final Integer EVERYTHING = 1; 
	public static final Integer NORMAL = 2;
	public static final Integer MINIMAL = 3;
	
	public Logger()
	{
		logfile = null;
		logLevel = Logger.NORMAL;
		logfileOpen = false;
	}
	
	public Logger(String fileName) throws IOException, InstanceAlreadyExistException, InterruptedException
	{
		this();
		this.openLogfile(fileName);
	}
	
	public Logger(String fileName, Integer logLevel) throws IOException, InstanceAlreadyExistException, InterruptedException
	{
		this();
		this.openLogfile(fileName);
		this.setLogLeve(logLevel);
	}
	
	protected void finalize() throws Throwable
	{
		try
		{
			this.closeLogfile();
		}
		finally
		{
			super.finalize();
		}
	}

	public void setLogLeve(Integer logLevel)
	{
		this.logLevel = logLevel;
	}
	
	public synchronized void openLogfile(String fileName) throws IOException, InstanceAlreadyExistException, InstanceAlreadyExistException, InterruptedException
	{
		if(fileName != null)
		{
			if(!logfileOpen)
			{
				logfileName = this.fixSlash(fileName);
				logfile = TextFile.getFile(logfileName);
				logfileOpen = true;
			}
		}
	}
	
	public synchronized void closeLogfile() throws IOException
	{
		if(logfile != null)
		{
			if(logfileOpen)
			{
				logfile.close();
				logfile = null;
				logfileName = null;
				logfileOpen = false;
			}
		}
	}
	
	
	public synchronized void showMessage(String message, int priorityForScrren, int priorityForLogfile)
	{
		boolean printOnScreen = (priorityForScrren >= logLevel);
		boolean printOnLogfile = (priorityForLogfile >= logLevel);

		String now = null;
		if (printOnScreen||printOnLogfile)
		{
			now = (new GregorianCalendar()).getTime().toString();
		}
		
		if(printOnScreen)
		{
			System.out.println("["+now+"] "+message);
		}
		
		if((printOnLogfile)&&(logfile != null))
		{
			try
			{
				logfile.writeLine("["+now+"] "+message);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	
	public synchronized void showMessage(String message)
	{
		String now = (new GregorianCalendar()).getTime().toString();
		System.out.println("["+now+"] "+message);
		if(logfile != null)
		{
			try
			{
				logfile.writeLine("["+now+"] "+message);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void showMessage(String message, boolean printOnScreen, boolean printOnFile)
	{
		String now = (new GregorianCalendar()).getTime().toString();
		
		if(printOnScreen)
		{
			System.out.println("["+now+"] "+message);
		}

		if(printOnFile)
		{
			if(logfile != null)
			{
				try
				{
					logfile.writeLine("["+now+"] "+message);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	
	public static synchronized String stackToString(Exception e)
	{
		return ss.stacktraceToString(e);
	}

	
	private String fixSlash(String str)
	{
		String result = null;
		if(str != null)
		{
			result = str.replace('\\', '/');
		}
		return result;
	}
	
	
	public static void main(String[] args) throws IOException, InstanceAlreadyExistException, InterruptedException
	{
		Logger log1 = new Logger("abc1.txt");
		Logger log2 = new Logger("abc1.txt");
		Logger log3 = new Logger("abc1.txt");
		Logger log4 = new Logger("abc1.txt");
		System.out.println(log1.toString());
		System.out.println(log2.toString());
		System.out.println(log3.toString());
		System.out.println(log4.toString());
		
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