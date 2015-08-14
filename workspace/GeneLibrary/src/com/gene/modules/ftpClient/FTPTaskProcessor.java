package com.gene.modules.ftpClient;



public class FTPTaskProcessor
{
	private FTPClient ftpClient;
	private FTPTask[] ftpTasks;
	
	public FTPTaskProcessor(){}
	
	public void setFTPClient(FTPClient ftpClient)
	{
		this.ftpClient = ftpClient;
	}
	
	public FTPClient getFTPClient()
	{
		return this.ftpClient;
	}

	public void setFTPTasks(FTPTask[] ftpTasks)
	{
		this.ftpTasks = ftpTasks;
	}
	
	public FTPTask[] getFTPTasks()
	{
		return this.ftpTasks;
	}
	
	
	
	public void proceed() throws Exception
	{
		if(this.ftpClient == null)
		{
			throw new Exception("ftpClient should not be null");
		}
		if(this.ftpTasks == null)
		{
			throw new Exception("ftpTasks should not be null");
		}
		
		
		
		if(this.ftpTasks.length > 0 )
		{
			for(int i=0; i<this.ftpTasks.length; ++i)
			{
				this.ftpClient.connect(ftpTasks[i].getFTPServerIP());
				this.ftpClient.login(ftpTasks[i].getUserName(), ftpTasks[i].getPassword());
				if(this.ftpTasks[i].getOperationType().equals(FTPTask.OperationType.DOWNLOAD))
				{
					int response = this.ftpClient.download(ftpTasks[i].getRemoteDirectory(), ftpTasks[i].getRemoteFileName(), ftpTasks[i].getLocalDirectory(), ftpTasks[i].getLocalFileName());
					this.ftpTasks[i].setResponse(new String[]{response+""});
				}
			}
			this.ftpClient.disconnect();
		}
	}
	
	
	public static void main(String[] args) throws Exception
	{
		FTPTaskProcessor p = new FTPTaskProcessor();
		p.proceed();
	}
}
