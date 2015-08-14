package com.gene.modules.db.tableManagement.table;


public class Table
{
	private TableInfo tableInfo;
	private TableData tableData;
	
	public Table(String tableName, Object... columnDetails)
	{
		this.tableInfo = new TableInfo(tableName, columnDetails);
		this.tableData = new TableData(this.tableInfo.getColumnNames());
	}
	
	public synchronized Table insertTuple(String... tuple)
	{
		this.tableData.addTuple(tuple);
		return this;
	}
}
