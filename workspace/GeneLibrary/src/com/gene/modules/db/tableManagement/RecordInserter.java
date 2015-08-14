package com.gene.modules.db.tableManagement;

import java.sql.SQLException;
import java.util.HashSet;





import org.apache.log4j.Logger;

import com.gene.modules.check.Check;
import com.gene.modules.db.SQLExecutor.OldSQLExecutor;
import com.gene.modules.db.tableManagement.data.Column;
import com.gene.modules.db.tableManagement.data.Record;
import com.gene.modules.db.tableManagement.info.TableInfo;




public class RecordInserter
{
	private static final Logger logger = Logger.getLogger(RecordInserter.class);
	
	private static final int COLUMN_NAME = 0;
	private static final int VALUE =1;
	private static final HashSet<String> ignorableValue;
	static
	{
		ignorableValue = new HashSet<String>();
		ignorableValue.add("");
		ignorableValue.add("-1");
		ignorableValue.add("NULL");
	}
	
	private OldSQLExecutor sqlExecutor;
	private TableInfo tableInfo;
	
	public synchronized OldSQLExecutor getSqlExecutor()
	{
		return this.sqlExecutor;
	}

	public synchronized void setSqlExecutor(OldSQLExecutor sqlExecutor)
	{
		this.sqlExecutor = sqlExecutor;
	}

	public synchronized TableInfo getTableInfo()
	{
		return this.tableInfo;
	}

	public synchronized void setTableInfo(TableInfo tableInfo)
	{
		this.tableInfo = tableInfo;
	}


	/**
	 * 
	 * @param record
	 * @return NULL if there is no pre-existing record.
	 * @throws ClassNotFoundException 
	 * @throws SQLException
	 * @throws InstanceNotExistException
	 * @throws InterruptedException
	 */
	public synchronized Record getExistingRecord(Record record) throws ClassNotFoundException, InterruptedException, SQLException 
	{
		if(this.sqlExecutor == null)
		{
			throw new IllegalArgumentException("dbHandler should not be null");
		}
		if(this.tableInfo == null)
		{
			throw new IllegalArgumentException("tableName should not be null");
		}
		if(record == null)
		{
			throw new IllegalArgumentException("record should not be null");
		}
		
		String[][] result = null;
		String select_sql = this.generateSelectSQL(record);
		if(select_sql != null)
		{
			result = (String[][]) this.sqlExecutor.execute(select_sql);
		}
		
		Record existingRecord = null;
		if((result != null)&&(result.length > 1))
		{
			existingRecord = new Record();
			for(int i=0; i<result[COLUMN_NAME].length; ++i)
			{
				existingRecord.putColumn(result[COLUMN_NAME][i], result[VALUE][i]);
			}
		}

		return existingRecord;
	}
	
	
	
	
	private synchronized boolean recordAlreadyExists(Record record) throws SQLException, ClassNotFoundException, InterruptedException 
	{
		if(this.sqlExecutor == null)
		{
			throw new IllegalArgumentException("dbHandler should not be null");
		}
		if(this.tableInfo == null)
		{
			throw new IllegalArgumentException("tableName should not be null");
		}
		if(record == null)
		{
			throw new IllegalArgumentException("record should not be null");
		}
		
		boolean exist = false;
		String select_sql = this.generateSelectSQL(record);
		if(select_sql != null)
		{
			String[][] result = (String[][]) this.sqlExecutor.execute(select_sql);
			exist = (result.length > 1);
		}

		return exist;
	}
	
	

	/**
	 * 
	 * @param newRecord
	 * @param existingRecord
	 * @return Never returns NULL
	 */
	private Record mergeRecord(Record newRecord, Record existingRecord)
	{
		Record existingtRecord = existingRecord.clone();
		Column[] newRecordColumns = newRecord.getEveryColumn();
		for(int i=0; i<newRecordColumns.length; ++i)
		{
			String columnName = newRecordColumns[i].getColumnName();
			String newValue = newRecordColumns[i].getValue()+"";
			String existingValue = existingtRecord.getColumn(columnName).getValue()+"";
			
			if((!ignorableValue.contains(newValue))&&(!Check.equals(newValue, existingValue)))
			{
				existingtRecord.putColumn(newRecordColumns[i].getColumnName(), newRecordColumns[i].getValue());
			}
		}
		
		return existingtRecord;
	}
	
	
	

