package com.gene.modules.exceptions;



public class InstanceNotExistException extends IllegalArgumentException
{
	static final long serialVersionUID = -8314136055918554071L;
	
	String errorMessage;
	public InstanceNotExistException()
	{
		super();
		this.errorMessage = null;
	}

	public InstanceNotExistException(String errorMessage)
	{
		super(errorMessage);
		this.errorMessage = errorMessage;
	}
	
	public InstanceNotExistException(String object, String reason)
	{
		super(((object == null) ? "" : object) + ((reason == null) ? "" : " (" + reason + ")"));
		this.errorMessage = ((object == null) ? "" : object) + ((reason == null) ? "" : " (" + reason + ")");
	}
	
	public InstanceNotExistException(Class classname, String object, String reason)
	{
		super(((classname == null) ? "" : classname) + ((object == null) ? "" : object)+((reason == null) ? "" : " (" + reason + ")"));
		this.errorMessage =  ((classname == null) ? "" : classname+"") +": "+ ((object == null) ? "" : object) +((reason == null) ? "" : " (" + reason + ")");
	}
  
	public String getMessage()
	{
		return this.errorMessage;
	}
}