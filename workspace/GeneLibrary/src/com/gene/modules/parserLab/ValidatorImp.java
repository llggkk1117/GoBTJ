package com.gene.modules.parserLab;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Vector;
import org.govgrnds.core.util.crypto.DefaultCryptoHelper;


import com.gene.modules.features.ArrangeItemsFeature;
import com.gene.modules.features.ValidateNumberFeature;


public class ValidatorImp implements Validator
{
	private ArrangeItemsFeature arrangeItemsFeature;
	private ValidateNumberFeature validateNumberFeature;
	private DefaultCryptoHelper crytoHelper;
	private final int MINNUM = 1;
	private final int MAXIMUM = 1600;
	
	public ValidatorImp()
	{
		arrangeItemsFeature = new ArrangeItemsFeature();
		validateNumberFeature = new ValidateNumberFeature();
		crytoHelper = new DefaultCryptoHelper();
	}
	
	
	public Vector<String> validate(HashMap<String, String> properties)
	{
		Vector<String> errorMessages = new Vector<String>();
		
		String stringFetchOzipSourceDatabase = properties.get("fetch.ozip.source.database");
		String urlForFetchOZips = properties.get("fetch.ozip.database.url");
		String idForFetchOZips = properties.get("fetch.ozip.database.id");
		String pwForFetchOZips = properties.get("fetch.ozip.database.pw");
		String decryptPwForFetchOZips = null;
		if(stringFetchOzipSourceDatabase == null)
		{
			errorMessages.add("[ERROR] No value found for 'fetch.ozip.source.database' in properties file.");
		}
		else if(!((stringFetchOzipSourceDatabase.equals("true"))||(stringFetchOzipSourceDatabase.equals("false"))))
		{
			errorMessages.add("[ERROR] Invalid value for 'fetch.ozip.source.database' in properties file.");
		}
		else
		{
			if(stringFetchOzipSourceDatabase.equals("true"))
			{
				boolean noErrorOnDecryptPwForFetchOZips = true;
				try
				{
					decryptPwForFetchOZips = crytoHelper.decrypt(pwForFetchOZips);
				}
				catch(Exception e)
				{
					errorMessages.add("[ERROR] Error found in the encrypted passwod 'fetch.ozip.database.pw'.");
					noErrorOnDecryptPwForFetchOZips = false;
				}
				
				if(noErrorOnDecryptPwForFetchOZips)
				{
					try
					{
						openConnection(urlForFetchOZips, idForFetchOZips, decryptPwForFetchOZips);
						closeConnection();
					}
					catch (Exception e)
					{
						errorMessages.add("[ERROR] Failed to connect to DB ("+urlForFetchOZips+") for fetching OZips. Check if the DB is on, and DB URL, ID, or password in properties file.");
					}
				}
			}
		}
		
		
		String stringFetchOzipSourceDatafile = properties.get("fetch.ozip.source.datafile");
		String datafileForOZip = properties.get("fetch.ozip.datafile.name");
		if(stringFetchOzipSourceDatafile == null)
		{
			errorMessages.add("[ERROR] No value found for 'fetch.ozip.source.datafile' in properties file.");
		}
		else if(!((stringFetchOzipSourceDatafile.equals("true"))||(stringFetchOzipSourceDatafile.equals("false"))))
		{
			errorMessages.add("[ERROR] Invalid value for 'fetch.ozip.source.datafile' in properties file.");
		}
		else
		{
			if(stringFetchOzipSourceDatafile.equals("true"))
			{
				if(datafileForOZip ==  null)
				{
					errorMessages.add("[ERROR] No value found for 'fetch.ozip.datafile.name' in properties file.");
				}
			}
		}
		
		boolean ozipSourceExists = false;
		if((stringFetchOzipSourceDatabase != null)&&(stringFetchOzipSourceDatabase.equals("true")))
		{
			ozipSourceExists = true;
		}
		if((stringFetchOzipSourceDatafile != null)&&(stringFetchOzipSourceDatafile.equals("true")))
		{
			ozipSourceExists = true;
		}
		if(!ozipSourceExists)
		{
			errorMessages.add("[ERROR] There is no source for fetching OZips. Please check 'fetch.ozip.source.database' or 'fetch.ozip.source.datafile' in properties file.");
		}

		
		
		String stringFetchOzipSaveToFile = properties.get("fetch.ozip.save_to_file");
		String stringFetchOzipSaveToFileFileName = properties.get("fatch.ozip.save_to_file.filename");
		if(stringFetchOzipSaveToFile == null)
		{
			errorMessages.add("[ERROR] No value found for 'fetch.ozip.save_to_file' in properties file.");
		}
		else if(!((stringFetchOzipSaveToFile.equals("true"))||(stringFetchOzipSaveToFile.equals("false"))))
		{
			errorMessages.add("[ERROR] Invalid value for 'fetch.ozip.save_to_file' in properties file.");
		}
		else
		{
			if(stringFetchOzipSaveToFile.equals("true"))
			{
				if(stringFetchOzipSaveToFileFileName ==  null)
				{
					errorMessages.add("[ERROR] No value found for 'fatch.ozip.save_to_file.filename' in properties file.");
				}
			}
		}
				

		
		
		String stringFetchDzipSourceDatabase = properties.get("fetch.dzip.source.database");
		String urlForFetchDZips = properties.get("fetch.dzip.database.url");
		String idForFetchDZips = properties.get("fetch.dzip.database.id");
		String pwForFetchDZips = properties.get("fetch.dzip.database.pw");
		String decryptPwForFetchDZips = null;
		if(stringFetchDzipSourceDatabase == null)
		{
			errorMessages.add("[ERROR] No value found for 'fetch.dzip.source.database' in properties file.");
		}
		else if(!((stringFetchDzipSourceDatabase.equals("true"))||(stringFetchDzipSourceDatabase.equals("false"))))
		{
			errorMessages.add("[ERROR] Invalid value for 'fetch.dzip.source.database' in properties file.");
		}
		else
		{
			if(stringFetchDzipSourceDatabase.equals("true"))
			{
				boolean noErrorOnDecryptPwForFetchDZips = true;
				try
				{
					decryptPwForFetchDZips = crytoHelper.decrypt(pwForFetchDZips);
				}
				catch(Exception e)
				{
					errorMessages.add("[ERROR] Error found in the encrypted passwod 'fetch.dzip.database.pw'.");
					noErrorOnDecryptPwForFetchDZips = false;
				}
				
				if(noErrorOnDecryptPwForFetchDZips)
				{
					try
					{
						openConnection(urlForFetchDZips, idForFetchDZips, decryptPwForFetchDZips);
						closeConnection();
					}
					catch (Exception e)
					{
						errorMessages.add("[ERROR] Failed to connect to DB ("+urlForFetchDZips+") for fetching DZips. Check if the DB is on, and DB URL, ID, or password in properties file.");
					}
				}
			}
		}
		
		
		String stringFetchDzipSourceDatafile = properties.get("fetch.dzip.source.datafile");
		String datafileForDZip = properties.get("fetch.dzip.datafile.name");
		if(stringFetchDzipSourceDatafile == null)
		{
			errorMessages.add("[ERROR] No value found for 'fetch.dzip.source.datafile' in properties file.");
		}
		else if(!((stringFetchDzipSourceDatafile.equals("true"))||(stringFetchDzipSourceDatafile.equals("false"))))
		{
			errorMessages.add("[ERROR] Invalid value for 'fetch.dzip.source.datafile' in properties file.");
		}
		else
		{
			if(stringFetchDzipSourceDatafile.equals("true"))
			{
				if(datafileForDZip ==  null)
				{
					errorMessages.add("[ERROR] No value found for 'fetch.dzip.datafile.name' in properties file.");
				}
			}
		}
		
		
		boolean dzipSourceExists = false;
		if((stringFetchDzipSourceDatabase != null)&&(stringFetchDzipSourceDatabase.equals("true")))
		{
			dzipSourceExists = true;
		}
		if((stringFetchDzipSourceDatafile != null)&&(stringFetchDzipSourceDatafile.equals("true")))
		{
			dzipSourceExists = true;
		}
		if(!dzipSourceExists)
		{
			errorMessages.add("[ERROR] There is no source for fetching DZips. Please check 'fetch.dzip.source.database' or 'fetch.dzip.source.datafile' in properties file.");
		}
		

		String stringFetchDzipSaveToFile = properties.get("fetch.dzip.save_to_file");
		String stringFetchDzipSaveToFileFileName = properties.get("fatch.dzip.save_to_file.filename");
		if(stringFetchDzipSaveToFile == null)
		{
			errorMessages.add("[ERROR] No value found for 'fetch.dzip.save_to_file' in properties file.");
		}
		else if(!((stringFetchDzipSaveToFile.equals("true"))||(stringFetchDzipSaveToFile.equals("false"))))
		{
			errorMessages.add("[ERROR] Invalid value for 'fetch.dzip.save_to_file' in properties file.");
		}
		else
		{
			if(stringFetchDzipSaveToFile.equals("true"))
			{
				if(stringFetchDzipSaveToFileFileName ==  null)
				{
					errorMessages.add("[ERROR] No value found for 'fatch.dzip.save_to_file.filename' in properties file.");
				}
			}
		}
		
		
		
		
		String stringInsertToDatabase = properties.get("insert.to.database");
		String urlForInsertODPairs = properties.get("insert.database.url");
		String idForInsertODPairs = properties.get("insert.database.id");
		String pwForInsertODPairs = properties.get("insert.database.pw");
		String decryptPwForInsertODPairs = null;
		String tableForInsertODPairs = properties.get("insert.database.table");
		String numOfConnectionsForInsertODPairs = properties.get("insert.database.num_of_connection");
		String initForInsertODPairs = properties.get("insert.database.initialize");
		if(stringInsertToDatabase == null)
		{
			errorMessages.add("[ERROR] No value found for 'insert.to.database' in properties file.");
		}
		else if(!((stringInsertToDatabase.equals("true"))||(stringInsertToDatabase.equals("false"))))
		{
			errorMessages.add("[ERROR] Invalid value for 'insert.to.database' in properties file.");
		}
		else
		{
			if(stringInsertToDatabase.equals("true"))
			{
				boolean noErrorOnDecryptPwForInsertODPairs = true;
				try
				{
					decryptPwForInsertODPairs = crytoHelper.decrypt(pwForInsertODPairs);
				}
				catch(Exception e)
				{
					errorMessages.add("[ERROR] Error found in the encrypted passwod 'insert.database.pw'.");
					noErrorOnDecryptPwForInsertODPairs = false;
				}
				
				if(noErrorOnDecryptPwForInsertODPairs)
				{
					try
					{
						openConnection(urlForInsertODPairs, idForInsertODPairs, decryptPwForInsertODPairs);
						closeConnection();
					}
					catch (Exception e)
					{
						errorMessages.add("[ERROR] Failed to connect to DB ("+urlForInsertODPairs+") for inserting invalid OD-pairs. Check if the DB is on, and DB URL, ID, or password in properties file.");
					}
				}
				
				if(tableForInsertODPairs == null)
				{
					errorMessages.add("[ERROR] No value found for 'insert.database.table' in properties file.");
				}
				
				if(numOfConnectionsForInsertODPairs == null)
				{
					errorMessages.add("[ERROR] No value found for 'insert.database.num_of_connection' in properties file.");
				}
				else
				{
					try
					{
						Integer.parseInt(numOfConnectionsForInsertODPairs);
					}
					catch(Exception e)
					{
						errorMessages.add("[ERROR] Invalid value for 'insert.database.num_of_connection' ");
					}
				}
				
				if(initForInsertODPairs == null)
				{
					errorMessages.add("[ERROR] No value found for 'insert.database.initialize' in properties file.");
				}
				else if(!((initForInsertODPairs.equals("true"))||(initForInsertODPairs.equals("false"))))
				{
					errorMessages.add("[ERROR] Invalid value for 'insert.database.initialize' in properties file.");
				}
			}
		}
		
		
		String stringInsertToDatafile = properties.get("insert.to.datafile");
		String stringInsertDatafileFilename = properties.get("insert.datafile.filename");
		String stringInsertDatafileInitialize = properties.get("insert.datafile.initialize");
		if(stringInsertToDatafile == null)
		{
			errorMessages.add("[ERROR] No value found for 'insert.to.datafile' in properties file.");
		}
		else if(!((stringInsertToDatafile.equals("true"))||(stringInsertToDatafile.equals("false"))))
		{
			errorMessages.add("[ERROR] Invalid value for 'insert.to.datafile' in properties file.");
		}
		else
		{
			if(stringInsertToDatafile.equals("true"))
			{
				if(stringInsertDatafileFilename ==  null)
				{
					errorMessages.add("[ERROR] No value found for 'insert.datafile.filename' in properties file.");
				}
				
				if(stringInsertDatafileInitialize == null)
				{
					errorMessages.add("[ERROR] No value found for 'insert.datafile.initialize' in properties file.");
				}
				else if(!((stringInsertDatafileInitialize.equals("true"))||(stringInsertDatafileInitialize.equals("false"))))
				{
					errorMessages.add("[ERROR] Invalid value for 'insert.datafile.initialize' in properties file.");
				}
			}
		}
		
		boolean odpairTargetExists = false;
		if((stringInsertToDatabase != null)&&(stringInsertToDatabase.equals("true")))
		{
			odpairTargetExists = true;
		}
		if((stringInsertToDatafile != null)&&(stringInsertToDatafile.equals("true")))
		{
			odpairTargetExists = true;
		}
		if(!odpairTargetExists)
		{
			errorMessages.add("[ERROR] There is no source for fetching OZips. Please check 'insert.to.database' or 'insert.to.datafile' in properties file.");
		}
		

		String stringLogLevel = properties.get("log.level");
		int intIogLevel = 0;
		if(stringLogLevel != null)
		{
			try
			{
				intIogLevel = Integer.parseInt(stringLogLevel);
				if((intIogLevel < 0)||(intIogLevel >5))
				{
					errorMessages.add("[ERROR] Value for 'log.level' should be between 0 to 5");
				}
			}
			catch(Exception e)
			{
				errorMessages.add("[ERROR] Invalid value for 'log.level'");
			}
		}
		
		String stringErrorCode = properties.get("error.code");
		String[] arrayErrorCode = null;
		if(stringErrorCode != null)
		{
			stringErrorCode = arrangeItemsFeature.arrangeItems(stringErrorCode);
			arrayErrorCode = stringErrorCode.split(",");
			for(int i=0; i<arrayErrorCode.length; ++i)
			{
				if(!(validateNumberFeature.validateNumber(arrayErrorCode[i], "int")))
				{
					errorMessages.add("[ERROR] Invalid value for 'error.code': "+arrayErrorCode[i]);
				}
			}
		}
		
		
		String stringTimeLine = properties.get("time.line");
		long longTimeLine = 0;
		if(stringTimeLine != null)
		{
			try
			{
				longTimeLine = Integer.parseInt(stringTimeLine);
				if(longTimeLine < 0)
				{
					errorMessages.add("[ERROR] Value for 'time.line' cannot be less than 0");
				}
			}
			catch(Exception e)
			{
				errorMessages.add("[ERROR] Invalid value for 'time.line'");
			}
		}
		
		
		
		String stringThreadNumber = properties.get("thread.number");
		int intThreadnumber = 0;
		if(stringThreadNumber != null)
		{
			try
			{
				intThreadnumber = Integer.parseInt(stringThreadNumber);
				if(intThreadnumber < MINNUM)
				{
					errorMessages.add("[ERROR] A value for 'thread.number' cannot be less than "+MINNUM+".");
				}
				else if(intThreadnumber > MAXIMUM)
				{
					errorMessages.add("[ERROR] A value for 'thread.number' cannot be more than "+MAXIMUM+".");
				}
			}
			catch(Exception e)
			{
				errorMessages.add("[ERROR] Invalid value for 'thread.number'");
			}
		}
		
		
		String stringDisplayPeriod = properties.get("display.period");
		int intDisplayPeriod = 0;
		if(stringDisplayPeriod != null)
		{
			try
			{
				intDisplayPeriod = Integer.parseInt(stringDisplayPeriod);
				if(intDisplayPeriod < 0)
				{
					errorMessages.add("[ERROR] Value for 'display.period' cannot be less than 0");
				}
			}
			catch(Exception e)
			{
				errorMessages.add("[ERROR] Invalid value for 'display.period'");
			}
		}
		
		return errorMessages;
	}
	
	
	
