package com.gene.modules.thread.superSingleton;

import java.util.HashMap;
import java.util.Set;

public final class SharedResource
{
	private static HashMap<String, SharedResource> instanceRegistry;
	private static Semaphore semaphore;
	static
	{
		instanceRegistry = new HashMap<String, SharedResource>();
		semaphore = new Semaphore();
	}
	
	
	public SharedResource() throws InterruptedException 
	{
		SharedResource.semaphore.acquire();

		// Critical Section
		
		SharedResource.semaphore.release();
	}
	
	
	
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
	
	
	public synchronized void close()
	{
		
	}

	
	public static synchronized SharedResource getInstance(String key) throws InterruptedException
	{
		SharedResource instance = SharedResource.instanceRegistry.get(key); 
		if(instance == null)
		{
			instance = new SharedResource();
		}
		return instance;
	}
	
	
	public static Integer getNumOfInstance()
	{
		return SharedResource.instanceRegistry.size();
	}

	
	public static synchronized void removeInstance(String key)
	{
		SharedResource temp = SharedResource.instanceRegistry.get(key);
		if(temp != null)
		{
			temp.close();
			temp  = null;
		}
	}

	
	public static synchronized void removeEveryInstance()
	{
		String[] everyKey = SharedResource.getEveryKey();
		for(int i=0; i<everyKey.length; ++i)
		{
			SharedResource.removeInstance(everyKey[i]);
		}
		SharedResource.instanceRegistry.clear();
	}
	
	
	public static synchronized String[] getEveryKey()
	{
		Set<String> keySet = SharedResource.instanceRegistry.keySet();
		Object[] keySetObjectArray = keySet.toArray();
		String[] keySetStringArray = new String[keySetObjectArray.length];
		for(int i=0; i<keySetStringArray.length; ++i)
		{
			keySetStringArray[i] = (String)keySetObjectArray[i];
		}
		keySet = null;
		keySetObjectArray = null;
		
		return keySetStringArray;
	}
	

	
	public static synchronized boolean checkIfInstanceExist(String key)
	{
		if(SharedResource.instanceRegistry.get(key) != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
