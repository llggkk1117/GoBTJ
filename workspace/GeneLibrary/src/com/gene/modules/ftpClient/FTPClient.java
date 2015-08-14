package com.gene.modules.ftpClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.govgrnds.core.util.crypto.DefaultCryptoHelper;


/**
 * This class represents an FTP client.  Initially, this class will only
 * provide upload capabilities, though it may be expanded to entail a complete
 * FTP client.  Instances of this class may be used in one of two ways:
 * interactive, or single upload.  Interactive means that the caller has
 * complete control over what commands are sent to the FTP server.  Usually,
 * in interactive mode, the caller will connect (using connect()), and issue
 * multiple sendCommand() calls.  Finally, disconnect() should be called.  For
 * single upload, the caller need only call send() (assuming host, user, and
 * other parameters have already been set) and then disconnect().  The FTP
 * protocol may be reviewed at http://www.ietf.org/rfc/rfc0959.txt.
 */

public class FTPClient
{
	public static final Logger logger;
	static
	{
		logger = Logger.getLogger(FTPClient.class);
	}

	//
	private String FTPServerIP;
	private int FTPServerPort;
	private String username;
	private String password;
	
	
	//
	private Socket socket = null;
	private DataInputStream controlIn = null;
	private DataOutputStream controlOut = null;
	private BufferedReader controlReader = null;
	private BufferedWriter controlWriter = null;
	
	private boolean isLoggedIn;
	private DefaultCryptoHelper cryptoHelper; 
	private boolean passwordEncrypted;
	private String dataTransferType;
	

	
	public FTPClient()
	{
		this.initialize();
	}
	
	
	private void initialize()
	{
		this.FTPServerIP = null;
		this.FTPServerPort = 0;
		this.username = null;
		this.password = null;
		
		this.isLoggedIn = false;
		this.setPasswordEncrypted(false);
		this.dataTransferType = FTPClientConstant.TYPE_BINARY;
		
		this.socket = null;
		this.controlIn = null;
		this.controlOut = null;
		this.controlReader = null;
		this.controlWriter = null;
	}

	
	

	public void setPasswordEncrypted(boolean passwordEncrypted)
	{
		this.passwordEncrypted = passwordEncrypted;
		if(this.passwordEncrypted)
		{
			if(cryptoHelper == null)
			{
				cryptoHelper = new DefaultCryptoHelper();
			}
		}
		else
		{
			cryptoHelper = null;
		}
	}










	/**
	 * Files can be sent in either binary, ascii or ebcdic format.  This method sets
	 * that method to binary (which is the default format).
	 */
	public void setBinary()
	{
		this.dataTransferType = FTPClientConstant.TYPE_BINARY;
	}



	/**
	 * Files can be sent in either binary, ascii or ebcdic format.  This method sets
	 * that method to ascii.
	 */
	public void setASCII()
	{
		this.dataTransferType = FTPClientConstant.TYPE_ASCII;
	}
	
	/**
	 * Files can be sent in either binary, ascii or ebcdic format.  This method sets
	 * that method to ebcdic.
	 */
	public void setEBCDIC()
	{
		this.dataTransferType = FTPClientConstant.TYPE_EBCDIC;
	}



	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Connects to the remote host/proxy.  This method is used for interactive
	 * FTPing.  Calling this method is only necessary if the caller wants to
	 * have full control over the communication with the FTP server.  If
	 * calling the send() method, calling this method is unnecessary.
	 * @exception Exception Errors trying to connect to remote host.
	 */
	public String[] connect(String FTPServerIP, int FTPServerPort) throws Exception
	{
		if(FTPServerIP == null)
		{
			throw new Exception("FTPServerIP should not be null");
		}
		if(!this.validateHost(FTPServerIP))
		{
			throw new Exception("FTPServerIP is not valid");
		}

		
		String[] response = null;
		boolean isConnected = this.isConnected();
		boolean isSameAddress = ((FTPServerIP.equals(this.FTPServerIP))&&(FTPServerPort == this.FTPServerPort));
		if((!isConnected)||((isConnected)&&(!isSameAddress)))
		{
			this.disconnect();
			
			this.FTPServerIP = FTPServerIP;
			this.FTPServerPort = FTPServerPort;

			this.socket = new Socket(this.FTPServerIP, this.FTPServerPort);
			this.socket.setSoTimeout(FTPClientConstant.SOCKET_TIMEOUT);
			this.controlOut = new DataOutputStream(this.socket.getOutputStream());
			this.controlIn = new DataInputStream(this.socket.getInputStream());
			this.controlWriter = new BufferedWriter(new OutputStreamWriter(this.controlOut));
			this.controlReader = new BufferedReader(new InputStreamReader(this.controlIn));

			response = this.receiveResponseFromServer();
		}
		
		return response;
	}
	
	public String[] connect(String FTPServerIP) throws Exception
	{
		return this.connect(FTPServerIP, FTPClientConstant.FTP_PORT);
	}

	

	/**
	 * Interactive login.  This method is meant to log the user in.  This
	 * method should only be called when using this class in interactive mode.
	 *
	 * @exception Exception Login errors.
	 */
	public void login(String username, String password, boolean passwordEncrypted) throws Exception
	{
		if(username == null)
		{
			throw new Exception("username should not be null");
		}
		if(password == null)
		{
			throw new Exception("password shoudl not be null");
		}
		if(!this.isConnected())
		{
			throw new Exception("Connection needs to be created before this operation");
		}
		
		
		
		// if it is logged in but not the same as previous credential, then logout
		boolean isSameCredential = ((username.equals(this.username))&&((passwordEncrypted ?  (new DefaultCryptoHelper()).decrypt(password) : password).equals(this.password)));	
		if((this.isLoggedIn)&&(!isSameCredential))
		{
			String serverIP = this.FTPServerIP;
			int serverPort = this.FTPServerPort;
			this.disconnect();
			this.connect(serverIP, serverPort);
		}
		
		
		if(!this.isLoggedIn)
		{
			this.setPasswordEncrypted(passwordEncrypted);
			this.username = username;
			if(this.passwordEncrypted)
			{
				this.password = (new DefaultCryptoHelper()).decrypt(password);
			}
			else
			{
				this.password = password;
			}

			
			if(!this.command_sendUserName(this.username).isSuccessful())
			{
				throw new Exception("USER command failed attempting to login to remote host.");
			}
			
			if(!this.command_sendPassword(this.password).isSuccessful())
			{
				throw new Exception("PASS command failed attempting to login to remote host.");
			}
	
			this.isLoggedIn = true;
		}
	}
	

