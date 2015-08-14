package com.gene.modules.simpleTest;


import java.sql.*;
import java.util.Vector;

import org.h2.jdbcx.JdbcConnectionPool;



public class H2DBTest
{
	public static void test1() throws Exception
	{
		Class.forName("org.h2.Driver");
		Connection conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
		conn.setAutoCommit(true);
		Statement statement = conn.createStatement();
 
		String sql = "select * from TEST";
		ResultSet resultSet = statement.executeQuery(sql);
		ResultSetMetaData metadata = resultSet.getMetaData();
		int numOfColumns = metadata.getColumnCount();
		Vector<String> resultData = new Vector<String>();
		String record="";
		System.out.println("numOfColumns: "+numOfColumns);
		while(resultSet.next())
		{
			for(int i=1; i<=numOfColumns; ++i)
			{
				record += resultSet.getString(i);
				if(i < numOfColumns)
				{
					record +=",";
				}
			}
			resultData.add(record);
			record = "";
			System.out.println(resultData.lastElement());
		}
		
//		sql = "insert into TEST values(3,'bb')";
//		statement.executeUpdate(sql);
		
		statement.close();
		conn.close();
	}
	
	public static void test2() throws Exception
	{
		JdbcConnectionPool cp = JdbcConnectionPool.create("jdbc:h2:~/test", "sa", "");
		String[] sqls = new String[3];
		sqls[0] = "insert into TEST values(4, 'ww')";
		sqls[1] = "insert into TEST values(5, 'tt')";
		sqls[2] = "insert into TEST values(6, 'jk')";
		
        for (int i = 0; i < sqls.length; i++)
        {
            Connection conn = cp.getConnection();
            conn.createStatement().execute(sqls[i]);
            conn.close();
        }
        cp.dispose();
    }
	
	public static void main(String[] args) throws Exception
	{
		H2DBTest.test1();
	}
}