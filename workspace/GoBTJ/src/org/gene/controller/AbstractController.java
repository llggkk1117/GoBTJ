package org.gene.controller;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.reflect.TypeToken;

import org.gene.modules.utils.JsonUtils;


public abstract class AbstractController
{
	private static final String JSON_STRING_FOR_SENDING = "jsonStringForSending";
	private static final String JSON_STRING_RECEIVED = "jsonStringReceived";
	
	protected AbstractController(){}
	
	protected static <T> T receiveObject(HttpServletRequest request, Type typeOfT)
	{
		T requestObject = null;
		if(request!=null && typeOfT!=null)
		{
			String json_string_request = request.getParameter(JSON_STRING_FOR_SENDING);
			System.out.println(JSON_STRING_FOR_SENDING+": "+json_string_request);
			if(json_string_request!=null && !"".equals(json_string_request))
			{
				requestObject = JsonUtils.jsonToObject(json_string_request, typeOfT);
			}
		}
		
		return requestObject;
	}
	
	
	
	protected static <T> T receiveObject(HttpServletRequest request, Class<T> classOfT)
	{
		T requestObject = null;
		if(request!=null && classOfT!=null)
		{
			String json_string_request = request.getParameter(JSON_STRING_FOR_SENDING);
			System.out.println(JSON_STRING_FOR_SENDING+": "+json_string_request);
			if(json_string_request!=null && !"".equals(json_string_request))
			{
				requestObject = JsonUtils.jsonToObject(json_string_request, classOfT);
			}
		}
		
		return requestObject;
	}
	
	
	
	protected static Map<String, Object> receiveObject(HttpServletRequest request)
	{
		Map<String, Object> result = null;
		if(request!=null)
		{
			Type type = new TypeToken<HashMap<String, Object>>(){}.getType();
			result = receiveObject(request, type);
		}
		
		return result;
	}
	
	
	
	protected static void sendObject(HttpServletRequest request, Object object)
	{
		if(request!=null && object!=null)
		{
			String json_string_response = JsonUtils.objectToJson(object);
			System.out.println(json_string_response);
			if(json_string_response!=null && !"".equals(json_string_response))
			{
				request.setAttribute(JSON_STRING_RECEIVED, json_string_response);
			}
		}
	}
	
	
	
	protected static String encode(String str) throws UnsupportedEncodingException
	{
		// http://zzznara2.tistory.com/94
    	// http://stackoverflow.com/questions/4737841/urlencoder-not-able-to-translate-space-character
		return URLEncoder.encode(str , "UTF-8").replace("+", "%20");
	}
	

	
	protected static void putObjectInSession(HttpServletRequest request, String key, Object object)
	{
		 request.getSession().setAttribute(key, object);
	}
	
	protected static Object getObjectFromSession(HttpServletRequest request, String key)
	{
		 return request.getSession().getAttribute(key);
	}
	
	protected static void removeObjectFromSession(HttpServletRequest request, String key)
	{
		request.getSession().removeAttribute(key);
	}
	
	protected static void closeSession(HttpServletRequest request)
	{
		request.getSession().invalidate();
	}
	
	
	
	public static void main(String[] args)
	{
//		HashMap<String, Integer> origin = new HashMap<String, Integer>();
//		origin.put("one", 1);
//		origin.put("two", 2);
//		origin.put("three", 3);
//		origin.put("four", 4);
		
//		String origin = "hello";
		
		String[] origin = new String[]{"hello", "hi"};
		
		String json = JsonUtils.objectToJson(origin);
		System.out.println(json);
		String[] result = JsonUtils.jsonToObject(json, new TypeToken<String[]>(){}.getType());
		System.out.println(((String[])result)[0]);
		System.out.println(((String[])result)[1]);

//		HashMap<String, Integer> map_result = (HashMap<String, Integer>) result;
//		System.out.println(map_result.get("two"));
	}
}

