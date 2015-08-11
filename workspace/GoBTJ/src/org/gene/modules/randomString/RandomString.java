package org.gene.modules.randomString;

import java.util.Random;

import org.gene.modules.check.Check;


public class RandomString
{
	public static final String ALPHABET_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String ALPHABET_LOWER = "abcdefghijklmnopqrstuvwxyz";
	public static final String NUMBER = "0123456789";
	private static final String SPECIAL_1 = "`~!@#$%^&*()-_=+[]{}\\|;:\'\",.<>/?";
//	private static final String SPECIAL_2 = "할렐루야";
	public static String SPECIAL = "";
	static
	{
		SPECIAL += SPECIAL_1;
//		SPECIAL += SPECIAL_2;
	}
	
	private Random random;
	private char[] symbolList;
	private char[] buffer;
	
	
	
	public RandomString(int length)
	{
		String charSequence = "";
		charSequence += ALPHABET_UPPER;
		charSequence += ALPHABET_LOWER;
		charSequence += NUMBER;
		charSequence += SPECIAL;
		this.initialize(length, charSequence);
	}
	

	public RandomString(int length, boolean specialCharOn)
	{
		String charSequence = "";
		charSequence += ALPHABET_UPPER;
		charSequence += ALPHABET_LOWER;
		charSequence += NUMBER;
		charSequence += (specialCharOn ? SPECIAL : "");
		
		this.initialize(length, charSequence);
	}
	

	
	public RandomString(int length, String... characterSets)
	{
		Check.notBlank((Object[])characterSets);
		Check.allTrue("length should be greater than 1", length>1);
		
		String allCharacterSets = "";
		for(int i=0; i<characterSets.length; ++i)
		{
			allCharacterSets += characterSets[i];
		}
		
		this.initialize(length, allCharacterSets);
	}
	
	
	
	
	public RandomString(int length, char... characters)
	{
		Check.allTrue("length should be greater than 1", length>1);

		String allCharacters = "";
		for(int i=0; i<characters.length; ++i)
		{
			allCharacters += characters[i];
		}
		
		this.initialize(length, allCharacters);
	}
	
	
	
	
	private synchronized void initialize(int length, String charSet)
	{
		Check.notBlank(charSet);
		Check.allTrue("length should be greater than 1", length>1);
		
		this.random = new Random();
		this.setCharSet(charSet);
		this.setLength(length);
	}
	
	

	
	public synchronized void setLength(int length)
	{
		Check.allTrue("length should be greater than 1", length>1);
		this.buffer = new char[length];
	}
	
	
	
	public int getLength()
	{
		return this.buffer.length;
	}
	
	
	
	public synchronized void setCharSet(String charSet)
	{
		Check.notBlank(charSet);
		this.symbolList = charSet.toCharArray();
	}
	

	
	public synchronized void addCharSet(String newCharSet)
	{
		Check.notBlank(newCharSet);
		
		String currentCharSet = null;
		if((this.symbolList == null)||(this.symbolList.length == 0))
		{
			currentCharSet = "";
		}
		else
		{
			currentCharSet = String.valueOf(this.symbolList);
		}
		
		currentCharSet += newCharSet;
		this.symbolList = currentCharSet.toCharArray();
	}
	
	
	
	
	public synchronized char[] getCharSet()
	{
		return this.symbolList;
	}
	
	
	
	
	public synchronized String nextString()
	{
		for (int i=0; i<this.buffer.length; ++i)
		{
			this.buffer[i] = this.symbolList[this.random.nextInt(this.symbolList.length)];
		}
		return String.valueOf(this.buffer);
	}
	
	
	
	
	public static void main(String[] args)
	{
		RandomString r0 = new RandomString(2, "abc", "a");
		for(int i=0; i<10; ++i)
		{
			System.out.println(r0.nextString());
		}
		r0.addCharSet("def");
		System.out.println("--------------------------------------");
		for(int i=0; i<10; ++i)
		{
			System.out.println(r0.nextString());
		}
		
//		RandomString r1 = new RandomString(10, false);
//		RandomString r2 = new RandomString(10, "ABCDabcd");
//		RandomString r3 = new RandomString(10, new String[]{"ABCDabcd", "1234"});
//		RandomString r8 = new RandomString(10, "ABCD", "1234", "!@#$", "abcd", "qwer", "_+=", "{}[]<>");
//		RandomString r4 = new RandomString(10, new char[]{'A', 'B', 'C', 'D'});
//		RandomString r6 = new RandomString(10, RandomString.SPECIAL);
//		RandomString r7 = new RandomString(10, new String[]{RandomString.ALPHABET_LOWER, RandomString.SPECIAL});
		
//		System.out.println(r0.nextString());
//		System.out.println(r1.nextString());
//		System.out.println(r2.nextString());
//		System.out.println(r3.nextString());
//		System.out.println(r4.nextString());
//		System.out.println(r5.nextString());
//		System.out.println(r6.nextString());
//		System.out.println(r7.nextString());
//		for(int i=0; i<10; ++i)
//		{
//			System.out.println(r0.nextString());
//		}
	}
}
