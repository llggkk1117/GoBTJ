package com.gene.modules.exceptions;




public class JavaModuleException extends Exception
{
	static final long serialVersionUID = 299076599373841618L;
	String errorMessage;
	public JavaModuleException()
	{
		super();
		this.errorMessage = null;
	}

	public JavaModuleException(String errorMessage)
	{
		super(errorMessage);
		this.errorMessage = errorMessage;
	}
	
	public JavaModuleException(String object, String reason)
	{
		super(((object == null) ? "" : object) + ((reason == null) ? "" : " (" + reason + ")"));
		this.errorMessage = ((object == null) ? "" : object) + ((reason == null) ? "" : " (" + reason + ")");
	}
	
	public JavaModuleException(Class classname, String object, String reason)
	{
		super(((classname == null) ? "" : classname) + ((object == null) ? "" : object)+((reason == null) ? "" : " (" + reason + ")"));
		this.errorMessage =  ((classname == null) ? "" : classname+"") +": "+ ((object == null) ? "" : object) +((reason == null) ? "" : " (" + reason + ")");
	}
  
	public String getMessage()
	{
		return this.errorMessage;
	}
}