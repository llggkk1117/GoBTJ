package com.gene.modules.check;

import java.lang.reflect.Constructor;



public class Check
{
	private static IllegalArgumentException getExceptionInstance(Class<?> exceptionClass, String message)
	{
		IllegalArgumentException exception = null;
		try
		{
			Constructor<?> exceptionConstructor = exceptionClass.getConstructor(String.class);
			if((message == null)||("".equals(message)))
			{
				exception = (IllegalArgumentException) exceptionConstructor.newInstance();
			}
			else
			{
				exception = (IllegalArgumentException) exceptionConstructor.newInstance(message);
			}
		}
		catch(Exception e)
		{
			exception = new IllegalArgumentException();
		}
		
		return exception;
	}
	
	private static void raiseException(Class<?> exceptionClass, String message)
	{
		IllegalArgumentException exception = getExceptionInstance(exceptionClass, message);
		throw exception;
	}
	
	
	public static void checkCore(Class<?> exceptionClass, String message, boolean condition)
	{
		if(condition)
		{
			raiseException(exceptionClass, message);
		}
	}
	
	
	
	
	
	public static boolean isBlank(String... str)
	{
		boolean blank = true;
		if(str != null)
		{
			blank = false;
			for(int i=0; i<str.length; ++i)
			{
				if((str[i] == null)||("".equals(str[i])))
				{
					blank = true;
					break;
				}
			}
		}
		
		return blank;
	}
	
	public static boolean isBlank(String str)
	{
		return ((str == null)||("".equals(str)));
	}
	
	public static boolean isBlank(String str1, String str2)
	{
		return (str1 == null)||("".equals(str1))||(str2 == null)||("".equals(str2));
	}
	
	public static boolean isBlank(String str1, String str2, String str3)
	{
		return (str1 == null)||("".equals(str1))||(str2 == null)||("".equals(str2))||(str3 == null)||("".equals(str3));
	}
	
	public static boolean isBlank(String str1, String str2, String str3, String str4)
	{
		return (str1 == null)||("".equals(str1))||(str2 == null)||("".equals(str2))||(str3 == null)||("".equals(str3))||(str4 == null)||("".equals(str4));
	}
	
	public static void notBlankWithMessage(Class<?> exceptionClass, String message, String... args)
	{
		checkCore(exceptionClass, message, isBlank(args));
	}
	
	public static void notBlankWithMessage(Class<?> exceptionClass, String message, String arg)
	{
		checkCore(exceptionClass, message, isBlank(arg));
	}
	
	public static void notBlankWithMessage(Class<?> exceptionClass, String message, String arg1, String arg2)
	{
		checkCore(exceptionClass, message, isBlank(arg1, arg2));
	}
	
	public static void notBlankWithMessage(Class<?> exceptionClass, String message, String arg1, String arg2, String arg3)
	{
		checkCore(exceptionClass, message, isBlank(arg1, arg2, arg3));
	}
	
	public static void notBlankWithMessage(Class<?> exceptionClass, String message, String arg1, String arg2, String arg3, String arg4)
	{
		checkCore(exceptionClass, message, isBlank(arg1, arg2, arg3, arg4));
	}
	
	public static void notBlankWithMessage(String message, String... args)
	{
		checkCore(IllegalArgumentException.class, message, isBlank(args));
	}
	
	public static void notBlankWithMessage(String message, String arg)
	{
		checkCore(IllegalArgumentException.class, message, isBlank(arg));
	}
	
	public static void notBlankWithMessage(String message, String arg1, String arg2)
	{
		checkCore(IllegalArgumentException.class, message, isBlank(arg1, arg2));
	}
	
	public static void notBlankWithMessage(String message, String arg1, String arg2, String arg3)
	{
		checkCore(IllegalArgumentException.class, message, isBlank(arg1, arg2, arg3));
	}
	
