package com.gene.modules.thread.superSingleton;

public class Semaphore
{
	private Thread currentOwner;
	public synchronized void acquire() throws InterruptedException
	{
		while(currentOwner != null)
		{
        	wait();
		}
		currentOwner = Thread.currentThread();
    }
	
    public synchronized void release()
    {
        if (currentOwner == Thread.currentThread())
        {
        	currentOwner = null;
        	notifyAll();
        }
    }
}