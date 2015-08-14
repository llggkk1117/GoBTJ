package com.gene.modules.features;

import java.util.HashMap;
import java.util.Vector;

public class ParseToKeyAndValueFeature
{

	public HashMap<String, String> arrayToHashMap(String[][] keyAndValuesArray)
	{
		HashMap<String, String> keyAndValues = new HashMap<String, String>();
		
		for(int i=0; i<keyAndValuesArray.length; ++i)
		{
			keyAndValues.put(keyAndValuesArray[i][0], keyAndValuesArray[i][1]);
		}
		
		return keyAndValues;
	}
	
	public String[][] parseToKeyAndValue(Vector<String> lines)
	{
		Vector<String> keySet = new Vector<String>();
		Vector<String> valueSet = new Vector<String>();

		if(lines!= null)
		{
			String key = null;
			String value = null;

			for(int i =0 ; i<lines.size(); i++)
			{
				if (lines.elementAt(i).indexOf("=") >= 0)
				{
					key = lines.elementAt(i).substring(0, lines.elementAt(i).indexOf("="));
					value = (lines.elementAt(i).substring(lines.elementAt(i).indexOf("=")+1, lines.elementAt(i).length()));
					keySet.add(key);
					valueSet.add(value);
				}
			}
		}
		
		String[][] keyAndValues = new String[keySet.size()][2];
		
		for(int i=0; i<keySet.size(); ++i)
		{
			keyAndValues[i][0] = keySet.elementAt(i);
			keyAndValues[i][1] = valueSet.elementAt(i);
		}
		
		return keyAndValues;
	}
}