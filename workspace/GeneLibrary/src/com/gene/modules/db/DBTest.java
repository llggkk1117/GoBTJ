package com.gene.modules.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBTest 
{
	public static void main(String[] args) throws SQLException, ClassNotFoundException 
	{
		Class.forName("org.postgresql.Driver");
		String url = "jdbc:postgresql://localhost:5432/postgres";
		Properties props = new Properties();
		props.setProperty("user","postgres");
		props.setProperty("password","1234");
		Connection conn = DriverManager.getConnection(url, props);
		Statement stmt = conn.createStatement();
		stmt.execute("create table aaa (col1 text, col2 text)");
		stmt.close();
		conn.close();
	}
}
