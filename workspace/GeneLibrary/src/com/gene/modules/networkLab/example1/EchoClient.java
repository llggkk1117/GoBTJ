package com.gene.modules.networkLab.example1;

import java.io.*;
import java.net.*;
public class EchoClient implements Runnable
{
	public void run()
	{
		String host = "56.160.22.32";
		int port = 9090;
		String message = "hello!!";
		
		Socket socket;
		try
		{
			socket = new Socket(host, port);
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
			
			pw.print("A"); 
			pw.print("B");
			
			pw.print("\n"); // '\n' is recognized as an end of message
			pw.flush(); // sending message to server

//			pw.println(); // same as pw.print("\n")+pw.flush()
			
			System.out.println("Echo: " + br.readLine()); // receive message from server
			System.out.println("Echo: " + br.readLine()); 
			System.out.println("Echo: " + br.readLine()); 
			
			br.close();
			pw.close();
			socket.close();
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	} 
}