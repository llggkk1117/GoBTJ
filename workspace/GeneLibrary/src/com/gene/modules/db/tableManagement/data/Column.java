package com.gene.modules.db.tableManagement.data;

import com.gene.modules.check.Check;

public class Column
{
	private String columnName;
	private String value;
	
	public Column(){}
	
	public Column(String columnName, String value)
	{
		this.setColumnName(columnName);
		this.setValue(value);
		this.value = value;
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
	
	public void setValue(String value)
	{
		Check.notBlank(value);
		this.value = value;
	}
	
	public String getValue()
	{
		return this.value;
	}
	
	public synchronized Column clone()
	{
		Column newColumn = new Column();
		newColumn.columnName = this.columnName;
		newColumn.value = this.value;
		return newColumn;
	}
}


