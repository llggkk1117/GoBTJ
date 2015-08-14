package com.gene.modules.simpleTest;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
/**
*
* @author javaeschool.com
*/
public class OutputStreamExample
{
	private static void example1()
	{
		OutputStreamWriter osWriter = null;
		OutputStream outStream = null;
		File outputFile = null;
		String str1 = "Hello Java.";
		String str2 = "Java IO is fun.";
		
		try
		{
			outputFile = new File("outsWriter.txt");
			outStream = new FileOutputStream(outputFile);
			osWriter = new OutputStreamWriter(outStream);
			System.out.println("Writing data in file..!!");
			osWriter.write(str1);
			osWriter.append(str2);
			System.out.println("Data successfully written in file..!!");
		}
		catch(IOException e)
		{
			System.out.println("Exception caught..!!");
			e.printStackTrace();
		}
		finally
		{
			try
			{
				//closing objects
				System.out.println("Flushing object..!!");
				outStream.flush();
				osWriter.flush();
				System.out.println("Closing object..!!");
				outStream.close();
				osWriter.close();
			}
			catch(IOException ioe)
			{
				System.out.println("IOException caught..!!");
				ioe.printStackTrace();
			}
		}
	}
	
	private static void example2()
	{
		FileOutputStream fop = null;
		File file;
		String content = "This is the text content";
 
		try
		{
			file = new File("newfile.txt");
			fop = new FileOutputStream(file);
 
			// if file doesnt exists, then create it
			if (!file.exists())
			{
				file.createNewFile();
			}
 
			// get the content in bytes
			byte[] contentInBytes = content.getBytes();
 
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
 
			System.out.println("Done");
 
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (fop != null)
				{
					fop.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private static void example3() throws IOException
	{
		String msg = "hello";
		byte[] contentByte = msg.getBytes();

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byteArrayOutputStream.write(contentByte);
		
		File file = new File("sample.txt");
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		byteArrayOutputStream.writeTo(fileOutputStream);
		fileOutputStream.close();
		byteArrayOutputStream.close();
	}
	
	public static void main(String args[]) throws IOException
	{
//		example1();
//		example2();
		example3();
	}
}