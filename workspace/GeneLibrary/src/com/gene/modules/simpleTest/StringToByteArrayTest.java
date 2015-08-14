package com.gene.modules.simpleTest;

import org.apache.commons.codec.binary.StringUtils;

public class StringToByteArrayTest
{
	public static void main(String[] args)
	{
		String msg = "hello";
		byte[] byteContent1 = StringUtils.getBytesUtf8(msg);
		byte[] byteContent2 = msg.getBytes();
		
		for(int i=0; i<byteContent1.length; ++i)
		{
			System.out.print("0x"+byteContent1[i]+"  ");
			System.out.println("0x"+byteContent2[i]);
		}
	}
}