	public static void notBlankWithMessage(String message, String arg1, String arg2, String arg3, String arg4)
	{
		checkCore(IllegalArgumentException.class, message, isBlank(arg1, arg2, arg3, arg4));
	}
	
	public static void notBlank(Class<?> exceptionClass, String... args)
	{
		checkCore(exceptionClass, null, isBlank(args));
	}
	
	public static void notBlank(Class<?> exceptionClass, String arg)
	{
		checkCore(exceptionClass, null, isBlank(arg));
	}
	
	public static void notBlank(Class<?> exceptionClass, String arg1, String arg2)
	{
		checkCore(exceptionClass, null, isBlank(arg1, arg2));
	}
	
	public static void notBlank(Class<?> exceptionClass, String arg1, String arg2, String arg3)
	{
		checkCore(exceptionClass, null, isBlank(arg1, arg2, arg3));
	}
	
	public static void notBlank(Class<?> exceptionClass, String arg1, String arg2, String arg3, String arg4)
	{
		checkCore(exceptionClass, null, isBlank(arg1, arg2, arg3, arg4));
	}
	
	public static void notBlank(String... args)
	{
		checkCore(IllegalArgumentException.class, null, isBlank(args));
	}
	
	public static void notBlank(String arg)
	{
		checkCore(IllegalArgumentException.class, null, isBlank(arg));
	}
	
	public static void notBlank(String arg1, String arg2)
	{
		checkCore(IllegalArgumentException.class, null, isBlank(arg1, arg2));
	}
	
	public static void notBlank(String arg1, String arg2, String arg3)
	{
		checkCore(IllegalArgumentException.class, null, isBlank(arg1, arg2, arg3));
	}
	
	public static void notBlank(String arg1, String arg2, String arg3, String arg4)
	{
		checkCore(IllegalArgumentException.class, null, isBlank(arg1, arg2, arg3, arg4));
	}
	
	
	
	
	
	public static boolean isBlank(Object... obj)
	{
		return isBlank(toStringArray(obj));
	}
	
	public static boolean isBlank(Object str)
	{
		return ((str == null)||("".equals(str)));
	}
	
	public static boolean isBlank(Object str1, Object str2)
	{
		return (str1 == null)||("".equals(str1))||(str2 == null)||("".equals(str2));
	}
	
	public static boolean isBlank(Object str1, Object str2, Object str3)
	{
		return (str1 == null)||("".equals(str1))||(str2 == null)||("".equals(str2))||(str3 == null)||("".equals(str3));
	}
	
	public static boolean isBlank(Object str1, Object str2, Object str3, Object str4)
	{
		return (str1 == null)||("".equals(str1))||(str2 == null)||("".equals(str2))||(str3 == null)||("".equals(str3))||(str4 == null)||("".equals(str4));
	}
	
	public static void notBlankWithMessage(Class<?> exceptionClass, String message, Object... args)
	{
		checkCore(exceptionClass, message, isBlank(args));
	}
	
	public static void notBlankWithMessage(Class<?> exceptionClass, String message, Object arg)
	{
		checkCore(exceptionClass, message, isBlank(arg));
	}
	
	public static void notBlankWithMessage(Class<?> exceptionClass, String message, Object arg1, Object arg2)
	{
		checkCore(exceptionClass, message, isBlank(arg1, arg2));
	}
	
	public static void notBlankWithMessage(Class<?> exceptionClass, String message, Object arg1, Object arg2, Object arg3)
	{
		checkCore(exceptionClass, message, isBlank(arg1, arg2, arg3));
	}
	
	public static void notBlankWithMessage(Class<?> exceptionClass, String message, Object arg1, Object arg2, Object arg3, Object arg4)
	{
		checkCore(exceptionClass, message, isBlank(arg1, arg2, arg3, arg4));
	}
	
	public static void notBlankWithMessage(String message, Object... args)
	{
		checkCore(IllegalArgumentException.class, message, isBlank(args));
	}
	
