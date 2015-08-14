package com.gene.modules.ftpClient;

public class FTPTaskProcessorAssembler
{
	private FTPTaskProcessor ftpTaskProcessor;
	private FTPClient ftpClient;
	private FTPTask[] ftpTasSet;
	
	public FTPTaskProcessorAssembler(String taskFileName) throws Exception
	{
		String task_file_name = ((taskFileName != null) ? taskFileName : "settings/FTPTask.properties");

		ftpTaskProcessor = new FTPTaskProcessor();
		ftpClient = new FTPClient();
		ftpTasSet = FTPTaskLoader.getFTPTasks(task_file_name);
		
		ftpTaskProcessor.setFTPClient(ftpClient);
		ftpTaskProcessor.setFTPTasks(ftpTasSet);
	}
	
	public FTPTaskProcessorAssembler() throws Exception
	{
		this(null);
	}
	
	public FTPTaskProcessor getFTPTaskProcessor()
	{
		return this.ftpTaskProcessor;
	}
}
