package com.gene.modules.features;

import java.util.Vector;

public class StringSpliterFeature
{
	public static String[] split(final String originalString, final String delimiter)
	{
		String[] result = null;
		if((originalString != null)&&(!originalString.equals(""))&&(delimiter != null)&&(!originalString.equals("")))
		{
			int fromIndex = 0;
			int delimiterindex = 0;
			boolean continueFlag = true;
			Vector<String> temp = new Vector<String>(); 
			
			while(continueFlag)
			{
				delimiterindex = originalString.indexOf(delimiter, fromIndex);
				System.out.println("delimiterindex: "+delimiterindex);
				if(delimiterindex >= 0) //fromIndex �?�후로 delimiter가 존재하는 경우
				{
					temp.add(originalString.substring(fromIndex, delimiterindex));
					if((delimiterindex+delimiter.length()) < originalString.length()) // delimiter 다�?��? 문�?가 남아 있는 경우
					{
						fromIndex = delimiterindex+delimiter.length(); //delimiter 다�?� 문�?�?� 위치를 fromInex로 지정
						System.out.println("fromIndex: "+fromIndex);
					}
					else
					{
						continueFlag  = false;
					}
				}
				else
				{
					temp.add(originalString.substring(fromIndex, originalString.length()));
					continueFlag = false;
				}
			}
			
			result = new String[temp.size()];
			for(int i=0; i<temp.size(); ++i)
			{
				result[i] = temp.elementAt(i);
			}
			
			temp.clear();
			temp = null;
		}
		
		return result;
	}

	public static void main(String[] args)
	{
		String str = "aabbcc";
		String[] result = StringSpliterFeature.split(str, "bb");
		if(result != null)
		{
			for(int i=0; i<result.length; ++i)
			{
				System.out.println(result[i]);
			}
		}
		else
		{
			System.out.println(result);
		}
	}
}
