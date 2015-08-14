package com.gene.modules.db.tableManagement.tableRecordConverter.externalData;

public class DataField
{
	private String fieldName;
	private String value;
	
	public DataField(){}
	
	public DataField(String fieldName, String value)
	{
		this.fieldName = fieldName.toUpperCase();
		this.value = value;
	}
	
	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName.toUpperCase();
	}
	
	public String getFieldName()
	{
		return this.fieldName;
	}
	
	public void setValue(String value)
	{
		this.value = value;
	}
	
	public String getValue()
	{
		return this.value;
	}
}


