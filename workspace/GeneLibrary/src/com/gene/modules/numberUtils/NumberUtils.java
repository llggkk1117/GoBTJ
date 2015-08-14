package com.gene.modules.numberUtils;

public class NumberUtils
{
	public static boolean isNumeric(String str)
	{
		boolean numeric = true;
		try
		{
			Double.parseDouble(str);
		}
		catch(NumberFormatException e)
		{
			numeric = false;
		}
		
		return  numeric;
	}
	
	private static float Round(float value, int place)
	{
		float p = (float)Math.pow(10,place);
		value = value * p;
		float tmp = Math.round(value);
		return (float)tmp/p;
	}
}
