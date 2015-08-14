package com.gene.modules.simpleTest;

import org.apache.commons.lang.Validate;

public class ValidationTest
{
	public static void main(String[] args)
	{
		String a = null;
		Validate.notNullWithMessage(a, "Yo!");
	}
}
