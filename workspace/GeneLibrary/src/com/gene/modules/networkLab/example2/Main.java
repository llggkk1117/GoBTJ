package com.gene.modules.networkLab.example2;

public class Main
{
	public static void main(String[] args)
	{
		EchoClient client = new EchoClient();
		EchoServer server = new EchoServer();
		Thread tClient = new Thread(client);
		Thread tServer = new Thread(server);
		
		tServer.start();
		tClient.start();
	}

}
