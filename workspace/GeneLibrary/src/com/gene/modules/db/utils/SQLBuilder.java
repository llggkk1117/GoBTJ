package com.gene.modules.db.utils;

import java.util.Vector;

import com.gene.modules.check.Check;
import com.gene.modules.randomString.RandomString;

public class SQLBuilder
{
	private static String[] INVALID_STRING = {" ", "\t", ";", "--", "(", ")", "\'"}; // to prevent SQL injection
	
	public static String buildSQL(String sql, Object... args)
	{
		Check.allTrue(!Check.isBlank(sql, args) && !Check.isContainingAny(args, INVALID_STRING));
		
		String sqlStatement = new String(sql);
		Object[] arguments = normalize(args);
		
		RandomString random = new RandomString(20, RandomString.ALPHABET_UPPER, RandomString.ALPHABET_LOWER, RandomString.NUMBER);
		String replacementString = random.nextString();
		sqlStatement = sqlStatement.replace("?", replacementString);
		
		// checking if the number of ? mark and the number of argument   
		int numOfQuestionMark = 0;
		String temp = new String(sqlStatement);
		while(temp.contains(replacementString))
		{
			numOfQuestionMark++;
			temp = temp.replaceFirst(replacementString, "");
		}
		if(numOfQuestionMark > arguments.length)
		{
			throw new IllegalArgumentException("The number of arguments is less than the number of \'?\' marks");
		}
		else if(numOfQuestionMark < arguments.length)
		{
			throw new IllegalArgumentException("The number of arguments is more than the number of \'?\' marks");
		}
		
		
		for(int i=0; i<arguments.length; ++i)
		{
			sqlStatement = sqlStatement.replaceFirst(replacementString, arguments[i].toString());
		}
		
		return sqlStatement;
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
		System.out.println(SQLBuilder.buildSQL("a ? b ?", "-11", "l", "s"));
	}
}
