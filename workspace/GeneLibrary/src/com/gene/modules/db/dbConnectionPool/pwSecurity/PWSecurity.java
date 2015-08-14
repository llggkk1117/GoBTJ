package com.gene.modules.db.dbConnectionPool.pwSecurity;

import org.govgrnds.core.util.crypto.DefaultCryptoHelper;

public class PWSecurity
{
	private static DefaultCryptoHelper crytoHelper;
	static
	{
		PWSecurity.crytoHelper = new DefaultCryptoHelper();
	}
	
	public static String encrypt(String word)
	{
		return PWSecurity.crytoHelper.encrypt(word);
	}
	
	public static String decrypt(String word)
	{
		return PWSecurity.crytoHelper.decrypt(word);
	}
}
