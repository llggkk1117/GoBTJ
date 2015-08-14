package com.gene.modules.exceptions;


public class InstanceAlreadyExistException extends IllegalArgumentException
{
	static final long serialVersionUID = -8998880193176098597L;
	String errorMessage;
	public InstanceAlreadyExistException()
	{
		super();
		this.errorMessage = null;
	}

	public InstanceAlreadyExistException(String errorMessage)
	{
		super(errorMessage);
		this.errorMessage = errorMessage;
	}
	
	public InstanceAlreadyExistException(String object, String reason)
	{
		super(object + ((reason == null) ? "" : " (" + reason + ")"));
		this.errorMessage = object + ((reason == null) ? "" : " (" + reason + ")");
	}
  
	public String getMessage()
	{
		return this.errorMessage;
	}
}