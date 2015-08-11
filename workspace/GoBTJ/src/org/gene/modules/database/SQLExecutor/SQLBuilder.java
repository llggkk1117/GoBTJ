package org.gene.modules.database.SQLExecutor;

import java.util.Vector;

import org.gene.modules.check.Check;


public class SQLBuilder
{
	public static String buildSQL(String sql, Object... args)
	{
		Check.notBlank(sql, args);
		
		args = normalize(args);
		
		int numOfQuestionMark = sql.length() - sql.replace("?", "").length();
		if(numOfQuestionMark > args.length)
		{
			throw new IllegalArgumentException("The number of arguments is less than the number of \'?\' marks");
		}
		else if(numOfQuestionMark < args.length)
		{
			throw new IllegalArgumentException("The number of arguments is more than the number of \'?\' marks");
		}
		
		for(int i=0; i<args.length; ++i)
		{
			sql = sql.replaceFirst("\\?", args[i].toString());
		}
		
		return sql;
	}

	
	
	private static Object[] normalize(Object... args)
	{
		Object[] result = null;
		if(args != null)
		{
			boolean containsArray = false;
			for(int i=0; i<args.length; ++i)
			{
				if(args[i] instanceof Object[])
				{
					containsArray = true;
					break;
				}
			}
			
			if(containsArray)
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
				result = args;
			}
		}
		
		return result;
	}
	
	
	

	
	public static void main(String[] args)
	{
		System.out.println(SQLBuilder.buildSQL("a \'?\' b ?", "-11", "l"));
	}
}
