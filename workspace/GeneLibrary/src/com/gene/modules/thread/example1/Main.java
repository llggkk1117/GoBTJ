package com.gene.modules.thread.example1;

import java.util.Vector;

public class Main
{
	public static void main(String[] args)
	{
		Vector<Consumer> consumers = new Vector<Consumer>();
		Vector<Thread> threads = new Vector<Thread>();
		
		int NUM_OF_THREAD = 10;
		
		for(int i=0; i<NUM_OF_THREAD; ++i)
		{
			consumers.add(new Consumer());
			threads.add(new Thread(consumers.lastElement()));
		}
		
		for(int i=0; i<NUM_OF_THREAD; ++i)
		{
			threads.elementAt(i).start();
		}
	}
}
