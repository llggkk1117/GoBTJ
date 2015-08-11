package org.gene.modules.properties;

import java.io.FileInputStream;
import java.io.IOException;


public class Properties extends java.util.Properties
{
	private static final long serialVersionUID = -2839697579626698833L;
	private boolean caseSensitive;
	
	public Properties(String fileName, boolean caseSensitive) throws IOException
	{
		super();
		this.caseSensitive= caseSensitive;
		if(fileName!=null && !"".equals(fileName))
		{
			this.loadFile(fileName);
		}
	}
	
	public Properties(String fileName) throws IOException
	{
		this(fileName, false);
	}
	
	public Properties(boolean caseSensitive) throws IOException
	{
		this(null, caseSensitive);
	}
	
	public Properties() throws IOException
	{
		this(null, false);
	}
	
	public synchronized void loadFile(String fileName) throws IOException
	{
		FileInputStream fileInputStream = new FileInputStream(fileName);
        
		if(this.caseSensitive)
		{
			super.load(fileInputStream);
		}		
		else
		{
			java.util.Properties propertiesTemp =  new java.util.Properties();
			propertiesTemp.load(fileInputStream);
			Object[] keyArray = propertiesTemp.keySet().toArray();
	        for(int i=0; i<keyArray.length; ++i)
	        {
	        	String key = (String)keyArray[i];
	        	String value = propertiesTemp.getProperty(key);
	        	super.put(key.toLowerCase(), value);
	        }
	        propertiesTemp.clear();
		}
		fileInputStream.close();
	}
	
	public synchronized String getProperty(String key)
	{
		String value = null;
		if(key != null)
		{
			if(!this.caseSensitive)
			{
				key = key.toLowerCase();
			}
			value = super.getProperty(key);
		}
		return value;
	}
	
	public synchronized Object setProperty(String key, String value)
	{
		Object previouseValue = null;
		if(key != null)
		{
			if(!this.caseSensitive)
			{
				key = key.toLowerCase();
			}
			previouseValue = super.setProperty(key, value);
		}
		return previouseValue;
	}
	
	public synchronized boolean containsKey(String key)
	{
		boolean constains = false;
		if(key != null)
		{
			if(!this.caseSensitive)
			{
				key = key.toLowerCase();
			}
			constains = super.containsKey(key);
		}
		return constains;
	}
	
	public static void main(String[] args) throws IOException
	{
		Properties p = new Properties("settings/database.properties");
		System.out.println(p.getProperty("usernAme"));
	}
}