	public static void notBlankWithMessage(String message, Object arg)
	{
		checkCore(IllegalArgumentException.class, message, isBlank(arg));
	}
	
	public static void notBlankWithMessage(String message, Object arg1, Object arg2)
	{
		checkCore(IllegalArgumentException.class, message, isBlank(arg1, arg2));
	}
	
	public static void notBlankWithMessage(String message, Object arg1, Object arg2, Object arg3)
	{
		checkCore(IllegalArgumentException.class, message, isBlank(arg1, arg2, arg3));
	}
	
	public static void notBlankWithMessage(String message, Object arg1, Object arg2, Object arg3, Object arg4)
	{
		checkCore(IllegalArgumentException.class, message, isBlank(arg1, arg2, arg3, arg4));
	}
	
	public static void notBlank(Class<?> exceptionClass, Object... args)
	{
		checkCore(exceptionClass, null, isBlank(args));
	}
	
	public static void notBlank(Class<?> exceptionClass, Object arg)
	{
		checkCore(exceptionClass, null, isBlank(arg));
	}
	
	public static void notBlank(Class<?> exceptionClass, Object arg1, Object arg2)
	{
		checkCore(exceptionClass, null, isBlank(arg1, arg2));
	}
	
	public static void notBlank(Class<?> exceptionClass, Object arg1, Object arg2, Object arg3)
	{
		checkCore(exceptionClass, null, isBlank(arg1, arg2, arg3));
	}
	
	public static void notBlank(Class<?> exceptionClass, Object arg1, Object arg2, Object arg3, Object arg4)
	{
		checkCore(exceptionClass, null, isBlank(arg1, arg2, arg3, arg4));
	}
	
	public static void notBlank(Object... args)
	{
		checkCore(IllegalArgumentException.class, null, isBlank(args));
	}
	
	public static void notBlank(Object arg)
	{
		checkCore(IllegalArgumentException.class, null, isBlank(arg));
	}
	
	public static void notBlank(Object arg1, Object arg2)
	{
		checkCore(IllegalArgumentException.class, null, isBlank(arg1, arg2));
	}
	
	public static void notBlank(Object arg1, Object arg2, Object arg3)
	{
		checkCore(IllegalArgumentException.class, null, isBlank(arg1, arg2, arg3));
	}
	
	public static void notBlank(Object arg1, Object arg2, Object arg3, Object arg4)
	{
		checkCore(IllegalArgumentException.class, null, isBlank(arg1, arg2, arg3, arg4));
	}
	
	
	
	
	
	
	
	
	
	
	public static void allTrue(Class<?> exceptionClass, String message, boolean condition)
	{
		checkCore(exceptionClass, message, !condition);
	}
	
	public static void allTrue(String message, boolean condition)
	{
		checkCore(IllegalArgumentException.class, message, !condition);
	}
	
	public static void allTrue(Class<?> exceptionClass, boolean condition)
	{
		checkCore(exceptionClass, null, !condition);
	}
	
	public static void allTrue(boolean condition)
	{
		checkCore(IllegalArgumentException.class, null, !condition);
	}
	
	
	
	
	public static boolean isNull(Object... obj)
	{
		boolean isNull = true;
		if(obj != null)
		{
			isNull = false;
			for(int i=0; i<obj.length; ++i)
			{
				if(obj[i] == null)
				{
					isNull = true;
					break;
				}
			}
		}
		return isNull;
	}
	
	public static boolean isNull(Object str)
	{
		return (str == null);
	}
	
	public static boolean isNull(Object str1, Object str2)
	{
		return (str1 == null)||(str2 == null);
	}
	
	public static boolean isNull(Object str1, Object str2, Object str3)
	{
		return (str1 == null)||(str2 == null)||(str3 == null);
	}
	
	public static boolean isNull(Object str1, Object str2, Object str3, Object str4)
	{
		return (str1 == null)||(str2 == null)||(str3 == null)||(str4 == null);
	}
	
