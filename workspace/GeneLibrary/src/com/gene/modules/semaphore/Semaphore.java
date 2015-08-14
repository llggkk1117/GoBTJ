package com.gene.modules.semaphore;

public class Semaphore
{
	private Thread currentOwner;
	
	// Acquires a semaphore and hold it so that other threads stop and sleep here and wait for the semaphore to be released.
	// Codes between semaphore.acquire() and semaphore.release() is locked by a thread holding the semaphore
	// This mechanism makes this method like a static synchronized method
	public synchronized void acquire() throws InterruptedException
	{
		while(currentOwner != null)
		{
        	wait();
		}
		this.currentOwner = Thread.currentThread();
    }
	
	// returns semaphore to wake up other threads and keep working
    public synchronized void release()
    {
        if (this.currentOwner == Thread.currentThread())
        {
        	this.currentOwner = null;
        	notifyAll();
        }
    }
}