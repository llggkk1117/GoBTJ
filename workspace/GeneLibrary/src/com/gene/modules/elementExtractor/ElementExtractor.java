package com.gene.modules.elementExtractor;

import java.util.Vector;
import org.apache.commons.lang.StringUtils;
import com.gene.modules.randomString.RandomString;

public class ElementExtractor
{
	private static String openBracket = "{";
	private static String closeBracket = "}";
	
	public static String[] getItems(Object object)
	{
		String text = object.toString();
		Vector<String> textSet = new Vector<String>(); 
		
		if(text != null)
		{
			RandomString random = new RandomString(30, false);
			String tempSymbol = random.nextString();
			String tempText = text;
			Vector<Integer> open_indexes= new Vector<Integer>();
			int index = 0;
			tempText = tempText.replace(openBracket, tempSymbol);

			while((index = tempText.indexOf(tempSymbol)) >= 0)
			{
				open_indexes.add(index);
				tempText = tempText.replaceFirst(tempSymbol, "-");
			}
			
			for(int i=0; i<open_indexes.size(); ++i)
			{
				System.out.println("open_indexes.elementAt("+i+"): "+open_indexes.elementAt(i));
			}
			
			tempText = text;
			Vector<Integer> close_indexes= new Vector<Integer>();
			tempText = tempText.replace(closeBracket, tempSymbol);
			while((index = tempText.indexOf(tempSymbol)) >= 0)
			{
				close_indexes.add(index);
				tempText = tempText.replaceFirst(tempSymbol, "-");
			}
			
			for(int i=0; i<close_indexes.size(); ++i)
			{
				System.out.println("close_indexes.elementAt("+i+"): "+close_indexes.elementAt(i));
			}
			
			
			if(open_indexes.size() == close_indexes.size())
			{
				while(open_indexes.size() > 0)
				{
					int open = open_indexes.remove(open_indexes.size()-1);
					int close = -1;
					for(int i=0; i<close_indexes.size(); ++i)
					{
						if(open < close_indexes.elementAt(i))
						{
							close = close_indexes.remove(i);
							break;
						}
					}
					
					if(close != -1)
					{
						textSet.add(text.substring(open+1, close));
					}
					else
					{
						break;
					}
				}
			}
		}
	
		String[] result = null;
		if(textSet.size() > 0)
		{
			result = new String[textSet.size()];
			textSet.toArray(result);
			
			for(int i=0; i<result.length; ++i)
			{
				System.out.println("result["+i+"]: "+result[i]);
			}
		}
				
		return result;
	}
	
	
	public static String getText(Object object)
	{
		String[] temp = getItems(object);
		
		String result = null;
		if((temp != null)&&(StringUtils.isNotBlank(temp[0])))
		{
			result = temp[0];
		}
		
		return result;
	}
	

	public static void main(String[] args)
	{
		System.out.println(ElementExtractor.getText("!w@"));
	}
}
