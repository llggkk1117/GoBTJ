package com.gene.modules.check;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.gene.modules.check.DeepCheckConstants.Result;

public class DeepCheckResult
{
	private int result;
	private List<Object[]> data;
	
	public DeepCheckResult()
	{
		this(Result.UNKNOWN);
	}
	
	public DeepCheckResult(int result)
	{
		this.result = result;
		this.data = new ArrayList<Object[]>();
	}
	
	public DeepCheckResult(int result, Object... data)
	{
		this(result);
		this.set(result, data);
	}
	
	public void setResult(int result)
	{
		this.result = (result<-1 ? Result.UNKNOWN : (result>1 ? Result.TRUE : result));
	}
	
	public int getResult()
	{
		return this.result;
	}

	public void putData(Object... data)
	{
		if((data != null)&&(data.length > 0))
		{
			data = normalize(data);
			this.data.add(data);
		}
	}

	public List<Object[]> getData()
	{
		return this.data;
	}
	
	public void set(int result, Object... data)
	{
		this.setResult(result);
		this.putData(data);
	}
	
	
	private static Object[] normalize(Object... args)
	{
		Object[] result = null;
		if(args != null)
		{
			if(args.length > 0)
			{
				Vector<Object> queue = new Vector<Object>();
				for(int i=0; i<args.length; ++i)
				{
					queue.add(args[i]);
				}

				for(int i=0; i<queue.size(); ++i)
				{
					if(queue.elementAt(i) instanceof Object[])
					{
						Object[] element = (Object[])queue.remove(i);
						for(int j=element.length-1; j>=0; --j)
						{
							queue.add(i, element[j]);
						}
						i--;
					}
				}
				
				result = new Object[queue.size()];
				queue.toArray(result);
			}
			else
			{
				result = new Object[0];
			}
		}
		
		return result;
	}
	
	
	public static void main(String[] args)
	{
		DeepCheckResult validationResult = new DeepCheckResult(0, "aa", "bb", 11);
		for(int i=0; i<validationResult.getData().size(); ++i)
		{
			System.out.println(validationResult.getResult());
			System.out.println(validationResult.getData().get(i)[2]);
		}
	}
}
