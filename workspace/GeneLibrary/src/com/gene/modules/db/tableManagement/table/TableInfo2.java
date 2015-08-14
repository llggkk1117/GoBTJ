package cse530a.db.db.tableManagement.table;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import cse530a.db.check.Check;



public class TableInfo2
{
	private static class CONSTRAINT
	{
		private static final int PRIMARY_KEY = 0;
		private static final int FOREIGN_KEY = 1;
		private static final int NOT_NULL = 2;
		private static final int UNIQUE = 3;
	}
	
	private String tableName;
	private Vector<String> columnNames;
	private HashMap<String, Integer> columnIndex;
	private Vector<String> dataTypes;
	private HashMap<Integer, HashSet<Object>> constraints;
	

	private TableHoldPolicy tableHoldPolicy;
	
	private TableInfo2(){}
	
	public TableInfo2(String tableName, String... columnDetails)
	{
		this.columnNames = new Vector<String>();
		this.columnIndex = new HashMap<String, Integer>();
		this.dataTypes = new Vector<String>();
		
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
	
	public synchronized void addColumn(String columnName, String dataType)
	{
		Check.allTrue(columnName!=null && !"".equals(columnName) && dataType!=null && !"".equals(dataType));
		this.removeColumn(columnName);
		this.columnNames.add(columnName.toLowerCase());
		this.dataTypes.add(dataType);
		this.columnIndex.put(columnName, this.columnIndex.size());
	}
	
	public synchronized void addColumn(String... columnDetails)
	{
		Check.allTrue(columnDetails!=null && columnDetails.length > 0 && columnDetails.length%2==0);
		int numOfColumns = columnDetails.length/2;
		for(int i=0; i<numOfColumns; ++i)
		{
			this.addColumn((String)columnDetails[i*2], (String)columnDetails[i*2+1]);
		}
	}
	
	private synchronized void removeColumn(String columnName)
	{
		int columnIndex = this.getColumnIndex(columnName);
		this.columnNames.remove(columnIndex);
		this.dataTypes.remove(columnIndex);
		this.columnIndex.remove(columnName);
	}
	
	private synchronized void removeColumn(int index)
	{
		String columnName = this.getColumnName(index);
		this.columnNames.remove(index);
		this.dataTypes.remove(index);
		this.columnIndex.remove(columnName);
	}
	
	public synchronized int getColumnIndex(String columnName)
	{
		Check.notBlank(columnName);
		columnName = columnName.toLowerCase();
		Check.allTrue(this.columnIndex.containsKey(columnName));
		return this.columnIndex.get(columnName);
	}
	
	public synchronized String getColumnName(int index)
	{
		Check.allTrue(index >= 0 && index < this.columnNames.size());
		return this.columnNames.elementAt(index);
	}
	
	public synchronized String[] getColumnNames()
	{
		String[] columnNames = new String[this.columnNames.size()];
		this.columnNames.toArray(columnNames);
		
		return columnNames;
	}
	
	public synchronized String getDataType(int columnIndex)
	{
		Check.allTrue(columnIndex <= this.columnIndex.size());
		return this.dataTypes.elementAt(columnIndex);
	}
	
	public synchronized String getDataType(String columnName)
	{
		Check.notBlank(columnName);
		return this.getDataType(this.getColumnIndex(columnName));
	}
	
	
	public synchronized void addConstraints(Integer constraintType, Object... constraintContents)
	{
		HashSet<Object> contents = this.constraints.get(constraintType);
		if(contents == null)
		{
			contents = new HashSet<Object>();
			contents.addAll(Arrays.asList(constraintContents));
			this.constraints.put(constraintType, contents);
		}
		else
		{
			contents.addAll(Arrays.asList(constraintContents));
		}
	}
	
	
	public synchronized void removeConstraints(Integer constraintType)
	{
		this.constraints.remove(constraintType);
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
	public synchronized TableInfo2 clone()
	{
		TableInfo2 cloned = new TableInfo2();
		cloned.tableName = this.tableName;
		cloned.columnNames = (Vector<String>) this.columnNames.clone();
		cloned.dataTypes = (Vector<String>) this.dataTypes.clone();
		cloned.notNullConstraints = (Vector<Boolean>) this.notNullConstraints.clone();
		cloned.primaryKeyConstraints = (Vector<Boolean>) this.primaryKeyConstraints.clone();
		cloned.tableHoldPolicy = this.tableHoldPolicy.clone();
		
		return cloned;
	}
}
