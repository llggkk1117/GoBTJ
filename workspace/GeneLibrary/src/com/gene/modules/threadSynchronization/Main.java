package com.gene.modules.threadSynchronization;

import org.apache.log4j.Logger;

public class Main
{
	private static final Logger logger = Logger.getLogger(Main.class);
	private static final int numOfThread = 10;

	public void run() throws InterruptedException
	{
		for(int i=0; i<numOfThread; ++i)
		{
			Child child = new Child();
			Thread childThread = new Thread(child);
			childThread.start();
		}
		
		Child.threadControl.waitUntilThreadsStarted();
		Child.threadControl.waitUntilEveryThreadsFinished();
		Child.instanceInfo.clear();
	}
}
