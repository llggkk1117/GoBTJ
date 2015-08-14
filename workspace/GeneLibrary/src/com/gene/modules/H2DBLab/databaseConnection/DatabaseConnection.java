package com.gene.modules.H2DBLab.databaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DatabaseConnection
{
	public void close() throws SQLException;
	public ResultSet executeQuery(String sql) throws SQLException;
	public Integer executeUpdate(String sql) throws SQLException;
	public void commit() throws SQLException;
}
