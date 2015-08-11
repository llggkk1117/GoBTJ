package org.gene.test;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.gene.dao.UserDAO;
import org.gene.model.User;
import org.gene.modules.utils.JsonUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonTest {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, InterruptedException
	{
		DataObject obj = new DataObject();
		Gson gson = new Gson();
	 
		// convert java object to JSON format,
		// and returned as JSON formatted string
		String json = gson.toJson(obj);
		System.out.println(json);
		DataObject oo = (DataObject) gson.fromJson(json, DataObject.class);
		System.out.println(oo.toString());
		
		HashMap<String, Object> hs = new HashMap<String, Object>();
		HashMap<String, Object> sub_hs = new HashMap<String, Object>();
		hs.put("1", "aa");
		hs.put("2", "bb");
		hs.put("3", "cc");
		sub_hs.put("4", "dd");
		sub_hs.put("5", "ee");
		sub_hs.put("6", null);
		hs.put("4", sub_hs);
		
		json = gson.toJson(hs);
		System.out.println(json);
		
		Type collectionType = new TypeToken<HashMap<String, Object>>(){}.getType();
		HashMap<String, Object> hm2 = gson.fromJson(json, collectionType);
		System.out.println(hm2);
		
		
		User user = UserDAO.retrieveUser(1L);
		json = JsonUtils.objectToJson(user);
		System.out.println(json);
		
	}

}

class DataObject
{
	 
	private int data1 = 100;
	private String data2 = null;
	private List<String> list = new ArrayList<String>() {
	  {
		add("String 1");
		add("String 2");
		add("String 3");
	  }
	};
 
 
	@Override
	public String toString() {
	   return "DataObject [data1=" + data1 + ", data2=" + data2 + ", list="
		+ list + "]";
	}
 
}
