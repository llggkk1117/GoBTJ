package com.gene.modules.db.SQLProcessor;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;

import com.gene.modules.exceptions.DependencyNotSatisfiedException;
import com.gene.modules.exceptions.InstanceAlreadyExistException;
import com.gene.modules.exceptions.InstanceNotExistException;
import com.gene.modules.exceptions.InvalidConnectionException;
import com.gene.modules.exceptions.MissingDependancyException;
import com.gene.modules.check.Check;
import com.gene.modules.db.SQLExecutor.OldSQLExecutor;
import com.gene.modules.db.SQLExecutor.SQLExecutorFactory;
import com.gene.modules.db.table.tableInfo.TableInfo;
import com.gene.modules.db.table.tableInfoLibrary.TableInfoLibrary;
import com.gene.modules.db.table.tableInfoLibrary.TableInfoLibraryFactory;


public class DeleteSQLProcessor
{
	private OldSQLExecutor sqlExecutor;
	private TableInfoLibrary tableInfoLibrary;
	
	private String tableName;
	private String filteringCondition;
	private Vector<String[]> whereConditions;
	private String columnName;
	
	private TableNameDecidedState tableNameDecidedState;
	private FilteringConditionDecidedState filteringConditionDecidedState;
	private WhereColumnDecidedState whereColumnDecidedState;
	private WhereOperatorDecidedState whereOperatorDecidedState;
	
