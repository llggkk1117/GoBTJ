package org.gene.modules.utils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;





public class JsonUtils
{
	private static final Gson gson;
	static
	{
		gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().setDateFormat("MM/dd/yyyy HH:mm:ss").create();
	}
	
	public static <T> T jsonToObject(String json, Class<T> classOfT)
	{
		T result = null;
		if(json!=null && !"".equals(json) && classOfT!=null)
		{
			result = gson.fromJson(json, classOfT);
		}
		
		return result;
	}
	
	
	public static <T> T jsonToObject(String json, Type typeOfT)
	{
		T result = null;
		if(json!=null && !"".equals(json) && typeOfT!=null)
		{
			result = gson.fromJson(json, typeOfT);
		}
		
		return result;
	}

	public static Map<String, Object> jsonToObject(String json)
	{
		Map<String, Object> result = null;
		if(json!=null && !"".equals(json))
		{
			Type type = new TypeToken<HashMap<String, Object>>(){}.getType();
			result = jsonToObject(json, type);
		}
		
		return result;
	}
	
	
	
	public static String objectToJson(Object object)
	{
		String json = null;
		if(object!=null)
		{
			json = gson.toJson(object);
		}
		
		return json;
	}
	
	
	
	public static <T> T convertTypeTo(Map<String, Object> jsonObject, Class<T> classOfT)
	{
		String json = objectToJson(jsonObject);
		return jsonToObject(json, classOfT);
	}
	
	
	public static <T> T convertTypeTo(Map<String, Object> jsonObject, Type typeOfT)
	{
		String json = objectToJson(jsonObject);
		return jsonToObject(json, typeOfT);
	}
}





/*
 * References:
 * 
 * https://sites.google.com/site/gson/gson-user-guide#TOC-Finer-Points-with-Objects
 * http://www.mkyong.com/java/how-do-convert-java-object-to-from-json-format-gson-api/
 * http://stackoverflow.com/questions/4802887/gson-how-to-exclude-specific-fields-from-serialization-without-annotations
 * 
 * https://code.google.com/p/json-simple/wiki/EncodingExamples
 * https://code.google.com/p/json-simple/wiki/DecodingExamples
 * http://www.mkyong.com/java/how-to-convert-java-object-to-from-json-jackson/
 * http://www.leveluplunch.com/java/examples/convert-json-to-from-map-jackson/
 */