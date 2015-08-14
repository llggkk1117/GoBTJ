package com.gene.modules.ftpClient;

import java.util.Random;

public class RandomString
{
	public static final String ALPHABET_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String ALPHABET_LOWER = "abcdefghijklmnopqrstuvwxyz";
	public static final String NUMBER = "0123456789";
	public static final String SPECIAL_1 = "`~!@#$%^&*()-_=+[]{}\\|;:\'\",.<>/?";
	public static final String SPECIAL = SPECIAL_1;
	
	private Random random;
	private char[] symbolList;
	private char[] buffer;
	
	
	public RandomString(int length)
	{
		this(length, new String[]{RandomString.ALPHABET_UPPER, RandomString.ALPHABET_LOWER, RandomString.NUMBER, RandomString.SPECIAL});
	}
	
	public RandomString(int length, boolean specialCharOn)
	{
		this(length, new String[]{RandomString.ALPHABET_UPPER, RandomString.ALPHABET_LOWER, RandomString.NUMBER, (specialCharOn ? RandomString.SPECIAL : "")});
	}
	

	
	
	public RandomString(int length, char[] charSequence)
	{
		this.initialize(length, charSequence);
	}
	
	
	public void initialize(int length, char[] charSequence)
	{
		if (length < 1)
		{
			throw new IllegalArgumentException("length < 1: " + length);
		}
		if(charSequence == null)
		{
			throw new IllegalArgumentException("charSequence should not be null");
		}
		
		this.random = new Random();
		this.symbolList = charSequence;
		this.buffer = new char[length];
	}
	
	

	public RandomString(int length, String charSequence)
	{
		this.initialize(length, charSequence.toCharArray());
	}

	
	
	public RandomString(int length, char[][] charSequenceSet)
	{
		String temp = "";
		for(int i=0; i<charSequenceSet.length; ++i)
		{
			temp += String.valueOf(charSequenceSet[i]);
		}

		this.initialize(length, temp.toCharArray());
	}
	
	
	
//	public RandomString(int length, String[] charSequenceSet)
//	{
//		String temp = "";
//		for(int i=0; i<charSequenceSet.length; ++i)
//		{
//			temp += charSequenceSet[i];
//		}
//		
//		this.initialize(length, temp.toCharArray());
//	}
	
	public RandomString(int length, String... charSequenceSet)
	{
		String temp = "";
		for(int i=0; i<charSequenceSet.length; ++i)
		{
			temp += charSequenceSet[i];
		}
		
		this.initialize(length, temp.toCharArray());
	}
	
	
	
	public String nextString()
	{
		for (int i=0; i<this.buffer.length; ++i)
		{
			this.buffer[i] = symbolList[random.nextInt(symbolList.length)];
		}
		return String.valueOf(this.buffer);
	}
	
	
	
	public static void main(String[] args)
	{
		RandomString r0 = new RandomString(10);
		RandomString r1 = new RandomString(10, false);
		RandomString r2 = new RandomString(10, "ABCDabcd");
		RandomString r3 = new RandomString(10, new String[]{"ABCDabcd", "1234"});
		RandomString r8 = new RandomString(10, "ABCD", "1234", "!@#$", "abcd", "qwer", "_+=", "{}[]<>");
		RandomString r4 = new RandomString(10, new char[]{'A', 'B', 'C', 'D'});
		RandomString r5 = new RandomString(10, new char[][]{{'A', 'B', 'C', 'D'}, {'1', '2', '3'}});
		RandomString r6 = new RandomString(10, RandomString.SPECIAL);
		RandomString r7 = new RandomString(10, new String[]{RandomString.ALPHABET_LOWER, RandomString.SPECIAL});
		
//		System.out.println(r0.nextString());
//		System.out.println(r1.nextString());
//		System.out.println(r2.nextString());
//		System.out.println(r3.nextString());
//		System.out.println(r4.nextString());
//		System.out.println(r5.nextString());
//		System.out.println(r6.nextString());
//		System.out.println(r7.nextString());
		System.out.println(r8.nextString());
	}
}