	public DeleteSQLProcessor()
	{
		this.tableNameDecidedState = new TableNameDecidedState(this);
		this.filteringConditionDecidedState = new FilteringConditionDecidedState(this);
		this.whereColumnDecidedState = new WhereColumnDecidedState(this);
		this.whereOperatorDecidedState = new WhereOperatorDecidedState(this);
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
	
	
	
	public TableNameDecidedState deleteFrom(String tableName)
	{
		Check.notBlankWithMessage(tableName);
		
		this.tableName = tableName.toUpperCase();
		this.filteringCondition = null;
		this.whereConditions = new Vector<String[]>();
		
		return this.tableNameDecidedState;
	}
	
	
	private static String toUpperCaseSkipQuote(String str) 
	{
		Check.notBlankWithMessage(str);
		
		String[] temp = str.split("'");
		for(int i=0; i<temp.length; ++i)
		{
			if((i%2) == 0)
			{
				temp[i] = temp[i].toUpperCase(); 
			}
		}
		
		String result = "";
		for(int i=0; i<temp.length; ++i)
		{
			result += temp[i];
			if(i < temp.length-1)
			{
				result += "'";
			}
		}
		
		if(str.endsWith("'"))
		{
			result += "'";
		}
		
		return result;
	}
	
	
	private void filter(String condition)
	{
		Check.notBlankWithMessage(condition);
		
		if(Check.isNotBlank(this.filteringCondition))
		{
			this.filteringCondition = toUpperCaseSkipQuote(condition);
		}
		else
		{
			this.filteringCondition += " AND "+toUpperCaseSkipQuote(condition);
		}
	}
	
	
	private synchronized String generateSQL()
	{
		Check.notBlankWithMessage(this.tableName);
		
		String tableName = this.tableName;

		String whereClause = null;
		if(this.whereConditions.size() > 0)
		{
			whereClause = "";
			for(int i=0; i<this.whereConditions.size(); ++i)
			{
				for(int j=0; j<this.whereConditions.elementAt(i).length; ++j)
				{
					whereClause += this.whereConditions.elementAt(i)[j];
				}
				
				if(i < this.whereConditions.size()-1)
				{
					whereClause += " AND ";
				}
			}
		}
		else
		{
			whereClause = this.filteringCondition;
		}
		
		Check.notBlankWithMessage(whereClause);
		
		String sql = "DELETE FROM "+tableName+" WHERE "+whereClause;
		
		return sql;
	}
	
	
	private int execute() throws SQLException, InterruptedException, IOException, ClassNotFoundException 
	{
		Check.checkNotNull(MissingDependancyException.class, this.sqlExecutor);
		
		Boolean valid = null;
		if((this.tableInfoLibrary != null)&&(Check.isNotBlank(this.tableName))&&(this.tableInfoLibrary.getTableInfo(this.tableName) != null))
		{
			valid = this.varify();
		}
		
		int result = -1;
		if((valid == null)||valid)
		{
			String sql = this.generateSQL();
			System.out.println(sql);
			result = (Integer) this.sqlExecutor.execute(sql);
		}
		
		return result;
	}
	
	
	private synchronized Boolean varify() throws SQLException, InterruptedException, IOException, ClassNotFoundException
	{
		Boolean valid = null;

		if(this.tableInfoLibrary != null)
		{
			TableInfo tableInfo = this.tableInfoLibrary.getTableInfo(this.tableName);
			if(tableInfo != null)
			{
				valid = true;
				for(int i=0; i<this.whereConditions.size(); ++i)
				{
					int columnID = tableInfo.getColumnID(this.whereConditions.elementAt(i)[0]);
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
	
	
	private void where(String columnName)
	{
		Check.notBlankWithMessage(columnName);

		this.columnName = columnName.toUpperCase();
	}
	
	
	
	private void where(int columnID) throws SQLException, InterruptedException, IOException, ClassNotFoundException
	{
		if(this.tableInfoLibrary == null)
		{
			throw new MissingDependancyException();
		}
		TableInfo tableInfo = this.tableInfoLibrary.getTableInfo(this.tableName);
		if(tableInfo == null)
		{
			throw new IllegalArgumentException();
		}
		
		String columnName = tableInfo.getColumnInfo(columnID).getColumnName();
		this.where(columnName);
	}
	
	
	
	private static String addQuoteIfNotNumeric(Object value)
	{
		if(value == null)
		{
			throw new IllegalArgumentException("value: "+value);
		}
		
		String valueString = value+"";
		if(!Check.isNumeric(valueString))
		{
			valueString = "'"+valueString+"'";
		}
		
		return valueString;
	}
	
	
	private void greaterThan(Object value)
	{
		String valueString = addQuoteIfNotNumeric(value);
		this.whereConditions.add(new String[]{this.columnName, " > ", valueString});
	}
	
	private void lessThan(Object value)
	{
		String valueString = addQuoteIfNotNumeric(value);
		this.whereConditions.add(new String[]{this.columnName, " < ", valueString});
	}
	
	private void sameAs(Object value)
	{
		String valueString = addQuoteIfNotNumeric(value);
		this.whereConditions.add(new String[]{this.columnName, "=", valueString});
	}
	
	private void isNull()
	{
		this.whereConditions.add(new String[]{this.columnName, " IS NULL"});
	}
	
	private void like(Object value)
	{
		String valueString = addQuoteIfNotNumeric(value);
		this.whereConditions.add(new String[]{this.columnName, " LIKE ", valueString});
	}
	
	
	
	public class TableNameDecidedState
	{
		private DeleteSQLProcessor root;
		
		public TableNameDecidedState(DeleteSQLProcessor root)
		{
			this.root = root;
		}

		
		public FilteringConditionDecidedState filter(String condition)
		{
			this.root.filter(condition);
			return this.root.filteringConditionDecidedState;
		}
		
		public WhereColumnDecidedState where(String columnName)
		{
			this.root.where(columnName);
			return this.root.whereColumnDecidedState;
		}
		
		public WhereColumnDecidedState where(int columnID) throws SQLException, InterruptedException, IOException, ClassNotFoundException
		{
			this.root.where(columnID);
			return this.root.whereColumnDecidedState;
		}
	}
	
	
	
	public class FilteringConditionDecidedState
	{
		private DeleteSQLProcessor root;
		
		public FilteringConditionDecidedState(DeleteSQLProcessor root)
		{
			this.root = root;
		}
		
		public FilteringConditionDecidedState filter(String condition)
		{
			this.root.filter(condition);
			return this.root.filteringConditionDecidedState;
		}
		
		public int execute() throws SQLException, InterruptedException, IOException, ClassNotFoundException 
		{
			return this.root.execute();
		}
	}
	
	
	public class WhereColumnDecidedState
	{
		private DeleteSQLProcessor root;
		
		public WhereColumnDecidedState(DeleteSQLProcessor root)
		{
			this.root = root;
		}
		
		public WhereOperatorDecidedState greaterThan(Object value)
		{
			this.root.greaterThan(value);
			return this.root.whereOperatorDecidedState;
		}
		
		public WhereOperatorDecidedState lessThan(Object value)
		{
			this.root.lessThan(value);
			return this.root.whereOperatorDecidedState;
		}
		
		public WhereOperatorDecidedState sameAs(Object value)
		{
			this.root.sameAs(value);
			return this.root.whereOperatorDecidedState;
		}
		
		public WhereOperatorDecidedState isNull()
		{
			this.root.isNull();
			return this.root.whereOperatorDecidedState;
		}
		
		public WhereOperatorDecidedState like(Object value)
		{
			this.root.like(value);
			return this.root.whereOperatorDecidedState;
		}
	}
	
	
	public class WhereOperatorDecidedState
	{
		private DeleteSQLProcessor root;
		
		public WhereOperatorDecidedState(DeleteSQLProcessor root)
		{
			this.root = root;
		}
		
		public WhereColumnDecidedState where(String columnName)
		{
			this.root.where(columnName);
			return this.root.whereColumnDecidedState;
		}
		
		public int execute() throws SQLException, InterruptedException, IOException, ClassNotFoundException 
		{
			return this.root.execute();
		}
	}
	
	
	
	
	public static void main(String[] args) throws SQLException, InstanceNotExistException, InterruptedException, IOException, InstanceAlreadyExistException, InvalidConnectionException, ClassNotFoundException, DependencyNotSatisfiedException
	{
		TableInfoLibrary tableInfoLibrary = TableInfoLibraryFactory.getInstance();
		
		DeleteSQLProcessor dd = new DeleteSQLProcessor();
		dd.setSQLExecutor(SQLExecutorFactory.getInstance());
		dd.setTableInfoLibrary(tableInfoLibrary);
		
		System.out.println(dd.deleteFrom("temptemp4").where("column1").like("1%").where("column2").sameAs(14).execute());
	}

}

