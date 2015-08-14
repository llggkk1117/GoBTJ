package com.gene.modules.simpleTest;


import org.govgrnds.core.util.crypto.DefaultCryptoHelper;

public class EncriptionTest
{
	public static void main(String[] args)
	{
		DefaultCryptoHelper helper = new DefaultCryptoHelper();
		System.out.println("sdc: "+helper.encrypt("sdc"));
		System.out.println("sdc5: "+helper.encrypt("sdc5"));
		System.out.println("sdc10: "+helper.encrypt("sdc10"));
	}
}
