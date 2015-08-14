package com.gene.modules.thread.example1;

public class Consumer implements Runnable
{
	public void run()
	{
		for(int i=0; i<10; ++i)
		{
			try
			{
				Resource tr = new Resource(i);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			Thread.yield();
		}
	}
}