package com.gene.modules.db.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.gene.modules.db.tableManagement.data.Record;

public class DataParser
{
	public synchronized Record parse(String line, LengthParseInfo lengthparseInfo)
	{
		LengthParseInfoElement[] lengthParseInfoElements= lengthparseInfo.getEveryParseInfoElement();
		HashMap<String, String> parsedData = new HashMap<String, String>();
		for(int i=0; i<lengthParseInfoElements.length; ++i)
		{
			String value = line.substring(lengthParseInfoElements[i].getParseBegin()-1, lengthParseInfoElements[i].getParseEnd());
			value = value.replace(" ", "").replace("\t", "").trim();
			parsedData.put(lengthParseInfoElements[i].getFieldName(), value);
		}
		
		Record record = new Record();
		record.putRecordElement("FISCAL_YR", parsedData.get("fiscal_year".toUpperCase()));
		record.putRecordElement("POSTAL_QTR", parsedData.get("postal_quarter".toUpperCase()));
		record.putRecordElement("ORGN_ZIP3_CD", parsedData.get("origin_zip_code".toUpperCase()));
		record.putRecordElement("DEST_ZIP3_CD", parsedData.get("dest_zip_code".toUpperCase()));
		String mailClassCode = parsedData.get("mail_class_code".toUpperCase()).toUpperCase();
		record.putRecordElement(mailClassCode+"_SVC_STD_NBR", parsedData.get("service_standard".toUpperCase()));
		String mailClass[] = new String[]{"FCM", "PER", "PKG", "PRI", "STD", "PMG"};
		for(int i=0; i<mailClass.length; ++i)
		{
			if(!mailClass[i].equals(mailClassCode))
			{
				record.putRecordElement(mailClass[i]+"_SVC_STD_NBR", "-1");
			}
		}
		record.putRecordElement("UPDT_USER_ID", "SSD_EXCEPTION_BATCH");
		record.putRecordElement("LAST_UPDT_DTM", (new SimpleDateFormat("yyyy-MM-dd H:mm:ss:SSS")).format(Calendar.getInstance().getTime()));

		return record;
	}
	
	
	public synchronized Record parse(String line, DelimiterParseInfo delimiterParseInfo)
	{
		DelimiterParseInfoElement[] delimiterParseInfoElements= delimiterParseInfo.getEveryParseInfoElement();
		HashMap<String, String> parsedData = new HashMap<String, String>();
		for(int i=0; i<delimiterParseInfoElements.length; ++i)
		{
			String[] factors = line.split(delimiterParseInfoElements[i].getDelimiter());
			String value = factors[delimiterParseInfoElements[i].getOrder()-1];
			value = value.replace(" ", "").replace("\t", "").trim();
			parsedData.put(delimiterParseInfoElements[i].getFieldName(), value);
		}
		
		String date = (new SimpleDateFormat("yy-MM")).format(Calendar.getInstance().getTime());
		String[] dateFactors = date.split("-");
		String year = dateFactors[0];
		int month = Integer.parseInt(dateFactors[1]);
		String quarter = (((month <= 4 ? month+12 : month) - 4) / 3)+"";
		
		Record record = new Record();
		record.putRecordElement("FISCAL_YR", year);
		record.putRecordElement("POSTAL_QTR", quarter);
		record.putRecordElement("ORGN_ZIP3_CD", parsedData.get("origin_zip_code".toUpperCase()));
		record.putRecordElement("DEST_ZIP3_CD", parsedData.get("dest_zip_code".toUpperCase()));
		String mailClassCode = "PRI";
		record.putRecordElement(mailClassCode+"_SVC_STD_NBR", parsedData.get("service_standard".toUpperCase()));
		String mailClass[] = new String[]{"FCM", "PER", "PKG", "PRI", "STD", "PMG"};
		for(int i=0; i<mailClass.length; ++i)
		{
			if(!mailClass[i].equals(mailClassCode))
			{
				record.putRecordElement(mailClass[i]+"_SVC_STD_NBR", "-1");
			}
		}
		record.putRecordElement("UPDT_USER_ID", "SSD_EXCEPTION_BATCH");
		record.putRecordElement("LAST_UPDT_DTM", (new SimpleDateFormat("yyyy-MM-dd H:mm:ss:SSS")).format(Calendar.getInstance().getTime()));
		

		return record;
	}
	
	
	public static void main(String[] args) throws ParseException
	{
//		TextFile file = TextFile.getInstance("ssd_exception.txt");
//		String temp = file.readLine();
//		file.close();
//		logger.debug(temp);
//		DataParser parser = new DataParser();
//		parser.setSpec(SSDExceptionSpecGenerator.getSpec());
//		Data record = parser.parse(temp);
//		String[] field_names = record.getEveryFieldName();
//		for(int i=0; i<field_names.length; ++i)
//		{
//			logger.debug(field_names[i]+": "+record.getDataField(field_names[i]).getValue());
//		}
		
//		System.out.println((new SimpleDateFormat("yy-MM")).format(Calendar.getInstance().getTime()));
		
		SimpleDateFormat  df = new SimpleDateFormat("yyyy-MM-dd H:mm:ss:SSS");
		Date now =Calendar.getInstance().getTime();
		String str = df.format(now);
		System.out.println(str);
		System.out.println(now);
		
		Date date = df.parse(str);
		System.out.println(date);
	}
}