	public static void notNullWithMessage(Class<?> exceptionClass, String message, Object... obj)
	{
		checkCore(exceptionClass, message, isNull(obj));
	}
	
	public static void notNullWithMessage(Class<?> exceptionClass, String message, Object arg)
	{
		checkCore(exceptionClass, message, isNull(arg));
	}
	
	public static void notNullWithMessage(Class<?> exceptionClass, String message, Object arg1, Object arg2)
	{
		checkCore(exceptionClass, message, isNull(arg1, arg2));
	}
	
	
	public static void notNullWithMessage(String message, Object... obj)
	{
		checkCore(IllegalArgumentException.class, message, isNull(obj));
	}
	
	public static void notNullWithMessage(String message, Object arg)
	{
		checkCore(IllegalArgumentException.class, message, isNull(arg));
	}
	
	public static void notNullWithMessage(String message, Object arg1, Object arg2)
	{
		checkCore(IllegalArgumentException.class, message, isNull(arg1, arg2));
	}
	
	
	
	public static void notNull(Class<?> exceptionClass, Object... obj)
	{
		checkCore(exceptionClass, null, isNull(obj));
	}
	
	public static void notNull(Class<?> exceptionClass, Object arg)
	{
		checkCore(exceptionClass, null, isNull(arg));
	}
	
	public static void notNull(Class<?> exceptionClass, Object arg1, Object arg2)
	{
		checkCore(exceptionClass, null, isNull(arg1, arg2));
	}
	
	
	
	public static void notNull(Object... obj)
	{
		checkCore(IllegalArgumentException.class, null,isNull(obj));
	}
	
	public static void notNull(Object arg)
	{
		checkCore(IllegalArgumentException.class, null, isNull(arg));
	}
	
	public static void notNull(Object arg1, Object arg2)
	{
		checkCore(IllegalArgumentException.class, null, isNull(arg1, arg2));
	}
	
	public static void notNull(Object arg1, Object arg2, Object arg3)
	{
		checkCore(IllegalArgumentException.class, null, isNull(arg1, arg2, arg3));
	}
	
	public static void notNull(Object arg1, Object arg2, Object arg3, Object arg4)
	{
		checkCore(IllegalArgumentException.class, null, isNull(arg1, arg2, arg3, arg4));
	}
	
	
	
	
	
	public static boolean isEmptyArray(Object[]... array)
	{
		boolean emptyArray = true;
		if((array != null)&&(array.length > 0))
		{
			emptyArray = false;
			for(int i=0; i<array.length; ++i)
			{
				if(array[i] == null || array[i].length == 0)
				{
					emptyArray = true;
					break;
				}
			}
		}
		return emptyArray;
	}
	
	
	
	
	
	
	
	
	public static boolean isContainingAny(String[] str1, String... str2)
	{
		boolean contains = false;
		if(str1 != null && str2 != null)
		{
			for(int i=0; i<str1.length; ++i)
			{
				for(int j=0; j<str2.length; ++j)
				{
					if(str1[i].contains(str2[j]))
					{
						contains = true;
						break;
					}
				}
			}
		}
		return contains;
	}
	
	public static void notContainingAnyWithMessage(Class<?> exceptionClass, String message, String[] str1, String... str2)
	{
		checkCore(exceptionClass, message, isContainingAny(str1, str2));
	}
	
	public static void notContainingAnyWithMessage(String message, String[] str1, String... str2)
	{
		checkCore(IllegalArgumentException.class, message, isContainingAny(str1, str2));
	}
	
	public static void notContainingAny(Class<?> exceptionClass, String[] str1, String... str2)
	{
		checkCore(exceptionClass, null, isContainingAny(str1, str2));
	}
	
	public static void notContainingAny(String[] str1, String... str2)
	{
		checkCore(IllegalArgumentException.class, null,isContainingAny(str1, str2));
	}
	
	
	
