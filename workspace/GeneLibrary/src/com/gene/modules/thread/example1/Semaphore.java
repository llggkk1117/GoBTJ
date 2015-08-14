package com.gene.modules.thread.example1;

public class Semaphore
{
	private Thread currentOwner;
	public synchronized void acquire() throws InterruptedException
	{
		while(currentOwner != null)
		{
			System.out.println(Thread.currentThread()+" wait");
        	wait();
        	System.out.println(Thread.currentThread()+" woke up");
		}
		currentOwner = Thread.currentThread();
		System.out.println(currentOwner+" got");
    }
	
    public synchronized void release()
    {
        if (currentOwner == Thread.currentThread())
        {
        	System.out.println(currentOwner+" release");
        	currentOwner = null;
        	notifyAll();
        	//notify();
        }
    }
}