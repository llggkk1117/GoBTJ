package com.gene.modules.db.parser;

public class LengthParseInfoElement
{
	private String fieldName;
	private int parseBegin;
	private int parseEnd;
	
	public LengthParseInfoElement(){}
	
	public LengthParseInfoElement(String columnName, int parseBegin, int parseEnd)
	{
		this.fieldName = columnName;
		this.parseBegin = parseBegin;
		this.parseEnd = parseEnd;
	}
	
	public void setFieldName(String columnName)
	{
		this.fieldName = columnName;
	}
	
	public String getFieldName()
	{
		return this.fieldName;
	}
	
	public void setParseBegin(int parseBegin)
	{
		this.parseBegin = parseBegin;
	}
	
	public int getParseBegin()
	{
		return this.parseBegin;
	}
	
	public void setParseEnd(int parseEnd)
	{
		this.parseEnd = parseEnd;
	}
	
	public int getParseEnd()
	{
		return this.parseEnd;
	}
}
