package com.gene.modules.simpleTest;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class ArrayTest
{
	public static void createArray()
	{
		String[] a = {"a", "b"};
		Object[] b = new Object[]{"a", "b"};
		Object[] c = {"a", "b"};
		System.out.println(a[0]);
		System.out.println(b[0]);
		System.out.println(c[0]);
	}	
	
	public static void arrayToList()
	{
		String[] arr = {"a", "b", "c"};
		
		// array를 list형태로 만들어서 return한다.
	
		List<String> list = Arrays.asList(arr);
		System.out.println(list.get(0));
		arr[0]="d";
		System.out.println(list.get(0));
		
		System.out.println(list.size());
		list.add(3, "q");
	}
	
	
	public static void arrayToVector()
	{
		String[] arr = {"a", "b", "c"};
		Vector<String> vec = new Vector<String>();
		vec.addAll(Arrays.asList(arr));
		
		System.out.println(vec.elementAt(0));
		arr[0]="d";
		System.out.println(vec.elementAt(0));
	}
	
	public static void main(String[] args)
	{
		arrayToList();
//		arrayToVector();
	}
}