    private Connection connection;
    private Statement statement; //sql 명령문�?� 전달하는  Statement �?�터페�?�스 �?체 �?성
	
    public void openConnection(String url, String id, String pw) throws ClassNotFoundException, SQLException
    {
    	closeConnection();
    	Class.forName("oracle.jdbc.driver.OracleDriver");
    	connection = DriverManager.getConnection(url, id, pw);
    	statement = connection.createStatement();
    }
    
    public void closeConnection() throws SQLException
    {
    	if(connection != null)
    	{
    		connection.close();
			connection = null;
    	}
		if(statement != null)
		{
			statement.close();
			statement = null;
		}
    }
    
    public Vector<String> sendSQL(String sql) throws SQLException
    {
    	if(sql.substring(0, 6).toLowerCase().equals("select")) //select문 �?�경우
    	{
    		return sendSelectSQL(sql);
    	}
    	else
    	{
    		Vector<String> result = new Vector<String>();
    		result.add(sendOtherSQL(sql)+"");
    		return result;
    	}
    }

    public Vector<String> sendSelectSQL(String sql) throws SQLException
    {
    	ResultSet resultSet=statement.executeQuery(sql); //executeQuery 메서드를 통한 DB 정보를 ResultSet 반환
    	Vector<String> resultData = new Vector<String>();
    	String record;
		while(resultSet.next())
		{
			record = resultSet.getString(1); //첫번째 column�?� data를 가져옴
			resultData.add(record);
		}
		resultSet.close();
		return resultData;
    }
    
    public int sendOtherSQL(String sql) throws SQLException
    {
    	return statement.executeUpdate(sql);
    }

    public void commit() throws SQLException
    {
		connection.commit();
	}
}