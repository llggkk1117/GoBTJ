package com.gene.modules.db.tableManagement.tableRecordConverter.externalDataParser.fixedLengthParser;



public class SSDExceptionDataParsingInfoGenerator
{
	private static FixedLengthDataParsingInfo parsingInfo;
	
	private SSDExceptionDataParsingInfoGenerator(){}

	public static synchronized FixedLengthDataParsingInfo getDataParsingInfo()
	{
		if(parsingInfo == null)
		{
			parsingInfo = new FixedLengthDataParsingInfo();
			parsingInfo.put("fiscal_year", 1, 2);
			parsingInfo.put("postal_quarter", 3, 3);
			parsingInfo.put("origin_zip_code", 4, 8);
			parsingInfo.put("dest_zip_code", 9, 13);
			parsingInfo.put("mail_class_code", 14, 16);
			parsingInfo.put("service_standard", 17, 18);
		}
		
		return SSDExceptionDataParsingInfoGenerator.parsingInfo;
	}
}
