package com.gene.modules.db.parser;

import java.util.HashMap;
import java.util.Vector;

public class DelimiterParseInfo
{
	private HashMap<String, DelimiterParseInfoElement> delimiterParseInfoElementSet;
	private Vector<String> fieldNameInOrder;
	
	public DelimiterParseInfo()
	{
		this.delimiterParseInfoElementSet = new HashMap<String, DelimiterParseInfoElement>();
		this.fieldNameInOrder = new Vector<String>();
	}
	
	public synchronized void put(String fieldName, String delimiter, int order)
	{
		String field_name = fieldName.toUpperCase();
		
		if(!this.delimiterParseInfoElementSet.containsKey(field_name))
		{
			this.fieldNameInOrder.add(field_name);
		}
		
		this.delimiterParseInfoElementSet.put(field_name, new DelimiterParseInfoElement(field_name, delimiter, order));
	}
	
	public synchronized DelimiterParseInfoElement getParseInfoElement(String fieldName)
	{
		return this.delimiterParseInfoElementSet.get(fieldName.toUpperCase());
	}
	
	public synchronized void removeParseInfoElement(String fieldName)
	{
		String field_name = fieldName.toUpperCase();
		this.delimiterParseInfoElementSet.remove(field_name);
		this.fieldNameInOrder.removeElement(field_name);
	}
	
	public synchronized String[] getEveryFieldName()
	{
		String[] fieldNames = new String[this.fieldNameInOrder.size()];
		this.fieldNameInOrder.toArray(fieldNames);

		return fieldNames;
	}
	
	public synchronized DelimiterParseInfoElement[] getEveryParseInfoElement()
	{
		DelimiterParseInfoElement[] elements = new DelimiterParseInfoElement[this.delimiterParseInfoElementSet.size()];
		for(int i=0; i<this.fieldNameInOrder.size(); ++i)
		{
			elements[i] = this.delimiterParseInfoElementSet.get(this.fieldNameInOrder.elementAt(i));
		}
		
		return elements;
	}
	
	
//	public static void main(String[] args)
//	{
//		DelimiterParseInfo info = new DelimiterParseInfo();
//		info.put("hh", ",", 0);
//		info.put("gg", ",", 1);
//		DelimiterParseInfoElement[] es = info.getEveryParseInfoElement();
//		for(int i=0; i<es.length; ++i)
//		{
//			System.out.println(es[i].getFieldName());
//		}
//	}
}
