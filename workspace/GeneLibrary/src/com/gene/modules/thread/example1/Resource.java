package com.gene.modules.thread.example1;

public class Resource
{
	private static Semaphore semaphore;
	static
	{
		semaphore = new Semaphore();
	}
	
	public Resource(int id) throws InterruptedException
	{
		// use static semaphore to make critical section in non-static function
		Resource.semaphore.acquire(); // critical section begin
		System.out.println(Thread.currentThread()+": resouce "+id+" created.");
		Resource.semaphore.release(); // critical section end
	}
	
	public static synchronized void func1()
	{
		// inside of static synchronized function becomes critical section automatically
	}
	
	public synchronized void func2()
	{
		// every function needs to be synchronized for keeping consistency
	}
	
	private synchronized void func3()
	{
		// every function needs to be synchronized for keeping consistency
	}
}