	public static boolean isContainingAny(Object[] str1, String... str2)
	{
		return isContainingAny(toStringArray(str1), str2);
	}
	
	public static void notContainingAnyWithMessage(Class<?> exceptionClass, String message, Object[] str1, String... str2)
	{
		checkCore(exceptionClass, message, isContainingAny(str1, str2));
	}
	
	public static void notContainingAnyWithMessage(String message, Object[] str1, String... str2)
	{
		checkCore(IllegalArgumentException.class, message, isContainingAny(str1, str2));
	}
	
	public static void notContainingAny(Class<?> exceptionClass, Object[] str1, String... str2)
	{
		checkCore(exceptionClass, null, isContainingAny(str1, str2));
	}
	
	public static void notContainingAny(Object[] str1, String... str2)
	{
		checkCore(IllegalArgumentException.class, null,isContainingAny(str1, str2));
	}
	
	
	
	
	public static boolean isContainingAny(String[] str1, Object... str2)
	{
		return isContainingAny(str1, toStringArray(str2));
	}
	
	public static void notContainingAnyWithMessage(Class<?> exceptionClass, String message, String[] str1, Object... str2)
	{
		checkCore(exceptionClass, message, isContainingAny(str1, str2));
	}
	
	public static void notContainingAnyWithMessage(String message, String[] str1, Object... str2)
	{
		checkCore(IllegalArgumentException.class, message, isContainingAny(str1, str2));
	}
	
	public static void notContainingAny(Class<?> exceptionClass,String[] str1, Object... str2)
	{
		checkCore(exceptionClass, null, isContainingAny(str1, str2));
	}
	
	public static void notContainingAny(String[] str1, Object... str2)
	{
		checkCore(IllegalArgumentException.class, null,isContainingAny(str1, str2));
	}
	
	
	
	
	
	
	public static boolean isContainingAny(Object[] str1, Object... str2)
	{
		return isContainingAny(toStringArray(str1), toStringArray(str2));
	}
	
	public static void notContainingAnyWithMessage(Class<?> exceptionClass, String message, Object[] str1, Object... str2)
	{
		checkCore(exceptionClass, message, isContainingAny(str1, str2));
	}
	
	public static void notContainingAnyWithMessage(String message, Object[] str1, Object... str2)
	{
		checkCore(IllegalArgumentException.class, message, isContainingAny(str1, str2));
	}
	
	public static void notContainingAny(Class<?> exceptionClass,Object[] str1, Object... str2)
	{
		checkCore(exceptionClass, null, isContainingAny(str1, str2));
	}
	
	public static void notContainingAny(Object[] str1, Object... str2)
	{
		checkCore(IllegalArgumentException.class, null,isContainingAny(str1, str2));
	}


	
	
	
	
	public static boolean isContaining(String str1, String... str2)
	{
		boolean contains = false;
		if(str1 != null && str2 != null)
		{
			for(int i=0; i<str2.length; ++i)
			{
				if(str1.contains(str2[i]))
				{
					contains = true;
					break;
				}
			}
		}
		return contains;
	}
	
	
	
	

	private static String[] toStringArray(Object[] args)
	{
		String[] converted = null;
		if(args != null)
		{
			if(args instanceof String[])
			{
				converted = (String[]) args;
			}
			else
			{
				converted = new String[args.length];
				for(int i=0; i<args.length; ++i)
				{
					converted[i] = args[i]+"";
				}
			}
		}
		
		return converted;
	}
	
		
	
	public static boolean isNumeric(Object object)
	{
		boolean numeric = false;
		
		if(object != null)
		{
			numeric = true;
			try
			{
				Double.parseDouble(object+"");
			}
			catch(Throwable e)
			{
				numeric = false;
			}
		}

		return numeric;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) throws Exception
	{
		isBlank("", "s");
	}
}
