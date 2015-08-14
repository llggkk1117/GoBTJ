/*
 * ValidateNumberFeature 1.1
 */

package com.gene.modules.features;

public class ValidateNumberFeature
{
	public boolean validateNumber(String number, String type)
	{
		boolean isNumber = true;
		try
		{
			if((type.toLowerCase().equals("int"))||(type.toLowerCase().equals("integer")))
			{
				Integer.parseInt(number);
			}
			else if (type.toLowerCase().equals("double"))
			{
				Double.parseDouble(number);
			}
			else if (type.toLowerCase().equals("float"))
			{
				Float.parseFloat(number);
			}
			else
			{
				isNumber = false;
			}
		}
		catch(java.lang.NumberFormatException e)
		{
			isNumber = false;
		}
		
		return isNumber;
	}
	
//	public static void main(String[] args)
//	{
//		ValidateNumberFeature v = new ValidateNumberFeature();
//		System.out.println(v.validateNumber(".1", "int"));
//	}
}
