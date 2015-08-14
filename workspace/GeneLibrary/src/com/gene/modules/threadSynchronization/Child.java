package com.gene.modules.threadSynchronization;

import com.gene.modules.threadSynchronization.instance.InstanceInfo;
import com.gene.modules.threadSynchronization.threadControl.ThreadControl;



public class Child implements Runnable
{
	public static ThreadControl threadControl;
	public static InstanceInfo<Child> instanceInfo;
	static
	{
		Child.threadControl = new ThreadControl();
		Child.instanceInfo = new InstanceInfo<Child>();
	}
	private int instanceId;
	
	public Child()
	{
		this.instanceId = Child.instanceInfo.notifyInstanceCreated(this);
	}
	
	public void run()
	{
		Child.threadControl.notifyfThreadStarted();
		
		boolean loop = true;
		while(loop)
		{
			// do job
			Thread.yield();
		}
		
		Child.threadControl.notifyfThreadFinished();
	}
}
