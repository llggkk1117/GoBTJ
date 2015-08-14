package com.gene.modules.db.SQLAdapter;

import java.sql.SQLException;

import com.gene.modules.exceptions.MissingDependancyException;
import com.gene.modules.check.Check;
import com.gene.modules.db.SQLExecutor.OldSQLExecutor;
import com.gene.modules.db.SQLExecutor.SQLExecutorFactory;




public class TableRecordInserter
{
	private OldSQLExecutor sqlExecutor;
	
	public void setSQLExecutor(OldSQLExecutor sqlExecutor)
	{
		if(sqlExecutor == null)
		{
			throw new IllegalArgumentException();
		}
		
		this.sqlExecutor = sqlExecutor;
	}
	
	public OldSQLExecutor getSQLExecutor()
	{
		return this.getSQLExecutor();
	}
	
	
	private static synchronized String[][] select(TableRecord tableRecord, OldSQLExecutor sqlExecutor) throws SQLException, InterruptedException, ClassNotFoundException
	{
		if(tableRecord == null)
		{
			throw new IllegalArgumentException();
		}
		if(tableRecord.isEmpty())
		{
			throw new IllegalArgumentException();
		}
		if(sqlExecutor == null)
		{
			throw new IllegalArgumentException();
		}
		

		String select_sql = TableRecordInserter.generateSelectSQL(tableRecord);
		System.out.println(select_sql);

		sqlExecutor.openSession();
		String[][] result = (String[][]) sqlExecutor.execute(select_sql);
		sqlExecutor.closeSession();
		
		return result;
	}
	
	
	
	
	private synchronized String[][] select(TableRecord tableRecord) throws SQLException, InterruptedException, ClassNotFoundException
	{
		if(tableRecord == null)
		{
			throw new IllegalArgumentException();
		}
		if(tableRecord.isEmpty())
		{
			throw new IllegalArgumentException();
		}
		if(this.sqlExecutor == null)
		{
			throw new MissingDependancyException();
		}
		
		
		return TableRecordInserter.select(tableRecord, this.sqlExecutor);
	}
	
	
	
	
	
	/**
	 *  This method searchs to see if there is a table record which has the same primary keys as given tableRecord's.
	 *  If given tableRecord does not have any primary key, this method searchs to see if there is exactly the same record as given tableRecord.
	 * @param tableRecord - given tableRecord
	 * @param sqlExecutor - this method searchs in database by using this sqlExecutor
	 * @return true when the same primary key record exists; false when not exists; 
	 *            null if given tableRecord does not have its tableInfo, or any column name in tableRecord is incorrect
	 * @throws IllegalArgumentException when given tableRecord is null or empty, or given sqlExecutor is null
	 * @throws SQLException
	 * @throws InterruptedException
	 * @throws ClassNotFoundException 
	 */
	private static synchronized Boolean checkSamePrimaryKeyRecordAlreadyExists(TableRecord tableRecord, OldSQLExecutor sqlExecutor) throws SQLException, InterruptedException, ClassNotFoundException
	{
		if(tableRecord == null)
		{
			throw new IllegalArgumentException();
		}
		if(tableRecord.isEmpty())
		{
			throw new IllegalArgumentException();
		}
		if(sqlExecutor == null)
		{
			throw new IllegalArgumentException();
		}
		
		
		Boolean recordAlreadyExists = null;
		TableRecord primaryKeyRecord = TableRecordInserter.extractPrimaryKeyColumnRecord(tableRecord);
		
		
		if(primaryKeyRecord != null)
		{
			recordAlreadyExists = false;
			if(!primaryKeyRecord.isEmpty())
			{
				if(TableRecordInserter.select(primaryKeyRecord, sqlExecutor).length > 0)
				{
					recordAlreadyExists = true;
				}
			}
			else
			{
				if(TableRecordInserter.select(tableRecord, sqlExecutor).length > 0)
				{
					recordAlreadyExists = true;
				}
			}
		}
		
		
		System.out.println("recordAlreadyExists: "+recordAlreadyExists);
		
		return recordAlreadyExists;
	}
	
	
	
	
	