	public synchronized void insertToDB(Record record) throws SQLException, ClassNotFoundException, InterruptedException 
	{
//		String sql = null;
//		
//		if(this.recordAlreadyExists(record))
//		{
//			Record tempRecordData = new Record(this.tableInfo);
//			RecordElement[]  elements = record.getEveryRecordElement();
//			for(int i=0; i<elements.length; ++i)
//			{
//				String columnName = elements[i].getColumnName();
//				String value = elements[i].getValue();
//				
//				if((this.tableInfo.isPrimaryKey(columnName))||(!ignorableValue.contains(value)))
//				{
//					tempRecordData.putRecordElement(columnName, elements[i].getValue());
//				}
//			}
//			sql = this.generateUpdateSQL(tempRecordData);
//		}
//		else
//		{
//			sql = this.generateInsertSQL(record);
//		}
//		
//		logger.info(sql);
//		
//		this.sqlExecutor.execute(sql);
		
		boolean insertSuccessful = true;
		String sql = this.generateInsertSQL(record);
		try
		{
			logger.info(sql);
			this.sqlExecutor.execute(sql);
		}
		catch (Exception e)
		{
			insertSuccessful = false;
		} 
		
		if(!insertSuccessful)
		{
			logger.info("Insertion failed -- trying to update existing record"+record);
			Record tempRecordData = new Record(this.tableInfo);
			Column[]  elements = record.getEveryRecordElement();
			for(int i=0; i<elements.length; ++i)
			{
				String columnName = elements[i].getColumnName();
				String value = elements[i].getValue();
				
				if((this.tableInfo.isPrimaryKey(columnName))||(!ignorableValue.contains(value)))
				{
					tempRecordData.putRecordElement(columnName, elements[i].getValue());
				}
			}
			sql = this.generateUpdateSQL(tempRecordData);
			logger.info(sql);
			this.sqlExecutor.execute(sql);
		}
	}
	

	/**
	 * 
	 * @param record
	 * @return Never returns Null.
	 */
	private synchronized String[][] recordToArray(Record record)
	{
		Column[] columns = record.getEveryColumn();
		String[][] columnNameAndValueSet = new String[2][columns.length];
		
		for(int i=0; i<columns.length; ++i)
		{
			columnNameAndValueSet[COLUMN_NAME][i] = columns[i].getColumnName();
		}
		
		for(int i=0; i<columns.length; ++i)
		{
			columnNameAndValueSet[VALUE][i] = columns[i].getValue();
		}
		
		return columnNameAndValueSet;
	}
	
	
	
	private synchronized Record extractPrimaryRecord(Record record)
	{
		Record primaryRecord = new Record();
		Column[]  columns = record.getEveryColumn();
		for(int i=0; i<columns.length; ++i)
		{
			String columnName = columns[i].getColumnName();
			if(this.tableInfo.isPrimaryKey(columnName))
			{
				primaryRecord.putColumn(columnName, columns[i].getValue());
			}
		}
		
		return primaryRecord;
	}
	
	
	
