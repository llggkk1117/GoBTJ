package com.gene.modules.db.tableManagement.table;

import java.util.HashMap;
import java.util.Vector;

import com.gene.modules.check.Check;
import com.gene.modules.db.tableManagement.TableHoldPolicy;


public class TableInfo
{
	private String tableName;
	
	private Vector<String> columnNames;
	private Vector<String> dataTypes;
	private Vector<Boolean> notNullConstraints;
	private Vector<Boolean> primaryKeyConstraints;
	private HashMap<String, Integer> columnIndex;

	private TableHoldPolicy tableHoldPolicy;
	
	private TableInfo(){}
	
	public TableInfo(String tableName, Object... columnDetails)
	{
		this.columnNames = new Vector<String>();
		this.dataTypes = new Vector<String>();
		this.notNullConstraints = new Vector<Boolean>();
		this.notNullConstraints = new Vector<Boolean>();
		this.columnIndex = new HashMap<String, Integer>();
		this.setTableName(tableName);
		this.addColumn(columnDetails);
	}
	
	public synchronized int getNumOfColumns()
	{
		return this.columnNames.size();
	}
	
	public synchronized void setTableName(String tableName)
	{
		Check.allTrue(tableName!=null && !"".equals(tableName));
		this.tableName = tableName.toLowerCase();
	}
	
	public synchronized String getTableName()
	{
		return this.tableName;
	}
	
	public synchronized int getColumnIndex(String columnName)
	{
		Check.allTrue(columnName!=null && !"".equals(columnName));
		columnName = columnName.toLowerCase();
		Check.allTrue(this.columnIndex.containsKey(columnName));
		return this.columnIndex.get(columnName);
	}
	
	public synchronized String getColumnName(int index)
	{
		Check.allTrue(index >= 0 && index < this.columnNames.size());
		return this.columnNames.elementAt(index);
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
	
	public synchronized void addColumn(String columnName, String dataType, boolean notNullConstraint, boolean primaryKeyConstraint)
	{
		Check.allTrue(columnName!=null && !"".equals(columnName) && dataType!=null && !"".equals(dataType));
		this.removeColumn(columnName);
		this.columnNames.add(columnName.toLowerCase());
		this.dataTypes.add(dataType);
		this.notNullConstraints.add(notNullConstraint);
		this.primaryKeyConstraints.add(primaryKeyConstraint);
		this.columnIndex.put(columnName, this.columnIndex.size());
	}
	
	public synchronized void removeColumn(String columnName)
	{
		this.removeColumn(this.getColumnIndex(columnName), columnName);
	}
	
	public synchronized void removeColumn(int index)
	{
		this.removeColumn(index, this.getColumnName(index));
	}
	
	private synchronized void removeColumn(int index, String columnName)
	{
		this.columnNames.remove(index);
		this.dataTypes.remove(index);
		this.notNullConstraints.remove(index);
		this.primaryKeyConstraints.remove(index);
		this.columnIndex.remove(columnName);
	}
	
	
	public synchronized void addColumn(Object... columnDetails)
	{
		Check.allTrue(columnDetails!=null && !"".equals(columnDetails) && columnDetails.length%4==0);
		
		final int NAME = 0;
		final int TYPE = 1;
		final int NOT_NULL = 2;
		final int PRIMARY_KEY = 3;
		
		for(int i=0; i<(columnDetails.length/4); ++i)
		{
			this.addColumn((String)columnDetails[i*4+NAME], (String)columnDetails[i*4+TYPE], (boolean) columnDetails[i*4+NOT_NULL], (boolean) columnDetails[i*4+PRIMARY_KEY]);
		}
	}
	

	
	public synchronized String[] getColumnNames()
	{
		String[] columnNames = new String[this.columnNames.size()];
		this.columnNames.toArray(columnNames);
		
		return columnNames;
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
		
		for(int i=0; i<this.columnNames.size(); ++i)
		{
			if((constraint==CONSTRAINT.PRIMARY_KEY && this.primaryKeyConstraints.elementAt(i)) || (constraint==CONSTRAINT.NOT_NULL && this.notNullConstraints.elementAt(i)))
			{
				columnNamesTemp.add(this.columnNames.elementAt(i));
			}
		}
		
		String[] columnNames = new String[columnNamesTemp.size()];
		columnNamesTemp.toArray(columnNames);
		
		return columnNames;
	}
	
	
	public synchronized boolean isPrimaryKey(String columnName)
	{
		Integer index = this.columnIndex.get(columnName);
		if(index != null)
		{
			return this.primaryKeyConstraints.elementAt(index);
		}
		else
		{
			return false;
		}
	}
	
	public synchronized boolean hasNotNullConstraint(String columnName)
	{
		Integer index = this.columnIndex.get(columnName);
		if(index != null)
		{
			return this.notNullConstraints.elementAt(index);
		}
		else
		{
			return false;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public synchronized TableInfo clone()
	{
		TableInfo cloned = new TableInfo();
		cloned.tableName = this.tableName;
		cloned.columnNames = (Vector<String>) this.columnNames.clone();
		cloned.dataTypes = (Vector<String>) this.dataTypes.clone();
		cloned.notNullConstraints = (Vector<Boolean>) this.notNullConstraints.clone();
		cloned.primaryKeyConstraints = (Vector<Boolean>) this.primaryKeyConstraints.clone();
		cloned.tableHoldPolicy = this.tableHoldPolicy.clone();
		
		return cloned;
	}
}
