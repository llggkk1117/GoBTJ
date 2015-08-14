package com.gene.modules.validatorLab;

import java.util.Vector;

public class Validator
{
	private Vector<ValidationElement> objectList;
	
	public Validator()
	{
		objectList = new  Vector<ValidationElement>();
	}
	
	public void addObject(String objectName, Object object, Object errorValue)
	{
		this.objectList.add(new ValidationElement(objectName, object, errorValue));
	}
	
	public ValidationElement[] proceed()
	{
		for(int i=0; i<this.objectList.size(); ++i)
		{
			this.objectList.elementAt(i).isValid = !(this.objectList.elementAt(i).object.equals(this.objectList.elementAt(i).errorValue));
		}
		
		ValidationElement[] result = this.getObjectList();
		return result;
	}
	
	public ValidationElement[] getObjectList()
	{
		ValidationElement[] result = new ValidationElement[objectList.size()];
		this.objectList.toArray(result);
		return result;
	}
	
	public void clear()
	{
		this.objectList.clear();
	}
	
	public String generateErrorMessage(ValidationElement[] elements)
	{
		Vector<String> errorMessages = new Vector<String>();
		
		for(int i=0; i<elements.length; ++i)
		{
			if(!elements[i].isValid)
			{
				errorMessages.add(elements[i].objectName+" should not be "+elements[i].errorValue);
			}
		}
		
		String errorMessage = null;
		
		if(errorMessages.size() > 0)
		{
			errorMessage = "";
			for(int i=0; i<errorMessages.size(); ++i)
			{
				errorMessage += errorMessages.elementAt(i);
				if(i < errorMessages.size()-1)
				{
					errorMessage += "; ";
				}
				else
				{
					errorMessage += " ";
				}
			}
		}
		
		return errorMessage;
	}
}




class ValidationElement
{
	public String objectName;
	public Object object;
	public Object errorValue;
	public Boolean isValid;
	public String reason;
	public ValidationElement(String objectName, Object object, Object errorValue)
	{
		this.objectName = objectName;
		this.object = object;
		this.errorValue = errorValue;
		this.isValid = null;
		this.reason = null;
	}
}
