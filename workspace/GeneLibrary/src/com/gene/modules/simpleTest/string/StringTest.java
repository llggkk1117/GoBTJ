package com.gene.modules.simpleTest.string;

public class StringTest {

	public static void main(String[] args)
	{
		String text = "hello (hihi) ";
		String[] temp = text.split("\\(");
		String text1 = temp[0].trim();
		String text2 = temp[1].replace(")", "").trim();
		System.out.println(text);
		System.out.println(text1);
		System.out.println(text2);
		System.out.println(text.contains("\\("));
		System.out.println(text.contains("("));
	}
}
