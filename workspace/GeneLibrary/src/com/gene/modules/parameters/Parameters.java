package com.gene.modules.parameters;


import java.util.HashMap;
import java.util.Vector;

public class Parameters
{
	private HashMap<String, Object> parameters;
	public Parameters()
	{
		parameters = new HashMap<String, Object>();
	}
	
	public Object get(final String key)
	{
		return this.parameters.get(key);
	}
	
	public void put(final String key, final Object value)
	{
		this.parameters.put(key, value);
	}
	
	public void remove(final String key)
	{
		this.parameters.remove(key);
	}
	
	public int size()
	{
		return this.parameters.size();
	}
	
	public String[] keySet()
	{
		return ((String[]) this.parameters.keySet().toArray());
	}
	
	public boolean containsKey(final String key)
	{
		return this.parameters.containsKey(key);
	}
	
	public boolean containsValue(final Object value)
	{
		return this.parameters.containsValue(value);
	}
	
	public boolean isEmpty()
	{
		return this.parameters.isEmpty();
	}
}