	private static synchronized Boolean containsEveryNotNullColumn(TableRecord tableRecord)
	{
		Boolean containsEveryNotNullColumn = null;
		
		TableInfo tableInfo = tableRecord.getTableInfo();
		if(tableInfo != null)
		{
			containsEveryNotNullColumn = true;
			
			String[] notNullcolumnNameSet = tableInfo.getNotNullColumnNames();
			for(int i=0; i<notNullcolumnNameSet.length; ++i)
			{
				TableColumnRecord tableColumnRecord = tableRecord.getTableColumnRecord(notNullcolumnNameSet[i]);
				if(tableColumnRecord == null)
				{
					containsEveryNotNullColumn = false;
					break;
				}
			}
		}
		
		return containsEveryNotNullColumn;
	}
	
	
	
	
	private static synchronized Boolean containsEveryPrimaryKeyColumn(TableRecord tableRecord)
	{
		Boolean containsEveryPrimaryKeyColumn = null;
		
		TableInfo tableInfo = tableRecord.getTableInfo();
		if(tableInfo != null)
		{
			containsEveryPrimaryKeyColumn = true;
			
			String[] primaryKeycolumnNameSet = tableInfo.getPrimaryKeyColumnNames();
			for(int i=0; i<primaryKeycolumnNameSet.length; ++i)
			{
				TableColumnRecord tableColumnRecord = tableRecord.getTableColumnRecord(primaryKeycolumnNameSet[i]);
				if(tableColumnRecord == null)
				{
					containsEveryPrimaryKeyColumn = false;
					break;
				}
			}
		}
		
		return containsEveryPrimaryKeyColumn;
	}
	
	
	
	
	
	
	/**
	 * This method inserts given tableRecord into the table by using given sqlExecutor.  
	 * @param tableRecord which this method inserts to database. This argument should not be null or empty.
	 * @param sqlExecutor which this method use for inserting given tableRecord. This argument should not be null.
	 * @return either the row count for INSERT statements, or 0 for SQL statements that return nothing
	 * @throws SQLException
	 * @throws InterruptedException
	 * @throws ClassNotFoundException 
	 */
	private static synchronized int insert(TableRecord tableRecord, OldSQLExecutor sqlExecutor) throws SQLException, InterruptedException, ClassNotFoundException
	{
		if(tableRecord == null)
		{
			throw new IllegalArgumentException();
		}
		if(tableRecord.isEmpty())
		{
			throw new IllegalArgumentException();
		}
		if(sqlExecutor == null)
		{
			throw new IllegalArgumentException();
		}
		
		
		
		Boolean containsEveryNotNullColumn = TableRecordInserter.containsEveryNotNullColumn(tableRecord);
		if((containsEveryNotNullColumn != null)&&(!containsEveryNotNullColumn))
		{
			throw new IllegalArgumentException();
		}

		
		Boolean recordAlreadyExists = TableRecordInserter.checkSamePrimaryKeyRecordAlreadyExists(tableRecord, sqlExecutor);
		if((recordAlreadyExists != null) && recordAlreadyExists)
		{
			throw new IllegalArgumentException();
		}
		
		
		String sql = TableRecordInserter.generateInsertSQL(tableRecord);
//		System.out.println("-----> "+sql);
		
//		sqlExecutor.openSession();
		Integer result = (Integer) sqlExecutor.execute(sql);
		sqlExecutor.commit();
//		sqlExecutor.closeSession();
	
//		int result = 0;
//		if((Validator.isNotBlank(resultTemp[0][0]))&&(Validator.isNumeric(resultTemp[0][0])))
//		{
//			result = Integer.parseInt(resultTemp[0][0]);
//		}
		
		return result;
	}
	
	
	
	
	private static synchronized int insertForce(TableRecord tableRecord, OldSQLExecutor sqlExecutor) throws SQLException, InterruptedException, ClassNotFoundException
	{
		if(tableRecord == null)
		{
			throw new IllegalArgumentException();
		}
		if(tableRecord.isEmpty())
		{
			throw new IllegalArgumentException();
		}
		if(sqlExecutor == null)
		{
			throw new IllegalArgumentException();
		}
		
		Boolean containsEveryNotNullColumn = TableRecordInserter.containsEveryNotNullColumn(tableRecord);
		if((containsEveryNotNullColumn != null)&&(!containsEveryNotNullColumn))
		{
			throw new IllegalArgumentException();
		}
		
		
		Boolean recordAlreadyExists = TableRecordInserter.checkSamePrimaryKeyRecordAlreadyExists(tableRecord, sqlExecutor);
		String sql = null;
		if((recordAlreadyExists == null)||(!recordAlreadyExists))
		{
			sql = TableRecordInserter.generateInsertSQL(tableRecord);
		}
		else
		{
			sql = TableRecordInserter.generateUpdateSQL(tableRecord);
		}
		System.out.println(sql);
		
//		sqlExecutor.openSession();
		Integer result = (Integer) sqlExecutor.execute(sql);
		sqlExecutor.commit();
//		sqlExecutor.closeSession();
		
		
//		int result = 0;
//		if((Validator.isNotBlank(resultTemp[0][0]))&&(Validator.isNumeric(resultTemp[0][0])))
//		{
//			result = Integer.parseInt(resultTemp[0][0]);
//		}
		
		
		
		return result;
	}
	
	
	
	
	public synchronized int insert(TableRecord tableRecord) throws SQLException, InterruptedException, ClassNotFoundException 
	{
		if(tableRecord == null)
		{
			throw new IllegalArgumentException();
		}
		if(tableRecord.isEmpty())
		{
			throw new IllegalArgumentException();
		}
		if(this.sqlExecutor == null)
		{
			throw new MissingDependancyException();
		}
		
		
		return TableRecordInserter.insert(tableRecord, this.sqlExecutor);
	}
	
	

	

