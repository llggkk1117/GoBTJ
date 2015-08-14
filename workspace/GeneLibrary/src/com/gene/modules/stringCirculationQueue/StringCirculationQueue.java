package com.gene.modules.stringCirculationQueue;

public class StringCirculationQueue
{
	private String[] queue;
	private int indexHead;
	private int indexTail;
	private int size;
	private int numOfItems;
	
	public StringCirculationQueue(int a_size)
	{
		size = a_size;
		queue = new String[size];
		indexHead =-1;
		indexTail = -1;
		numOfItems = 0;
	}
	
	public synchronized boolean isFull()
	{
		if(this.numOfItems == this.size)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public synchronized boolean isEmpty()
	{
		if(numOfItems == 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public synchronized boolean put(String item)
	{
		boolean success;
		if(numOfItems == 0) //텅비었�?�때
		{
			indexHead = 0;
			indexTail = 0;
			queue[0] = item;
			numOfItems++;
			success = true;
		}
		else if	(numOfItems == size) // 꽉 찼�?� 때
		{
			success = false;
		}
		else
		{
			indexTail = (indexTail+1) % size;
			queue[indexTail] = item;
			numOfItems++;
			success = true;
		}
		return success;
	}
	
	public synchronized String pop()
	{
		String result;
		if(numOfItems == 1) //1개가 남았�?� 때
		{
			result = queue[indexHead];
			indexHead = -1;
			indexTail = -1;
			numOfItems = 0;
		}
		else if(numOfItems == 0) //텅비었�?�때
		{
			result = null;
		}
		else
		{
			result = queue[indexHead];
			indexHead = (indexHead+1) % size;
			numOfItems--;
		}
		return result;
	}
	
	public synchronized int getQueueSize()
	{
		return size;
	}
	
	public synchronized int getNumOfItems()
	{
		return this.numOfItems;
	}
	
	public static void main(String[] args)
	{
		StringCirculationQueue q = new StringCirculationQueue(3);
		q.put("a");
		q.put("b");
		q.put("c");
		System.out.println(q.pop());
		q.put("d");
		System.out.println(q.pop());
		System.out.println(q.pop());
		System.out.println(q.pop());
		
	}
}