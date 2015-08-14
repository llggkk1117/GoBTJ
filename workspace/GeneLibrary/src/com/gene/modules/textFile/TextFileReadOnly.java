package com.gene.modules.textFile;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.gene.modules.exceptions.InstanceAlreadyExistException;
import com.gene.modules.semaphore.Semaphore;


public class TextFileReadOnly
{
	private static HashSet<TextFileReadOnly> textFileReadOnlyRegistry;
	private static Semaphore semaphore;
	static
	{
		textFileReadOnlyRegistry = new HashSet<TextFileReadOnly>();
		semaphore = new Semaphore();
	}
	private String fileName;
	private RandomAccessFile file;

	

	public TextFileReadOnly(String fileName) throws InterruptedException, IOException 
	{
		this.fileName = fileName;
		this.file = new RandomAccessFile(this.fileName, "r");
		this.reset();
		TextFileReadOnly.semaphore.acquire();
		TextFileReadOnly.textFileReadOnlyRegistry.add(this);
		TextFileReadOnly.semaphore.release();
	}
	

	protected void finalize() throws Throwable
	{
	    try
	    {
	    	this.close();
	    }
	    finally
	    {
	        super.finalize();
	    }
	}
	

	
	public synchronized void close() throws IOException
	{
		if(this.file != null)
		{
			this.file.close();
			this.file = null;
		}
		
		TextFileReadOnly.textFileReadOnlyRegistry.remove(this);
	}
	
	
	public synchronized void reset() throws IOException
	{
		this.file.seek(0);
	}
	
	
	public synchronized char readChar() throws IOException
	{
		char character = (char) this.file.read();
		return character;
	}
	
	

	public synchronized char readCharUnicode() throws IOException
	{
		char character = this.file.readChar();
		return character;
	}
	
	
	
	public synchronized String readLine() throws IOException
	{
		String line = null;
		
		if(this.file != null)
		{
			line = this.file.readLine();
		}
		
		return line;
	}

	
	
	
	public synchronized String[] readAll() throws IOException
	{
		List<String> lines_temp = new ArrayList<String>();
		
		if(this.file != null)
		{
			long currentOffset = file.getFilePointer();
			this.file.seek(0);
			String line = null;
			while((line = file.readLine()) != null)
			{
				lines_temp.add(line);
			}
			this.file.seek(currentOffset);
		}
		
		String[] lines = new String[lines_temp.size()];
		lines_temp.toArray(lines);
		
		return lines;
	}
	
	
	
	public synchronized long getNumOfLines() throws IOException
	{
		long count = 0;
		
		if(this.file != null)
		{
			long currentOffset = this.file.getFilePointer();
			this.file.seek(0);
			while(this.file.readLine() != null)
			{
				count++;
			}
			this.file.seek(currentOffset);
		}
		
		return count;
	}
	
	
	

	public synchronized long getNumOfLinesStartingWith(String... prefixes) throws IOException
	{
		long count = 0;
		
		if((prefixes != null)&&(prefixes.length > 0)&&(this.file != null))
		{
			long currentOffset = this.file.getFilePointer();
			this.file.seek(0);
			String line = null;
			while((line = this.file.readLine()) != null)
			{
				if(!"".equals(line))
				{
					for(int i=0; i<prefixes.length; ++i)
					{
						if(line.startsWith(prefixes[i]))
						{
							count++;
							break;
						}
					}
				}
			}
			this.file.seek(currentOffset);
		}
		
		return count;
	}
	
	
	
	public synchronized long getNumOfLinesExclude(String... prefixes) throws IOException
	{
		long count = 0;
		
		if((prefixes != null)&&(prefixes.length > 0)&&(this.file != null))
		{
			long currentOffset = this.file.getFilePointer();
			this.file.seek(0);
			String line = null;
			while((line = this.file.readLine()) != null)
			{
				if(!"".equals(line))
				{
					boolean valid = true;
					for(int i=0; i<prefixes.length; ++i)
					{
						if(line.startsWith(prefixes[i]))
						{
							valid = false;
							break;
						}
					}
					
					if(valid)
					{
						count++;
					}
				}
			}
			this.file.seek(currentOffset);
		}
		
		return count;
	}
	
	
	
	private synchronized long adjustOffset(long offset) throws IOException
	{
		long validOffset = offset;
		long endOfFileOffset = this.file.length();
		if(validOffset < 0 )
		{
			validOffset = 0;
		}
		else if(validOffset > endOfFileOffset)
		{
			validOffset = endOfFileOffset;
		}
		
		return validOffset;
	}
	
	

	public synchronized void setReadOffset(long readOffset) throws IOException
	{
		this.file.seek(this.adjustOffset(readOffset));
	}
	
	
	
	public synchronized long getReadOffset() throws IOException
	{
		return this.file.getFilePointer();
	}
	

	public synchronized long getFileSize() throws IOException
	{
		return this.file.length();
	}


	
	public synchronized String readLineBack() throws IOException
	{
		String line = null;
		if(this.file.getFilePointer() > 0)
		{
			line = "";
			boolean loop = true;
			while(loop)
			{
				Character temp =  this.readCharBack();
				if(temp == null)
				{
					loop = false;
				}
				else if("\n".equals(temp+""))
				{
					temp =  this.readCharBack();
					if(!"\r".equals(temp+""))
					{
						this.file.seek(this.file.getFilePointer()+1);
					}
					loop = false;
				}
				else
				{
					line = temp+line;
				}
			}
		}
		return line;
	}
	
	
	