	public synchronized int insertForce(TableRecord tableRecord) throws SQLException, InterruptedException, ClassNotFoundException 
	{
		if(tableRecord == null)
		{
			throw new IllegalArgumentException();
		}
		if(tableRecord.isEmpty())
		{
			throw new IllegalArgumentException();
		}
		if(this.sqlExecutor == null)
		{
			throw new MissingDependancyException();
		}
		
		
		return TableRecordInserter.insertForce(tableRecord, this.sqlExecutor);
	}

	
	
	
	/**
	 * This method creates a TableRecord object containing only primary key columns and values in given tableRecord. 
	 * @param tableRecord - given tableRecord; it should not be null or empty.
	 * @return newly created TableRecord object containing only primary key columns and values in given tableRecord;
	 *            Return value can be empty if given tableRecord does not have any primary key;
	 *            Return value can be null if given tableRecord does not have its tableInfo, or any column name in tableRecord is incorrect.
	 * @throws IllegalArgumentException if given tableRecord is null or empty
	 */
	private static synchronized TableRecord extractPrimaryKeyColumnRecord(TableRecord tableRecord)
	{
		if(tableRecord == null)
		{
			throw new IllegalArgumentException();
		}
		if(tableRecord.isEmpty())
		{
			throw new IllegalArgumentException();
		}
		
		
		boolean validCondition = false;
		TableRecord tempPrimaryKeyRecord = null;
		
		TableInfo tableInfo = tableRecord.getTableInfo();
		if(tableInfo != null)
		{
			validCondition = true;
			tempPrimaryKeyRecord = new TableRecord(tableInfo);
			
			String[] primaryKeycolumnNameSet = tableInfo.getPrimaryKeyColumnNames();
			for(int i=0; i<primaryKeycolumnNameSet.length; ++i)
			{
				TableColumnRecord tableColumnRecord = tableRecord.getTableColumnRecord(primaryKeycolumnNameSet[i]);
				if(tableColumnRecord != null)
				{
					tempPrimaryKeyRecord.putTableColumnRecord(primaryKeycolumnNameSet[i], tableColumnRecord.getValue());
				}
				else
				{
					validCondition = false;
					break;
				}
			}
		}
		
		
		TableRecord primaryKeyRecord = null;
		if(validCondition)
		{
			primaryKeyRecord = tempPrimaryKeyRecord;
			
			// For Test
			//System.out.println("=====================");
			//String[] c = primaryKeyRecord.getEveryColumnName();
			//for(int i=0; i<c.length; ++i)
			//{
			//	System.out.println(c[i]+": "+primaryKeyRecord.getTableColumnRecord(c[i]).getValue());
			//}
			//System.out.println("=====================");
		}
		
		return primaryKeyRecord;
	}
	
	

	
	/**
	 * This method creates 'select' sql statement for getting given tableRecord as a result from database.  
	 * The tableName value of given tableRecord is used for generating 'from' clause, and  
	 * the column names and values in given tableRecord are used for generating 'where' clause.
	 * @param tableRecord - given tableRecord; it should not be null or empty.
	 * @return sql statement for getting given tableRecord as a result from database; never be null
	 * @throws IllegalArgumentException if given tableRecord is null or empty
	 */
	private static synchronized String generateSelectSQL(TableRecord tableRecord)
	{
		if(tableRecord == null)
		{
			throw new IllegalArgumentException();
		}
		if(tableRecord.isEmpty())
		{
			throw new IllegalArgumentException();
		}
		
		
		String sql = "SELECT ";
		String[] columnNameSet = tableRecord.getEveryColumnName();
		for(int i=0; i<columnNameSet.length; ++i)
		{
			sql += columnNameSet[i];
			if(i < (columnNameSet.length-1))
			{
				sql += ", ";
			}
		}
		
		sql += " FROM "+tableRecord.getTableName()+" WHERE ";
		
		String value = null;
		for(int i=0; i<columnNameSet.length; ++i)
		{
			value = tableRecord.getTableColumnRecord(columnNameSet[i]).getValue();
			sql += columnNameSet[i]+"="+(TableRecordInserter.isNumeric(value) ? value : "\'"+value+"\'");
			if(i < (columnNameSet.length-1))
			{
				sql += " AND ";
			}
		}
		

		return sql;
	}
	
	
	
	
	
	
	
	
	/**
	 * This method creates 'insert' sql statement for inserting given tableRecord into the table.  
	 * The tableName value of given tableRecord is used for generating 'insert into' clause, and  
	 * the column names and values in given tableRecord are used for generating 'values' clause.
	 * @param tableRecord - given tableRecord; it should not be null or empty.
	 * @return sql statement for inserting given tableRecord to database; never be null
	 * @throws IllegalArgumentException when given tableRecord is null or empty
	 */
	private static synchronized String generateInsertSQL(TableRecord tableRecord)
	{
		if(tableRecord == null)
		{
			throw new IllegalArgumentException();
		}
		if(tableRecord.isEmpty())
		{
			throw new IllegalArgumentException();
		}
		
		
		String sql = "INSERT INTO "+tableRecord.getTableName()+" (";
		String[] columnNameSet = tableRecord.getEveryColumnName();
		for(int i=0; i<columnNameSet.length; ++i)
		{
			sql += columnNameSet[i];
			if(i < (columnNameSet.length-1))
			{
				sql += ", ";
			}
		}
		
		sql += ") VALUES (";
		
		String value = null;
		for(int i=0; i<columnNameSet.length; ++i)
		{
			value = tableRecord.getTableColumnRecord(columnNameSet[i]).getValue();
			sql += ((Check.isNumeric(value)) ? value : "\'"+value+"\'");
			if(i < (columnNameSet.length-1))
			{
				sql += ", ";
			}
		}
		sql += ")";
		

		return sql;
	}
	
	
	
	
	/**
	 * This method creates 'delete' sql statement for removing given tableRecord from database.  
	 * The tableName value of given tableRecord is used for generating 'from' clause, and  
	 * the column names and values in given tableRecord are used for generating 'where' clause.
	 * @param tableRecord - given tableRecord; It should not be null or empty.
	 * @return sql statement for deleting given tableRecord from database; never be null
	 * @throws IllegalArgumentException if given tableRecord is null or empty
	 */
	private static synchronized String generateDeleteSQL(TableRecord tableRecord)
	{
		if(tableRecord == null)
		{
			throw new IllegalArgumentException();
		}
		if(tableRecord.isEmpty())
		{
			throw new IllegalArgumentException();
		}
		
		
		String sql = "DELETE FROM "+tableRecord.getTableName()+" WHERE ";
			
		String[] columnNameSet = tableRecord.getEveryColumnName();
		String value = null;
		for(int i=0; i<columnNameSet.length; ++i)
		{
			value = tableRecord.getTableColumnRecord(columnNameSet[i]).getValue();
			sql += columnNameSet[i]+"="+(TableRecordInserter.isNumeric(value) ? value : "\'"+value+"\'");
			if(i < (columnNameSet.length-1))
			{
				sql += " AND ";
			}
		}
		
		return sql;
	}
	
	
	
	
	
