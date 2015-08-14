package com.gene.modules.parserLab;

import java.util.HashMap;
import java.util.Vector;

import com.gene.modules.features.ParseToKeyAndValueFeature;
import com.gene.modules.features.ReadLinesFeature;
import com.gene.modules.features.RemoveInvalidCharFeature;
import com.gene.modules.features.RemoveInvalidLinesFeature;




public class Parser
{
	private ReadLinesFeature readLinesFeature;
	private RemoveInvalidLinesFeature removeInvalidLinesFeature;
	private ParseToKeyAndValueFeature parseToKeyAndValueFeature;
	private RemoveInvalidCharFeature removeInvalidCharFeature;

	public Parser()
	{
		readLinesFeature = new ReadLinesFeature();
		removeInvalidLinesFeature = new RemoveInvalidLinesFeature();
		parseToKeyAndValueFeature = new ParseToKeyAndValueFeature();
		removeInvalidCharFeature = new RemoveInvalidCharFeature();
	}
	
	public HashMap<String, String> read(String fileName)
	{
		Vector<String> lines;
		HashMap<String, String> properties;
		String[][] propertiesArray;
		
		lines = readLinesFeature.readLines(fileName);
		lines = removeInvalidLinesFeature.removeInvalidLines(lines);
		propertiesArray = parseToKeyAndValueFeature.parseToKeyAndValue(lines);
		for(int i=0; i<propertiesArray.length; ++i)
		{
			propertiesArray[i][0] = removeInvalidCharFeature.removeInvalidChar(propertiesArray[i][0]);
			propertiesArray[i][1] = propertiesArray[i][1].trim();
		}
		properties = parseToKeyAndValueFeature.arrayToHashMap(propertiesArray);
		System.gc();
		return properties;
	}
	
	public static void main(String[] args)
	{
		Parser p = new Parser();
		HashMap<String, String> properties = p.read("settings.properties");
		System.out.println(properties.get("error.code"));
	}
}