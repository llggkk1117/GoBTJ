package com.gene.modules.clock;

import java.util.GregorianCalendar;

public class Clock
{
	private long startTime;
	private long stopTime;
	
	public Clock(){}

	public void resetTimer()
	{
		startTime = 0;
		stopTime = 0;
	}
	
	public void startTimer()
	{
		resetTimer();
		startTime = System.currentTimeMillis();
	}
	
	public void stopTimer()
	{
		stopTime = System.currentTimeMillis();
	}

	public long getDurationMilliSec()
	{
		long duration = -1;
		if ((startTime > 0)&&(stopTime > 0))
		{
			duration =  stopTime - startTime;
		}
		return duration;
	}
	
	public static String showCurrentTime()
	{
		GregorianCalendar now = new GregorianCalendar();
		String nowString = now.getTime().toString();
		now = null;
		return  nowString;
	}
	
	public long getStartTime()
	{
		return startTime;
	}
	
	public long getStopTime()
	{
		return stopTime;
	}
}