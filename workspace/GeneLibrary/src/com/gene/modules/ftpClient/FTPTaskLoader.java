package com.gene.modules.ftpClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class FTPTaskLoader
{
	private static RandomString randomString;
	private static final String DEFAULT_ID;
	static
	{
		randomString = new RandomString(20, false);
		DEFAULT_ID = randomString.nextString();
	}
	
	public static synchronized FTPTask[] getFTPTasks(String filename) throws Exception
	{
		Properties properties = FTPTaskLoader.loadProperties(filename);
		String[] keySet = new String[properties.size()];
		properties.keySet().toArray(keySet);
		
		HashMap<String, FTPTask> ftpTaskMap = new HashMap<String, FTPTask>();
		String taskID = null;
		String propertyKey = null;
		for(int i=0; i<keySet.length; ++i)
		{
			taskID = ((keySet[i].indexOf(".") >= 0) ? (keySet[i].substring(0, keySet[i].indexOf("."))) : DEFAULT_ID);
			if(!ftpTaskMap.containsKey(taskID))
			{
				ftpTaskMap.put(taskID, new FTPTask());
			}
			
			propertyKey = keySet[i].substring(keySet[i].indexOf(".")+1, keySet[i].length());
			System.out.println(propertyKey);
			System.out.println(taskID);
			if(propertyKey.equals("FTPServerIP"))
			{
				ftpTaskMap.get(taskID).setFTPServerIP(properties.getProperty(keySet[i]));
			}
			else if(propertyKey.equals("UserName"))
			{
				ftpTaskMap.get(taskID).setUserName(properties.getProperty(keySet[i]));
			}
			else if(propertyKey.equals("Password"))
			{
				ftpTaskMap.get(taskID).setPassword(properties.getProperty(keySet[i]));
			}
			else if(propertyKey.equals("PasswordEncrypted"))
			{
				boolean encrypted = Boolean.parseBoolean(properties.getProperty(keySet[i]));
				ftpTaskMap.get(taskID).setPasswordEncrypted(encrypted);
			}
			else if(propertyKey.equals("RemoteDirectory"))
			{
				ftpTaskMap.get(taskID).setRemoteDirectory(properties.getProperty(keySet[i]));
			}
			else if(propertyKey.equals("RemoteFileName"))
			{
				ftpTaskMap.get(taskID).setRemoteFileName(properties.getProperty(keySet[i]));
			}
			else if(propertyKey.equals("LocalDirectory"))
			{
				ftpTaskMap.get(taskID).setLocalDirectory(properties.getProperty(keySet[i]));
			}
			else if(propertyKey.equals("LocalFileName"))
			{
				ftpTaskMap.get(taskID).setLocalFileName(properties.getProperty(keySet[i]));
			}
			else if(propertyKey.equals("OperationType"))
			{
				ftpTaskMap.get(taskID).setOperationType(properties.getProperty(keySet[i]));
			}
		}
		
		FTPTask[] ftpTasks = new FTPTask[ftpTaskMap.size()];  
		ftpTaskMap.values().toArray(ftpTasks);

		return ftpTasks;
	}
	
	
	private static synchronized Properties loadProperties(String filename) throws IOException
    {
    	Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream(filename);
        properties.load(fileInputStream);
        fileInputStream.close();
        fileInputStream = null;
        
        return properties;
    }
	
	public static void main(String[] args) throws Exception
	{
		FTPTask[] tasks = FTPTaskLoader.getFTPTasks("settings/SSD_Exception_File_Download.properties");
		for(int i=0; i<tasks.length; ++i)
		{
			System.out.println(tasks[i].getFTPServerIP());
			System.out.println(tasks[i].getUserName());
			System.out.println(tasks[i].getPassword());
			System.out.println(tasks[i].getPasswordEncrypted());
			System.out.println(tasks[i].getRemoteDirectory());
			System.out.println(tasks[i].getRemoteFileName());
			System.out.println(tasks[i].getLocalDirectory());
			System.out.println(tasks[i].getLocalFileName());
			System.out.println(tasks[i].getOperationType());
		}
	}
}
