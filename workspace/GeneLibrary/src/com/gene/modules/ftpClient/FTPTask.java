package com.gene.modules.ftpClient;


public class FTPTask
{
	private String FTPServerIP;
	private String username;
	private String password;
	private boolean passwordEncrypted;
	private String remoteDirectory;
	private String localDirectory;
	private String remoteFileName;
	private String localFileName;
	private String operationType;
	private String transferType;
	private String[] response;
	
	
	static class OperationType
	{
		public static final String DOWNLOAD;
		private static final String[] operationTypeSet;
		static
		{
			DOWNLOAD = "DOWNLOAD";
			operationTypeSet = new String[]{DOWNLOAD};
		}
		private static boolean validateOperationType(String operationType)
		{
			boolean valid = false;
			for(int i=0; i<operationTypeSet.length; ++i)
			{
				if(operationTypeSet[i].equals(operationType))
				{
					valid = true;
				}
			}
			
			return valid;
		}
	}

	
	
	
	
	
	
	
	public String getFTPServerIP()
	{
		return this.FTPServerIP;
	}
	
	public void setFTPServerIP(String FTPServerIP)
	{
		this.FTPServerIP = FTPServerIP;
	}

	public String getLocalDirectory()
	{
		return localDirectory;
	}

	public void setLocalDirectory(String localDirectory)
	{
		this.localDirectory = localDirectory;
	}

	public String getLocalFileName()
	{
		return localFileName;
	}

	public void setLocalFileName(String localFileName)
	{
		this.localFileName = localFileName;
	}

	public String getRemoteDirectory()
	{
		return remoteDirectory;
	}

	public void setRemoteDirectory(String remoteDirectory)
	{
		this.remoteDirectory = remoteDirectory;
	}

	public String getRemoteFileName()
	{
		return remoteFileName;
	}

	public void setRemoteFileName(String remoteFileName)
	{
		this.remoteFileName = remoteFileName;
	}

	public String getTransferType()
	{
		return transferType;
	}

	public void setTransferType(String transferType)
	{
		this.transferType = transferType;
	}

	public String getUserName()
	{
		return username;
	}

	public void setUserName(String username)
	{
		this.username = username;
	}
	
	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
		
	}
	
	public void setPasswordEncrypted(boolean encrypted)
	{
		this.passwordEncrypted = encrypted;	
	}
	
	public boolean getPasswordEncrypted()
	{
		return this.passwordEncrypted;
	}
	
	public String getOperationType()
	{
		return this.operationType;
	}
	
	public void setOperationType(String operationType) throws Exception
	{
		if(FTPTask.OperationType.validateOperationType(operationType))
		{
			this.operationType = operationType;
		}
		else
		{
			throw new Exception(operationType+" is not FTPTask operation type");
		}
	}
	
	public String[] getResponse()
	{
		return this.response;
	}
	
	public void setResponse(String[] response)
	{
		this.response = response;
	}
}






