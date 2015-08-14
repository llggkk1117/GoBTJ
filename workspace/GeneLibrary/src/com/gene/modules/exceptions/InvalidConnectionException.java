package com.gene.modules.exceptions;



public class InvalidConnectionException extends IllegalArgumentException
{
	static final long serialVersionUID = -1309302199058664946L;
	String errorMessage;
	public InvalidConnectionException()
	{
		super();
		this.errorMessage = null;
	}

	public InvalidConnectionException(String errorMessage)
	{
		super(errorMessage);
		this.errorMessage = errorMessage;
	}
	
	public InvalidConnectionException(String object, String reason)
	{
		super(((object == null) ? "" : object) + ((reason == null) ? "" : " (" + reason + ")"));
		this.errorMessage = ((object == null) ? "" : object) + ((reason == null) ? "" : " (" + reason + ")");
	}
	
	public InvalidConnectionException(Class classname, String object, String reason)
	{
		super(((classname == null) ? "" : classname) + ((object == null) ? "" : object)+((reason == null) ? "" : " (" + reason + ")"));
		this.errorMessage =  ((classname == null) ? "" : classname+"") +": "+ ((object == null) ? "" : object) +((reason == null) ? "" : " (" + reason + ")");
	}
  
	public String getMessage()
	{
		return this.errorMessage;
	}
}