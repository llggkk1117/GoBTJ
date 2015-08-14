
package com.gene.modules.textFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.gene.modules.exceptions.InstanceAlreadyExistException;
import com.gene.modules.semaphore.Semaphore;



public final class TextFile
{
	private static HashMap<String, TextFile> textFileRegistry;
	private static Semaphore semaphore;
	static
	{
		textFileRegistry = new HashMap<String, TextFile>();
		semaphore = new Semaphore();
	}
	private String fileName;
	private RandomAccessFile file;
	private long readOffset;
	private long writeOffset;
	
	
	
	public TextFile(String fileName) throws InterruptedException, IOException 
	{
		boolean successful = false;
		
		TextFile.semaphore.acquire();

		if(TextFile.textFileRegistry.get(fileName) == null)
		{
			this.file = new RandomAccessFile(fileName, "rwd");
			this.fileName = fileName;
			this.reset();
			TextFile.textFileRegistry.put(fileName, this);
			successful = true;
		}
		
		TextFile.semaphore.release();
		
		if(!successful)
		{
			throw new InstanceAlreadyExistException("Instance for "+fileName+" file already exists.");
		}
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
		if(file != null)
		{
			this.file.close();
			this.file = null;
		}
		this.readOffset = 0;
		this.writeOffset = 0;
		
		TextFile.textFileRegistry.remove(this.fileName);
	}
	
	
	
	public synchronized void reset() throws IOException
	{
		this.file.seek(0);
		this.readOffset = 0;
		this.writeOffset = this.file.length();
	}
	
	
	
	public synchronized char readChar() throws IOException
	{
		this.file.seek(this.readOffset);
		char character = (char) this.file.read();
		this.readOffset = this.file.getFilePointer();
		return character;
	}
	
	
	
	public synchronized char readCharUnicode() throws IOException
	{
		this.file.seek(this.readOffset);
		char character = this.file.readChar();
		this.readOffset = this.file.getFilePointer();
		return character;
	}
	
	
	
	public synchronized String readLine() throws IOException
	{
		String line = null;
		
		if(file != null)
		{
			this.file.seek(this.readOffset);
			line = this.file.readLine();
			this.readOffset = this.file.getFilePointer();
		}
		
		return line;
	}
	
	
	
	public synchronized String[] readAll() throws IOException
	{
		List<String> lines_temp = new ArrayList<String>();
		
		if(this.file != null)
		{
			long currentOffset = this.file.getFilePointer();
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
			long currentOffset = file.getFilePointer();
			this.file.seek(0);
			while(this.file.readLine() != null)
			{
				count++;
			}
			this.file.seek(currentOffset);
		}
		
		return count;
	}
	
	
	
