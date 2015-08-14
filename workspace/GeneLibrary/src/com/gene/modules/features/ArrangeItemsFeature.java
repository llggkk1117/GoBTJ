package com.gene.modules.features;

import java.util.Vector;

public class ArrangeItemsFeature
{
	public String arrangeItems(String str)
	{
		String arrangedItems = null;
		if(str != null)
		{
			String strItems = str;
			String[] arrayItems = null;
			Vector<String> vectorItems = new Vector<String>();
			arrangedItems = "";
			
			strItems = strItems.replace(" ", "");
			strItems = strItems.replace("\t", "");
			arrayItems = strItems.split(",");
			if(arrayItems != null)
			{
				for(int i=0; i<arrayItems.length; ++i)
				{
					if(!arrayItems[i].equals(""))
					{
						vectorItems.add(arrayItems[i]);
					}
				}
			}
			
			for(int i=0; i<vectorItems.size(); ++i)
			{
				arrangedItems += vectorItems.elementAt(i);
				if(i != (vectorItems.size()-1))
				{
					arrangedItems += ",";
				}
			}
		}
		
		return arrangedItems;
	}
	
	public static void main(String[] args)
	{
		ArrangeItemsFeature a = new ArrangeItemsFeature();
		String str = null;
		str = a.arrangeItems(str);
		System.out.println(str);
	}
}