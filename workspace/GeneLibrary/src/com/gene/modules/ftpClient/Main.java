package com.gene.modules.ftpClient;

public class Main
{
	public static void func1(String[] args) throws Exception
	{
		FTPTaskProcessor processor = new FTPTaskProcessor();
		processor.setFTPClient(new FTPClient());
		processor.setFTPTasks(FTPTaskLoader.getFTPTasks("settings/FTPTask.properties"));
		processor.proceed();
	}
	
	public static void func2(String[] args) throws Exception
	{
		FTPTaskProcessorAssembler ftpTaskProcessorAssembler = new FTPTaskProcessorAssembler();
		FTPTaskProcessor processor= ftpTaskProcessorAssembler.getFTPTaskProcessor();
		processor.proceed();
	}
	
	public static void main(String[] args) throws Exception
	{
		Main.func2(args);
	}
}
