package com.gene.modules.check;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Vector;

import com.gene.modules.check.DeepCheckConstants.ErrorCode;
import com.gene.modules.check.DeepCheckConstants.Result;



public class DeepCheck
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
	
	
	private static void raiseException(Class<?> exceptionClass, String message, String internalMessage)
	{
		if(message == null){message = "";}
		if(internalMessage == null){internalMessage = "";}
		String entireMessage = message+("".equals(message) ? internalMessage : " ("+internalMessage+")");
		
		raiseException(exceptionClass, entireMessage);
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
	
	
	
	
	private static String getOrdinal(int num)
	{
		return (num==1?"st":(num==2?"nd":(num==3?"rd":"th")));
	}
	

	
	private static String getObjectString(Object location)
	{
		String object = "";
		if(location != null)
		{
			if(location instanceof int[])
			{
				int[] objectLocation = (int[]) location;
				if(objectLocation.length > 0)
				{
					if(objectLocation.length==1)
					{
						object += objectLocation[0]+getOrdinal(objectLocation[0])+" argument";
					}
					else if(objectLocation.length==2)
					{
						object += objectLocation[1]+getOrdinal(objectLocation[1])+" element in "+objectLocation[0]+getOrdinal(objectLocation[0])+" argument";
					}
				}
			}
			else if (location instanceof Integer)
			{
				int objectLocation = (int) location;
				object += objectLocation+getOrdinal(objectLocation)+" argument";
			}
		}

		return object;
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
			catch(Exception e)
			{
				numeric = false;
			}
		}

		return numeric;
	}
	
	
	private static void raiseExceptionIfError(Class<?> exceptionClass, String customMessage, String errorMessage)
	{
		if(!"".equals(errorMessage))
		{
			raiseException(exceptionClass, customMessage, errorMessage);
		}
	}
	
	
	private static String parseErrorMessage(DeepCheckResult validationResult)
	{
		String message = "";
		if((validationResult!= null)&&(validationResult.getResult() != Result.TRUE))
		{
			List<Object[]>data = validationResult.getData();
			
			for(int i=0; i<data.size(); ++i)
			{
				Object[] currentData = data.get(i);
				message += (i==0?"":"\n");
				
				String status = ErrorCode.toMessage((int)currentData[0]);
				if(currentData.length == 1)
				{
					message += "argument"+" "+status;
				}
				else if(currentData.length == 2)
				{
					String objectLocation = getObjectString(currentData[1]);
					message += objectLocation+" "+status;
				}
				else if(currentData.length == 3)
				{
					String objectLocation = getObjectString(currentData[1]);
					Object objectValue = currentData[2];
					message += objectLocation+" (\'"+objectValue+"\') "+status;
				}
				else if(currentData.length == 5)
				{
					String leftObjectLocation = getObjectString(currentData[1]);
					String leftObjectString = currentData[2]+"";
					String rightObjectLocation = getObjectString(currentData[3]);
					String rightObjectString = currentData[4]+"";
					message += leftObjectLocation+" (\'"+leftObjectString+"\') "+status+" "+rightObjectLocation+" (\'"+rightObjectString+"\')";
				}
			}
		}
		
		return message;
	}
	
	
	
	
	
	
	
	
	
	public static class CHECK_MODE
	{
		public static int NUMERIC = 0;
		public static int NOT_NULL = 1;
		public static int NOT_BLANK = 2;
		public static int TRUE = 3;
		public static int FALSE = 4;
	}
	
	public static DeepCheckResult checkCore(int checkMode, Object... args)
	{
		if(args==null){args = new Object[]{null};}
		
		DeepCheckResult validationResult = new DeepCheckResult(Result.TRUE);
		
		if(args.length == 0){validationResult.set(Result.UNKNOWN, ErrorCode.ZERO_LENGTH);}
		else
		{
			for(int i=0; i<args.length; ++i)
			{
				if(!((args[i] instanceof Object[])))
				{
					Object currentObject = args[i];
					int[] currentObjectLocation = new int[]{i+1};
					businessLogic(checkMode, currentObjectLocation, currentObject, validationResult);
					if(validationResult.getResult() != Result.TRUE){break;}
				}
				else
				{
					Object[] normalizedCurrentObject =  (Object[])normalize(args[i]);
					for(int j=0; j<normalizedCurrentObject.length; ++j)
					{
						Object currentObject = normalizedCurrentObject[j];
						int[] currentObjectLocation = new int[]{i+1, j+1};
						businessLogic(checkMode, currentObjectLocation, currentObject, validationResult);
						if(validationResult.getResult() != Result.TRUE){break;}
					}
					if(validationResult.getResult() != Result.TRUE){break;}
				}
			}
		}
			
		return validationResult;
	}
	
	private static void businessLogic(int checkMode, int[] currentObjectLocation, Object currentObject, DeepCheckResult validationResult)
	{
		if(checkMode == CHECK_MODE.NUMERIC)
		{
			if(!isNumeric(currentObject+""))
			{
				validationResult.set(Result.FALSE, ErrorCode.NOT_NUMERIC, currentObjectLocation, currentObject);
			}
		}
		else if(checkMode == CHECK_MODE.NOT_NULL)
		{
			if(currentObject==null)
			{
				validationResult.set(Result.FALSE, ErrorCode.NULL, currentObjectLocation, currentObject);
			}
		}
		else if(checkMode == CHECK_MODE.NOT_BLANK)
		{
			if(currentObject==null)
			{
				validationResult.set(Result.FALSE, ErrorCode.NULL, currentObjectLocation, currentObject);
			}
			else if("".equals(currentObject))
			{
				validationResult.set(Result.FALSE, ErrorCode.EMPTY, currentObjectLocation, currentObject);
			}
		}
		else if(checkMode == CHECK_MODE.TRUE)
		{
			boolean isTrue = (currentObject!=null) && (currentObject instanceof Boolean) && ((boolean)currentObject);
			if(!isTrue)
			{
				validationResult.set(Result.FALSE, ErrorCode.NOT_TRUE, currentObjectLocation, currentObject);
			}
		}
	}
	
	public static boolean simpleCheck(int checkMode, Object... args)
	{
		return (checkCore(checkMode, args).getResult()==Result.TRUE);
	}
	
	public static void checkWithMessage(Class<?> exceptionClass, String customMessage, int checkMode, Object... args)
	{
		String errorMessage = parseErrorMessage(checkCore(checkMode, args));
		raiseExceptionIfError(exceptionClass, customMessage, errorMessage);
	}

	public static void checkWithMessage(String customMessage, int checkMode, Object... args)
	{
		checkWithMessage(IllegalArgumentException.class, customMessage, checkMode, args);
	}
	
	public static void check(Class<?> exceptionClass, int checkMode, Object... args)
	{
		checkWithMessage(exceptionClass, null, checkMode, args);
	}
	
	public static void check(int checkMode, Object... args)
	{
		checkWithMessage(IllegalArgumentException.class, null, checkMode, args);
	}

	
	
	
	
	public static class COMPARE_MODE
	{
		public static int EQUAL = 0;
		public static int NOT_EQUAL = 1;
		public static int NOT_CONTAINS = 2;
		public static int LESS = 3;
		public static int LESS_OR_EQUAL = 4;
		public static int GREATER = 5;
		public static int GREATER_OR_EQUAL = 6;
	}
	
	
	public static DeepCheckResult compareCore(int compareMode, Object[] object1, Object... object2)
	{
		if(object1== null){object1 = new Object[]{null};}
		if(object2== null){object2 = new Object[]{null};}
		
		DeepCheckResult validationResult = new DeepCheckResult(Result.TRUE);
		
		if(object1.length == 0){validationResult.set(Result.UNKNOWN, ErrorCode.ZERO_LENGTH, new int[]{1});}
		else if(object2.length == 0){validationResult.set(Result.UNKNOWN, ErrorCode.ZERO_LENGTH, new int[]{2});}
		else
		{
			for(int i=0; i<object1.length; ++i)
			{
				if(!(object1[i] instanceof Object[]))
				{
					Object currentObject1 = object1[i];
					int[] currentObject1Location = new int[]{i+1};
					
					for(int k=0; k<object2.length; ++k)
					{
						if(!(object2[k] instanceof Object[]))
						{
							Object currentObject2 = object2[k];
							int[] currentObject2Location = new int[]{object1.length+k+1};
							businessLogic(compareMode, currentObject1Location, currentObject1, currentObject2Location, currentObject2, validationResult);
							if(validationResult.getResult() != Result.TRUE){break;}
						}
						else
						{
							Object[] normalizedCurrentObject2 =  (Object[])normalize(object2[k]);
							for(int r=0; r<normalizedCurrentObject2.length; ++r)
							{
								Object currentObject2 = normalizedCurrentObject2[r];
								int[] currentObject2Location = new int[]{object1.length+k+1, r+1};
								businessLogic(compareMode, currentObject1Location, currentObject1, currentObject2Location, currentObject2, validationResult);
								if(validationResult.getResult() != Result.TRUE){break;}
							}
						}
					}
				}
				else
				{
					Object[] normalizedCurrentObject1 =  (Object[])normalize(object1[i]);
					for(int j=0; j<normalizedCurrentObject1.length; ++j)
					{
						Object currentObject1 = normalizedCurrentObject1[j];
						int[] currentObject1Location = new int[]{i+1, j+1};
						
						for(int k=0; k<object2.length; ++k)
						{
							if(!(object2[k] instanceof Object[]))
							{
								Object currentObject2 = object2[k];
								int[] currentObject2Location = new int[]{object1.length+k+1};
								businessLogic(compareMode, currentObject1Location, currentObject1, currentObject2Location, currentObject2, validationResult);
								if(validationResult.getResult() != Result.TRUE){break;}
							}
							else
							{
								Object[] normalizedCurrentObject2 =  (Object[])normalize(object2[k]);
								for(int r=0; r<normalizedCurrentObject2.length; ++r)
								{
									Object currentObject2 = normalizedCurrentObject2[r];
									int[] currentObject2Location = new int[]{object1.length+k+1, r+1};
									businessLogic(compareMode, currentObject1Location, currentObject1, currentObject2Location, currentObject2, validationResult);
									if(validationResult.getResult() != Result.TRUE){break;}
								}
							}
						}
					}
				}
				if(validationResult.getResult() != Result.TRUE){break;}
			}
		}
			
		return validationResult;
	}
	
	private static void businessLogic(int compareMode, int[] standardObjectLocation, Object standardObject, int[] currentObjectLocation, Object currentObject, DeepCheckResult validationResult)
	{
		if(compareMode == COMPARE_MODE.EQUAL || compareMode == COMPARE_MODE.NOT_EQUAL || compareMode == COMPARE_MODE.LESS || compareMode == COMPARE_MODE.LESS_OR_EQUAL || compareMode == COMPARE_MODE.GREATER || compareMode == COMPARE_MODE.GREATER_OR_EQUAL)
		{
			boolean isStandardNumber = (!(standardObject instanceof String)) && isNumeric(standardObject);
			boolean isCurrentObjectNumber = (!(currentObject instanceof String)) && isNumeric(currentObject);
			
			if(isStandardNumber != isCurrentObjectNumber)
			{
				validationResult.set(Result.FALSE, ErrorCode.TYPE_MISMATCH, standardObjectLocation, standardObject, currentObjectLocation, currentObject);
			}
			else
			{
				Double difference = 0.0;
				if(isStandardNumber)
				{
					difference = Double.parseDouble(standardObject+"") - Double.parseDouble(currentObject+"");
				}
				else
				{
					difference = (double) (standardObject+"").compareTo(currentObject+"");
				}
				
				boolean correct = 
						(
						((compareMode==COMPARE_MODE.LESS || compareMode==COMPARE_MODE.LESS_OR_EQUAL) && (difference<0)) ||
						((compareMode==COMPARE_MODE.GREATER || compareMode==COMPARE_MODE.GREATER_OR_EQUAL) && (difference>0)) ||
						((compareMode==COMPARE_MODE.LESS_OR_EQUAL || compareMode==COMPARE_MODE.GREATER_OR_EQUAL || compareMode==COMPARE_MODE.EQUAL) && (difference==0)) ||
						((compareMode==COMPARE_MODE.NOT_EQUAL) && (difference!=0))
						);
				
				if(!correct)
				{
					validationResult.set(Result.FALSE, (difference < 0 ? ErrorCode.LESS : difference == 0 ? ErrorCode.EQUAL : ErrorCode.GREATER ), standardObjectLocation, standardObject, currentObjectLocation, currentObject);
				}
			}
		}
		else if(compareMode == COMPARE_MODE.NOT_CONTAINS)
		{
			boolean contains = ((standardObject+"").contains(currentObject+"")); 
			if(contains)
			{
				validationResult.set(Result.FALSE, ErrorCode.CONTAINS, standardObjectLocation, standardObject, currentObjectLocation, currentObject);
			}
		}
	}
	
	public static boolean simpleCompare(int compareMode, Object[] object1, Object... object2)
	{
		return (compareCore(compareMode, object1, object2).getResult()==Result.TRUE);
	}
	
	public static void compareWithMessage(Class<?> exceptionClass, String customMessage, int compareMode, Object[] object1, Object... object2)
	{
		String errorMessage = parseErrorMessage(compareCore(compareMode, object1, object2));
		raiseExceptionIfError(exceptionClass, customMessage, errorMessage);
	}
	
	public static void compareWithMessage(String customMessage, int compareMode, Object[] object1, Object... object2)
	{
		compareWithMessage(IllegalArgumentException.class, customMessage, compareMode, object1, object2);
	}
	
	public static void compare(Class<?> exceptionClass, int compareMode, Object[] object1, Object... object2)
	{
		compareWithMessage(exceptionClass, null, compareMode, object1, object2);
	}
	
	public static void compare(int compareMode, Object[] object1, Object... object2)
	{
		compareWithMessage(IllegalArgumentException.class, null, compareMode, object1, object2);
	}
	
	public static void compare(int compareMode, Object object1, Object... object2)
	{
		compareWithMessage(IllegalArgumentException.class, null, compareMode, convert(object1), object2);
	}
	
	private static Object[] convert(Object object)
	{
		return (object instanceof Object[]) ? (Object[])object : new Object[]{object};
	}
	
	@SuppressWarnings("unused")
	private static <T> Object[] convert(T[] object)
	{
		Object[] converted = new Object[object.length];
		for(int i=0; i<object.length; ++i)
		{
			converted[i] = (Object) object[i];
		}
		
		return converted;
	}
	
	

	
	

	
	// noBlank ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public static boolean isNotBlank(Object... args)
	{
		return simpleCheck(CHECK_MODE.NOT_BLANK, args);
	}
	
	public static void checkNotBlankWithMessage(Class<?> exceptionClass, String customMessage, Object... args)
	{
		checkWithMessage(exceptionClass, customMessage, CHECK_MODE.NOT_BLANK, args);
	}
	
	public static void checkNotBlankWithMessage(String customMessage, Object... args)
	{
		checkWithMessage(customMessage, CHECK_MODE.NOT_BLANK, args);
	}
	
	public static void checkNotBlank(Class<?> exceptionClass, Object... args)
	{
		check(exceptionClass, CHECK_MODE.NOT_BLANK, args);
	}
	
	public static void checkNotBlank(Object... args)
	{
		check(CHECK_MODE.NOT_BLANK, args);
	}
	
	
	


	
	
	
	
	
	
	// noNull ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public static boolean isNotNull(Object... args)
	{
		return simpleCheck(CHECK_MODE.NOT_NULL, args);
	}
	
	public static void checkNotNullWithMessage(Class<?> exceptionClass, String customMessage, Object... args)
	{
		checkWithMessage(exceptionClass, customMessage, CHECK_MODE.NOT_NULL, args);
	}
	
	public static void checkNotNullWithMessage(String customMessage, Object... args)
	{
		checkWithMessage(customMessage, CHECK_MODE.NOT_NULL, args);
	}
	
	public static void checkNotNull(Class<?> exceptionClass, Object... args)
	{
		check(exceptionClass, CHECK_MODE.NOT_NULL, args);
	}
	
	public static void checkNotNull(Object... args)
	{
		check(CHECK_MODE.NOT_NULL, args);
	}
	
	
	
	
	
	
	// allTrue ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public static boolean isTrue(Object... expressions)
	{
		return simpleCheck(CHECK_MODE.TRUE, expressions);
	}
	
	public static void checkTrueWithMessage(Class<?> exceptionClass, String customMessage, Object... expressions)
	{
		checkWithMessage(exceptionClass, customMessage, CHECK_MODE.TRUE, expressions);
	}
	
	public static void checkTrueWithMessage(String customMessage, Object... expressions)
	{
		checkWithMessage(customMessage, CHECK_MODE.TRUE, expressions);
	}
	
	public static void checkTrue(Class<?> exceptionClass, Object... expressions)
	{
		check(exceptionClass, CHECK_MODE.TRUE, expressions);
	}
	
	public static void checkTrue(Object... expressions)
	{
		check(CHECK_MODE.TRUE, expressions);
	}
	
	
	
	
	
	
	// numeric ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public static boolean isNumeric(Object... args)
	{
		return simpleCheck(CHECK_MODE.NUMERIC, args);
	}
	
	public static void checkNumericWithMessage(Class<?> exceptionClass, String customMessage, Object... args)
	{
		checkWithMessage(exceptionClass, customMessage, CHECK_MODE.NUMERIC, args);
	}
	
	public static void checkNumericWithMessage(String customMessage, Object... args)
	{
		checkWithMessage(customMessage, CHECK_MODE.NUMERIC, args);
	}
	
	public static void checkNumeric(Class<?> exceptionClass, Object... args)
	{
		check(exceptionClass, CHECK_MODE.NUMERIC, args);
	}
	
	public static void checkNumeric(Object... args)
	{
		check(CHECK_MODE.NUMERIC, args);
	}
	
	
	
	
	
	
	// equals ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	private static Object[] removeFirstElement(Object[] args)
	{
		Object[] rest = null;
		if ((args!=null)&&(args.length>0)) 
		{
			rest = new Object[args.length-1];
			for(int i=1; i<args.length; ++i)
			{
				rest[i-1] = args[i];
			}	
		}
		else
		{
			rest = new Object[0];
		}
			
		return rest;
	}
	
	public static boolean equals(Object... args)
	{
		return simpleCompare(COMPARE_MODE.EQUAL, new Object[]{args[0]}, removeFirstElement(args));
	}

	public static void checkEqualsWithMessage(Class<?> exceptionClass, String customMessage, Object... args)
	{
		compareWithMessage(exceptionClass, customMessage, COMPARE_MODE.EQUAL, new Object[]{args[0]}, removeFirstElement(args));
	}

	public static void checkEqualsWithMessage(String customMessage, Object... args)
	{
		compareWithMessage(customMessage, COMPARE_MODE.EQUAL, new Object[]{args[0]}, removeFirstElement(args));
	}

	public static void checkEquals(Class<?> exceptionClass, Object... args)
	{
		compare(exceptionClass, COMPARE_MODE.EQUAL, new Object[]{args[0]}, removeFirstElement(args));
	}

	public static void checkEquals(Object... args)
	{
		compare(COMPARE_MODE.EQUAL, new Object[]{args[0]}, removeFirstElement(args));
	}
	
	
	
	
	
	
	


	// notContains ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public static boolean notContains(Object[] object1, Object... object2)
	{
		return simpleCompare(COMPARE_MODE.NOT_CONTAINS, object1, object2);
	}

	public static void checkNotContainsWithMessage(Class<?> exceptionClass, String customMessage, Object[] object1, Object... object2)
	{
		compareWithMessage(exceptionClass, customMessage, COMPARE_MODE.NOT_CONTAINS, object1, object2);
	}

	public static void checkNotContainsWithMessage(String customMessage, Object[] object1, Object... object2)
	{
		compareWithMessage(customMessage, COMPARE_MODE.NOT_CONTAINS, object1, object2);
	}

	public static void checkNotContains(Class<?> exceptionClass,Object[] object1, Object... object2)
	{
		compare(exceptionClass, COMPARE_MODE.NOT_CONTAINS, object1, object2);
	}

	public static void checkNotContains(Object[] object1, Object... object2)
	{
		compare(COMPARE_MODE.NOT_CONTAINS, object1, object2);
	}
	
	
	
	
	
	
	// GREATER ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	public static boolean greaterThan(Object object1, Object... object2)
	{
		return simpleCompare(COMPARE_MODE.GREATER,convert(object1), object2);
	}
	
	public static void checkGreaterThanWithMessage(Class<?> exceptionClass, String customMessage, Object[] object1, Object... object2)
	{
		compareWithMessage(exceptionClass, customMessage, COMPARE_MODE.GREATER, object1, object2);
	}

	public static void checkGreaterThanWithMessage(String customMessage, Object object1, Object... object2)
	{
		compareWithMessage(customMessage, COMPARE_MODE.GREATER, convert(object1), object2);
	}

	public static void checkGreaterThan(Class<?> exceptionClass,Object[] object1, Object... object2)
	{
		compare(exceptionClass, COMPARE_MODE.GREATER, object1, object2);
	}

	public static void checkGreaterThan(Object[] object1, Object... object2)
	{
		compare(COMPARE_MODE.GREATER, object1, object2);
	}


	
	
	
	
	
	public static void main(String[] args) throws Exception
	{
//		checkAndRaiseException("equal", 1,2);
		
//		checkAllTrue(true, 1, false);
//		checkNumeric("23", "w");
//		compare("<=", new Object[]{1,0,0}, 2, 1, new Object[]{0,1,0});
		
//		compare("==", new Object[]{1}, 3, new Object[]{2});
		
//		System.out.println(noNull(0, new Object[]{1, new Object[]{2, 4, null}}, 1, 2 , ""));
//		checkNoNull(0, new Object[]{1, new Object[]{2, ""}}, 1, 2 , null);
//		checkNoBlank(0, new Object[]{1, new Object[]{2, 4, null}}, 1, 2 , "");
		//checkAndRaiseException("not equals", 1);
		
		
//		checkNotContains(new Object[]{1,0,0}, new Object[]{0,1,0});
//		Object o = new Object();
		checkEquals("1", 2, "3");
		
//		checkNoBlankExistsWithMessage("aa", new Object[]{new Object[][]{{"a", ""}}}, 1);
//		checkNoBlankExistsWithMessage(MissingDependancyException.class, "hello", "a", null);
		
//		System.out.println(Validator.anyBlankExists("", "l"));
//		System.out.println(Validator.isAnyNull("", null));
//		System.out.println(Validator.isGreaterThanAll(3, 2));
//		System.out.println(Validator.isAllNumeric("10.221"));
//		System.out.println(Validator.isAllNumeric(10.221));
//		System.out.println(Validator.validateContaints(1100, "").getResult());
//		System.out.println(Validator.validateContaints(new Object[]{170}, "570").getResult());
		
//		checkNoBlankExists(MissingDependancyException.class, "1", "as", "s", null);
//		checkNotContains(1, null);
//		checkNoNullExists(MissingDependancyException.class, null);
//		checkAllNotNullWithMessage(MissingDependancyException.class, "bb", null);
	}

}
