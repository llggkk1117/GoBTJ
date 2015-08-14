package com.gene.modules.exceptions;

public class DependencyNotSatisfiedException extends Exception
{
	static final long serialVersionUID = -3470709797899353769L;
	
	String errorMessage;
	public DependencyNotSatisfiedException()
	{
		super();
		this.errorMessage = null;
	}

	public DependencyNotSatisfiedException(String errorMessage)
	{
		super(errorMessage);
		this.errorMessage = errorMessage;
	}
	
	public DependencyNotSatisfiedException(String object, String reason)
	{
		super(object + ((reason == null) ? "" : " (" + reason + ")"));
		this.errorMessage = object + ((reason == null) ? "" : " (" + reason + ")");
	}
  
	public String getMessage()
	{
		return this.errorMessage;
	}
}
