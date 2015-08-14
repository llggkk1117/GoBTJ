package com.gene.modules.simpleTest;

import java.util.HashMap;
import java.util.Map;

public class MapTest
{
	public static void test1()
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("1", "aa");
		map.put("1", "bb");
		System.out.println(map.get("1"));
		map.remove("1");
		System.out.println(map.get("1"));
	}

	public static void main(String[] args)
	{
		MapTest.test1();
	}
}