	private static synchronized String generateDeleteSQLwithPrimaryKey(TableRecord tableRecord)
	{
		if(tableRecord == null)
		{
			throw new IllegalArgumentException();
		}
		if(tableRecord.isEmpty())
		{
			throw new IllegalArgumentException();
		}
		TableRecord primaryKeyRecord = TableRecordInserter.extractPrimaryKeyColumnRecord(tableRecord);
		if(primaryKeyRecord == null)
		{
			throw new IllegalArgumentException();
		}
		if(primaryKeyRecord.isEmpty())
		{
			throw new IllegalArgumentException();
		}
		
		
		return TableRecordInserter.generateDeleteSQL(primaryKeyRecord);
	}
	
	
	
	
	/**
	 * This method creates 'update' sql statement for updateing existing record to make the same as given tableRecord.
	 * The tableName value of given tableRecord is used for generating 'update' clause,   
	 * the column names and values in given tableRecord are used for generating 'set' clause,
	 * and primary key column names and values are used for genertating 'where' clause.
	 * If given tableRecord does not have any primary key, the result sql statement updates every record in the table.
	 * @param tableRecord - given tableRecord; it should not be null or empty.
	 * @return update sql statement for given tableRecord; never be null
	 * @throws IllegalArgumentException if given tableRecord is null or empty
	 */
	private static final synchronized String generateUpdateSQL(TableRecord tableRecord)
	{
		if((tableRecord == null)||(tableRecord.isEmpty()))
		{
			throw new IllegalArgumentException();
		}
		Boolean contains = containsEveryPrimaryKeyColumn(tableRecord);
		if((contains == null)|| !contains)
		{
			throw new IllegalArgumentException();
		}
		
		
		String sql = "UPDATE "+tableRecord.getTableName()+" SET ";
		
		String[] columnNameSet = tableRecord.getEveryColumnName();
		String value = null;
		for(int i=0; i<columnNameSet.length; ++i)
		{
			value = tableRecord.getTableColumnRecord(columnNameSet[i]).getValue();
			sql += columnNameSet[i]+"="+(TableRecordInserter.isNumeric(value) ? value : "\'"+value+"\'");

			if(i < (columnNameSet.length-1))
			{
				sql += ", ";
			}
		}
		
		TableRecord primaryKeyRecord = TableRecordInserter.extractPrimaryKeyColumnRecord(tableRecord);
		
		if((primaryKeyRecord != null)&&(!primaryKeyRecord.isEmpty()))
		{
			sql += " WHERE ";
			String[] primaryColumnNameSet = primaryKeyRecord.getEveryColumnName();
			String primaryKeyValue = null;
			for(int i=0; i<primaryColumnNameSet.length; ++i)
			{
				primaryKeyValue = primaryKeyRecord.getTableColumnRecord(primaryColumnNameSet[i]).getValue();
				sql += primaryColumnNameSet[i]+"="+(TableRecordInserter.isNumeric(primaryKeyValue) ? primaryKeyValue : "\'"+primaryKeyValue+"\'");
				if(i < (primaryColumnNameSet.length-1))
				{
					sql += " AND ";
				}
			}
		}

		return sql;
	}
	
	
	
