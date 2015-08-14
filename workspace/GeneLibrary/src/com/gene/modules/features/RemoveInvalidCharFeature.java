package com.gene.modules.features;

public class RemoveInvalidCharFeature
{
	private static final char[] invalidCharSet = {'\t', '*', '-', '+', '/', '!', '@', '$', '%', '^', '&', '(', ')', '=', '|', '~', '`', '<', '>', '#', ' '};
	
	public String removeInvalidChar(String str)
	{
		char[] temp = null;
		String actualString = null;
		temp = str.toCharArray();
		
		for(int i=0; i<temp.length; ++i)
		{
			for(int j=0; j<invalidCharSet.length; ++j)
			{
				if(temp[i]==invalidCharSet[j])
				{
					temp[i] = ' ';
				}
			}
		}

		actualString = (new String(temp)).replace(" ", "");
		temp = null;
		return actualString;
	}
}