package com.gene.modules.parserLab;

import java.util.HashMap;
import org.govgrnds.core.util.crypto.DefaultCryptoHelper;

import com.gene.modules.features.ArrangeItemsFeature;


public class Translator
{
	private ArrangeItemsFeature arangeItemsFeature;
	private DefaultCryptoHelper crytoHelper;
	
	public Translator()
	{
		arangeItemsFeature = new ArrangeItemsFeature();
		crytoHelper = new DefaultCryptoHelper();
	}
	
	public HashMap<String, String> translate(HashMap<String, String> a_properties)
	{
		@SuppressWarnings("unchecked")
		HashMap<String, String> properties = (HashMap<String, String>) a_properties.clone();
		String strErrorCodes = properties.get("error.code");
		strErrorCodes = arangeItemsFeature.arrangeItems(strErrorCodes);
		properties.remove("error.code");
		properties.put("error.code", strErrorCodes);
		
		
		String tempEnPW = null;
		String tempDePW = null;
		
		tempEnPW = properties.get("fetch.ozip.database.pw");
		if(tempEnPW != null)
		{
			tempDePW = crytoHelper.decrypt(tempEnPW);
			properties.remove("fetch.ozip.database.pw");
			properties.put("fetch.ozip.database.pw", tempDePW);	
		}
		tempEnPW = properties.get("fetch.dzip.database.pw");
		if(tempEnPW != null)
		{
			tempDePW = crytoHelper.decrypt(tempEnPW);
			properties.remove("fetch.dzip.database.pw");
			properties.put("fetch.dzip.database.pw", tempDePW);	
		}
		tempEnPW = properties.get("insert.database.pw");
		if(tempEnPW != null)
		{
			tempDePW = crytoHelper.decrypt(tempEnPW);
			properties.remove("insert.database.pw");
			properties.put("insert.database.pw", tempDePW);	
		}
		
		return properties;
	}
}