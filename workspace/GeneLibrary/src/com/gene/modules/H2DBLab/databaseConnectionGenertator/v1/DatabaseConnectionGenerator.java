package com.gene.modules.H2DBLab.databaseConnectionGenertator.v1;

import java.sql.SQLException;

import com.gene.modules.H2DBLab.databaseConnection.DatabaseConnection;

public interface DatabaseConnectionGenerator
{
	public DatabaseConnection getConnection() throws SQLException;
	public void close() throws SQLException;
	public String getURL();
	public String getUsername();
}
