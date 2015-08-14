package com.gene.modules.features;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

public class ReadLinesFeature
{
	public Vector<String> readLines(String fileName)
	{
		FileReader fr = null;
		BufferedReader br = null;
		String line = null;
		Vector<String> lines = new Vector<String>();
		
		try
		{
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);
			while((line = br.readLine()) != null)
			{
				lines.add(line);
			}
			br.close();
			br = null;
			fr.close();
			fr = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return lines;
	}
}