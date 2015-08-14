package com.gene.modules.simpleTest.utils;

import org.apache.commons.lang.ObjectUtils;

public class ObjectUtilsTest
{
	public static void test1()
	{
		System.out.println(ObjectUtils.equals("aa", "aa"));
		System.out.println(ObjectUtils.equals(new Object(), new Object()));
	}
	
	public static void main(String[] args)
	{
		ObjectUtilsTest.test1();
	}
}
