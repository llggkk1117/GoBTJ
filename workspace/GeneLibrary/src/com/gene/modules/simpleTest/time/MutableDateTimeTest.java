package com.gene.modules.simpleTest.time;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

public class MutableDateTimeTest
{
	public static void test1()
	{
		MutableDateTime time = new MutableDateTime();
		System.out.println(time);
		time.addWeeks(1);
		time.addYears(20);
		System.out.println(time);
		Date date = time.toDate();
		System.out.println(date);
		DateTime dateTime = time.toDateTime();
		System.out.println(dateTime);
	}
	
	public static void main(String[] args)
	{
		test1();
	}
}
