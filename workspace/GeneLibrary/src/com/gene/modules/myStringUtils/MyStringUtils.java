package com.gene.modules.myStringUtils;

import com.gene.modules.check.Check;

public class MyStringUtils
{
	public static String[] split(String originalString, String delimiter)
	{
		String[] result = null;
		if(Check.NoBlankExists(originalString, delimiter))
		{
			result =  originalString.split(delimiter);
		}
		
		return result;
	}
	
	public static void main(String[] args)
	{
		String[] result = MyStringUtils.split("abc,,de", ",,");
		
		for(int i=0; i<result.length; ++i)
		{
			System.out.println(result[i]);
		}
	}
}



	
