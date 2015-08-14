package com.gene.modules.simpleTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCTest
{
	public static boolean loadDriver(final String driver)
	{
		boolean successful = true;
		try
		{
			Class.forName(driver);
		}
		catch (ClassNotFoundException e)
		{
			successful = false;
		}
		
		return successful;
	}
	
	
	
	
	public static void test1() 
	{
		String driver = "oracle.jdbc.OracleDriver";
		String URL = "jdbc:oracle:thin:@localhost:1521/xe";
		String username = "sdc-local";
		String password = "sdc";
				
		
        

        try {
			connection.setAutoCommit(false);
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        
        
        Connection connection;
        Statement statement;

        try
        {
        	connection = DriverManager.getConnection(URL, username, password);
        	statement = connection.createStatement();
        	ResultSet rs = statement.executeQuery("select * from demo_users");
        	int numOfcolumns = rs.getMetaData().getColumnCount();
        	
        	while(rs.next())
        	{
        		String row = null;
        		for(int i=1; i<=numOfcolumns; ++i)
        		{
        			row += rs.getObject(i);
        			if(i != numOfcolumns)
        			{
        				row += ", ";
        			}
        			else
        			{
        				row += "\n";
        			}
        		}
        		System.out.println(row);
        	}
        }
        catch(SQLException e){}
        finally
        {
        	try
        	{
				connection.close();
			}
        	catch (SQLException e){}
        }
	}


	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{
		test1();
	}
}
