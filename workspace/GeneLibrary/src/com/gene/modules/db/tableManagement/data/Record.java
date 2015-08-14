package com.gene.modules.db.tableManagement.data;

import java.util.HashMap;
import java.util.Vector;


public class Record
{
	private HashMap<String, Column> columnSet;
	private Vector<String> columnNameInOrder;
	
	public Record()
	{
		this.columnSet = new HashMap<String, Column>();
		this.columnNameInOrder = new Vector<String>();
	}
	
	public synchronized void putColumn(String columnName, String value)
	{
		columnName = columnName.toLowerCase();
		
		if(!this.columnSet.containsKey(columnName))
		{
			this.columnNameInOrder.add(columnName);
		}
		
		this.columnSet.put(columnName, new Column(columnName, value));
	}

	public synchronized Column getColumn(int columnIndex)
	{
		return this.getColumn(this.columnNameInOrder.elementAt(columnIndex));
	}
	
	public synchronized Column getColumn(String columnName)
	{
		return this.columnSet.get(columnName.toLowerCase());
	}
	
	public synchronized void removeColumn(String columnName)
	{
		columnName = columnName.toLowerCase();
		this.columnSet.remove(columnName);
		this.columnNameInOrder.removeElement(columnName);
	}
	
	public synchronized void removeColumn(int columnIndex)
	{
		this.removeColumn(this.columnNameInOrder.elementAt(columnIndex));
	}
	
	public synchronized String getColumnName(int columnIndex)
	{
		return this.columnNameInOrder.elementAt(columnIndex);
	}
	
	
	public synchronized int size()
	{
		return this.columnSet.size();
	}
	
	public synchronized boolean isEmpty()
	{
		return this.columnSet.isEmpty();
	}
	
	public synchronized String[] getEveryColumnName()
	{
		String[] ColumnNames = new String[this.columnNameInOrder.size()];
		this.columnNameInOrder.toArray(ColumnNames);
		
		return ColumnNames;
	}
	
	public synchronized Column[] getEveryColumn()
	{
		Column[] columns = new Column[this.columnNameInOrder.size()];
		for(int i=0; i<this.columnNameInOrder.size(); ++i)
		{
			columns[i] = this.columnSet.get(this.columnNameInOrder.elementAt(i));
		}
		
		return columns;
	}
	
	
	public synchronized Record clone()
	{
		Record clonedRecord = new Record();
		Vector<String> keys = new Vector<String>(this.columnSet.keySet());
		for(int i=0; i<keys.size(); ++i)
		{
			clonedRecord.columnSet.put(keys.elementAt(i), this.columnSet.get(keys.elementAt(i)).clone());
		}

		return clonedRecord;
	}
	
	
	
	
	
	
	
	
	
	
	
//	public static void main(String[] args)
//	{
//		Record r = new Record();
//		r.putRecordElement("eer", "12");
//		r.putRecordElement("sss", "34");
//		
//		RecordElement[] e = r.getEveryRecordElement();
//		for(int i=0; i<e.length; ++i)
//		{
//			System.out.println(e[i].getColumnName());
//		}
//		
//		String[] s = r.getEveryColumnName();
//		for(int i=0; i<s.length; ++i)
//		{
//			System.out.println(s[i]);
//		}
//	}
}
