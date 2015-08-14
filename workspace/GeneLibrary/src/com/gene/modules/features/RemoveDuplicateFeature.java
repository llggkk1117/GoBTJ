package com.gene.modules.features;

import java.util.Vector;

public class RemoveDuplicateFeature
{
	public Vector<String> removeDuplicate(Vector<String> a_items)
	{
		@SuppressWarnings("unchecked")
		Vector<String> items = (Vector<String>) a_items.clone();
		
		for(int i=0; i<items.size(); i++)
		{
			for(int j=i+1; j<items.size(); j++)
			{
				if(items.elementAt(i).equals(items.elementAt(j)))
				{
					items.remove(j);
					j--;
				}
			}
		}
		return items;
	}
}