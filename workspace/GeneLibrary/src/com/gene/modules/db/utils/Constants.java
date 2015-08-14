package com.gene.modules.db.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Constants
{
	public static class DB
	{
		public static class Settings
		{
			private static String DB_SETTING_DIRECTORY = "src/com/gene/modules/db/setting";
			private static String DB_SETTING_FILENAME = "database.properties";
			private static String GLOBAL_SETTING_DIRECTORY = "settings";
			private static String[] DB_PROPERTIES_PATH = 
				{
					DB_SETTING_DIRECTORY+"/"+DB_SETTING_FILENAME, 
					GLOBAL_SETTING_DIRECTORY+"/"+DB_SETTING_FILENAME
				};
			
			public static String getDBPropertiesPath()
			{
				String path = null;
				for(int i=0; i<DB_PROPERTIES_PATH.length; ++i)
				{
					path = DB_PROPERTIES_PATH[i];
					if(exists(path))
					{
						break;
					}
					else
					{
						path = null;
					}
				}
				
				return path;
			}
			
			private static synchronized boolean exists(String fileName)
			{
				boolean fileExists = false;
				if((fileName != null)&&(!"".equals(fileName)))
				{
					fileExists = true;
					FileReader fr = null;
					try
					{
						fr = new FileReader(fileName);
					}
					catch (FileNotFoundException e)
					{
						fileExists = false;
					}
					
					if(fr != null)
					{
						try
						{
							fr.close();
						}
						catch (IOException e){}
						fr = null;
					}
				}
				
				return fileExists;
			}
		}
		
		public static class Type
		{
			public final static int oracle = 0;
			public final static int postgresql = 1;
		}
		
		public static class SQL
		{
			public static class Postgresql
			{
				public final static String columnInfoSQL = 
						"SELECT column_name, data_type, character_maximum_length, is_nullable "+ 
						"FROM information_schema.columns c, information_schema.tables t "+
						"WHERE t.table_schema = '?' AND t.table_name = c.table_name AND c.table_name = '?'";

				public final static String primaryKeyCheckSQL =
						"SELECT kc.column_name "+
						"FROM "+
							"information_schema.table_constraints tc "+
							"join information_schema.key_column_usage kc "+
							"ON kc.table_name = tc.table_name AND kc.table_schema = tc.table_schema "+
						"WHERE "+
							"tc.constraint_type = 'PRIMARY KEY' AND "+
							"tc.table_schema = '?' AND "+
							"tc.table_name = '?' AND "+
							"kc.column_name = '?'";
				
				public final static String tableExistCheckSQL = "SELECT table_name FROM information_schema.tables WHERE table_name = '?'";
				public final static String dropTableSQL = "DROP TABLE ?";
			}
			
			public static class Oracle
			{
				public final static String columnInfoSQL = 
						"SELECT column_name, data_type, data_length, nullable "+
						"FROM all_tab_cols "+
						"WHERE owner='?' AND table_name ='?'";

				public final static String primaryKeyCheckSQL = 
							"SELECT "+
								"cols.column_name "+
							"FROM all_constraints cons, all_cons_columns cols "+
							"WHERE "+
								"cols.constraint_name = cons.constraint_name AND "+
								"cons.constraint_type = 'P' AND "+
								"cols.owner = cons.owner and cols.owner = '?' AND "+
								"cols.table_name = '?' AND "+
								"cols.column_name = '?' ";
				
				public final static String tableExistCheckSQL = "SELECT table_name FROM user_tables WHERE table_name = '?'";
				public final static String grantSelectSQL = "GRANT SELECT ON ? TO PUBLIC";
				public final static String dropTableCascadeSQL = "DROP TABLE ? CASCADE CONSTRAINTS";
				public final static String dropTableSQL = "DROP TABLE ?";
				public final static String sql_lastUpdateTime = "SELECT max(?) from ?";
			}
		}

		public static class Variable
		{
			public static class Postgresql
			{
				public final static String nullableNo = "NO";
			}
			
			public static class Oracle
			{
				public final static String nullableNo = "N";
			}
		}
	
		public static class Driver
		{
			public static final String oracle = "oracle.jdbc.OracleDriver";
			public static final String postgresql = "org.postgresql.Driver";
		}
	}
}