	/**
	 * This method finds out if given string is numeric or not.
	 * @param str - given string; it can be null or empty
	 * @return true if given string is numeric; false if not.
	 */
	private static boolean isNumeric(String str)
	{
		boolean numeric = true;
		try
		{
			Double.parseDouble(str);
		}
		catch(Exception e)
		{
			numeric = false;
		}
		
		return  numeric;
	}
	
	
	

	


	
	

	
	
	public static void main(String[] args) throws Exception
	{
		
		TableRecordInserter insert = new TableRecordInserter();
		insert.setSQLExecutor(SQLExecutorFactory.getInstance());
		
		TableInfo ti = SSDExceptionTableInfoGenerator.getSSDExceptionTableInfo();
		TableRecord tr = new TableRecord(ti);
		tr.putTableColumnRecord("ORGN_ZIP3_CD", "99504");
		tr.putTableColumnRecord("dest_ZIP3_CD", "99512");
		tr.putTableColumnRecord("pri_svc_std_nbr", "3");
		tr.putTableColumnRecord("std_svc_std_nbr", "3");
		tr.putTableColumnRecord("fcm_svc_std_nbr", "3");
		tr.putTableColumnRecord("pkg_svc_std_nbr", "3");
		tr.putTableColumnRecord("per_svc_std_nbr", "3");
		tr.putTableColumnRecord("pmg_svc_std_nbr", "2");
		tr.putTableColumnRecord("fiscal_yr", "99");
		tr.putTableColumnRecord("postal_qtr", "0");
		
		System.out.println(tr.getTableColumnRecord("ORGN_ZIP3_CD").getTableColumnInfo().isPrimaryKey());
		
		String[] e = TableRecordInserter.extractPrimaryKeyColumnRecord(tr).getEveryColumnName();
		for(int i=0; i<e.length; ++i)
		{
			System.out.println(e[i]);
		}
		
		String[][] e1 = insert.select(tr);
		for(int i=0; i<e1.length; ++i)
		{
			System.out.println(e1[i]);
		}
		
		insert.insert(tr);
		
		
//		SQLExecutorAssembler as = new SQLExecutorAssembler();
//		SQLExecutor db = as.getSQLExecutor();
//		
		
//		
//		RecordInserter re = new RecordInserter();
//		re.setSQLExecutor(db);
//		re.setTableInfo(ti);
//		
//		Data data = new Data();
//		data.putDataField("fiscal_year", "12");
//		data.putDataField("postal_quarter", "03");
//		data.putDataField("origin_zip_code", "63146");
//		data.putDataField("dest_zip_code", "63017");
//		data.putDataField("mail_class_code", "PRI");
//		data.putDataField("service_standard", "2");
//		
		
////		
////		tr = re.convertToTableRecord(data);
////		e = tr.getEveryColumnName();
////		for(int i=0; i<e.length; ++i)
////		{
////			System.out.println(e[i]);
////		}
//		
//		re.createTable();
//		TableRecord tr = re.convertToTableRecord(data);
//		re.insert(tr);
		
//		System.out.println(InsertSQLProcessor.generateSelectSQL(ti, null, "  ", "", ""));
		
	}
}
