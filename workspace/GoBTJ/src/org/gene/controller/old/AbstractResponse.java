package org.gene.controller.old;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public abstract class AbstractResponse
{
	@Expose protected boolean success;
	@Expose protected List<String> errorMessages;
	@Expose protected List<String> warningMessages;

	protected AbstractResponse()
	{
		this.success = false;
		this.errorMessages = new ArrayList<String>();
		this.warningMessages = new ArrayList<String>();
	}
	
	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @return the errorMessages
	 */
	public List<String> getErrorMessages() {
		return errorMessages;
	}

	/**
	 * @param errorMessages the errorMessages to set
	 */
	public void setErrorMessages(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}

	public List<String> getWarningMessages() {
		return warningMessages;
	}

	public void setWarningMessages(List<String> warningMessages) {
		this.warningMessages = warningMessages;
	}
}