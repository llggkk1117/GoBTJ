package com.gene.modules.simpleTest.time;

import java.util.Date;

import org.joda.time.Instant;
import org.joda.time.DateTime;
import org.joda.time.DateMidnight;


public class JodaTimeTest
{
	    public static void ex1()
	    {
	        //
	        // Create DateMidnight object of the current system dateMidnight.
	        //
	        DateMidnight dateMidnight = new DateMidnight();
	        System.out.println("dateMidnight = " + dateMidnight);

	        //
	        // Create DateMidnight object by year, month and day.
	        //
	        dateMidnight = new DateMidnight(2012, 1, 1);
	        System.out.println("dateMidnight = " + dateMidnight);

	        //
	        // Create DateMidnight object from JDK's Date milliseconds.
	        //
	        Date date = new Date();
	        dateMidnight = new DateMidnight(date.getTime());
	        System.out.println("dateMidnight = " + dateMidnight);
	    }
	    
	    public static void ex2()
	    {
	        //
	        // An instant in the datetime continuum specified as
	        // a number of milliseconds from 1970-01-01T00:00Z.
	        //
	        // The declaration below creates 1 seconds instant from
	        // 1970.
	        //
	        Instant instant = new Instant(1000);

	        //
	        // Get a new copy of instant with 500 duration added.
	        //
	        instant = instant.plus(500);

	        //
	        // Get a new copy of instant with 250 duration taken away.
	        //
	        instant = instant.minus(250);
	        System.out.println("Milliseconds = " + instant.getMillis());

	        //
	        // Creating an instant that represent the current date.
	        //
	        DateTime dateTime = new DateTime();
	        System.out.println("Date Time = " + dateTime);

	        //
	        // Creating an instant of a specific date and time.
	        //
	        DateTime independenceDay = new DateTime(2012, 8, 17, 0, 0, 0, 0);
	        System.out.println("Independence Day = " + independenceDay);
	    }
	    
	    public static void main(String[] args)
	    {
	    	JodaTimeTest.ex2();
	    }
}
