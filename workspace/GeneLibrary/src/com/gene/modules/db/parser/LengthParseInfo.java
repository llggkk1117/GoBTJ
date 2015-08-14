package com.gene.modules.db.parser;

import java.util.HashMap;
import java.util.Vector;

public class LengthParseInfo
{
	private HashMap<String, LengthParseInfoElement> parseInfoElementSet;
	private Vector<String> fieldNameInOrder;
	
	public LengthParseInfo()
	{
		this.parseInfoElementSet = new HashMap<String, LengthParseInfoElement>();
		this.fieldNameInOrder = new Vector<String>();
	}
	
	public synchronized void put(String fieldName, int parseBegin, int parseEnd)
	{
		String field_name = fieldName.toUpperCase();
		
		if(!this.parseInfoElementSet.containsKey(field_name))
		{
			this.fieldNameInOrder.add(field_name);
		}
		
		this.parseInfoElementSet.put(field_name, new LengthParseInfoElement(field_name, parseBegin, parseEnd));
	}
	
	public synchronized LengthParseInfoElement getParseInfoElement(String fieldName)
	{
		return this.parseInfoElementSet.get(fieldName.toUpperCase());
	}
	
	public synchronized void removeParseInfoElement(String fieldName)
	{
		String field_name = fieldName.toUpperCase();
		this.parseInfoElementSet.remove(field_name);
		this.fieldNameInOrder.removeElement(field_name);
	}
	
	public synchronized String[] getEveryFieldName()
	{
		String[] fieldNames = new String[this.fieldNameInOrder.size()];
		this.fieldNameInOrder.toArray(fieldNames);
		
		return fieldNames;
	}
	
	public synchronized LengthParseInfoElement[] getEveryParseInfoElement()
	{
		LengthParseInfoElement[] elements = new LengthParseInfoElement[this.parseInfoElementSet.size()];
		for(int i=0; i<this.fieldNameInOrder.size(); ++i)
		{
			elements[i] = this.parseInfoElementSet.get(this.fieldNameInOrder.elementAt(i));
		}
		
		return elements;
	}
	
	
//	public static void main(String[] args)
//	{
//		LengthParseInfo info = new LengthParseInfo();
//		info.put("aa", 1, 2);
//		info.put("bb", 3, 4);
//		LengthParseInfoElement[] es = info.getEveryParseInfoElement();
//		for(int i=0; i<es.length; ++i)
//		{
//			System.out.println(es[i].getFieldName());
//		}
//	}
}
