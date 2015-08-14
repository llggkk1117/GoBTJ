package com.gene.modules.db.tableManagement.tableRecordConverter.externalDataParser.fixedLengthParser;

public class FixedLengthDataFieldParsingInfo
{
	private String fieldName;
	private int parseBegin;
	private int parseEnd;
	
	public FixedLengthDataFieldParsingInfo(){}
	
	public FixedLengthDataFieldParsingInfo(String fieldName, int parseBegin, int parseEnd)
	{
		this.fieldName = fieldName;
		this.parseBegin = parseBegin;
		this.parseEnd = parseEnd;
	}
	
	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName.toUpperCase();
	}
	
	public String getFieldName()
	{
		return this.fieldName;
	}
	
	public void setParseBeginPosition(int parseBegin)
	{
		this.parseBegin = parseBegin;
	}
	
	public int getParseBeginPosition()
	{
		return this.parseBegin;
	}
	
	public void setParseEndPosition(int parseEnd)
	{
		this.parseEnd = parseEnd;
	}
	
	public int getParseEndPosition()
	{
		return this.parseEnd;
	}
}
