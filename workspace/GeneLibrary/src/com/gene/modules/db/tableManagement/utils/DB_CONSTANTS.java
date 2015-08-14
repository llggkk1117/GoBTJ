package com.gene.modules.db.tableManagement.utils;

public class DB_CONSTANTS
{
//	public static class DB_TYPE
//	{
//		public final static int ORACLE = 0;
//		public final static int POSTGRESQL = 1;
//	}
//	
//	public static class POSTGRESQL
//	{
//		public final static String columnInfoSQL = 
//				"SELECT column_name, data_type, character_maximum_length, is_nullable "+ 
//				"FROM information_schema.columns c, information_schema.tables t "+
//				"WHERE t.table_schema = '?' AND t.table_name = c.table_name AND c.table_name = '?'";
//
//		public final static String primaryKeyCheckSQL =
//				"SELECT kc.column_name "+
//				"FROM "+
//					"information_schema.table_constraints tc "+
//					"join information_schema.key_column_usage kc "+
//					"ON kc.table_name = tc.table_name AND kc.table_schema = tc.table_schema "+
//				"WHERE "+
//					"tc.constraint_type = 'PRIMARY KEY' AND "+
//					"tc.table_schema = '?' AND "+
//					"tc.table_name = '?' AND "+
//					"kc.column_name = '?'";
//		
//		public final static String nullableNo = "NO";
//		
//		public final static String tableExistCheckSQL = "SELECT table_name FROM information_schema.tables WHERE table_name = '?'";
//		public final static String dropTableSQL = "DROP TABLE ?";
//	}
//	
//	public static class ORACLE
//	{
//		public final static String columnInfoSQL = 
//				"SELECT column_name, data_type, data_length, nullable "+
//				"FROM all_tab_cols "+
//				"WHERE owner='?' AND table_name ='?'";
//
//		public final static String primaryKeyCheckSQL = 
//					"SELECT "+
//						"cols.column_name "+
//					"FROM all_constraints cons, all_cons_columns cols "+
//					"WHERE "+
//						"cols.constraint_name = cons.constraint_name AND "+
//						"cons.constraint_type = 'P' AND "+
//						"cols.owner = cons.owner and cols.owner = '?' AND "+
//						"cols.table_name = '?' AND "+
//						"cols.column_name = '?' ";
//		
//		public final static String nullableNo = "N";
//		
//		public final static String tableExistCheckSQL = "SELECT table_name FROM user_tables WHERE table_name = '?'";
//		public final static String grantSelectSQL = "GRANT SELECT ON ? TO PUBLIC";
//		public final static String dropTableCascadeSQL = "DROP TABLE ? CASCADE CONSTRAINTS";
//		public final static String dropTableSQL = "DROP TABLE ?";
//		public final static String sql_lastUpdateTime = "SELECT max(?) from ?";
//	}
}
