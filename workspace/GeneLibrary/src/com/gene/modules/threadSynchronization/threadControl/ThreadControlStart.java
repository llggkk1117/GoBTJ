package com.gene.modules.threadSynchronization.threadControl;

import java.util.HashSet;

public class ThreadControlStart
{
	private HashSet<Thread> runningThreads;
	
	public ThreadControlStart(HashSet<Thread> runningThreads)
	{
		this.runningThreads = runningThreads;
	}
	
	public boolean isThreadStarted()
	{
		return (this.runningThreads.size() > 0);
	}
	
	public synchronized void notifyfThreadStarted()
	{
		this.runningThreads.add(Thread.currentThread());
		notifyAll();
	}
	
	public synchronized void waitUntilThreadsStarted() throws InterruptedException
	{
		while(!this.isThreadStarted())
		{
        	wait();
		}
	}
}