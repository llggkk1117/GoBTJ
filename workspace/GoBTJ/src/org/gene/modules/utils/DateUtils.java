package org.gene.modules.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtils
{
	private static String[] detectTimeZone(String str)
	{
		String[] result = null;
		
		String timeZone = null;
		String timeZonePattern = null;
		Matcher matcher = getMatcher("[+-]\\d{2}(:\\d{2})?", str);
		if(matcher.find())
		{
			timeZone = matcher.group().replace(":", "");
			if(timeZone.length()==3)
			{
				timeZone+="00";
			}
			timeZonePattern = "zzzzz";
		}
		if(timeZone==null)
		{
			matcher = getMatcher("[A-Z]{3}", str);
			if(matcher.find())
			{
				timeZone = matcher.group();
				timeZonePattern = "ZZZ";
			}
		}
		
		if(timeZone!=null)
		{
			result = new String[]{timeZone, timeZonePattern};
		}
		
		return result;
	}
	
	
	
	private static String[] detectDate(String str)
	{
		String[] result = null;
		
		String date = null;
		String datePattern = null;
		Matcher matcher = getMatcher("(\\d{4})[-/]{1,2}(\\d{2})[-/]{1,2}{1,2}(\\d{2})", str);
		if(matcher.find())
		{
			date = matcher.group(1)+"-"+matcher.group(2)+"-"+matcher.group(3);
			datePattern = "yyyy-MM-dd";
		}
		if(date==null)
		{
			matcher = getMatcher("([A-Z][a-z]{2}).(\\d{1,2}).+(\\d{4})", str);
			if(matcher.find())
			{
				date = matcher.group(1)+"-"+matcher.group(2)+"-"+matcher.group(3);
				datePattern = "MMM-dd-yyyy";
			}
		}
		
		if(date!=null)
		{
			result = new String[]{date, datePattern};
		}
		
		return result;
	}
	
	
	
	private static String[] detectTime(String str)
	{
		String[] result = null;
		
		String time = null;
		String timePattern = null;
		Matcher matcher = getMatcher("(\\d{2})\\D(\\d{2})\\D(\\d{2})\\D(\\d{3})", str);
		if(matcher.find())
		{
			time = matcher.group(1)+":"+matcher.group(2)+":"+matcher.group(3)+"."+matcher.group(4);
			timePattern = "HH:mm:ss.SSS";
		}
		if(time==null)
		{
			matcher = getMatcher("(\\d{2})\\D(\\d{2})\\D(\\d{2})", str);
			if(matcher.find())
			{
				time = matcher.group(1)+":"+matcher.group(2)+":"+matcher.group(3);
				timePattern = "HH:mm:ss";
			}
		}
		if(time==null)
		{
			matcher = getMatcher("(\\d{2})\\D(\\d{2})", str);
			if(matcher.find())
			{
				time = matcher.group(1)+":"+matcher.group(2);
				timePattern = "HH:mm";
			}
		}
		
		if(time!=null)
		{
			result = new String[]{time, timePattern};
		}
		
		return result;
	}
	
	
	
	public static Date convertToDate(String str)
	{
		Date date = null;
		
		List<String[]> resultList = new ArrayList<String[]>();
		String[] result = detectTimeZone(str);
		if(result != null){resultList.add(result);}
		result = detectDate(str);
		if(result != null){resultList.add(result);}
		result = detectTime(str);
		if(result != null){resultList.add(result);}
		
		if(resultList.size() > 0)
		{
			String arrangedInput = "";
			String pattern = "";
			for(int i=0; i<resultList.size(); ++i)
			{
				String[] element = resultList.get(i);
				arrangedInput += element[0];
				pattern += element[1];
				if(i<resultList.size()-1)
				{
					arrangedInput += " ";
					pattern += " ";
				}
			}
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			try
			{
				date = simpleDateFormat.parse(arrangedInput);
			}
			catch (Throwable t){}
		}
		
		return date;
	}
	
	private static Matcher getMatcher(String regex, String str)
	{
		return Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(str);
	}
	
	
	public static Date convertToDate_Old(String str)
	{
		Date date = null;
		String regex = null;
		if(str.matches(regex = "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{3})([+-]\\d{2}(:\\d{2})?)"))
		{
			Matcher matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(str);
			matcher.find();
			String dateTime = matcher.group(1);
			String timeZoneOffset = matcher.group(2).replace(":", "");
			if(timeZoneOffset.length()==3)
			{
				timeZoneOffset+="00";
			}

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSzzzzz");
			try
			{
				date = simpleDateFormat.parse(dateTime+timeZoneOffset);
			}
			catch (Throwable t){}
		}
		else if(str.matches(regex = "\\d{4}-\\d{2}-\\d{2}"))
		{
			Matcher matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(str);
			matcher.find();
			String dateString = matcher.group();
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			try
			{
				date = simpleDateFormat.parse(dateString);
			}
			catch (Throwable t){}
		}
		
		return date;
	}
	
	
	public static String format(String pattern, String str)
	{
		String result = null;
		
		boolean continues = str!=null && !"".equals(str);
		
		Date date = null;
		if(continues)
		{
			date = convertToDate(str);
			continues = date!=null;
		}

		SimpleDateFormat simpleDateFormat = null;
		if(continues)
		{
			String defaultPattern = "yyyy-MM-dd HH:mm zzz";
			if(pattern==null || "".equals(pattern)){pattern = defaultPattern;}
			try
			{
				simpleDateFormat = new SimpleDateFormat(pattern);
			}
			catch(Throwable t)
			{
				simpleDateFormat = new SimpleDateFormat(defaultPattern);
			}
			continues = simpleDateFormat!=null;
		}
		
		if(continues)
		{
			result = simpleDateFormat.format(date);
		}
		
		return result;
	}
	
	
	public static String format(String str)
	{
		return format(null, str);
	}
	
	
	public static void main(String[] args) throws ParseException
	{
		System.out.println(format("Mar 02 14:54:39:316 -03:30 2012"));
		System.out.println(convertToDate("2015-03-03"));
//		System.out.println(format(null, "2015-03-03 14:54:39.316-03:30"));
	}
}
