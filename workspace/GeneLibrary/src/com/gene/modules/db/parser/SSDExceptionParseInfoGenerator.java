package com.gene.modules.db.parser;

public class SSDExceptionParseInfoGenerator
{
	public static synchronized LengthParseInfo getLengthParseInfo()
	{
		LengthParseInfo lengthParseInfo = new LengthParseInfo();
		lengthParseInfo.put("fiscal_year", 1, 2);
		lengthParseInfo.put("postal_quarter", 3, 3);
		lengthParseInfo.put("origin_zip_code", 4, 8);
		lengthParseInfo.put("dest_zip_code", 9, 13);
		lengthParseInfo.put("mail_class_code", 14, 16);
		lengthParseInfo.put("service_standard", 17, 18);
		
		return lengthParseInfo;
	}
	
	
	public static synchronized DelimiterParseInfo getDelimiterParseInfo()
	{
		DelimiterParseInfo delimiterParseInfo = new DelimiterParseInfo();
		delimiterParseInfo.put("origin_zip_code", ",", 1);
		delimiterParseInfo.put("dest_zip_code", ",", 2);
		delimiterParseInfo.put("service_standard", ",", 3);
		
		return delimiterParseInfo;
	}
}
