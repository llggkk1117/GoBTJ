package com.gene.modules.simpleTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//http://javatechniques.com/blog/dateformat-and-simpledateformat-examples/

public class SimpleDateFormatTest {


	public static void test1()
	{
        // Make a new Date object. It will be initialized to the current time.
        Date now = new Date();

        // See what toString() returns
        System.out.println(" 1. " + now.toString());

        // Next, try the default DateFormat
        System.out.println(" 2. " + DateFormat.getInstance().format(now));

        // And the default time and date-time DateFormats
        System.out.println(" 3. " + DateFormat.getTimeInstance().format(now));
        System.out.println(" 4. " +
            DateFormat.getDateTimeInstance().format(now));

        // Next, try the short, medium and long variants of the
        // default time format
        System.out.println(" 5. " +
            DateFormat.getTimeInstance(DateFormat.SHORT).format(now));
        System.out.println(" 6. " +
            DateFormat.getTimeInstance(DateFormat.MEDIUM).format(now));
        System.out.println(" 7. " +
            DateFormat.getTimeInstance(DateFormat.LONG).format(now));

        // For the default date-time format, the length of both the
        // date and time elements can be specified. Here are some examples:
        System.out.println(" 8. " + DateFormat.getDateTimeInstance(
            DateFormat.SHORT, DateFormat.SHORT).format(now));
        System.out.println(" 9. " + DateFormat.getDateTimeInstance(
            DateFormat.MEDIUM, DateFormat.SHORT).format(now));
        System.out.println("10. " + DateFormat.getDateTimeInstance(
            DateFormat.LONG, DateFormat.LONG).format(now));
    }
	
	public static void test2()
	{
		// Make a String that has a date in it, with MEDIUM date format
        // and SHORT time format.
        String dateString = "Nov 4, 2003 8:14 PM";

        // Get the default MEDIUM/SHORT DateFormat
        DateFormat format =
            DateFormat.getDateTimeInstance(
            DateFormat.MEDIUM, DateFormat.SHORT);

        // Parse the date
        try {
            Date date = format.parse(dateString);
            System.out.println("Original string: " + dateString);
            System.out.println("Parsed date    : " +
                 date.toString());
        }
        catch(ParseException pe) {
            System.out.println("ERROR: could not parse date in string \"" +
                dateString + "\"");
        }
	}
	
	public static void test3()
	{
	       // Get the default MEDIUM/SHORT DateFormat
        DateFormat format =
            DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
            DateFormat.SHORT);

        // Read and parse input, stopping on a blank input line
        BufferedReader reader =
            new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print("ENTER DATE STRING: ");
            String dateString = reader.readLine();

            while ((dateString != null) && (dateString.length() > 0)) {
                // Parse the date
                try {
                    Date date = format.parse(dateString);
                    System.out.println("Original string: " + dateString);
                    System.out.println("Parsed date    : " +
                        date.toString());
                    System.out.println(); // Skip a line
                }
                catch(ParseException pe) {
                    System.out.println(
                        "ERROR: could not parse date in string \"" +
                        dateString + "\"");
                }

                // Read another string
                System.out.print("ENTER DATE STRING: ");
                dateString = reader.readLine();
            }
        }
        catch(IOException ioe) {
            System.out.println("I/O Exception: " + ioe);
        }
	}
	
	
	public static void test4()
	{
	       // Make a new Date object. It will be initialized to the
        // current time.
        Date now = new Date();

        // Print the result of toString()
        String dateString = now.toString();
        System.out.println(" 1. " + dateString);

        // Make a SimpleDateFormat for toString()'s output. This
        // has short (text) date, a space, short (text) month, a space,
        // 2-digit date, a space, hour (0-23), minute, second, a space,
        // short timezone, a final space, and a long year.
        SimpleDateFormat format =
            new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

        // See if we can parse the output of Date.toString()
        try {
            Date parsed = format.parse(dateString);
            System.out.println(" 2. " + parsed.toString());
        }
        catch(ParseException pe) {
            System.out.println("ERROR: Cannot parse \"" + dateString + "\"");
        }

        // Print the result of formatting the now Date to see if the result
        // is the same as the output of toString()
        System.out.println(" 3. " + format.format(now));
	}
	
	
	public static void test5()
	{
		  String s;
		  Format formatter;
		  Date date = new Date();

		  // Time formate 01:12:53 AM
		  formatter = new SimpleDateFormat("hh:mm:ss a");
		  s = formatter.format(date);
		  System.out.println("hh:mm:ss a formate : " + s);

		  //Time formate 14.36.33
		  formatter = new SimpleDateFormat("HH.mm.ss");
		  s = formatter.format(date);
		  System.out.println("HH.mm.ss : formate : " + s);
	}
	
	
	
	
	public static void test6()
	{
		String stringTime = null;
		int time = 1534;
		try
		{
			stringTime = (new SimpleDateFormat("h:mm a")).format((new SimpleDateFormat("HHmm")).parse(time+""));
		}
		catch (Exception e)
		{
			stringTime = "";
		};
		System.out.println(stringTime);
	}
	
	
	
	public static void test8()
	{
		String result = null;
		String date = "10/13/2012"; 
		try
		{
			result = (new SimpleDateFormat("yyyy-MM-dd")).format((new SimpleDateFormat("MM/dd/yyyy")).parse(date));
		}
		catch (Exception e)
		{
			result = "";
		};
		System.out.println(result);
	}
	
	
	
	public static String reformat(String currentFormat, String newFormat, String currentValue) 
	{
		String result = null;
		try
		{
			SimpleDateFormat parser = new SimpleDateFormat(currentFormat);
			Date parsedCurrentValue = parser.parse(currentValue);
			
			SimpleDateFormat formater = new SimpleDateFormat(newFormat);
			result = formater.format(parsedCurrentValue);
		}
		catch(Exception e)
		{
			result = currentValue;
		}
		
		
		return result;
	}
	
	
	
	public static void test7()
	{
	       
        // Create Date object        
        Date date = new Date();
       
        //Specify the desired date format        
        String DATE_FORMAT = "MM/dd/yyyy";
       
        //Create object of SimpleDateFormat and pass the desired date format        
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
       
        /*
        Use format method of SimpleDateFormat class to format the date.
        */
       
        System.out.println("Today is " + sdf.format(date));
   
    }
	
	
	
	
	
	
	
	
	public static void test9() throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date date = sdf.parse("10/11/2012");
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		String dayOfWeek1 = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US);
		String dayOfWeek3 = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);
		int dayOfWeek2 = calendar.get(Calendar.DAY_OF_WEEK);
		// Sunday = 1, Monday = 2, Tuesday = 3, Wednesday = 4, Thursday = 5, Friday = 6, Saturday = 7
		System.out.println(dayOfWeek1);
		System.out.println(dayOfWeek2);
		System.out.println(dayOfWeek3);
	}
	
	public static void main(String[] args) throws ParseException
	{
		test9();
//		System.out.println(reformat("MM/dd/yyyy", "yyyy-MM-dd", "10/13/2012"));
//		System.out.println(reformat("HHmm", "h:mm a", "1538"));
	}

}