	public void login(String username, String password) throws Exception
	{
		this.login(username, password, false);
	}


	
	
	/**
	 * Disconnects from the remote host.  This method should be called when no
	 * more files need to be sent.
	 * @throws Exception 
	 */
	public String[] disconnect() throws Exception
	{
		String[] response = null;
		
		if(this.isConnected())
		{
			response = this.command_terminateConnection().getResponseMessage();
		}
		
		if (this.socket != null)
		{
			this.socket.close();
		}
		
		if(this.controlIn != null)
		{
			this.controlIn.close();
		}
		
		if(this.controlOut != null)
		{
			this.controlOut.close();
		}
		
		if(this.controlReader != null)
		{
			this.controlReader.close();
		}
		
		if(this.controlWriter != null)
		{
			this.controlWriter.close();
		}
		
		this.initialize();
		
		return response;
	}


	

	/**
	 * Determines whether you are connected to the remote host.
	 *
	 * @return Returns true if connected to the remote host.
	 */
	public boolean isConnected()
	{
		boolean isConnected = true;
		
		if (this.socket == null)
		{
			isConnected = false;
		}
		else if (this.controlIn == null)
		{
			isConnected = false;
		}
		else if (this.controlOut == null)
		{
			isConnected = false;
		}
		else if (this.controlReader == null)
		{
			isConnected = false;
		}
		else if (this.controlWriter == null)
		{
			isConnected = false;
		}
		
		return isConnected;
	}
	
	
	
	
	
	
	private static String stacktraceToString(Exception e)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String message = sw.toString();
        try
        {
			sw.close();
		}
        catch (IOException e1)
        {
			e1.printStackTrace();
		}
        sw = null;
        pw.close();
        pw = null;
		return message;
	}
	
	
	

	
	private static String[] convertRawAddressToNormalAddress(String raw_IP_and_port) throws Exception
	{
		if(raw_IP_and_port == null)
		{
			throw new Exception("raw_IP_and_port cannot be null");
		}
		
		StringTokenizer tokens = new StringTokenizer(raw_IP_and_port, ",");
		int numOfTokens = tokens.countTokens();
		String[] normal_IP_and_port = null;
		if(numOfTokens == 6)
		{
			String[] elements = new String[numOfTokens];
			for(int i=0; i<elements.length; ++i)
			{
				elements[i] = tokens.nextToken();
			}
			
			for(int i=0; i<elements.length; ++i)
			{
				try
				{
					Integer.parseInt(elements[i]);
				}
				catch(NumberFormatException e)
				{
					throw new NumberFormatException("Invalid raw IP and port");
				}
			}
			
			String IP = elements[0]  + "." + elements[1] + "." + elements[2] + "." + elements[3];
			String port = String.valueOf((Integer.parseInt(elements[4]) << 8) + Integer.parseInt(elements[5]));
			normal_IP_and_port = new String[2];
			normal_IP_and_port[0] = IP;
			normal_IP_and_port[1] = port;
		}
		else
		{
			throw new Exception();
		}
		
		return normal_IP_and_port;
	}

	
	
	private static String convertNormalAddressToRawAddress(short[] normal_IP, int normal_port) throws Exception
	{
		if(normal_IP == null)
		{
			throw new Exception("normal_IP cannot be null");
		}
		
		String raw_IP_and_port =  normal_IP[0]+","+normal_IP[1]+","+normal_IP[2]+","+normal_IP[3]+","+((normal_port & 0xFF00) >> 8)+","+(normal_port & 0x00FF);
		return raw_IP_and_port;
	}
	
	
	
	
	/**
	 * After retrieving the command response from the sendCommand() method, the
	 * response can be passed to this method to get the actual response code.
	 *
	 * @param serverResponse The returned value from calling sendCommand().
	 *
	 * @return Returns the three-digit response code from the FTP server.
	 * @throws Exception 
	 */
	
	private int parseResponseCode(String[] response) throws Exception
	{
		if(response == null)
		{
			throw new Exception("response cannot be null");
		}
		
		int code = Integer.parseInt(response[response.length-1].substring(0, 3));

		return code;
	}
	
	
	
	private String parseResponseMessage(String[] response, boolean fullMessage) throws Exception
	{
		if(response == null)
		{
			throw new Exception("response cannot be null");
		}
		
		String lastLine = response[response.length-1];
		String message = null;
		if(fullMessage)
		{
			message = lastLine;
		}
		else
		{
			message = lastLine.substring(3, lastLine.length());
		}
		
		return message;
	}
	
	
	
	
	
	

	private Socket enterPassiveMode() throws Exception
	{
		if(!this.isConnected())
		{
			throw new Exception("Connection needs to be created before this operation");
		}
		if(!this.isLoggedIn)
		{
			throw new Exception("Login is needed before this operation");
		}
		
		Socket socket = null;
		ServerResponse response = this.command_passiveMode();
		if(response.isSuccessful())
		{
			String[] responseMessage = response.parseResponseMessage();
			String addressInfoLine = responseMessage[responseMessage.length-1];
			String raw_IP_and_port = addressInfoLine.substring(addressInfoLine.indexOf("(") + 1, addressInfoLine.indexOf(")"));
			String[] normalAddress = FTPClient.convertRawAddressToNormalAddress(raw_IP_and_port);
			String host = normalAddress[0];
			int port = Integer.parseInt(normalAddress[1]);
			socket = new Socket(InetAddress.getByName(host), port);
		}
		return socket;
	}
	
	

	
	


	
	
	/**
	 * Opens a socket for listening (for use as a data port).
	 *
	 * @return Returns the open, listening socket.
	 * @throws SocketException 
	 *
	 * @exception Exception Connecting errors.
	 */
	private ServerSocket openServerSocket() throws Exception
	{
		ServerSocket serverSocket = null;
		int localPort = FTPClientConstant.MIN_PORT;
		boolean continueLoop = true;
		while(continueLoop)
		{
			try
			{
				serverSocket = new ServerSocket(localPort);
				serverSocket.setSoTimeout(FTPClientConstant.SOCKET_TIMEOUT);
				continueLoop = false;
			}
			catch (IOException e)
			{
				localPort++;
				if(localPort >= FTPClientConstant.MAX_PORT)
				{
					continueLoop = false;
				}
			}
		}
		
		if (serverSocket == null)
		{
			throw new Exception("Unable to open socket on data port.");
		}

		return serverSocket;
	}
	
	
	
	
	
	private ServerSocket openPortToReceiveData() throws Exception
	{
		if(!this.isConnected())
		{
			throw new Exception("Connection needs to be created before this operation");
		}
		if(!this.isLoggedIn)
		{
			throw new Exception("Login is needed before this operation");
		}
		

		ServerSocket serverSocket = this.openServerSocket();
		int localPort = serverSocket.getLocalPort();
		short[] localIP = getLocalIpAddress();
		String raw_address = FTPClient.convertNormalAddressToRawAddress(localIP, localPort);
		if(!this.command_notifyDataPortOpen(raw_address).isSuccessful())
		{
			serverSocket.close();
			serverSocket = null;
		}
		
		return serverSocket;
	}	
	
	
	

	/**
	 * Retrieves the local IP address in network byte order.
	 *
	 * @return Returns the local IP address.
	 *
	 * @exception Exception Network errors.
	 */
	private static short[] getLocalIpAddress() throws Exception
	{
		InetAddress localIp = InetAddress.getLocalHost();
		byte[] ipBytes = localIp.getAddress();
		short[] ip = new short[ipBytes.length];

		for (int i = 0; i < ip.length; ++i)
		{
			ip[i] = ipBytes[i];

			//If the byte is greater than 127, it gets converted as a negative number.
			if (ip[i] < 0)
			{
				ip[i] += 256;
			}
		}

		return ip;
	}
	
	
	
	/**
	 * Verifies that the file can be read from the local directory.  If so, a
	 * File object representing it is returned.  If not, an exception will be
	 * thrown.  This method is guaranteed to NOT return a null reference (an
	 * exception will be thrown instead).
	 *
	 * @param fileName The name of the file.
	 *
	 * @return Returns a File representation of the file name passed in.
	 *
	 * @exception Exception If the file can not be read.
	 */
	private String validateFile(String localDirectory, String localFileName) throws Exception
	{
		if(localFileName == null)
		{
			throw new Exception("Local filename is null");
		}
		String local_directory = ((localDirectory != null) ? localDirectory : ".");
		String local_file_name =  localFileName;
		

		String errorMessage = null;
		File file = new File(local_directory, local_file_name);
		String local_file_path = local_directory+"/"+local_file_name;
		if(!file.exists())
		{
			errorMessage = "File "+local_file_path+" does not exist";
		}
		else if(!file.canRead())
		{
			errorMessage = "Unable to read file: "+local_file_path;
		}

		return errorMessage;
	}
	
	
	
	
	
	/**
	 * Verifies that the remote host and proxy (if it exists) are valid.
	 *
	 * @throws Exception DOCUMENT ME!
	 */
	private boolean validateHost(String serverIP)
	{
		boolean isValid = true;
		try
		{
			InetAddress.getByName(serverIP).getHostName();
		}
		catch(Exception e)
		{
			isValid = false;
		}

		return isValid;
	}
	
	
	
	
	
	
	
	private String[] receiveResponseFromServer() throws Exception
	{
		if(!this.isConnected())
		{
			throw new Exception("Not connected");
		}
		
		Vector<String> responseVector = new Vector<String>();
		String reponseLine = null;
		do
		{
			reponseLine = this.controlReader.readLine();
			responseVector.add(reponseLine);
		}
		while(((reponseLine.length() > 3) && (reponseLine.charAt(3) != ' ')));
		String[] response = new String[responseVector.size()];
		responseVector.toArray(response);
		responseVector = null;

		return response;
	}
	
	
	
	
	private ServerResponse receiveResponseAndVarify(Integer[] expectedResponseCode) throws Exception
	{
		if(!this.isConnected())
		{
			throw new Exception("Not connected");
		}
		
		
		String[] response = this.receiveResponseFromServer();
		
		ServerResponse serverResponse = new ServerResponse();
		serverResponse.addResponseMessage(response);
		serverResponse.varify(expectedResponseCode);
		
		
		return  serverResponse;
	}
	
	
	
	

	/**
	 * Sends a low-level command to the FTP server.  This method is used when
	 * in interactive mode.  If not connected, this method will automatically
	 * connect to the remote host.
	 *
	 * @param command The command to send.  
	 *
	 * @return Returns the response to the command.
	 * @exception Exception Errors in communicating with the FTP server or
	 * 			  host.
	 */
	public String[] sendCommand(String command) throws Exception
	{
		if(!this.isConnected())
		{
			throw new Exception("Connection needs to be created before this operation");
		}
		if(command == null)
		{
			throw new Exception("command cannot be null");
		}
		
		this.controlWriter.write(command + FTPClientConstant.END_OF_LINE);
		this.controlWriter.flush();
		String[] response = this.receiveResponseFromServer();

		return response;
	}



	
	
	







	/**
	 * Changes the current working directory on the remote host.  This is an
	 * interactive method call, which means that it will connect, login, and
	 * change the working directory on the remote host.
	 *
	 * @param basicCommand The absolute or relative path to set as the working dir.
	 *
	 * @return Returns true if the action was successful.
	 * @throws Exception 
	 */
	
	public ServerResponse basicCommand(String basicCommand, String value, Integer[] expectedResponseCode) throws Exception
	{
		if(!this.isConnected())
		{
			throw new Exception("Connection needs to be created before this operation");
		}
		if(basicCommand == null)
		{
			throw new Exception("basicCommand cannot be null");
		}
		

		String command = basicCommand + ((value != null) ? " "+value : "");
		System.out.println(command);
		String[] response = this.sendCommand(command);
		
		
		ServerResponse serverResponse = new ServerResponse();
		serverResponse.addResponseMessage(response);
		serverResponse.varify(expectedResponseCode);
		
		return serverResponse;
	}
	
	
	
	
	public ServerResponse command_changeDirectory(String remoteDirectory) throws Exception
	{
		if(!this.isConnected())
		{
			throw new Exception("Connection needs to be created before this operation");
		}
		if(!this.isLoggedIn)
		{
			throw new Exception("Login is needed before this operation");
		}
		
		return this.basicCommand(FTPClientConstant.COMMAND_CHANGE_DIR, remoteDirectory, new Integer[]{FTPClientConstant.FTP_FILE_ACTION_OK});		
	}
	
	
	
	
	public ServerResponse command_changeDirectory(String remoteDirectory, boolean createIfNotExist) throws Exception
	{
		if(!this.isConnected())
		{
			throw new Exception("Connection needs to be created before this operation");
		}
		if(!this.isLoggedIn)
		{
			throw new Exception("Login is needed before this operation");
		}
		
		ServerResponse response = new ServerResponse();

		if(createIfNotExist)
		{
			ServerResponse changeDirectoryResponse1 = this.command_changeDirectory(remoteDirectory);
			response.addResponseMessage(changeDirectoryResponse1.parseResponseMessage());

			if(changeDirectoryResponse1.isSuccessful())
			{
				response.setSuccessful(true);
			}
			else
			{
				ServerResponse createDirectoryResponse = this.command_createDirectory(remoteDirectory);
				response.addResponseMessage(createDirectoryResponse.parseResponseMessage());

				if(createDirectoryResponse.isSuccessful())
				{
					ServerResponse changeDirectoryResponse2 = this.command_changeDirectory(remoteDirectory);
					response.addResponseMessage(changeDirectoryResponse2.parseResponseMessage());
					response.setSuccessful(changeDirectoryResponse2.isSuccessful());
				}
				else
				{
					response.setSuccessful(false);
				}
			}
		}
		else
		{
			response = this.command_changeDirectory(remoteDirectory);
		}
		
		return response;
	}
	
	
	public ServerResponse command_createDirectory(String remoteDirectory) throws Exception
	{
		if(!this.isConnected())
		{
			throw new Exception("Connection needs to be created before this operation");
		}
		if(!this.isLoggedIn)
		{
			throw new Exception("Login is needed before this operation");
		}
		
		return this.basicCommand(FTPClientConstant.COMMAND_CREATE_DIR, remoteDirectory, new Integer[]{FTPClientConstant.FTP_DIRECTORY_CREATED_OK});
	}
	


	
	/**
	 * Deletes a remote file.
	 *
	 * @param remoteFileName The name of the file to delete in host.
	 * @return ServerResponse object which includes the result of this operation, result code,
	 *            result code description, and whole response message.
	 * @throws Exception when 
	 */
	public ServerResponse command_delete(String remoteFileName) throws Exception
	{
		if(!this.isConnected())
		{
			throw new Exception("Connection needs to be created before this operation");
		}
		if(!this.isLoggedIn)
		{
			throw new Exception("Login is needed before this operation");
		}
		
		return this.basicCommand(FTPClientConstant.COMMAND_DELETE_FILE, remoteFileName, new Integer[]{FTPClientConstant.FTP_FILE_ACTION_OK});
	}
	
	
	
	
	/**
	 * Terminate connection to host.
	 *
	 * @return ServerResponse object which includes the result of this operation, result code,
	 *            result code description, and whole response message.
	 * @throws Exception when
	 */
	public ServerResponse command_terminateConnection() throws Exception
	{
		if(!this.isConnected())
		{
			throw new Exception("Connection needs to be created before this operation");
		}
		
		return this.basicCommand(FTPClientConstant.COMMAND_QUIT, null, new Integer[]{FTPClientConstant.FTP_CLOSING_CONTROL_CON});
	}
	
	
	
	
	/**
	 * Pings the FTP Server to make sure that the connection is open and
	 * operating properly.  This method can also be used as a keep-alive.
	 *
	 * @return Returns true if the server is still up.  If false, the
	 * 		   connection to the FTP server has been lost (or there is an
	 * 		   error).
	 * @throws Exception 
	 */
	public ServerResponse command_ping() throws Exception
	{
		if(!this.isConnected())
		{
			throw new Exception("Connection needs to be created before this operation");
		}
		if(!this.isLoggedIn)
		{
			throw new Exception("Login is needed before this operation");
		}
		
		return this.basicCommand(FTPClientConstant.COMMAND_NO_OP, null, new Integer[]{FTPClientConstant.FTP_COMMAND_OK});
	}
	
	
	//Now, notify the FTP server that we have a data port open and waiting.
	public ServerResponse command_notifyDataPortOpen(String rawTypeIPAndPort) throws Exception
	{
		if(!this.isConnected())
		{
			throw new Exception("Connection needs to be created before this operation");
		}
		if(!this.isLoggedIn)
		{
			throw new Exception("Login is needed before this operation");
		}
		
		return this.basicCommand(FTPClientConstant.COMMAND_PORT, rawTypeIPAndPort, new Integer[]{FTPClientConstant.FTP_COMMAND_OK});
	}
	
	
	public ServerResponse command_choosetTransferType(String dataTransferType) throws Exception
	{
		if(!this.isConnected())
		{
			throw new Exception("Connection needs to be created before this operation");
		}
		if(!this.isLoggedIn)
		{
			throw new Exception("Login is needed before this operation");
		}
		
		return this.basicCommand(FTPClientConstant.COMMAND_TRANSFER_TYPE, dataTransferType, new Integer[]{FTPClientConstant.FTP_COMMAND_OK});
	}
	
	
	public ServerResponse command_storeFileToRemoteHost(String localFileName) throws Exception
	{
		if(!this.isConnected())
		{
			throw new Exception("Connection needs to be created before this operation");
		}
		if(!this.isLoggedIn)
		{
			throw new Exception("Login is needed before this operation");
		}
		
		return this.basicCommand(FTPClientConstant.COMMAND_STORE, localFileName, new Integer[]{FTPClientConstant.FTP_FILE_STATUS_OK, FTPClientConstant.FTP_CON_ALREADY_OPEN});
	}

	public ServerResponse command_sendUserName(String username) throws Exception
	{
		if(!this.isConnected())
		{
			throw new Exception("Connection needs to be created before this operation");
		}
		
		return this.basicCommand(FTPClientConstant.COMMAND_USER, username, new Integer[]{FTPClientConstant.FTP_NEED_PASSWORD});
	}
	
	
	public ServerResponse command_sendPassword(String password) throws Exception
	{
		if(!this.isConnected())
		{
			throw new Exception("Connection needs to be created before this operation");
		}
		
		return this.basicCommand(FTPClientConstant.COMMAND_PASSWORD, password, new Integer[]{FTPClientConstant.FTP_USER_LOGGED_IN});
	}
	
	
	public ServerResponse command_listRemoteFiles(String remoteDirectory) throws Exception
	{
		if(!this.isConnected())
		{
			throw new Exception("Connection needs to be created before this operation");
		}
		if(!this.isLoggedIn)
		{
			throw new Exception("Login is needed before this operation");
		}

		return this.basicCommand(FTPClientConstant.COMMAND_FILE_LIST, remoteDirectory, new Integer[]{FTPClientConstant.FTP_FILE_STATUS_OK, FTPClientConstant.FTP_CON_ALREADY_OPEN});
	}
	
	
	public ServerResponse command_listRemoteFilesDetail(String remoteDirectory) throws Exception
	{
		if(!this.isConnected())
		{
			throw new Exception("Connection needs to be created before this operation");
		}
		if(!this.isLoggedIn)
		{
			throw new Exception("Login is needed before this operation");
		}

		return this.basicCommand(FTPClientConstant.COMMAND_DETAIL_LIST, remoteDirectory, new Integer[]{FTPClientConstant.FTP_FILE_STATUS_OK, FTPClientConstant.FTP_CON_ALREADY_OPEN});
	}
	
	
	public ServerResponse command_passiveMode() throws Exception
	{
		if(!this.isConnected())
		{
			throw new Exception("Connection needs to be created before this operation");
		}
		if(!this.isLoggedIn)
		{
			throw new Exception("Login is needed before this operation");
		}

		return this.basicCommand(FTPClientConstant.COMMAND_PASSIVE, null, new Integer[]{FTPClientConstant.FTP_START_PASSIVE});
	}
	
	

	
	public ServerResponse command_download(String remoteFileName) throws Exception
	{
		if(!this.isConnected())
		{
			throw new Exception("Connection needs to be created before this operation");
		}
		if(!this.isLoggedIn)
		{
			throw new Exception("Login is needed before this operation");
		}

		return this.basicCommand(FTPClientConstant.COMMAND_DOWNLOAD_FILE, remoteFileName, new Integer[]{FTPClientConstant.FTP_FILE_STATUS_OK, FTPClientConstant.FTP_CON_ALREADY_OPEN});
	}
	
	
	
	public ServerResponse command_append(String remoteFileName) throws Exception
	{
		if(!this.isConnected())
		{
			throw new Exception("Connection needs to be created before this operation");
		}
		if(!this.isLoggedIn)
		{
			throw new Exception("Login is needed before this operation");
		}

		return this.basicCommand(FTPClientConstant.COMMAND_APPEND_FILE, remoteFileName, new Integer[]{FTPClientConstant.FTP_FILE_STATUS_OK, FTPClientConstant.FTP_CON_ALREADY_OPEN});
	}	
	

	
	
	

	
	


	
	

	/**
	 * Retrieves the file listing in the given directory.  If no directory
	 * is given, the current directory is used.
	 *
	 * @param directory Directory to retrieve files from (may be null).
	 *
	 * @return Returns a list of files in the given directory.
	 * @exception Exception Connecting errors.
	 */
	public String[] listFiles(String remoteDirectory, boolean detail) throws Exception
	{
		if(!this.isConnected())
		{
			throw new Exception("Connection needs to be created before this operation");
		}
		if(!this.isLoggedIn)
		{
			throw new Exception("Login is needed before this operation");
		}
		String remote_directory = (remoteDirectory != null) ? remoteDirectory : ".";
		
		
		

		ServerSocket serverSocket = this.openPortToReceiveData();
		if(serverSocket == null)
		{
			throw new Exception("Unable to change the data port.");
		}


		//Make sure we set up the transfer type correctly.
		if(!this.command_choosetTransferType(this.dataTransferType).isSuccessful())
		{
			throw new Exception("Unable to change the transfer type.");
		}


		//Now, to get the list of files.
		if(detail)
		{
			if(!this.command_listRemoteFilesDetail(remote_directory).isSuccessful())
			{
				throw new Exception("Unable to get directory listing.  LIST command failed.");
			}
		}
		else
		{
			if(!this.command_listRemoteFiles(remote_directory).isSuccessful())
			{
				throw new Exception("Unable to get directory listing.  NLST command failed.");
			}
		}
		
		
		Socket socket = serverSocket.accept();
		InputStream inputStream = socket.getInputStream();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)); 

		
		Vector<String> fileList = new Vector<String>();
		String fileListLine = null;
		while((fileListLine = bufferedReader.readLine()) != null)
		{
			fileList.add(fileListLine);
		}
		String[] files = new String[fileList.size()];
		fileList.toArray(files);
		
		
		bufferedReader.close();
		bufferedReader = null;
		inputStream.close();
		inputStream = null;
		socket.close();
		socket = null;
		
		
		ServerResponse response = this.receiveResponseAndVarify(new Integer[]{FTPClientConstant.FTP_TRANSFER_SUCCESSFUL, FTPClientConstant.FTP_FILE_ACTION_OK}); 
		if(!response.isSuccessful())
		{
			throw new Exception("Error transfering file: "+response.getReponseCode()+" "+response.getCodeDescription());
		}

		return files;
	}

	
	public String[] listFiles(String remoteDirectory) throws Exception
	{
		return this.listFiles(remoteDirectory, false);
	}
	
	/**
	 * Retrieves the directory listing in the given directory.  If no directory
	 * is given, the current directory is used.
	 *
	 * @param directory Directory to retrieve files from (may be null).
	 *
	 * @return Returns a list of files in the given directory.
	 *
	 * @exception Exception Connecting errors.
	 */
	public String[] listFilesWithDetails(String remoteDirectory) throws Exception
	{
		return this.listFiles(remoteDirectory, true);
	}
	
	

	



	/**
	 * Downloads the given file.  The file will be stored in the localFile If 
	 * the file is empty, this method will return 0 (though a zero-byte 
	 * file should still be written out).
	 *
	 * @param remoteDirectoryName The name of the directory in host, where the file to download stored in. It can have null value.
	 *                                 It will be current directory(".") if its value is null. 
	 * @param remoteFileName The name of the file to download in host. It cannot have null value.
	 * @param localDirectoryName The name of the directory in local, where the file will be downloaded to. It can have null value.
	 *                              It will be current directory(".") if its value is null.
	 * @param localFileName The name of the file after download to local.
	 *
	 * @return Returns the number of bytes received.
	 *
	 * @exception Exception Connecting errors.
	 */
	public int download(String remoteDirectoryName, String remoteFileName, String localDirectoryName, String localFileName) throws Exception
	{
		if(!this.isConnected())
		{
			throw new Exception("Connection needs to be created before this operation");
		}
		if(!this.isLoggedIn)
		{
			throw new Exception("Login is needed before this operation");
		}
		if(remoteFileName == null)
		{
			throw new Exception("remoteFile should not be null");
		}
		String remote_directory = ((remoteDirectoryName != null) ? remoteDirectoryName : ".");
		String local_directory =  ((localDirectoryName != null) ? localDirectoryName : ".");
		String remote_file = remoteFileName;
		String local_file = ((localFileName != null) ? localFileName : remoteFileName);
		
		


		// Enter Passive Mode
		Socket socket = this.enterPassiveMode();
		InputStream inputStream = null;
		if(socket != null)
		{
			inputStream = socket.getInputStream();
		}
		else
		{
			throw new Exception("Unable to enter passive mode");
		}
		
		


		//Make sure we set up the transfer type correctly.
		if(!this.command_choosetTransferType(this.dataTransferType).isSuccessful())
		{
			throw new Exception("Unable to change the transfer type.");
		}
		
		
		if (!command_changeDirectory(remote_directory, false).isSuccessful())
		{
			throw new Exception("Unable to download file.  CD command failed.");
		}


		//Download the file
		if(!this.command_download(remote_file).isSuccessful())
		{
			throw new Exception("Unable to download file.  RETR command failed.");
		}


		File localDir = new  File(local_directory);
		if(!localDir.exists())
		{
			if(!localDir.mkdirs())
			{
				throw new Exception("Unable to create local directory: "+local_directory);
			}
		}
			
		
		//setup the output file
		File outputFile = new File(local_directory, local_file);
		FileOutputStream fileWriter = new FileOutputStream(outputFile);
		int KB = 1024;
		byte buff[] = new byte[10*KB];
		
		
			
		//read in one character and write it to the file
		int bytesRead = 0;
		int totalBytes = 0;
		while((bytesRead = inputStream.read(buff)) > 0)
		{
			fileWriter.write(buff,0,bytesRead);
			totalBytes += bytesRead;
		}

		
		//close passive mode socket
		inputStream.close();
		inputStream = null;
		socket.close();
		socket = null;
		fileWriter.close();
		fileWriter = null;



		//Make sure that the FTP server received it okay.
		ServerResponse serverResponse = this.receiveResponseAndVarify(new Integer[]{FTPClientConstant.FTP_TRANSFER_SUCCESSFUL, FTPClientConstant.FTP_FILE_ACTION_OK}); 
		if(!serverResponse.isSuccessful())
		{
			throw new Exception("Error transfering file: "+serverResponse.getReponseCode()+" "+serverResponse.getCodeDescription());
		}
		

		return totalBytes;
	}


	
	

	/**
	 * Downloads the given file.
	 *
	 * @param fileName The name of the file to download.
	 *
	 * @return Returns the contents of the file that was download.
	 *
	 * @exception Exception Connecting errors.
	 */
	public String[] downloadFileContent(String remoteFileName) throws Exception
	{
		if(!this.isConnected())
		{
			throw new Exception("Connection needs to be created before this operation");
		}
		if(!this.isLoggedIn)
		{
			throw new Exception("Login is needed before this operation");
		}
		


		//Open the data port to listen on.
		ServerSocket serverSocket = this.openPortToReceiveData();
		if(serverSocket == null)
		{
			throw new Exception("Unable to change the data port.");
		}


		//Make sure we set up the transfer type correctly.
		if(!this.command_choosetTransferType(this.dataTransferType).isSuccessful())
		{
			throw new Exception("Unable to change the transfer type.");
		}
				

		

					
		//Download the file.
		if(!this.command_download(remoteFileName).isSuccessful())
		{
			throw new Exception("Unable to download file.  RETR command failed.");
		}
		
				
		Socket socket = serverSocket.accept();
		InputStream inputStream = socket.getInputStream();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		
		
		//Read the directory listing
		Vector<String> contentsVector = new Vector<String>();
		String contenstLine = null;
		while((contenstLine = bufferedReader.readLine()) != null)
		{
			contentsVector.add(contenstLine);
		}
		String[] contents = new String[contentsVector.size()];
		contentsVector.toArray(contents);
			

		bufferedReader.close();
		bufferedReader = null;
		inputStream.close();
		inputStream = null;
		socket.close();
		socket = null;
		


		//Make sure that the FTP server received it okay.
		ServerResponse serverResponse = this.receiveResponseAndVarify(new Integer[]{FTPClientConstant.FTP_TRANSFER_SUCCESSFUL, FTPClientConstant.FTP_FILE_ACTION_OK}); 
		if(!serverResponse.isSuccessful())
		{
			throw new Exception("Error transfering file: "+serverResponse.getReponseCode()+" "+serverResponse.getCodeDescription());
		}


		return contents;
	}





	

	
	
	/**
	 * This uploads a file during interactive mode.
	 *
	 * @param file The file to upload.
	 *
	 * @exception Exception Communication errors with the FTP server.
	 */
	public int uploadFile(String localDirectory, String localFileName, String remoteDirectory, String remoteFileName) throws Exception
	{
		if(!this.isConnected())
		{
			throw new Exception("Connection needs to be created before this operation");
		}
		if(!this.isLoggedIn)
		{
			throw new Exception("Login is needed before this operation");
		}
		if(localFileName == null)
		{
			throw new Exception("Local filename cannot be null");
		}
		if(remoteFileName == null)
		{
			throw new Exception("Remote filename cannot be null");
		}
		String local_directory = (localDirectory != null) ? localDirectory : ".";
		String local_file_name = localFileName;
		String remote_directory = (remoteDirectory != null) ? remoteDirectory : ".";
		String remote_file_name = remoteFileName;
				
		String errorMessage = this.validateFile(local_directory, local_file_name);
		if(errorMessage != null)
		{
			throw new Exception(errorMessage);
		}
		
		
		
		
		
		
		if(!this.command_changeDirectory(remote_directory, true).isSuccessful())
		{
			throw new Exception("Unable to upload file.  Creating remote directory "+remote_directory+" failed.");
		}

		

		
		//Now, notify the FTP server that we have a data port open and waiting.
		ServerSocket serverSocket = this.openPortToReceiveData();
		if(serverSocket == null)
		{
			throw new Exception("Unable to change the data port.");
		}
		
				
				
		//Make sure we set up the transfer type correctly.
		if(!this.command_choosetTransferType(this.dataTransferType).isSuccessful())
		{
			throw new Exception("Unable to change the transfer type.");
		}
		

			
		//Now, to store (upload) the file.
		if(!this.command_storeFileToRemoteHost(remote_file_name).isSuccessful())
		{
			throw new Exception("Unable to upload file.  STOR command failed.");
		}

		
		Socket socket = serverSocket.accept();
		OutputStream outputStream = socket.getOutputStream();
		FileInputStream fileInputStream = new FileInputStream(new File(local_directory, local_file_name));

		
		byte[] buffer = new byte[FTPClientConstant.BLOCK_SIZE];
		int bytesSent = 0;
		int byteRead = 0;
		while((byteRead = fileInputStream.read(buffer)) > 0)
		{
			outputStream.write(buffer, 0, byteRead);
			bytesSent += byteRead;
		}


		fileInputStream.close();
		fileInputStream = null;
		outputStream.close();
		outputStream = null;
		socket.close();
		socket = null;
		
		

		//Make sure that the FTP server received it okay.
		ServerResponse serverResponse = this.receiveResponseAndVarify(new Integer[]{FTPClientConstant.FTP_TRANSFER_SUCCESSFUL, FTPClientConstant.FTP_FILE_ACTION_OK}); 
		if(!serverResponse.isSuccessful())
		{
			throw new Exception("Error transfering file: "+serverResponse.getReponseCode()+" "+serverResponse.getCodeDescription());
		}
		
		
		return bytesSent;
	}

	

	


	public int uploadFile(String localDirectory, String localFileName, String remoteDirectory) throws Exception
	{
		return this.uploadFile(localDirectory, localFileName, remoteDirectory, localFileName);
	}

	public int uploadFile(String localDirectory, String localFileName) throws Exception
	{
		return this.uploadFile(localDirectory, localFileName, null, localFileName);
	}
	
	public int uploadFile(String localFileName) throws Exception
	{
		return this.uploadFile(null, localFileName, null, localFileName);
	}
	

	



	/**
	 * Appends file data to an existing file (or creates a new one if one does
	 * not already exist) remotely.  Calling this method only sends over each
	 * line (followed by returns).
	 *
	 * @param fileName The name of the remote file (in the current directory).
	 * @param fileData The file data (each String in the array is a separate
	 * 		  line in the resulting file).
	 *
	 * @return Returns true if the append is successful.
	 * @throws Exception 
	 */
	public int append(String[] fileContents, String remoteDirectory, String remoteFileName) throws Exception
	{
		if(!this.isConnected())
		{
			throw new Exception("Connection needs to be created before this operation");
		}
		if(!this.isLoggedIn)
		{
			throw new Exception("Login is needed before this operation");
		}
		if(fileContents == null)
		{
			throw new Exception("File contents can not be null");
		}
		if((remoteFileName == null)||(remoteFileName.equals("")))
		{
			throw new Exception("remoteFileName connot be null");
		}
		String[] file_contents = fileContents; 
		String remote_directory = (remoteDirectory != null) ? remoteDirectory : ".";
		String remote_file_name = remoteFileName;
		
		
		
		
		
		if(!this.command_changeDirectory(remote_directory, true).isSuccessful())
		{
			throw new Exception("Unable to upload file.  Creating remote directory "+remote_directory+" failed.");
		}
		
		
		
		
		//Now, notify the FTP server that we have a data port open and waiting.
		ServerSocket serverSocket = this.openPortToReceiveData();
		if(serverSocket == null)
		{
			throw new Exception("Unable to change the data port.");
		}
				

		

		//Make sure we set up the transfer type correctly.
		if(!this.command_choosetTransferType(this.dataTransferType).isSuccessful())
		{
			throw new Exception("Unable to change the transfer type.");
		}
			

	

		//Now, to append the file data.
		if(!this.command_append(remote_file_name).isSuccessful())
		{
			throw new Exception("Unable to append. APPE command failed");
		}
		
		
		Socket socket = serverSocket.accept();
		OutputStream outputStream = socket.getOutputStream();

		
		int totalBytes = 0;
		byte[] byteSent = null;
		for (int i=0; i<file_contents.length; ++i)
		{
			byteSent = file_contents[i].getBytes();
			totalBytes += byteSent.length;
			outputStream.write(byteSent);
			outputStream.write((int) '\n');
		}


		outputStream.close();
		outputStream = null;
		socket.close();
		socket = null;

		

		//Make sure that the FTP server received it okay.
		ServerResponse response = this.receiveResponseAndVarify(new Integer[]{FTPClientConstant.FTP_TRANSFER_SUCCESSFUL, FTPClientConstant.FTP_FILE_ACTION_OK}); 
		if(!response.isSuccessful())
		{
			throw new Exception("Error transfering file: "+response.getReponseCode()+" "+response.getCodeDescription());
		}

		
		return totalBytes;
	}
	
	
	