	// not tested yet
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
				for(int i=0; i<prefixes.length; ++i)
				{
					if(line.startsWith(prefixes[i]))
					{
						count++;
						break;
					}
				}
			}
			this.file.seek(currentOffset);
		}
		
		return count;
	}
	
	
	
	// not tested yet	
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
		this.readOffset = this.adjustOffset(readOffset);
	}
	
	
	
	public synchronized long getReadOffset()
	{
		return this.readOffset;
	}
	
	
	
	public synchronized void setWriteOffset(long writeOffset) throws IOException
	{
		this.writeOffset = this.adjustOffset(writeOffset);
	}
	
	
	
	public synchronized long getWriteOffset()
	{
		return this.writeOffset;
	}
	
	

	public synchronized long getFileSize() throws IOException
	{
		return this.file.length();
	}

	
	
	public synchronized String readLineBack() throws IOException
	{
		if(this.readOffset > 0)
		{
			char temp;
			boolean loop = true;
			
			// go back until it finds any character except '\n' and '\r'
			// if one of them is found, stop and put the offset right after the character
			// this logic put the offset right after the target line to return
			while((loop)&&(this.readOffset > 0))
			{
				temp =  this.readCharBack();
				if((temp != '\n')&&(temp != '\r'))
				{
					loop = false;
					this.readOffset++;
				}
			}
			
			// go back until it finds '\n' or '\r' character
			// if one of them is found, stop and put the offset right after the character
			// this logic puts the offset right before the target line to return
			loop = true;
			while((loop)&&(this.readOffset > 0))
			{
				temp =  this.readCharBack();
				if((temp == '\n')||(temp == '\r'))
				{
					loop = false;
					this.readOffset++;
				}
			}

			// while clause condition is order-sensitive
			// it checks first condition first and then checks the second condition
		}
		
		long currentReadOffset = this.readOffset;
		String line = this.readLine(); 
		this.readOffset = currentReadOffset;
		
		return line; 
	}
	
	
	
	public synchronized Character readCharBack() throws IOException
	{
		Character character = null;
		if(this.file != null)
		{
			long offset = this.readOffset;
			offset--;
			this.file.seek(offset);
			character = (char) this.file.read();
			this.file.seek(offset);
			this.readOffset = offset;
		}

		return character;
	}
	
	
	
	public synchronized void write(String message) throws IOException
	{
		if(this.file != null)
		{
			this.file.seek(this.writeOffset);
			this.file.writeBytes(message);
			this.writeOffset = this.file.getFilePointer();
		}
	}
	
	
	
	public synchronized void write(String delimeter, String... elements) throws IOException
	{
		if(elements != null)
		{
			for(int i=0; i<elements.length; ++i)
			{
				this.write(elements[i]);
				if(i < (elements.length-1))
				{
					this.write(delimeter);
				}
			}
			this.write("\n");
		}
	}
	
	
	
	public synchronized void writeLine(String message) throws IOException
	{
		this.write(message+"\n");
	}
	
	
	
	public synchronized void writeLines(String[] messages) throws IOException
	{
		for(int i=0; i<messages.length; ++i)
		{
			this.writeLine(messages[i]);
		}
	}
	
	
	
	public synchronized void clear() throws IOException
	{
		if((this.fileName != null)&&(!"".equals(this.fileName)))
		{
			FileWriter remover = new FileWriter(this.fileName, false);
			remover.write("");
			remover.flush();
			remover.close();
			remover = null;
			this.setReadOffset(0);
			this.setWriteOffset(0);
		}
	}
	
	
	
	public synchronized boolean delete() throws IOException
	{
		this.close();
		
		boolean result = false;
		if(this.fileName != null)
		{
			File currentFile = new File(this.fileName);
			result = currentFile.delete();
		}

		return result;
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
	
	

	//---static 
	
	
	
	private static synchronized void refreshAll() throws IOException
	{
		Set<String> tempFileNames = TextFile.textFileRegistry.keySet();
		String[] everyFileName = new String[tempFileNames.size()];
		tempFileNames.toArray(everyFileName);
		
		for(int i=0; i<everyFileName.length; ++i)
		{
			refresh(everyFileName[i]);
		}
	}

	
	
	private static synchronized void refresh(String fileName) throws IOException
	{
		TextFile textFile = textFileRegistry.get(fileName);
		
		if(textFile != null)
		{
			if((!TextFile.exists(fileName))||(!textFile.isOpen()))
			{
				TextFile.textFileRegistry.remove(fileName);
			}
		}
	}
	
	
	
	public static synchronized TextFile getFile(String fileName) throws IOException, InstanceAlreadyExistException, InterruptedException
	{
		TextFile.refresh(fileName);
		
		TextFile instance = TextFile.textFileRegistry.get(fileName); 
		if(instance == null)
		{
			instance = new TextFile(fileName);
		}
		
		return instance;
	}
	
	
	
	public static synchronized void close(String fileName) throws IOException
	{
		TextFile fileInstance = TextFile.textFileRegistry.get(fileName);
		if(fileInstance != null)
		{
			fileInstance.close();
			fileInstance = null;
		}
	}
	
	
	
	public static synchronized boolean closeAll() throws IOException
	{
		String[] everyFileName = TextFile.getEveryOpenedFileName();
		for(int i=0; i<everyFileName.length; ++i)
		{
			TextFile.close(everyFileName[i]);
		}
		
		return (TextFile.textFileRegistry.size() == 0);
	}
	
	
	
	private static synchronized String[] getEveryOpenedFileName() throws IOException
	{
		TextFile.refreshAll();
		
		Set<String> tempFileNames = TextFile.textFileRegistry.keySet();
		String[] fileNames = new String[tempFileNames.size()];
		tempFileNames.toArray(fileNames);
		
		return fileNames;
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
	
	
	
	public static synchronized boolean delete(String fileName) throws IOException
	{
		boolean result = false;
		if(exists(fileName))
		{
			TextFile fileInstance = TextFile.textFileRegistry.get(fileName);
			if(fileInstance != null)
			{
				fileInstance.close();
			}
			
			File tempFile = new File(fileName);
			result = tempFile.delete();
		}
		
		return result;
	}
	
	

	public static Integer getNumOfOpenedFiles() throws IOException
	{
		return TextFile.textFileRegistry.size();
	}

	

	public static synchronized boolean isOpened(String fileName)
	{
		return (TextFile.textFileRegistry.get(fileName) != null); 
	}

	
	
	public static void main(String[] args) throws InstanceAlreadyExistException, IOException, InterruptedException
	{
		TextFile.getFile("sample1.txt");
		TextFile.getFile("sample2.txt");
		TextFile.getFile("sample3.txt");

//		System.out.println(s.readLine());
//		s.readLine();
//		System.out.println(s.getReadOffset());
//		s.setReadOffset(s.getFileSize());
//		System.out.println("Line: "+s.readLineBack());
//		System.out.println("Line: "+s.readLineBack());
//		System.out.println("Line: "+s.readLineBack());
		
		String[] fileNames = TextFile.getEveryOpenedFileName();
		for(int i=0; i<fileNames.length; ++i)
		{
			System.out.println(fileNames[i]);
		}
		
		
		
//		System.out.println(TextFile.delete("sample1.txt"));
//		System.out.println(TextFile.delete("sample2.txt"));
//		System.out.println(TextFile.delete("sample3.txt"));
		
		TextFile file = TextFile.getFile("sample.txt");
		file.writeLine("aaabbb");
		file.writeLine("cccddd");
		file.writeLine("eeff");
		String[] contents = file.readAll();
		
		
		for(int i=0; i<contents.length; ++i)
		{
			System.out.println(contents[i]);
		}
		
		file.setReadOffset(file.getFileSize());
		System.out.println(file.getFileSize());
		System.out.println(file.readOffset);
		System.out.println(file.readLineBack());
		
		file.close();
		file.delete();
	}
}
