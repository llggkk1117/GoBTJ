package com.gene.modules.formater;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTimeConstants;

public class Formater
{
	public static String reformat(final String currentFormat, final String newFormat, final String currentValue) throws ParseException 
	{
		if(StringUtils.isBlank(currentFormat))
		{
			throw new IllegalArgumentException();
		}
		if(StringUtils.isBlank(newFormat))
		{
			throw new IllegalArgumentException();
		}
		if(StringUtils.isBlank(currentValue))
		{
			throw new IllegalArgumentException();
		}
		
		SimpleDateFormat parser = new SimpleDateFormat(currentFormat);
		Date parsedCurrentValue = parser.parse(currentValue);
		SimpleDateFormat formater = new SimpleDateFormat(newFormat);
		String result = formater.format(parsedCurrentValue);
		
		return result;
	}
	
	public static String format(final String format, final Date date)
	{
		if(StringUtils.isBlank(format))
		{
			throw new IllegalArgumentException();
		}
		if(date == null)
		{
			throw new IllegalArgumentException();
		}
		
		SimpleDateFormat formater = new SimpleDateFormat(format);
		String result = formater.format(date);
		
		return result;
	}
	
	public static Date parse(final String format, final String value) throws ParseException
	{
		if(StringUtils.isBlank(format))
		{
			throw new IllegalArgumentException();
		}
		if(StringUtils.isBlank(value))
		{
			throw new IllegalArgumentException();
		}
		
		SimpleDateFormat parser = new SimpleDateFormat(format);
		Date parsedValue = parser.parse(value);
		
		return parsedValue;
	}
	
	
	public static String formatTime(final int time) throws ParseException
	{
		if((time < 0)||(time > 2359))
		{
			throw new IllegalArgumentException();
		}
		
		String formattedTime = time+"";
		
		int difference = 4-formattedTime.length();
		for(int i=0; i<difference; ++i)
		{
			formattedTime = "0"+formattedTime;
		}

		formattedTime = Formater.reformat("HHmm", "h:mm a", formattedTime);
		return formattedTime;
	}
	
	
	public static String formatDay(final int day)
	{
		if((day < DateTimeConstants.MONDAY)||(day > DateTimeConstants.SUNDAY))
		{
			throw new IllegalArgumentException();
		}
		
		String formattedDay = null;
		if(day == DateTimeConstants.MONDAY)
		{
			formattedDay = "Monday";
		}
		else if(day == DateTimeConstants.TUESDAY)
		{
			formattedDay = "Tuesday";
		}
		else if(day == DateTimeConstants.WEDNESDAY)
		{
			formattedDay = "Wednesday";
		}
		else if(day == DateTimeConstants.THURSDAY)
		{
			formattedDay = "Thursday";
		}
		else if(day == DateTimeConstants.FRIDAY)
		{
			formattedDay = "Friday";
		}
		else if(day == DateTimeConstants.SATURDAY)
		{
			formattedDay = "Saturday";
		}
		else if(day == DateTimeConstants.SUNDAY)
		{
			formattedDay = "Sunday";
		}
		
		return formattedDay;
	}
	

	
	
	public static String formatDate(final Date date)
	{
		if(date == null)
		{
			throw new IllegalArgumentException();
		}
		
		String formattedDate = Formater.format("yyyy-MM-dd", date);
		return formattedDate;
	}
}
