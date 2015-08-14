package com.gene.modules.db.tableManagement;

import java.util.HashSet;

public class TableHoldPolicy
{
	public static class PeriodType
	{
		public static final int YEAR = 1;
		public static final int MONTH = 2;
		public static final int DAY = 5;
		public static final int HOUR = 10;
		public static final int MINUTE = 12;
		public final static int SECOND = 13;
		public final static int MILLISECOND = 14;
	}
	
	private static final HashSet<Integer> validPeriodType;
	static
	{
		validPeriodType = new HashSet<Integer>();
		validPeriodType.add(PeriodType.YEAR);
		validPeriodType.add(PeriodType.MONTH);
		validPeriodType.add(PeriodType.DAY);
		validPeriodType.add(PeriodType.HOUR);
		validPeriodType.add(PeriodType.MINUTE);
		validPeriodType.add(PeriodType.SECOND);
		validPeriodType.add(PeriodType.MILLISECOND);
	}
	
	private int tableHoldPeriod;
	private int tableHoldPeriodType;
	private String lastUpdateTimeColumnName;
	private String lastUpdateTimeFormatColumnFormat;
	
	public int getTableHoldPeriod()
	{
		return tableHoldPeriod;
	}

	public int getTableHoldPeriodType()
	{
		return tableHoldPeriodType;
	}
	
	public void setTableHoldPeriod(int periodType, int period)
	{
		if(!validPeriodType.contains(periodType)){throw new IllegalArgumentException("wrong period type");}
		
		this.tableHoldPeriodType = periodType;
		this.tableHoldPeriod = period;
	}
	
	public String getLastUpdateTimeColumnName()
	{
		return lastUpdateTimeColumnName;
	}

	public void setLastUpdateTimeColumnName(String lastUpdateTimeColumnName)
	{
		this.lastUpdateTimeColumnName = lastUpdateTimeColumnName.toLowerCase();
	}
	
	public String getLastUpdateTimeFormatColumnFormat()
	{
		return lastUpdateTimeFormatColumnFormat;
	}
	
	public void setLastUpdateTimeFormatColumnFormat(String lastUpdateTimeFormatColumnFormat)
	{
		this.lastUpdateTimeFormatColumnFormat = lastUpdateTimeFormatColumnFormat;
	}
	
	public TableHoldPolicy clone()
	{
		TableHoldPolicy cloned = new TableHoldPolicy();
		cloned.tableHoldPeriod = this.tableHoldPeriod;
		cloned.tableHoldPeriodType = this.tableHoldPeriodType;
		cloned.lastUpdateTimeColumnName = this.lastUpdateTimeColumnName;
		cloned.lastUpdateTimeFormatColumnFormat = this.lastUpdateTimeFormatColumnFormat;
		
		return cloned;
	}
}
