package com.gene.modules.threadSynchronization.threadControl;

import java.util.HashSet;

public class ThreadControlFinish
{
	private HashSet<Thread> runningThreads;
	
	public ThreadControlFinish(HashSet<Thread> runningThreads)
	{
		this.runningThreads = runningThreads;
	}
	
	public boolean isEveryThreadFinished()
	{
		return (this.runningThreads.size() == 0);
	}
	
	public synchronized void notifyfThreadFinished()
	{
		this.runningThreads.remove(Thread.currentThread());
		if(this.isEveryThreadFinished())
		{
			notifyAll();
		}
	}
	
	public synchronized void waitUntilEveryThreadsFinished() throws InterruptedException
	{
		while(!this.isEveryThreadFinished())
		{
        	wait();
		}
	}
}