package com.gene.modules.check;




import com.gene.modules.check.DeepCheck.CHECK_MODE;
import com.gene.modules.check.DeepCheck.COMPARE_MODE;
import com.gene.modules.check.DeepCheckConstants.ErrorCode;
import com.gene.modules.check.DeepCheckConstants.Result;

import junit.framework.TestCase;

public class DeepCheckTest  extends TestCase
{
	private static void validate(DeepCheckResult validationResult, int expectedResult, Object... expectedData)
	{
		int actualResult = validationResult.getResult();
		assertEquals(expectedResult, actualResult);
		if(actualResult != Result.TRUE)
		{
			Object[] actualData = validationResult.getData().get(0);
			assertEquals(expectedData.length, actualData.length);
			for(int i=0; i<expectedData.length; ++i)
			{
				if((actualData[i] instanceof Integer)&&(expectedData[i] instanceof int[])&&(((int[])expectedData[i]).length==1))
				{
					int eData = ((int[])expectedData[i])[0];
					int aData = (int)actualData[i];
					assertEquals(eData, aData);
				}
				else if((actualData[i] instanceof int[])&&(((int[])actualData[i]).length==1)&&(expectedData[i] instanceof Integer))
				{
					int eData = (int)expectedData[i];
					int aData = ((int[])actualData[i])[0];
					assertEquals(eData, aData);
				}
				else if ((actualData[i] instanceof int[])&&(expectedData[i] instanceof int[]))
				{
					int[] eData = (int[])expectedData[i];
					int[] aData = (int[])actualData[i];
					for(int j=0; j<eData.length; ++j)
					{
						assertEquals(eData[j], aData[j]);
					}
				}
				else
				{
					assertEquals(expectedData[i], actualData[i]);
				}
			}
		}
		else
		{
			assertEquals(validationResult.getData().size(), 0);
		}
	}
	
	public void testCheckCore()
	{
		validate(DeepCheck.checkCore(CHECK_MODE.NUMERIC, 1, "q"), Result.FALSE, ErrorCode.NOT_NUMERIC, new int[]{2}, "q");
		validate(DeepCheck.checkCore(CHECK_MODE.NUMERIC, "1"), Result.TRUE, new Object[]{});
		
	}
	
	public void testCheckCoreExceptionCase()
	{
		validate(DeepCheck.checkCore(CHECK_MODE.NUMERIC), Result.UNKNOWN, ErrorCode.ZERO_LENGTH);
		validate(DeepCheck.checkCore(CHECK_MODE.NUMERIC, (Object) null), Result.FALSE, ErrorCode.NOT_NUMERIC, 1, null);
		validate(DeepCheck.checkCore(CHECK_MODE.NUMERIC, new Object[]{}), Result.UNKNOWN, ErrorCode.ZERO_LENGTH);
	}
	
	public void testCompareCore()
	{
		validate(DeepCheck.compareCore(COMPARE_MODE.EQUAL, new Object[]{"a"}, "b"), Result.FALSE, ErrorCode.LESS, 1, "a", 2, "b");
	}
	
	
	public void testCompareCoreExceptionCasae()
	{
		validate(DeepCheck.compareCore(COMPARE_MODE.EQUAL, null), Result.UNKNOWN, ErrorCode.ZERO_LENGTH, 2);
		validate(DeepCheck.compareCore(COMPARE_MODE.EQUAL, new Object[]{}), Result.UNKNOWN, ErrorCode.ZERO_LENGTH, 1);
		validate(DeepCheck.compareCore(COMPARE_MODE.EQUAL, null, (Object) null), Result.TRUE);
		validate(DeepCheck.compareCore(COMPARE_MODE.EQUAL, null, new Object[]{}), Result.UNKNOWN, ErrorCode.ZERO_LENGTH, 2);
		validate(DeepCheck.compareCore(COMPARE_MODE.EQUAL, new Object[]{}, (Object) null), Result.UNKNOWN, ErrorCode.ZERO_LENGTH, 1);
		validate(DeepCheck.compareCore(COMPARE_MODE.EQUAL, new Object[]{}, new Object[]{}), Result.UNKNOWN, ErrorCode.ZERO_LENGTH, 1);
	}
}
