package com.gene.modules.db.tableManagement.info;

import java.util.HashMap;
import java.util.Vector;

import com.gene.modules.check.Check;
import com.gene.modules.db.tableManagement.TableHoldPolicy;

public class TableInfo
{
	private String tableName;
	private Vector<ColumnInfo> columnInfoSet;
	private HashMap<String, Integer> columnIndex;
	private TableHoldPolicy tableHoldPolicy;
	
	public TableInfo(){}
	
	public TableInfo(String tableName, Object... columnDetails)
	{
		this.setTableName(tableName);
		this.columnInfoSet = new Vector<ColumnInfo>();
		this.columnIndex = new HashMap<String, Integer>();
		this.addColumnInfo(columnDetails);
	}
	
	
	public synchronized int getNumOfColumns()
	{
		return this.columnInfoSet.size();
	}
	
	
	public synchronized void setTableName(String tableName)
	{
		Check.notBlank(tableName);
		this.tableName = tableName.toLowerCase();
	}
	
	
	public synchronized String getTableName()
	{
		return this.tableName;
	}
	
	public synchronized Integer getColumnIndex(String columnName)
	{
		Check.notBlank(columnName);
		columnName = columnName.toLowerCase();
		Check.notNull(this.columnIndex.containsKey(columnName));
		
		return this.columnIndex.get(columnName);
	}
	
	public synchronized String getColumnName(int columnIndex)
	{
		Check.allTrue(columnIndex>=0 && columnIndex<this.columnInfoSet.size());
		return this.columnInfoSet.elementAt(columnIndex).getColumnName();
	}
	

	//---------------------
	
	public TableHoldPolicy getTableHoldPolicy()
	{
		return tableHoldPolicy;
	}

	public void setTableHoldPolicy(TableHoldPolicy tableHoldPolicy)
	{
		this.tableHoldPolicy = tableHoldPolicy;
	}
	
	//-------------------------
	
	public synchronized void addColumnInfo(String columnName, String dataType, boolean isNotNull, boolean isPrimaryKey)
	{
		Check.notBlank(columnName, dataType);
		
		columnName = columnName.toLowerCase();
		if(columnIndex.containsKey(columnName))
		{
			this.columnInfoSet.remove(this.getColumnIndex(columnName));
			this.columnIndex.remove(columnName);
		}
		this.columnInfoSet.add(new ColumnInfo(columnName, dataType.toLowerCase(), isNotNull, isPrimaryKey));
		this.columnIndex.put(columnName, columnIndex.size());
	}
	
	
	public synchronized void addColumnInfo(Object... columnDetails)
	{
		Check.allTrue(columnDetails!=null && columnDetails.length%4==0);
		
		final int NAME = 0;
		final int TYPE = 1;
		final int NOT_NULL = 2;
		final int PRIMARY_KEY = 3;
		
		for(int i=0; i<(columnDetails.length/4); ++i)
		{
			this.addColumnInfo((String)columnDetails[i*4+NAME], (String)columnDetails[i*4+TYPE], (boolean) columnDetails[i*4+NOT_NULL], (boolean) columnDetails[i*4+PRIMARY_KEY]);
		}
	}
	
	
	public synchronized ColumnInfo getColumnInfo(int columnIndex)
	{
		return this.columnInfoSet.get(columnIndex);
	}
	
	public synchronized ColumnInfo getColumnInfo(String columnName)
	{
		return this.columnInfoSet.get(this.getColumnIndex(columnName));
	}
	
	

	//------------------------------
	
	public synchronized void removeColumnInfo(int columnIndex)
	{
		String columnName = this.getColumnName(columnIndex);
		this.columnInfoSet.remove(columnIndex);
		this.columnIndex.remove(columnName);
	}
	
	
	public synchronized void removeColumnInfo(String columnName)
	{
		Integer columnIndex = this.getColumnIndex(columnName);
		this.columnInfoSet.remove(columnIndex);
		this.columnIndex.remove(columnName.toLowerCase());
	}
	
	
	public synchronized String[] getColumnNames()
	{
		String[] columnNames = new String[this.columnInfoSet.size()];
		for(int i=0; i<columnNames.length; ++i)
		{
			columnNames[i] = this.columnInfoSet.elementAt(i).getColumnName();
		}
		
		return columnNames;
	}
	
	
	public synchronized ColumnInfo[] getEveryColumnInfo()
	{
		ColumnInfo[] everyColumnInfo = new ColumnInfo[this.columnInfoSet.size()];
		this.columnInfoSet.toArray(everyColumnInfo);
		
		return everyColumnInfo;
	}
	
	
	private static class CONSTRAINT
	{
		private static final int PRIMARY_KEY = 0;
		private static final int NOT_NULL = 1;
	}
	
	public synchronized String[] getPrimaryKeyColumnNames()
	{
		return this.getColumnsHavingConstraint(CONSTRAINT.PRIMARY_KEY);
	}
	
	public synchronized String[] getNotNullColumnNames()
	{
		return this.getColumnsHavingConstraint(CONSTRAINT.NOT_NULL);
	}
	
	private synchronized String[] getColumnsHavingConstraint(int constraint)
	{
		Vector<String> columnNamesTemp = new Vector<String>(); 
		
		for(int i=0; i<this.columnInfoSet.size(); ++i)
		{
			ColumnInfo columnInfo = this.columnInfoSet.elementAt(i);
			if(constraint==CONSTRAINT.PRIMARY_KEY && columnInfo.isPrimaryKey())
			{
				columnNamesTemp.add(columnInfo.getColumnName());
			}
			else if(constraint==CONSTRAINT.NOT_NULL && columnInfo.isNotNull())
			{
				columnNamesTemp.add(columnInfo.getColumnName());
			}
		}
		
		String[] columnNames = new String[columnNamesTemp.size()];
		columnNamesTemp.toArray(columnNames);
		
		return columnNames;
	}
	
	
	public synchronized boolean isPrimaryKey(String columnName)
	{
		return this.columnInfoSet.elementAt(this.getColumnIndex(columnName)).isPrimaryKey();
	}
	
	public synchronized boolean isNotNull(String columnName)
	{
		return this.columnInfoSet.elementAt(this.getColumnIndex(columnName)).isNotNull();
	}
	
	
	public synchronized TableInfo clone()
	{
		TableInfo cloned = new TableInfo();
		cloned.tableName = this.tableName;
		for(int i=0; i<this.columnInfoSet.size(); ++i)
		{
			ColumnInfo columnInfo = this.columnInfoSet.elementAt(i);
			cloned.columnInfoSet.add(columnInfo.clone());
			cloned.columnIndex.put(columnInfo.getColumnName(), i);
		}
		cloned.tableHoldPolicy = this.tableHoldPolicy.clone();
		
		return cloned;
	}
}
