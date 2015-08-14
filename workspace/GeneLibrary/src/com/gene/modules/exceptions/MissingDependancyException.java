package com.gene.modules.exceptions;

public class MissingDependancyException extends IllegalArgumentException
{
	static final long serialVersionUID = 8360738280662942703L;
	private String errorMessage;
	public MissingDependancyException()
	{
		super();
		this.errorMessage = null;
	}

	public MissingDependancyException(String errorMessage)
	{
		super(errorMessage);
		this.errorMessage = errorMessage;
	}
	
	public MissingDependancyException(String object, String reason)
	{
		super(object + ((reason == null) ? "" : " (" + reason + ")"));
		this.errorMessage = object + ((reason == null) ? "" : " (" + reason + ")");
	}
  
	public String getMessage()
	{
		return this.errorMessage;
	}
}
