package com.gene.modules.db.tableManagement.table;

import java.util.Vector;

import com.gene.modules.check.Check;
import com.gene.modules.db.tableManagement.utils.DBUtils;


public class TableData
{
	public String[] columnNames;
	public Vector<String[]> tuples;

	public TableData(String... columnNames)
	{
		Check.notBlank(columnNames);
		Check.notContainingAny(columnNames, " ", "\t");
		this.columnNames = columnNames;
		for(int i=0; i<this.columnNames.length; ++i)
		{
			this.columnNames[i] = this.columnNames[i].toLowerCase();
		}
		this.tuples = new  Vector<String[]>();
	}

	public synchronized TableData addTuple(String... tuple)
	{
		Check.allTrue(tuple!=null && tuple.length==this.columnNames.length);
		this.tuples.add(tuple);
		return this;
	}

	public synchronized TableData addTuple(int index, String... tuple)
	{
		Check.allTrue(index>=0 && index<this.tuples.size() && tuple!=null && tuple.length==this.columnNames.length);
		this.tuples.add(index, tuple);
		return this;
	}

	public synchronized TableData addTuples(String[]... tuples)
	{
		for(int i=0; i<tuples.length; ++i)
		{
			this.addTuple(tuples[i]);
		}
		return this;
	}

	public synchronized TableData addTuples(int index, String[]... tuples)
	{
		for(int i=0; i<tuples.length; ++i)
		{
			this.addTuple(index, tuples[i]);
			index++;
		}
		return this;
	}

	public synchronized String[] getTuple(int index)
	{
		Check.allTrue(index>=0 && index<this.tuples.size());
		return this.tuples.elementAt(index);
	}

	public synchronized String[][] getAllTuples()
	{
		String[][] allTuples = new String[this.tuples.size()][]; 
		this.tuples.toArray(allTuples);
		return allTuples;
	}

	public synchronized TableData removeTuple(int index)
	{
		Check.allTrue(index>=0 && index<this.tuples.size());
		this.tuples.remove(index);
		return this;
	}

	public synchronized TableData removeAllTuples()
	{
		this.tuples.clear();
		return this;
	}

	public synchronized TableData updateTuple(int index, String... tuple)
	{
		this.removeTuple(index);
		this.addTuple(index, tuple);
		return this;
	}

	public synchronized TableData updateTuples(int index, String[]... tuples)
	{
		this.removeTuple(index);
		this.addTuples(index, tuples);
		return this;
	}

	public synchronized int size()
	{
		return this.tuples.size();
	}

	public synchronized boolean isEmpty()
	{
		return this.tuples.isEmpty();
	}

	public synchronized boolean equals(TableData tableData)
	{
		return TableData.equals(this, tableData);
	}

	public static boolean equals(TableData tableData1, TableData tableData2)
	{
		boolean equal = true;
		if((tableData1.columnNames != null && tableData2.columnNames == null)||(tableData1.columnNames == null && tableData2.columnNames != null))
		{
			equal = false;
		}
		else if (tableData1.columnNames != null && tableData2.columnNames != null)
		{
			if(tableData1.columnNames.length == tableData2.columnNames.length)
			{
				for(int i=0; i<tableData1.columnNames.length; ++i)
				{
					if(tableData1.columnNames[i] != tableData2.columnNames[i])
					{
						equal = false;
						break;
					}
				}
			}
			else
			{
				equal = false;
			}
		}	

		if(equal)
		{
			if((tableData1.tuples != null && tableData2.tuples == null)||(tableData1.tuples == null && tableData2.tuples != null))
			{
				equal = false;
			}
			else if(tableData1.tuples != null && tableData2.tuples != null)
			{
				if(tableData1.tuples.size() == tableData2.tuples.size())
				{
					for(int i=0; i<tableData1.tuples.size(); ++i)
					{
						String[] row1 = tableData1.tuples.elementAt(i); 
						String[] row2 = tableData2.tuples.elementAt(i);

						if((row1!= null && row2== null)||(row1 == null && row2 != null))
						{
							equal = false;
							break;
						}
						else if(row1!= null && row2!= null)
						{
							if(row1.length == row2.length)
							{
								for(int j=0; j<row1.length; ++j)
								{
									if(!((row1[j]==null && row2[j]==null) || (row1[j].equals(row2[j]))))
									{
										equal = false;
										break;
									}
								}
								if(!equal){break;}
							}
							else
							{
								equal = false;
								break;
							}
						}
					}
				}
				else
				{
					equal = false;
				}
			}
		}
		
		return equal;
	}
	
	
	public synchronized String toString()
	{
		return TableData.toString(this);
	}
	
	private static synchronized int[] getMaxLength(TableData tableData)
	{
		Check.allTrue(tableData != null && tableData.columnNames != null);
		
		int[] maxLength = new int[tableData.columnNames.length];
		for(int i=0; i<maxLength.length; ++i)
		{
			if(maxLength[i] < tableData.columnNames[i].length())
			{
				maxLength[i] = tableData.columnNames[i].length();
			}
		}
		
		for(int r=0; r<tableData.tuples.size(); ++r)
		{
			String[] row = tableData.tuples.elementAt(r);
			for(int i=0; i<maxLength.length; ++i)
			{
				if(maxLength[i] < row[i].length())
				{
					maxLength[i] = row[i].length();
				}
			}
		}
		
		return maxLength;
	}
	
	public static String toString(TableData tableData)
	{
		int[]maxLength = getMaxLength(tableData);
		
		String result = "";
		String[] columnNames = tableData.columnNames;
		for(int i=0; i<columnNames.length; ++i)
		{
			result += columnNames[i];
			int space = maxLength[i] - columnNames[i].length();
			for(int b=0; b<space; ++b)
			{
				result += " ";
			}
			if(i<columnNames.length-1)
			{
				result += "  |  ";
			}
		}
		
		result += "\n";
		for(int i=0; i<maxLength.length; ++i)
		{
			for(int b=0; b<maxLength[i]; ++b)
			{
				result += "-";
			}
			if(i<maxLength.length-1)
			{
				result += "--+--";
			}
		}

		result += "\n";
		Vector<String[]>tuples = tableData.tuples;
		for(int r=0; r<tuples.size(); ++r)
		{
			String[] row = tuples.elementAt(r);
			for(int i=0; i<row.length; ++i)
			{
				result += row[i];
				int space = maxLength[i] - row[i].length();
				for(int b=0; b<space; ++b)
				{
					result += " ";
				}
				if(i<row.length-1)
				{
					result += "  |  ";
				}
			}
			result += "\n";
		}

		return result;
	}


	public static void main(String[] args)
	{
		TableData td1 = new TableData("col1", "col2");
		td1.addTuple("11", "pp");
		td1.addTuple("22", "33");
		// td1.addTuple("44", "55");
		DBUtils.showResult(td1.getAllTuples());
		System.out.println(td1);
		
		TableData td2 = new TableData("col1", "col2");
		td2.addTuple("11", "pp");
		td2.addTuple("22", "33");
		System.out.println(td1.equals(td2));
	}
}
