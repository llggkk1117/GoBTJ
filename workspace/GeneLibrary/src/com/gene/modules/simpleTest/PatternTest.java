package com.gene.modules.simpleTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternTest
{
	public static void main(String[] args)
	{
		// open parentheses charactor in regex: \\(
		// close parentheses charactor in regex: \\)
		// * charactor in regex: \\*

//		Pattern p = Pattern.compile("((a)|b|c)f");
//		Matcher m = p.matcher("cf");
		
		Pattern p1 = Pattern.compile("VARCHAR2\\(([0-9]+)(CHAR)?\\)");
		Matcher m1 = p1.matcher("VARCHAR2()");
		boolean b1 = m1.matches();


		Pattern p2 = Pattern.compile("NUMBER(\\((([0-9]+)|(((\\*)|([0-9]+)),((\\*)|([0-9]+))))\\))?");
		Matcher m2 = p2.matcher("NUMBER(*,2)");
		boolean b2 = m1.matches();
		
		System.out.println(b1);
	}
}
