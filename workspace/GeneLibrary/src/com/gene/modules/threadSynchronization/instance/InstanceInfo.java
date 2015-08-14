package com.gene.modules.threadSynchronization.instance;

import java.util.List;
import java.util.Vector;

public class InstanceInfo<T>
{
	private Vector<T> instances;
	public InstanceInfo()
	{
		this.instances = new Vector<T>();
	}
	
	public synchronized int notifyInstanceCreated(T instance)
	{
		int instanceId = this.instances.size();
		this.instances.add(instance);
		return instanceId;
	}
	
	public synchronized void clear()
	{
		this.instances.clear();
	}
	
	public synchronized int getNumOfInstances()
	{
		return this.instances.size();
	}
	
	public synchronized List<T> getInstances()
	{
		return this.instances;
	}
}
