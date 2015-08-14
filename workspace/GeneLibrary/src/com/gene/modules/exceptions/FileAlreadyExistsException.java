package com.gene.modules.exceptions;

import java.io.IOException;




public class FileAlreadyExistsException extends IOException 
{
	static final long serialVersionUID = 4727557137051933424L;

	public FileAlreadyExistsException()
	{
		super();
	}
	
	public FileAlreadyExistsException(String file)
	{
		super(file);
	}
	
	public FileAlreadyExistsException(String file, String reason)
	{
		super(file + ((reason == null) ? "" : " (" + reason + ")"));
	}
	
//	public static void main(String[] args)
//	{
//		try
//		{
//			throw (new FileAlreadyExistsException("aaaa"));
//		}
//		catch(FileAlreadyExistsException e)
//		{
//			System.out.println(e.getMessage());
//		}
//	}
}