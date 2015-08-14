package com.gene.modules.networkLab.example1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;


public class EchoServer implements Runnable
{
	public void run()
	{
		int port = 9090;
		ServerSocket ss;
		try
		{
			ss = new ServerSocket (port); // open port
			System.out.println (port + " Port Echo Server Running...");
			
			//while (true)
			for(int i=0; i<10; ++i) 
			{
				Socket socket = ss.accept(); // listen to port. if a client creates connection to the port, then create connection toward the client
				System.out.println (new Date().toString() + ":" + socket.toString());
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				
				String temp = br.readLine();
				
				
				bw.write(temp + " 1\n"); // '\n' is recognized as an end of message
				bw.flush();
				bw.write(temp + " 2\n");
				bw.flush();
				bw.write(temp + " 3\n");
				bw.flush();
				
				br.close();
				bw.close();
				socket.close();
				
				Thread.yield();
			}
			ss.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	} 
}