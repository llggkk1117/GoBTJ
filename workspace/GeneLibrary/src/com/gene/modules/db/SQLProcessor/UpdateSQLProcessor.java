package com.gene.modules.db.SQLProcessor;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;

import com.gene.modules.check.Check;
import com.gene.modules.db.SQLExecutor.OldSQLExecutor;
import com.gene.modules.db.SQLExecutor.SQLExecutorFactory;
import com.gene.modules.db.table.tableInfo.TableInfo;
import com.gene.modules.db.table.tableInfoLibrary.TableInfoLibrary;
import com.gene.modules.db.table.tableInfoLibrary.TableInfoLibraryFactory;
import com.gene.modules.exceptions.MissingDependancyException;


public class UpdateSQLProcessor
{
	private OldSQLExecutor sqlExecutor;
	private TableInfoLibrary tableInfoLibrary;
	
	private String tableName;
	private Vector<String[]> whereClauseElements;
	private Vector<String[]> setClauseElements;
	private String setClauseColumnName;
	private String whereClauseColumnName;
	
	private SetColumn setColumn;
	private SetOperator setOperator;
	private SetDoneWhereColumn setDoneWhereColumn;
	private WhereOperator whereOperator;
	private WhereDone whereDone;
	
	public UpdateSQLProcessor()
	{
		this.setColumn = new SetColumn(this);
		this.setOperator = new SetOperator(this);
		this.setDoneWhereColumn = new SetDoneWhereColumn(this);
		this.whereOperator = new WhereOperator(this);
		this.whereDone = new WhereDone(this);
	}
	
	
	
	
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
	
	public synchronized void setTableInfoLibrary(TableInfoLibrary tableInfoLibrary)
	{
		this.tableInfoLibrary = tableInfoLibrary;
	}
	
	public synchronized TableInfoLibrary getTableInfoLibrary()
	{
		return this.tableInfoLibrary;
	}
	
	public synchronized boolean isReadyToGo()
	{
		boolean isReady = true;
		
		if(this.sqlExecutor == null)
		{
			isReady = false;
		}
		
		return isReady;
	}
	
	
	
	
	private int execute() throws SQLException, InterruptedException, IOException, ClassNotFoundException
	{
		if(this.sqlExecutor == null)
		{
			throw new MissingDependancyException();
		}
		
		Boolean valid = null;
		if((this.tableInfoLibrary != null)&&(Check.NoBlankExists(this.tableName))&&(this.tableInfoLibrary.getTableInfo(this.tableName) != null))
		{
			valid = this.varify();
		}
		
		int result = -1;
		if((valid == null)||valid)
		{
			String sql = this.generateSQL();
			this.sqlExecutor.openSession();
			String[][] resultTemp = this.sqlExecutor.execute(sql);
			this.sqlExecutor.commit();
			this.sqlExecutor.closeSession();
			if((Check.NoBlankExists(resultTemp[0][0]))&&(Check.isAllNumeric(resultTemp[0][0])))
			{
				result = Integer.parseInt(resultTemp[0][0]);
			}
		}
		
		return result;
	}
	
	
	private String generateSQL()
	{
		if(Check.anyBlankExists(this.tableName))
		{
			throw new MissingDependancyException("Table name is missing");
		}
		if(this.setClauseElements.size() == 0)
		{
			throw new MissingDependancyException("set clause does not exist");
		}
		

		// update���� �ۼ��Ѵ�.
		String sql = "UPDATE "+this.tableName;
		
		
		// set������ �ۼ��Ѵ�.
		if(this.setClauseElements.size() > 0)
		{
			sql += " SET ";
			for(int i=0; i<this.setClauseElements.size(); ++i)
			{
				for(int j=0; j<this.setClauseElements.elementAt(i).length; ++j)
				{
					sql += this.setClauseElements.elementAt(i)[j];
				}
				
				if(i < this.setClauseElements.size()-1)
				{
					sql += ", ";
				}
			}
		}
		

		// where���� �ۼ��Ѵ�.
		if(this.whereClauseElements.size() > 0)
		{
			sql += " WHERE ";
			for(int i=0; i<this.whereClauseElements.size(); ++i)
			{
				for(int j=0; j<this.whereClauseElements.elementAt(i).length; ++j)
				{
					sql += this.whereClauseElements.elementAt(i)[j];
				}
				
				if(i < this.whereClauseElements.size()-1)
				{
					sql += " AND ";
				}
			}	
		}
		
		System.out.println(sql);
		
		return sql;
	}
	
	
	
