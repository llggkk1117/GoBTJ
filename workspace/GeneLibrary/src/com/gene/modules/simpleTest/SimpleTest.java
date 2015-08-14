package com.gene.modules.simpleTest;

import java.io.File;
import java.util.Arrays;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.govgrnds.core.util.crypto.DefaultCryptoHelper;

public class SimpleTest
{
	public static void func1()
	{
		System.out.println(1<<4);  // = 1*(2^4)
		System.out.println(2<<3);  // = 2*(2^3)
		System.out.println(256 >> 2); // = 256 / (2^2)
		System.out.println(1026 & 0xFF); // 1024 % (0xFF+1) = 1024 % 256
	}
	
	public static void func2()
	{
		Vector<String> set = new Vector<String>();
		set.add("aaa");
		set.add("bbb");
		set.add("ccc");
		
		String[] arr = new String[set.size()];
		set.toArray(arr);
		for(int i=0; i<arr.length; ++i)
		{
			System.out.println(arr[i]);
		}
	}
	
	public static void func3()
	{
		String strDirectoy ="test";
		String strManyDirectories="dir1/dir2/dir3";
		//(new File(strDirectoy)).mkdir();
		(new File(strDirectoy)).mkdirs();
		//(new File(strManyDirectories)).mkdirs();
		System.out.println((new File(strDirectoy)).exists());
	}
	
	public static void func4()
	{
		DefaultCryptoHelper crypto = new DefaultCryptoHelper();
		System.out.println(crypto.encrypt("aaa"));
	}
	
	public static void func5()
	{
		Object val1 = false;
		boolean val2 = false;
		Boolean val3 = false;
		Boolean val4 = null;
		String val5 = null;
		
		System.out.println(val1.equals(val2));
		System.out.println(val1 == val3);
		System.out.println(val1.equals(val3));
	}
	
	public static void func6()
	{
		Vector<String> v = new Vector<String>();
		v.add("aa");
		v.add("bb");
		v.add("cc");
		
		String[] s = new String[v.size()];
		v.toArray(s);
		
		for(int i=0; i<s.length; ++i)
		{
			System.out.println(s[i]);
		}
		
		v.clear();
		v = null;
		
		System.out.println("---");
		for(int i=0; i<s.length; ++i)
		{
			System.out.println(s[i]);
		}
	}
	
	public static void func7()
	{
		char[] alphabetLowerCase = new char[26];
		for (int i=0; i<alphabetLowerCase.length; ++i)
		{
			alphabetLowerCase[i] = (char) ('a'+i);
		}
		String everyChar = String.valueOf(alphabetLowerCase);
		System.out.println(everyChar);
	}
	
	public static void func8()
	{
		System.out.println("\\");
		System.out.println("\'");
	}
	
	public static void func9()
	{
		StringBuilder s = new StringBuilder("aaa").append(1).append(0.5);
		s.append("bbb");
		System.out.println(s.toString());
		
		System.out.println(StringUtils.isNotBlank(null)); 
		System.out.println(StringUtils.isNotBlank(""));
		System.out.println(StringUtils.isNotBlank(" "));
		
		System.out.println(StringUtils.isEmpty(null)); 
		System.out.println(StringUtils.isEmpty(""));
		System.out.println(StringUtils.isEmpty(" "));
	}
	
	public static void func10()
	{
		String[] temp = "'aaa'bb".split("'");
		for(int i=0; i<temp.length; ++i)
		{
			System.out.println(temp[i]);
		}
	}
	
	
	
	public static void func11()
	{
		String[] arrary = new String[]{"aa", "bb"};
		Vector<String> v = new Vector<String>(Arrays.asList(arrary));
		
		for(int i=0; i<v.size(); ++i)
		{
			System.out.println(v.elementAt(i));
		}
	}
	
	public static void func12()
	{
		throw new IllegalArgumentException();
	}
	
	public static void func13()
	{
		try
		{
			Double.parseDouble("!1");
		}
		catch(Exception e)
		{
			System.out.println("error");
		}
	}
	
	public static void func14()
	{
		String a = "";
		char[] bb = a.toCharArray();
		System.out.println(bb!=null);
		System.out.println(bb.length);
		for(int i=0; i<bb.length; ++i)
		{
			System.out.println(bb[i]+"<--");
		}
	}
	
	
	public static void func15()
	{
		String str = "bbbbaaabbbbbaaabbbbbbbaaaabbbbbbb";;
		String temp = str;
		int num = 0;
		while(temp.contains("aaa"))
		{
			num++;
			temp = temp.replaceFirst("aaa", "");
		}
		System.out.println(temp);
		System.out.println(str);
		System.out.println(num);
	}

	
	public static void main(String[] args)
	{
		SimpleTest.func15();
	}
}
