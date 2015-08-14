package com.gene.modules.db.parser;

public class DelimiterParseInfoElement
{
	private String fieldName;
	private String delimiter;
	private int order;
	
	public DelimiterParseInfoElement(){}
	
	public DelimiterParseInfoElement(String fieldName, String delimiter, int order)
	{
		this.fieldName = fieldName.toUpperCase();
		this.delimiter = delimiter;
		this.order = order;
	}
	
	public String getFieldName()
	{
		return fieldName;
	}

	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}

	public int getOrder()
	{
		return order;
	}

	public void setOrder(int order)
	{
		this.order = order;
	}

	public String getDelimiter()
	{
		return delimiter;
	}

	public void setDelimiter(String delimiter)
	{
		this.delimiter = delimiter;
	}
}