//
//	/**
//	 * For debugging and testing purposes only.
//	 */
//	protected static void interactiveTest()
//	{
//		FTPClient ftp = new FTPClient();
//		BufferedReader in;
//		String str;
//
//		try
//		{
//			System.out.println("FTP Client:");
//			System.out.print("Host:  ");
//			in = new BufferedReader(new InputStreamReader(System.in));
//			str = in.readLine();
//			ftp.setHost(str);
//			System.out.println("Connecting...");
//			ftp.connect();
//			System.out.print("Command:  ");
//			str = in.readLine();
//
//			while (!str.equalsIgnoreCase("quit"))
//			{
//				ftp.sendCommand(str);
//				System.out.println((String) ftp.traceLog.get(ftp.traceLog.size() - 1));
//				System.out.print("Command:  ");
//				str = in.readLine();
//			}
//
//			//end while ( !str.equalsIgnoreCase( "quit" ) )
//			ftp.disconnect();
//
//			System.out.println("Trace Log:");
//			System.out.println(ftp.getLog());
//		}
//		catch (Exception e)
//		{
//			System.out.println(e.toString());
//		}
//	}
//

//
//	//end interactiveTest()
//	
	public static void main(String[] args) throws Exception
	{
		try
		{
			
		
		
			FTPClient ftp = new FTPClient();

			
			
		

				
				
				
				
//				ftp.setHost("smftp.usps.gov");
//				ftp.setLogin("TSDCREAD", "dc080309");
				

				ftp.setBinary();
//				ftp.setRemoteCommands(new String[]{""});
			
//				if (!(ftp.download("test.txt", "text.txt")>0)) {
//					throw new CriticalBatchException("File Transfer Failed");
//				}
				//ftp.connect(null);
				String[] result1 = ftp.connect("localhost");
				for(int i=0; i<result1.length; ++i)
				{
					System.out.println(result1[i]);
				}
				
				ftp.login("aaa", "tsybU4lCJNk=", true);
				
//				result1 = ftp.sendCommand("USER aaa");
//				for(int i=0; i<result1.length; ++i)
//				{
//					System.out.println(result1[i]);
//				}
//				
//				result1 = ftp.sendCommand("PASS aaa");
//				for(int i=0; i<result1.length; ++i)
//				{
//					System.out.println(result1[i]);
//				}
//				
//				result1 = ftp.sendCommand("quit");
//				for(int i=0; i<result1.length; ++i)
//				{
//					System.out.println(result1[i]);
//				}
				
				
				
				//ftp.command_ping();
				
//				ftp.changeDirectory("ss");
//				ftp.changeDirectory("..");
//				ftp.createDirectory("aa");
				
				//ftp.delete("test.txt");
				
//				short[] ip = ftp.getLocalIpAddress();
//				for(int i=0; i<ip.length; ++i)
//				{
//					System.out.println(ip[i]);
//				}


				String[] files = ftp.listFiles(null, false);
				for(int i=0; i<files.length; ++i)
				{
					System.out.println(files[i]);
				}
				
				
//				String[] files = ftp.listFilesWithDetails(".");
//				for(int i=0; i<files.length; ++i)
//				{
//					System.out.println(files[i]);
//				}
				
//				int result = ftp.download(".", "test.txt", "test/aa", "test.txt");
//				System.out.println(result);
				
//				String[] contents = ftp.downloadFileContent("test.txt");
//				for(int i=0; i<contents.length; ++i)
//				{
//					System.out.println(contents[i]);
//				}
				
				//int result = ftp.uploadFile("test/aa","test.txt", "aa", "hey11.txt");
				//int result = ftp.uploadFile("test/aa","test.txt");
				//int result = ftp.uploadFile("text.txt");
				//System.out.println(result);
				
				//System.out.println(ftp.terminateConnection());
//				String[] contents = new String[3];
//				contents[0] = "aabb";
//				contents[1] = "ccdd";
//				contents[2] = "eeff";
//				System.out.println(ftp.append(contents, "ohoh", "myContents.txt"));
				
				ftp.disconnect();
				
//				ftp.connect();
//				ftp.command_ping();
				
				


//				String result = ftp.sendCommand("PASV");
		}
		catch(Exception e)
		{
			FTPClient.logger.error(FTPClient.stacktraceToString(e));
			throw e;
		}
//				
		
	}
	
	
	
	class ServerResponse
	{
		private Boolean successful;
		private Integer responseCode;
		private String codeDescription;
		private Vector<String> responseMessage;
		
		public ServerResponse()
		{
			successful = null;
			responseCode = null;
			codeDescription = null;
			responseMessage = new Vector<String>();
		}


		public void addResponseMessage(String[] responseMsg)
		{
			for(int i=0; i<responseMsg.length; ++i)
			{
				this.responseMessage.add(responseMsg[i]);
			}
			String lastLine = this.responseMessage.lastElement();
			this.responseCode = Integer.parseInt(lastLine.substring(0, 3));
			this.codeDescription = lastLine.substring(3, lastLine.length());
		}
		
		public String[] getResponseMessage()
		{
			String[] responseMsg = new String[this.responseMessage.size()];
			this.responseMessage.toArray(responseMsg);
			
			return responseMsg;
		}
		
		
		public void varify(Integer[] expectedResponseCode)
		{
			boolean result = false;
			
			if(expectedResponseCode != null)
			{
				for(int i=0; i<expectedResponseCode.length; ++i)
				{
					if (this.responseCode.compareTo(expectedResponseCode[i]) == 0)
					{
						result = true;
						break;
					}
				}
			}
			
			this.successful = result;
		}
		
		
		public String[] parseResponseMessage()
		{
			String[] responseMessageArray = new String[this.responseMessage.size()];
			this.responseMessage.toArray(responseMessageArray);
			return responseMessageArray;
		}
		
		public boolean isSuccessful()
		{
			return this.successful;
		}
		
		public void setSuccessful(boolean successful)
		{
			this.successful = successful;
		}
		
		public Integer getReponseCode()
		{
			return this.responseCode;
		}
		
		public String getCodeDescription()
		{
			return this.codeDescription;
		}
	}
}










