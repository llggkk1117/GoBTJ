package com.gene.modules.db.tableManagement.tableRecordConverter.externalData;

import java.util.Vector;

public class Data
{
	private Vector<DataField> dataFieldSet;
	public Data()
	{
		dataFieldSet = new Vector<DataField>();
	}
	
	public synchronized void putDataField(String fieldName, String value)
	{
		dataFieldSet.add(new DataField(fieldName, value));
	}

	
	public synchronized DataField getDataField(String fieldName)
	{
		DataField dataField = null;
		if(fieldName != null)
		{
			for(int i=0; i<this.dataFieldSet.size(); ++i)
			{
				if(fieldName.equalsIgnoreCase(this.dataFieldSet.elementAt(i).getFieldName()))
				{
					dataField = this.dataFieldSet.elementAt(i);
					break;
				}
			}
		}
		
		return dataField;
	}
	
	
	public synchronized void removeDataField(String fieldName)
	{
		if(fieldName != null)
		{
			for(int i=0; i<this.dataFieldSet.size(); ++i)
			{
				if(fieldName.equalsIgnoreCase(this.dataFieldSet.elementAt(i).getFieldName()))
				{
					this.dataFieldSet.remove(i);
					break;
				}
			}
		}
	}
	
	
	public synchronized String[] getEveryFieldName()
	{
		String[] fieldNames = new String[this.dataFieldSet.size()];
		for(int i=0; i<fieldNames.length; ++i)
		{
			fieldNames[i] = this.dataFieldSet.elementAt(i).getFieldName();
		}
		
		return fieldNames;
	}
	
	
	private  synchronized void setDataFieldSet(Vector<DataField> dataFieldSet)
	{
		this.dataFieldSet = dataFieldSet;
	}
	
	
	public synchronized Data clone()
	{
		Data clonedData = new Data();
		clonedData.setDataFieldSet((Vector<DataField>) this.dataFieldSet.clone());
		return clonedData;
	}
}
