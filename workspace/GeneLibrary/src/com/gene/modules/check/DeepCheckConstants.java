package com.gene.modules.check;

public class DeepCheckConstants
{
	public static class Result
	{
		public static final int UNKNOWN = -1;
		public static final int FALSE = 0;
		public static final int TRUE = 1;
	}
	
	public static class ErrorCode
	{
		public static final int UNKNOWN = -1;
		public static final int NULL = 0;
		public static final int EMPTY = 1;
		public static final int HAS_NULL = 2;
		public static final int HAS_EMPTY = 3;
		public static final int ZERO_LENGTH = 4;
		public static final int WRONG_NUM_OF_ARGS = 5;
		public static final int NOT_EQUALS = 6;
		public static final int EQUAL = 7;
		public static final int TYPE_MISMATCH = 8;
		public static final int LESS = 9;
		public static final int GREATER = 10;
		public static final int INVALID_NUM_OF_ARGS = 11;
		public static final int CONTAINS = 12;
		public static final int NOT_TRUE = 13;
		public static final int NOT_NUMERIC = 14;
		
		public static String toMessage(int errorCode)
		{
			String status = null;
			switch(errorCode)
			{
				case ErrorCode.NULL: status = "is null"; break;
				case ErrorCode.EMPTY: status = "is empty"; break;
				case ErrorCode.HAS_NULL: status = "has null value"; break;
				case ErrorCode.HAS_EMPTY: status = "has empty value"; break;
				case ErrorCode.ZERO_LENGTH: status = "has 0 length"; break;
				case ErrorCode.WRONG_NUM_OF_ARGS: status = "has wrong number of arguments"; break;
				case ErrorCode.NOT_EQUALS: status = "is not equal to"; break;
				case ErrorCode.EQUAL: status = "is equal to"; break;
				case ErrorCode.UNKNOWN: status = "has unknown problem"; break;
				case ErrorCode.TYPE_MISMATCH: status = "type is not matched to"; break;
				case ErrorCode.LESS: status = "is less than"; break;
				case ErrorCode.GREATER: status = "is greater than"; break;
				case ErrorCode.INVALID_NUM_OF_ARGS: status = "has invalid number of arguments"; break;
				case ErrorCode.CONTAINS: status = "contains"; break;
				case ErrorCode.NOT_TRUE: status = "is not true"; break;
				case ErrorCode.NOT_NUMERIC: status = "is not numeric"; break;
				default: status = "has unknown problem"; break;
			}
			
			return status;
		}
	}
}