	public synchronized Character readCharBack() throws IOException
	{
		Character character = null;
		long offset = this.file.getFilePointer();
		offset--;
		if(offset >= 0)
		{
			this.file.seek(offset);
			character = (char) this.file.read();
			this.file.seek(offset);
		}

		return character;
	}
	
	
	
	public synchronized Character readCharBackUnicode() throws IOException
	{
		Character character = null;
		long offset = this.file.getFilePointer();
		offset -= 2;
		if(offset >= 0)
		{
			this.file.seek(offset);
			character = this.file.readChar();
			this.file.seek(offset);
		}

		return character;
	}
	
	
	
	public synchronized boolean isOpen()
	{
		if(this.file != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	public synchronized String getFileName()
	{
		return this.fileName;
	}
	
	
	
	
	public static synchronized TextFileReadOnly getFile(String fileName) throws IOException, InstanceAlreadyExistException, InterruptedException
	{
		TextFileReadOnly instance = new TextFileReadOnly(fileName); 
		return instance;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public static synchronized void close(String fileName) throws IOException
	{
		Iterator<TextFileReadOnly> iterator = ((HashSet<TextFileReadOnly>) TextFileReadOnly.textFileReadOnlyRegistry.clone()).iterator();
		while(iterator.hasNext())
		{
			TextFileReadOnly file = iterator.next();
			if(fileName.equals(file.getFileName()))
			{
				file.close();
			}
		}
	}
	
	
	
	@SuppressWarnings("unchecked")
	public static synchronized boolean closeAll() throws IOException
	{
		Iterator<TextFileReadOnly> iterator = ((HashSet<TextFileReadOnly>) TextFileReadOnly.textFileReadOnlyRegistry.clone()).iterator();
		while(iterator.hasNext())
		{
			TextFileReadOnly file = iterator.next();
			file.close();
		}
		return (TextFileReadOnly.textFileReadOnlyRegistry.size() == 0);
	}
	
	

	
	
	
	
	public static synchronized boolean exists(String fileName)
	{
		boolean fileExists = false;
		if((fileName != null)&&(!"".equals(fileName)))
		{
			fileExists = true;
			FileReader fr = null;
			try
			{
				fr = new FileReader(fileName);
			}
			catch (FileNotFoundException e)
			{
				fileExists = false;
			}
			
			if(fr != null)
			{
				try
				{
					fr.close();
				}
				catch (IOException e){}
				fr = null;
			}
		}
		
		return fileExists;
	}
	

	public static int getNumOfOpenedFiles() throws IOException
	{
		return TextFileReadOnly.textFileReadOnlyRegistry.size();
	}
	

	public static synchronized boolean isOpened(String fileName)
	{
		boolean opened = false;
		Iterator<TextFileReadOnly> iterator = TextFileReadOnly.textFileReadOnlyRegistry.iterator();
		while(iterator.hasNext())
		{
			TextFileReadOnly file = iterator.next();
			if(fileName.equals(file.getFileName()))
			{
				opened = true;
				break;
			}
		}
		
		return opened; 
	}

	
	public static void main(String[] args) throws InstanceAlreadyExistException, IOException, InterruptedException
	{
//		TextFileReadOnly file1 = TextFileReadOnly.getFile("sample1.txt");
//		TextFileReadOnly file2 = TextFileReadOnly.getFile("sample2.txt");
//		TextFileReadOnly file3 = TextFileReadOnly.getFile("sample3.txt");
//		TextFileReadOnly file4 = TextFileReadOnly.getFile("sample3.txt");
//		System.out.println(file1);
//		System.out.println(file2);
//		System.out.println(file3);
//		System.out.println(file4);
//		System.out.println(TextFileReadOnly.textFileReadOnlyRegistry.size());
//		TextFileReadOnly.close("sample3.txt");
//		System.out.println(TextFileReadOnly.textFileReadOnlyRegistry.size());
//		file4.close();
//		System.out.println(TextFileReadOnly.textFileReadOnlyRegistry.size());
//		file1.close();
//		System.out.println(TextFileReadOnly.textFileReadOnlyRegistry.size());
//		System.out.println(file2.readLine());
//		file4.readLine();
//		System.out.println(TextFileReadOnly.closeAll());
		
//		TextFileReadOnly file1 = TextFileReadOnly.getFile("sample1.txt");
//		String line = null;
//		while((line = file1.readLine()) != null)
//		{
//			System.out.println(line);
//		}
//		
//		int count = 0;
//		while((line = file1.readLineBack()) != null)
//		{
//			System.out.println((count++)+": "+line);
//		}
//		
//		while((line = file1.readLine()) != null)
//		{
//			System.out.println(line);
//		}
//		
//		count = 0;
//		while((line = file1.readLineBack()) != null)
//		{
//			System.out.println((count++)+": "+line);
//		}
		
		
		TextFileReadOnly file1 = TextFileReadOnly.getFile("sample1.txt");
		long before = System.currentTimeMillis();
		long num = file1.getNumOfLinesStartingWith("aa", "bb");
		long after = System.currentTimeMillis();
		System.out.println(num);
		System.out.println((after-before)+"ms");
		
		num = file1.getNumOfLinesExclude("aa", "bb");
		System.out.println(num);
	}
}
