package org.gene.modules.database.dbUtils;

import java.util.List;

import org.gene.modules.check.Check;
import org.gene.modules.database.dbUtils.Constants.DB;


public class DBUtils
{
	public static String getURL(Integer dbType, String hostname, int port, String dbOrServiceName)
	{
		String URL = null;
		if(Check.isNotBlank(dbType, hostname, dbOrServiceName))
		{
			if(dbType == DB.Type.postgresql)
			{
				URL = "jdbc:postgresql://"+hostname+":"+port+"/"+dbOrServiceName;
			}
			else if(dbType == DB.Type.oracle)
			{
				URL = "jdbc:oracle:thin:@"+hostname+":"+port+"/"+dbOrServiceName;
			}
		}
		
		
		return URL;
	}
	

	public static Integer getDBType(String str)
	{
		Integer databaseType = null;
		if(str!=null && !"".equals(str))
		{
			str = str.toLowerCase();
			if(str.contains("postgresql"))
			{
				databaseType = DB.Type.postgresql;
			}
			else if(str.contains("oracle"))
			{
				databaseType = DB.Type.oracle;
			}
		}
		
		return databaseType;
	}
	
	
	
	public static String getDriver(String str)
	{
		String driver = null;
		if(str!=null && !"".equals(str))
		{
			Integer dbType = getDBType(str);
			driver = getDriver(dbType);
		}
		
		
		return driver;
	}
	
	
	public static String getDriver(Integer dbType)
	{
		String driver = null;
		if(dbType != null)
		{
			if(dbType == DB.Type.postgresql)
			{
				driver = DB.Driver.postgresql;
			}
			else if(dbType == DB.Type.oracle)
			{
				driver = DB.Driver.oracle;
			}
		}
		
		return driver;
	}
	
	
	@SuppressWarnings("rawtypes")
	public static void showResult(Object result)
	{
		if(result instanceof List<?>)
		{
			for(int i=0; i<((List)result).size(); ++i)
	    	{
				Object obj = ((List)result).get(i);
				if(obj instanceof String[])
				{
					for(int j=0; j<((String[])obj).length; j++)
					{
						System.out.print(((String[])obj)[j]+"\t");
					}
					System.out.print("\n");
				}
	    	}
		}
		else if(result instanceof String[][])
		{
			String[][] res = (String[][]) result;
			for(int i=0; i<res.length; ++i)
	    	{
	    		for(int j=0; j<res[i].length; j++)
	    		{
	    			System.out.print(res[i][j]+"\t");
	    		}
	    		System.out.print("\n");
	    	}
		}
		else if(result instanceof Integer)
		{
			Integer num = (Integer) result;
			System.out.println(num);
		}
	}
}