	/**
	 * update���� ����� �ۼ��Ǿ������� �˻��Ѵ�.
	 * @return null: TableInfoLibrary��ü�� �������� �ʾ� �˻簡 �Ұ����� ���
	 *            true: table�̸��� column�̸����� �˻��ؼ� ������ ���� ���
	 *            false: table�̸��� column�̸����� �˻��ؼ� ������ ���� ���
	 * @throws SQLException
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private synchronized Boolean varify() throws SQLException, InterruptedException, IOException, ClassNotFoundException
	{
		Boolean valid = null;

		if(this.tableInfoLibrary != null)
		{
			valid = true;
			
			// update���� �ִ� table�̸��� ������ �����ϴ����� Ȯ���Ѵ�.
			TableInfo tableInfo = this.tableInfoLibrary.getTableInfo(this.tableName);
			if(tableInfo == null)
			{
				valid = false;
			}
			
			
			// SET clause�� �ִ� column�̸��� ������ table�� �����ϴ����� check�Ѵ�.
			if(valid)
			{
				for(int i=0; i<this.setClauseElements.size(); ++i)
				{
					int columnID = tableInfo.getColumnID(this.setClauseElements.elementAt(i)[0]);
					if(columnID < 0)
					{
						valid = false;
						break;
					}
				}
			}
			
			// WHERE clause�� �ִ� column�̸��� ������ table�� �����ϴ����� check�Ѵ�.
			if(valid)
			{
				for(int i=0; i<this.whereClauseElements.size(); ++i)
				{
					int columnID = tableInfo.getColumnID(this.whereClauseElements.elementAt(i)[0]);
					if(columnID < 0)
					{
						valid = false;
						break;
					}
				}
			}
		}
		
		return valid;
	}
	


	private void setClauseElementColumn_columnName(String columnName)
	{
		if(Check.anyBlankExists(columnName))
		{
			throw new IllegalArgumentException("columnName: "+columnName);
		}
		
		this.setClauseColumnName = columnName.toUpperCase();
	}
	
	private void deleteSetClauseElement(String columnName)
	{
		if(Check.anyBlankExists(columnName))
		{
			throw new IllegalArgumentException("Column name is null or empty");
		}
		
		String column_name = columnName.toUpperCase();
		
		for(int i=0; i<this.setClauseElements.size(); ++i)
		{
			if(this.setClauseElements.elementAt(i)[0].equals(column_name))
			{
				this.setClauseElements.remove(i);
				break;
			}
		}
	}
	
	
	private void setClauseElementOperator_is(Object value)
	{
		this.deleteSetClauseElement(this.setClauseColumnName);
		String valueString = addQuoteIfNotNumeric(value);
		this.setClauseElements.add(new String[]{this.setClauseColumnName, "=", valueString});
	}
	
	
	
	
	
	private void whereClauseElementColumn_columnName(String columnName)
	{
		if(Check.anyBlankExists(columnName))
		{
			throw new IllegalArgumentException("Column name is null or empty");
		}
		
		this.whereClauseColumnName = columnName.toUpperCase();
	}
	
	
	private void deleteWhereClauseElement(String columnName)
	{
		if(Check.anyBlankExists(columnName))
		{
			throw new IllegalArgumentException("Column name is null or empty");
		}
		
		String column_name = columnName.toUpperCase();
		
		for(int i=0; i<this.whereClauseElements.size(); ++i)
		{
			if(this.whereClauseElements.elementAt(i)[0].equals(column_name))
			{
				this.whereClauseElements.remove(i);
				break;
			}
		}
	}
	
	
	private void whereClauseElementOperator_greaterThan(Object value)
	{
		this.deleteWhereClauseElement(this.whereClauseColumnName);
		String valueString = addQuoteIfNotNumeric(value);
		this.whereClauseElements.add(new String[]{this.whereClauseColumnName, " > ", valueString});
	}
	
	private void whereClauseElementOperator_lessThan(Object value)
	{
		this.deleteWhereClauseElement(this.whereClauseColumnName);
		String valueString = addQuoteIfNotNumeric(value);
		this.whereClauseElements.add(new String[]{this.whereClauseColumnName, " < ", valueString});
	}
	
	private void whereClauseElementOperator_is(Object value)
	{
		this.deleteWhereClauseElement(this.whereClauseColumnName);
		String valueString = addQuoteIfNotNumeric(value);
		this.whereClauseElements.add(new String[]{this.whereClauseColumnName, "=", valueString});
	}
	
	private void whereClauseElementOperator_isNull()
	{
		this.deleteWhereClauseElement(this.whereClauseColumnName);
		this.whereClauseElements.add(new String[]{this.whereClauseColumnName, " IS NULL"});
	}
	
	
	
	
	private static String addQuoteIfNotNumeric(Object value)
	{
		if(value == null)
		{
			throw new IllegalArgumentException("value: "+value);
		}
		
		String valueString = value+"";
		if(Check.isNotNumeric(valueString))
		{
			valueString = "'"+valueString+"'";
		}
		
		return valueString;
	}
	
	
	

	public SetColumn updateTable(String tableName)
	{
		if(Check.anyBlankExists(tableName))
		{
			throw new IllegalArgumentException("tableName: "+tableName);
		}
		
		this.tableName = null;
		this.tableName = tableName.toUpperCase();
		this.whereClauseElements = null;
		this.whereClauseElements = new Vector<String[]>();
		this.setClauseElements = null;
		this.setClauseElements = new Vector<String[]>();
		
		return this.setColumn;
	}



	
	
	public class SetColumn
	{
		private UpdateSQLProcessor root;
		
		public SetColumn(UpdateSQLProcessor root)
		{
			this.root = root;
		}
		
		public SetOperator setColumn(String columnName)
		{
			this.root.setClauseElementColumn_columnName(columnName);
			return this.root.setOperator;
		}
	}
	
	
	
	
	public class SetOperator
	{
		private UpdateSQLProcessor root;
		
		public SetOperator(UpdateSQLProcessor root)
		{
			this.root = root;
		}
		
		public SetDoneWhereColumn is(Object value)
		{
			this.root.setClauseElementOperator_is(value);
			return this.root.setDoneWhereColumn;
		}
	}
	
	
	
	public class SetDoneWhereColumn
	{
		private UpdateSQLProcessor root;
		
		public SetDoneWhereColumn(UpdateSQLProcessor root)
		{
			this.root = root;
		}
		
		public SetOperator setColumn(String columnName)
		{
			this.root.setClauseElementColumn_columnName(columnName);
			return this.root.setOperator;
		}
		
		public int execute() throws SQLException, InterruptedException, IOException, ClassNotFoundException
		{
			return this.root.execute();
		}
		
		public WhereOperator whereColumn(String columnName)
		{
			this.root.whereClauseElementColumn_columnName(columnName);
			return this.root.whereOperator;
		}
	}
	
	

	public class WhereOperator
	{
		private UpdateSQLProcessor root;
		
		public WhereOperator(UpdateSQLProcessor root)
		{
			this.root = root;
		}

		public WhereDone greaterThan(Object value)
		{
			this.root.whereClauseElementOperator_greaterThan(value);
			return this.root.whereDone;
		}
		
		public WhereDone lessThan(Object value)
		{
			this.root.whereClauseElementOperator_lessThan(value);
			return this.root.whereDone;
		}
		
		public WhereDone is(Object value)
		{
			this.root.whereClauseElementOperator_is(value);
			return this.root.whereDone;
		}
		
		public WhereDone isNull()
		{
			this.root.whereClauseElementOperator_isNull();
			return this.root.whereDone;
		}
		
		public int execute() throws SQLException, InterruptedException, IOException, ClassNotFoundException
		{
			return this.root.execute();
		}
	}
	
	
	public class WhereDone
	{
		private UpdateSQLProcessor root;
		
		public WhereDone(UpdateSQLProcessor root)
		{
			this.root = root;
		}
		
		public WhereOperator whereColumn(String columnName)
		{
			this.root.whereClauseElementColumn_columnName(columnName);
			return this.root.whereOperator;
		}
		
		public int execute() throws SQLException, InterruptedException, IOException, ClassNotFoundException
		{
			return this.root.execute();
		}
	}
	
	
	
	
	public static void main(String[] args) throws SQLException, InterruptedException, IOException, ClassNotFoundException
	{
		TableInfoLibrary tableInfoLibrary = TableInfoLibraryFactory.getInstance();
	
		UpdateSQLProcessor updateSQLProcessor = new UpdateSQLProcessor();
		updateSQLProcessor.setSQLExecutor(SQLExecutorFactory.getInstance());
		updateSQLProcessor.setTableInfoLibrary(tableInfoLibrary);
		System.out.println(updateSQLProcessor.updateTable("temptemp4").setColumn("column3").is(44511).whereColumn("column1").greaterThan(1).whereColumn("column2").greaterThan(1).execute());
	}
}

