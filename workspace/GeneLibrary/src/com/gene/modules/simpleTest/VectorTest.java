package com.gene.modules.simpleTest;

import java.util.Vector;

public class VectorTest
{
	public static void test1()
	{
		Vector<String> str = new Vector<String>();
		str.add(null);
		str.add("a");
		str.add("b");
		System.out.println(str.size());
	}
	
	public static void vectorToArray()
	{
		Vector<String> vec = new Vector<String>();
		vec.add("ab");
		vec.add("cde");
		vec.add("fg");
		
		String[] arr = new String[vec.size()];
		vec.toArray(arr);
		
		for(int i=0; i<arr.length; ++i)
		{
			System.out.println(arr[i]);
		}
		
		vec.clear();
		
		for(int i=0; i<arr.length; ++i)
		{
			System.out.println(arr[i]);
		}
	}
	
	public static void main(String[] args)
	{
		vectorToArray();
	}
}
