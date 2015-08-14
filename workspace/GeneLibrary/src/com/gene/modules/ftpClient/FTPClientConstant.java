package com.gene.modules.ftpClient;

public class FTPClientConstant
{
	/** The default port number for FTP. */
	public static final int FTP_PORT = 21;

	/** The default number of seconds for the socket to timeout. */
	public static final int SOCKET_TIMEOUT = 60000; //1 minute

	/** The maximum port number for the data port. */
	public static final int MAX_PORT = 65535;
	
	public static final int MIN_PORT = 0;

	/**
	 * The end of line characters sent after a command is issued to the FTP
	 * server.
	 */
	public static final String END_OF_LINE = "\r\n";

	/** Transfer type for binary. */
	public static final String TYPE_BINARY = "I";

	/** Transfer type for ascii (text). */
	public static final String TYPE_ASCII = "A";

	/** Transfer type for EBCDIC. */
	public static final String TYPE_EBCDIC = "E";

	/** The block size used in data transfer. */
	public static final int BLOCK_SIZE = 1024;

	/** The USPS domain ip address (the first octet). */
	public static String USPS_DOMAIN = "56.";

	// ***
	// FTP Commands
	// ***

	/** Command to specify the user name. */
	public static final String COMMAND_USER = "USER";

	/** Command to specify the user's password for authentication. */
	public static final String COMMAND_PASSWORD = "PASS";

	/** Command to change to a different remote directory. */
	public static final String COMMAND_CHANGE_DIR = "CWD";
	
	
	public static final String COMMAND_CREATE_DIR = "MKD";
	
	public static final String COMMAND_REMOVE_DIR = "RMD";

	/** Command to terminate the connection. */
	public static final String COMMAND_QUIT = "QUIT";

	/** Command to upload a file. */
	public static final String COMMAND_STORE = "STOR";

	/** Command to append to a remote file. */
	public static final String COMMAND_APPEND_FILE = "APPE";

	/** Command to specify the data port for file transfers. */
	public static final String COMMAND_PORT = "PORT";

	/** Command to specify the format type (i.e. binary or ascii). */
	public static final String COMMAND_TRANSFER_TYPE = "TYPE";

	/**
	 * Command to retrieve a directory listing of the current remote directory.
	 */
	public static final String COMMAND_FILE_LIST = "NLST";

	/**
	 * Command to retrieve a directory listing of the current remote directory.
	 */
	public static final String COMMAND_DETAIL_LIST = "LIST";

	/** Command to download a particular file. */
	public static final String COMMAND_DOWNLOAD_FILE = "RETR";

	/** Command to delete a remote file. */
	public static final String COMMAND_DELETE_FILE = "DELE";

	/**
	 * Command that does nothing other than verify that the server is still up.
	 */
	public static final String COMMAND_NO_OP = "NOOP";

	/**
	 * Command that returns the size of the file.
	 */
	public static final String COMMAND_SIZE = "SIZE";

	/**
	 * Command that runs site commands
	 */
	public static final String COMMAND_SITE = "SITE";

	/**
	 * Command that enters passive data transfer mode
	 */
	public static final String COMMAND_PASSIVE = "PASV";

	// ***
	// FTP response codes as per RFC 959
	// ***

	public static final int FTP_DIRECTORY_CREATED_OK = 257;
	
	
	/** Restart marker reply */
	public static final int FTP_RESET_MARKER_REPLY = 110;

	/** Service ready in nnn minutes */
	public static final int FTP_SERVICE_READY_IN = 120;

	/** Data connection already open; transfer starting */
	public static final int FTP_CON_ALREADY_OPEN = 125;

	/** File status okay; about to open data connection */
	public static final int FTP_FILE_STATUS_OK = 150;

	/** Command okay */
	public static final int FTP_COMMAND_OK = 200;

	/** Command not implemented, superfluous at this site */
	public static final int FTP_COMMAND_SUPERFLUOUS = 202;

	/** Service ready for new user */
	public static final int FTP_SERVICE_READY = 220;

	/** Service closing control connection */
	public static final int FTP_CLOSING_CONTROL_CON = 221;

	/** Service closing data connection; file transfer successful */
	public static final int FTP_TRANSFER_SUCCESSFUL = 226;

	/** Service entering passive mode */
	public static final int FTP_START_PASSIVE = 227;

	/** User logged in, proceed */
	public static final int FTP_USER_LOGGED_IN = 230;

	/** Requested file action okay, completed */
	public static final int FTP_FILE_ACTION_OK = 250;

	/** User name okay, need password */
	public static final int FTP_NEED_PASSWORD = 331;

	/** Need account for login */
	public static final int FTP_NEED_ACCOUNT = 332;

	/** Service not available, closing control connection */
	public static final int FTP_SERVICE_UNAVAILABLE = 421;

	/** Can't open data connection */
	public static final int FTP_NO_DATA_CON = 425;

	/** Connection closed; transfer aborted */
	public static final int FTP_CON_CLOSED = 426;

	/** Requested file action not taken; file unavailable (e.g., file busy) */
	public static final int FTP_FILE_UNAVAILABLE = 450;

	/** Requested action aborted: local error in processing */
	public static final int FTP_LOCAL_ERROR = 451;

	/** Requested action not taken; insufficient storage space in system */
	public static final int FTP_NO_STORAGE_SPACE = 452;

	/** Syntax error, command unrecognized */
	public static final int FTP_SYNTAX_ERROR = 500;

	/** Syntax error in parameters or arguments */
	public static final int FTP_BAD_PARAMS = 501;

	/** Command not implemented */
	public static final int FTP_COMMAND_NOT_IMPLEMENTED = 502;

	/** Bad sequence of commands */
	public static final int FTP_BAD_COMMAND_SEQUENCE = 503;

	/** Command not implemented for that parameter */
	public static final int FTP_NO_COMMAND_FOR_PARAM = 504;

	/** Not logged in */
	public static final int FTP_NOT_LOGGED_IN = 530;

	/** Need account for storing files */
	public static final int FTP_ACCOUNT_REQUIRED = 532;

	/**
	 * Requested action not taken; file unavailable (e.g., file not found, no
	 * access)
	 */
	public static final int FTP_FILE_ERROR = 550;

	/** Requested action aborted: page type unknown */
	public static final int FTP_PAGE_TYPE_ERROR = 551;

	/**
	 * Requested file action aborted; exceeded storage allocation (for current
	 * directory or dataset)
	 */
	public static final int FTP_EXCEEDED_STORAGE = 552;

	/** Requested action not taken; file name not allowed */
	public static final int FTP_INVALID_FILE_NAME = 553;
	
	/** returned file size **/
	public static final int FTP_FILE_SIZE = 213;
	
	
	
	
	
//	public static final String FTP_HOST = "FtpHost";
//	public static final String FTP_USER = "FtpUserName";
//	public static final String FTP_PASS = "FtpPassword";
//	public static final String FTP_REMOTE_DIR = "FtpRemoteDirectory";
//	public static final String FTP_REMOTE_FN = "FtpRemoteFileName";
//	public static final String FTP_LOCAL_DIR = "FtpLocalDirectory";
//	public static final String FTP_LOCAL_FN = "FtpLocalFileName";
//	public static final String FTP_REMOTE_CMDS = "FtpRemoteCommands";
//	public static final String FTP_TRANSFER_TYPE = "FtpTransferType";
}
