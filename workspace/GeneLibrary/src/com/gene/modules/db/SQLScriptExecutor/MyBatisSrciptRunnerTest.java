package com.gene.modules.db.SQLScriptExecutor;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;



//driver=oracle.jdbc.driver.OracleDriver
//URL=jdbc:oracle:thin:@localhost:1521/xe
//username=sdc-local
//password=o+7chrl1bqw=
//passwordEncrypted=true
//maxConnection=1



public class MyBatisSrciptRunnerTest
{
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException
	{
		Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/xe", "sdc-local", "1234");
        Reader reader = Resources.getResourceAsReader("com/gene/resource/aaa.sql");

        ScriptRunner runner = new ScriptRunner(conn);
//        runner.setDelimiter("]");
        runner.setLogWriter(null);
        runner.setErrorLogWriter(null);
        runner.runScript(reader);
        conn.commit();
        reader.close();
	}
}
