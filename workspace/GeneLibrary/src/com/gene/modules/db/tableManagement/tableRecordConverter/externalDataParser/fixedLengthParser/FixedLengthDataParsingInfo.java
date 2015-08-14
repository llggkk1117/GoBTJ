package com.gene.modules.db.tableManagement.tableRecordConverter.externalDataParser.fixedLengthParser;

import java.util.Vector;



public class FixedLengthDataParsingInfo
{
	private Vector<FixedLengthDataFieldParsingInfo> dataFieldParsingInfoSet;
	
	public FixedLengthDataParsingInfo()
	{
		dataFieldParsingInfoSet = new Vector<FixedLengthDataFieldParsingInfo>();
	}
	
	public synchronized void put(String fieldName, int parseBegin, int parseEnd)
	{
		dataFieldParsingInfoSet.add(new FixedLengthDataFieldParsingInfo(fieldName, parseBegin, parseEnd));
	}
	
	public synchronized FixedLengthDataFieldParsingInfo getDataField(String fieldName)
	{
		FixedLengthDataFieldParsingInfo specElement = null;
		if(fieldName != null)
		{
			for(int i=0; i<this.dataFieldParsingInfoSet.size(); ++i)
			{
				if(fieldName.equalsIgnoreCase(this.dataFieldParsingInfoSet.elementAt(i).getFieldName()))
				{
					specElement = this.dataFieldParsingInfoSet.elementAt(i);
					break;
				}
			}
		}
		
		return specElement;
	}
	
	public synchronized void removeDataField(String fieldName)
	{
		if(fieldName != null)
		{
			for(int i=0; i<this.dataFieldParsingInfoSet.size(); ++i)
			{
				if(fieldName.equalsIgnoreCase(this.dataFieldParsingInfoSet.elementAt(i).getFieldName()))
				{
					this.dataFieldParsingInfoSet.remove(i);
					break;
				}
			}
		}
	}
	

	
	public synchronized String[] getEveryFieldName()
	{
		String[] fieldNames = new String[this.dataFieldParsingInfoSet.size()];
		for(int i=0; i<fieldNames.length; ++i)
		{
			fieldNames[i] = this.dataFieldParsingInfoSet.elementAt(i).getFieldName();
		}
		
		return fieldNames;
	}
}
