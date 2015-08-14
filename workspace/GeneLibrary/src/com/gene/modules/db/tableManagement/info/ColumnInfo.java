package com.gene.modules.db.tableManagement.info;

import com.gene.modules.check.Check;


public class ColumnInfo
{
	private String columnName;
	private String dataType;
	private boolean isNotNull;
	private boolean isPrimaryKey;
	
	public ColumnInfo(){}
	
	public ColumnInfo(String columnName, String dataType, boolean isNotNull, boolean isPrimaryKey)
	{
		Check.notBlank(columnName, dataType);
		
		this.columnName = columnName.toLowerCase();
		this.dataType = dataType.toLowerCase();
		this.isNotNull = isNotNull;
		this.isPrimaryKey = isPrimaryKey;
	}
	
	public void setColumnName(String columnName)
	{
		Check.notBlank(columnName);
		
		this.columnName = columnName.toLowerCase();
	}
	
	public String getColumnName()
	{
		return this.columnName;
	}
	
	public void setDataType(String dataType)
	{
		Check.notBlank(dataType);
		
		this.dataType = dataType.toLowerCase();
	}
	
	public String getDataType()
	{
		return this.dataType;
	}
	
	public void setIsNotNull(boolean isNotNull)
	{
		this.isNotNull = isNotNull;
	}
	
	public boolean isNotNull()
	{
		return this.isNotNull;
	}
	
	public void setIsPrimaryKey(boolean isPrimaryKey)
	{
		this.isPrimaryKey = isPrimaryKey;
	}
	
	public boolean isPrimaryKey()
	{
		return this.isPrimaryKey;
	}
	
	public synchronized ColumnInfo clone()
	{
		ColumnInfo newColumnInfo = new ColumnInfo();
		newColumnInfo.columnName = this.columnName;
		newColumnInfo.dataType = this.dataType;
		newColumnInfo.isNotNull = this.isNotNull;
		newColumnInfo.isPrimaryKey = this.isPrimaryKey;
		
		return newColumnInfo;
	}
}
