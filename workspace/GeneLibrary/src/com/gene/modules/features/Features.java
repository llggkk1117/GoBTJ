package com.gene.modules.features;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Features
{
	public static String stacktraceToString(Exception e)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String message = sw.toString();
        try
        {
			sw.close();
		}
        catch (IOException e1)
        {
			e1.printStackTrace();
		}
        sw = null;
        pw.close();
        pw = null;
		return message;
	}
	
	public static void main(String[] args)
	{
		try
		{
			throw new Exception("for no reason!");
		}
		catch (Exception e)
		{
			System.out.println(Features.stacktraceToString(e));
		}
		
		try
		{
			throw new Exception("22222");
		}
		catch (Exception e)
		{
			System.out.println(Features.stacktraceToString(e));
		}
	}
}
