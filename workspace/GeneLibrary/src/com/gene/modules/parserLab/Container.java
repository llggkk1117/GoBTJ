package com.gene.modules.parserLab;

import java.util.HashMap;
import java.util.Vector;

public class Container
{
	private Parser propertiesParser;
	private Validator propertiesValidator;
	private Translator propertiesTranslator;
	private String propertiesFileName;
	private HashMap<String, String> properties;
	private Vector<String> errorMessages;
	
	public Container(String a_propertiesFileName)
	{
		propertiesParser = new Parser();
		propertiesValidator = new ValidatorImp();
		propertiesTranslator = new Translator();
		propertiesFileName = a_propertiesFileName;
		properties = propertiesParser.read(propertiesFileName);
		errorMessages = propertiesValidator.validate(properties);
		
		if(errorMessages.size() == 0)
		{
			properties = propertiesTranslator.translate(properties);
		}
		//System.out.println(properties.get("error.code"));
	}
	
	public boolean isValid()
	{
		if(errorMessages.size() == 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public String getProperty(String key)
	{
		return properties.get(key);
	}
	
	public void removeProperty(String key)
	{
		properties.remove(key);
	}
	
	public void insertProperty(String key, String value)
	{
		properties.put(key, value);
	}
	
	public Vector<String> getErrorMessages()
	{
		return errorMessages;
	}
	
	public static void main(String[] args)
	{
		Container p = new Container("settings.properties");
//		System.out.println(p.getProperty("error.code"));
//		System.out.println(p.isValid());
//		for(int i=0; i<p.getErrorMessages().size(); i++)
//		{
//			System.out.println(p.getErrorMessages().elementAt(i));
//		}
	}
}