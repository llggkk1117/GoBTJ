package com.gene.modules.db.tableManagement.tableRecordConverter.externalDataParser.fixedLengthParser;

import java.io.IOException;

import com.gene.modules.db.tableManagement.tableRecordConverter.externalData.Data;
import com.gene.modules.db.tableManagement.tableRecordConverter.externalDataParser.ExternalDataParser;
import com.gene.modules.exceptions.InstanceAlreadyExistException;
import com.gene.modules.textFile.TextFile;

public class FixedLengthDataParser implements ExternalDataParser
{
	private FixedLengthDataParsingInfo dataParsingInfo;
	
	public void setDataParsingInfo(FixedLengthDataParsingInfo dataParsingInfo)
	{
		this.dataParsingInfo = dataParsingInfo;
	}
	
	public FixedLengthDataParsingInfo getDataParsingInfo()
	{
		return this.dataParsingInfo;
	}
	

	public synchronized Data parse(String line)
	{
		String[] fieldNames = this.dataParsingInfo.getEveryFieldName();
		Data record = new Data();

		FixedLengthDataFieldParsingInfo dataFieldParsingInfo = null;
		String value = null;
		for(int i=0; i<fieldNames.length; ++i)
		{
			dataFieldParsingInfo = this.dataParsingInfo.getDataField(fieldNames[i]);
			value = line.substring(dataFieldParsingInfo.getParseBeginPosition()-1, dataFieldParsingInfo.getParseEndPosition());
			value = value.replace(" ", "");
			record.putDataField(fieldNames[i], value);
		}

		return record;
	}	
	
	
	
	public static void main(String[] args) throws IOException, InstanceAlreadyExistException, InterruptedException
	{
		TextFile file = TextFile.getFile("ssd_exception.txt");
		String temp = file.readLine();
		file.close();
		
		System.out.println(temp);
		ExternalDataParser parser = new FixedLengthDataParser();
		((FixedLengthDataParser)parser).setDataParsingInfo(SSDExceptionDataParsingInfoGenerator.getDataParsingInfo());
		Data record = parser.parse(temp);
		String[] field_names = record.getEveryFieldName();
		for(int i=0; i<field_names.length; ++i)
		{
			System.out.println(field_names[i]+": "+record.getDataField(field_names[i]).getValue());
		}
	}
}
