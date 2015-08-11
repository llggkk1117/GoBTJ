package org.gene.dao;

import java.io.IOException;
import java.sql.SQLException;

import org.gene.modules.database.db.Database;

public class AbstractDAO
{
	protected static Database db;
	static
	{
		try
		{
			db = new Database();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	protected AbstractDAO(){}
	
	protected void finalize() throws Throwable
	{
	    try
	    {
	    	AbstractDAO.db.close();
	    }
	    finally
	    {
	        super.finalize();
	    }
	}
}
