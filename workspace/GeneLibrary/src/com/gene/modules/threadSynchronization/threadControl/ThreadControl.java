package com.gene.modules.threadSynchronization.threadControl;

import java.util.HashSet;

public class ThreadControl
{
	private HashSet<Thread> runningThreads;
	private ThreadControlStart threadControlStart;
	private ThreadControlFinish threadControlFinish;
	
	public ThreadControl()
	{
		this.runningThreads = new HashSet<Thread>();
		this.threadControlStart = new ThreadControlStart(runningThreads);
		this.threadControlFinish = new ThreadControlFinish(runningThreads);
	}
	
	public boolean isThreadStarted()
	{
		return this.threadControlStart.isThreadStarted();
	}
	
	public synchronized void notifyfThreadStarted()
	{
		this.threadControlStart.notifyfThreadStarted();
	}
	
	public void waitUntilThreadsStarted() throws InterruptedException
	{
		this.threadControlStart.waitUntilThreadsStarted();
	}
	
	public boolean isEveryThreadFinished()
	{
		return this.threadControlFinish.isEveryThreadFinished();
	}
	
	public synchronized void notifyfThreadFinished()
	{
		this.threadControlFinish.notifyfThreadFinished();
	}
	
	public void waitUntilEveryThreadsFinished() throws InterruptedException
	{
		this.threadControlFinish.waitUntilEveryThreadsFinished();
	}
}





	
		


