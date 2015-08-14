package com.gene.modules.features;

import java.util.Vector;

public class RemoveInvalidLinesFeature
{
	private final static String[] invalidLineSet = {""};
	private final static String[] invalidLineStartWith = {"#"};
	public Vector<String> removeInvalidLines(Vector<String> lines)
	{
		if(lines != null)
		{
			boolean invalidLine = false;
			for(int i=0; i<lines.size(); ++i)
			{
				//�? string중�?서 #로 시작하거나 �?는 비어있는 string�?� 제거한다.
				
				for(int j=0; j<invalidLineSet.length; ++j)
				{
					if(lines.elementAt(i).equals(invalidLineSet[j]))
					{
						invalidLine = true;
					}
				}
				
				for(int j=0; j<invalidLineStartWith.length; ++j)
				{
					if(lines.elementAt(i).startsWith(invalidLineStartWith[j]))
					{
						invalidLine = true;
					}
				}
				
				if (invalidLine)
				{
					lines.remove(i);
					--i;
				}
				
				invalidLine = false;
			}
		}
		
		return lines;
	}
	
	public static void main(String[] args)
	{
		RemoveInvalidLinesFeature r = new RemoveInvalidLinesFeature();
		Vector<String> lines = new Vector<String>();
		lines.add("asdnf");
		lines.add("#asdnf");
		lines.add("asdnf");
		lines = r.removeInvalidLines(lines);
		for(int i=0; i<lines.size(); ++i)
		{
			System.out.println(lines.elementAt(i));
		}
	}
}