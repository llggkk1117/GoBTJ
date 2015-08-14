package com.gene.modules.networkLab.example2;

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
			
			pw.print("A"); //�?��?�터 보내기
			pw.print("B"); 
			pw.print("\n");
			pw.flush();
//			pw.println(); //�?��?�터 보내기
			
			System.out.print("Echo: " + br.readLine()); //�?�답 받기
			System.out.print("Echo: " + br.readLine()); //�?�답 받기
			System.out.print("Echo: " + br.readLine()); //�?�답 받기
			
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