	private synchronized String generateSelectSQL(Record record)
	{
		String[] columnNameSet = this.recordToArray(record)[COLUMN_NAME];
		Record primaryRecord = this.extractPrimaryRecord(record); 
		String[][] primaryRecordArray = this.recordToArray(primaryRecord);
		String[] primaryColumnNameSet = primaryRecordArray[COLUMN_NAME];
		String[] primaryValueSet = primaryRecordArray[VALUE];
		
		String sql = null;
		if((columnNameSet.length > 0)&&(primaryColumnNameSet.length > 0))
		{
			sql = "select ";
			for(int i=0; i<columnNameSet.length; ++i)
			{
				sql += columnNameSet[i];
				if(i < (columnNameSet.length-1))
				{
					sql += ", ";
				}
			}
			sql += " from "+this.tableInfo.getTableName()+" where ";
			for(int i=0; i<primaryColumnNameSet.length; ++i)
			{
				sql += primaryColumnNameSet[i]+"="+(Check.isNumeric(primaryValueSet[i]) ? primaryValueSet[i] : "\'"+primaryValueSet[i]+"\'");
				if(i < (primaryValueSet.length-1))
				{
					sql += " and ";
				}
			}
			sql = sql.toUpperCase();
		}

		return sql;
	}
	
	
	
	private synchronized String generateInsertSQL(Record record)
	{
		String[] columnNameSet = this.recordToArray(record)[COLUMN_NAME];
		String[] valueSet = this.recordToArray(record)[VALUE];
		
		String sql = null;
		if(columnNameSet.length >0)
		{
			sql = "insert into "+this.tableInfo.getTableName()+" (";
			for(int i=0; i<columnNameSet.length; ++i)
			{
				sql += columnNameSet[i];
				if(i < (columnNameSet.length-1))
				{
					sql += ", ";
				}
			}
			sql += ") values (";
			for(int i=0; i<valueSet.length; ++i)
			{
				sql += "\'"+valueSet[i]+"\'";
				if(i < (valueSet.length-1))
				{
					sql += ", ";
				}
			}
			sql += ")";
			
			sql = sql.toUpperCase();
		}
		
		return sql;
	}
	
	
	
	private synchronized String generateDeleteSQL(Record record)
	{
		Record primaryRecord = this.extractPrimaryRecord(record);
		String[] primaryColumnNameSet = this.recordToArray(primaryRecord)[COLUMN_NAME];
		String[] primaryValueSet = this.recordToArray(primaryRecord)[VALUE];
		
		String sql = null;
		if(primaryColumnNameSet.length > 0)
		{
			sql = "delete from "+this.tableInfo.getTableName()+" where ";
			for(int i=0; i<primaryColumnNameSet.length; ++i)
			{
					sql += primaryColumnNameSet[i]+"="+(StringUtils.isNumeric(primaryValueSet[i]) ? primaryValueSet[i] : "\'"+primaryValueSet[i]+"\'");
					if(i < (primaryColumnNameSet.length-1))
					{
						sql += " and ";
					}
			}
			sql = sql.toUpperCase();
		}
		
		return sql;
	}
	
	
	
	
	private final synchronized String generateUpdateSQL(Record record) 
	{
		String[] columnNameSet = this.recordToArray(record)[COLUMN_NAME];
		String[] valueSet = this.recordToArray(record)[VALUE];
		
		Record primaryRecord = this.extractPrimaryRecord(record);
		String[] primaryColumnNameSet = this.recordToArray(primaryRecord)[COLUMN_NAME];
		String[] primaryValueSet = this.recordToArray(primaryRecord)[VALUE];
		
		String sql = null;
		if((columnNameSet.length > 0)&&(primaryColumnNameSet.length > 0))
		{
			sql = "update "+this.tableInfo.getTableName()+" set ";
			for(int i=0; i<columnNameSet.length; ++i)
			{
				sql += columnNameSet[i]+"= \'"+valueSet[i]+"\'";
				if(i < (columnNameSet.length-1))
				{
					sql += ", ";
				}
			}
			sql += " where ";
			
			for(int i=0; i<primaryColumnNameSet.length; ++i)
			{
				sql += primaryColumnNameSet[i]+"="+(StringUtils.isNumeric(primaryValueSet[i]) ? primaryValueSet[i] : "\'"+primaryValueSet[i]+"\'");
				if(i < (primaryValueSet.length-1))
				{
					sql += " and ";
				}
			}
			
			sql = sql.toUpperCase();
		}
		
		return sql;
	}
	
	public static void main(String[] args)
	{
	}
}