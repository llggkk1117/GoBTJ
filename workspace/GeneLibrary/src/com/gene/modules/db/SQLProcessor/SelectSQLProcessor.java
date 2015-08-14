package com.gene.modules.db.SQLProcessor;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;



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
import com.gene.modules.db.table.tableRecord.TableRecord;



public class SelectSQLProcessor
{
	private OldSQLExecutor sqlExecutor;
	private TableInfoLibrary tableInfoLibrary;
	
	private TableInfo tableInfo;
	private String tableName;
	private Vector<String> columns;
	private String filteringCondition;
	private Vector<String> whereConditions;
	private Vector<String> orderByColumns;
	private String order;
	
	private String columnName;
	
	
	private TableNameDecidedState tableNameDecidedState;
	private ColumnNameDecidedState columnNameDecidedState;
	private EveryColumnNameDecidedState everyColumnNameDecidedState;
	private WhereColumnDecidedState whereColumnDecidedState;
	private DirectionDecidedState directionDecidedState;
	private OrderByColumnNameDecidedState orderByColumnNameDecidedState;
	private WhereOperatorDecidedState whereOperatorDecidedState;
	private FilterDecidedState filterDecidedState;
	
	
	public SelectSQLProcessor()
	{
		this.sqlExecutor = null;
		this.tableInfoLibrary = null;
		this.tableInfo = null;
		this.tableName = null;
		this.columns = null;
		this.filteringCondition = null;
		this.whereConditions = null;
		this.orderByColumns = null;
		this.order = null;
		
		this.tableNameDecidedState = new TableNameDecidedState(this);
		this.columnNameDecidedState = new ColumnNameDecidedState(this);
		this.everyColumnNameDecidedState = new EveryColumnNameDecidedState(this);
		this.whereColumnDecidedState = new WhereColumnDecidedState(this);
		this.directionDecidedState = new DirectionDecidedState(this);
		this.orderByColumnNameDecidedState = new OrderByColumnNameDecidedState(this);
		this.whereOperatorDecidedState = new WhereOperatorDecidedState(this);
		this.filterDecidedState = new FilterDecidedState(this);
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
	
	public void setTableInfoLibrary(TableInfoLibrary tableInfoLibrary)
	{
		this.tableInfoLibrary = tableInfoLibrary;
	}
	
	public TableInfoLibrary getTableInfoLibrary()
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
	
	
	
	public TableNameDecidedState selectTable(String tableName) throws SQLException, InterruptedException, IOException, ClassNotFoundException
	{
		if(Check.anyBlankExists(tableName))
		{
			throw new IllegalArgumentException();
		}
		
		this.tableName = null;
		this.tableName = tableName.toUpperCase();
		if(this.tableInfoLibrary != null)
		{
			this.tableInfo = null;
			this.tableInfo = this.tableInfoLibrary.getTableInfo(this.tableName);
		}
		this.columns = null;
		this.columns = new Vector<String>();
		this.filteringCondition = null;
		this.whereConditions = null;
		this.whereConditions = new Vector<String>();
		this.orderByColumns = null;
		this.orderByColumns = new Vector<String>();
		this.order = null;
			
		return this.tableNameDecidedState;
	}
	
	
	private String[] getColumnNames(int... columnIDs)
	{
		if(this.tableInfo == null)
		{
			throw new MissingDependancyException();
		}
		
		String[] columnNames= new String[columnIDs.length];
		for(int i=0; i<columnNames.length; ++i)
		{
			columnNames[i] =  tableInfo.getColumnInfo(columnIDs[i]).getColumnName();
		}
		
		return columnNames;
	}
	
	
	
	private String getColumnName(int columnID)
	{
		if(this.tableInfo == null)
		{
			throw new MissingDependancyException();
		}
		
		String columnName = this.tableInfo.getColumnInfo(columnID).getColumnName();
		
		return columnName;
	}
	
	
	private void selectColumn(String... columnNames)
	{
		for(int i=0; i<columnNames.length; ++i)
		{
			this.columns.add(columnNames[i].toUpperCase());
		}
	}

	private void selectColumn(int... columnIDs) throws SQLException, InterruptedException, IOException
	{
		this.selectColumn(this.getColumnNames(columnIDs));
	}
	
	
	private void selectEveryColumn()
	{
		this.columns.clear();
		this.columns.add("*");
	}
	
	
	private static String toUpperCaseSkipQuote(String str) 
	{
		if(StringUtils.isBlank(str))
		{
			throw new IllegalArgumentException();
		}
			
		
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
	
	
	
	private void whereColumn(String columnName)
	{
		if(Check.anyBlankExists(columnName))
		{
			throw new IllegalArgumentException("columnName: "+columnName);
		}
		
		this.columnName = columnName.toUpperCase();
	}
	
	
	private void whereColumn(int columnID)
	{
		this.whereColumn(this.getColumnName(columnID));
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
	
	
	
	private void filter(String condition)
	{
		if(StringUtils.isBlank(condition))
		{
			throw new IllegalArgumentException();
		}
		
		if(StringUtils.isBlank(this.filteringCondition))
		{
			this.filteringCondition = toUpperCaseSkipQuote(condition);
		}
		else
		{
			this.filteringCondition += " AND "+toUpperCaseSkipQuote(condition);
		}
	}
	

	private void greaterThan(Object value)
	{
		String valueString = addQuoteIfNotNumeric(value);
		this.whereConditions.add(this.columnName+" > "+valueString);
	}
	
	private void lessThan(Object value)
	{
		String valueString = addQuoteIfNotNumeric(value);
		this.whereConditions.add(this.columnName+" < "+valueString);
	}
	
	private void sameAs(Object value)
	{
		String valueString = addQuoteIfNotNumeric(value);
		this.whereConditions.add(this.columnName+"="+valueString);
	}
	
	private void isNull()
	{
		this.whereConditions.add(this.columnName+" IS NULL");
	}
	
	private void like(Object value)
	{
		String valueString = addQuoteIfNotNumeric(value);
		this.whereConditions.add(this.columnName+" LIKE "+valueString);
	}
	
	
	private TableRecord[] execute() throws SQLException, InterruptedException, IOException, ClassNotFoundException
	{
		if(this.sqlExecutor == null)
		{
			throw new MissingDependancyException();
		}
		
		String sql = this.generateSQL();
		System.out.println(sql);
		
		TableRecord[] records = null;
		if(Check.NoBlankExists(sql))
		{
			this.sqlExecutor.openSession();
			String[][] result = this.sqlExecutor.execute(sql);
			this.sqlExecutor.closeSession();
			
			TableInfo tableInfo = null;
			if(this.tableInfoLibrary != null)
			{
				tableInfo = this.tableInfoLibrary.getTableInfo(this.tableName);
			}
			
			records = new TableRecord[result.length];
			for(int i=0; i<result.length; ++i)
			{
				records[i] = new TableRecord(tableInfo);
				for(int j=0; j<this.columns.size(); ++j)
				{
					records[i].putTableColumnRecord(this.columns.elementAt(j), result[i][j]);
				}
			}
		}
		
		return records;
	}
	
	
	private String generateSQL()
	{
		if(Check.anyBlankExists(this.tableName))
		{
			throw new IllegalArgumentException();
		}
		if(this.columns.size() == 0)
		{
			throw new IllegalArgumentException();
		}
		
		
		String everyColumnSelected = "";
		for(int i=0; i<this.columns.size(); ++i)
		{
			everyColumnSelected += this.columns.elementAt(i);
			if(i<(this.columns.size()-1))
			{
				everyColumnSelected += ", ";
			}
		}
		System.out.println(everyColumnSelected);
		
		

		String tableName = this.tableName.toUpperCase();
		System.out.println(tableName);

		

		String everyWhereConditions = null;
		if(this.whereConditions.size() > 0)
		{
			everyWhereConditions = "";
			for(int i=0; i<this.whereConditions.size(); ++i)
			{
				everyWhereConditions += this.whereConditions.elementAt(i);
				if(i < (this.whereConditions.size()-1))
				{
					everyWhereConditions += " AND ";
				}
			}
		}
		else if(Check.NoBlankExists(this.filteringCondition))
		{
			everyWhereConditions = this.filteringCondition;
		}
		
		
		String everyOrderByColumns = null;
		if(this.orderByColumns.size() > 0)
		{
			everyOrderByColumns = "";
			for(int i=0; i<this.orderByColumns.size(); ++i)
			{
				everyOrderByColumns += this.orderByColumns.elementAt(i);
				if(i<(this.orderByColumns.size()-1))
				{
					everyOrderByColumns += ", ";
				}
			}
		}
		
		
		String order = null;
		if(this.orderByColumns.size() > 0)
		{
			if(Check.NoBlankExists(this.order))
			{
				order = this.order;
			}
			else
			{
				order = "ASC";
			}
		}
		
		
		String sql = "SELECT "+everyColumnSelected+" FROM "+tableName;
		if(Check.NoBlankExists(everyWhereConditions))
		{
			sql += " WHERE "+everyWhereConditions;
		}
		if(Check.NoBlankExists(everyOrderByColumns))
		{
			sql += " ORDER BY "+everyOrderByColumns+" "+order;
		}
		
		return sql;
	}
	
	
	
	private void orderBy(String... columnNames)
	{
		for(int i=0; i<columnNames.length; ++i)
		{
			this.orderByColumns.add(columnNames[i].toUpperCase());
		}
	}
	

	private void orderBy(int... columnIDs)
	{
		this.orderBy(this.getColumnNames(columnIDs));
	}
	
	
	
	private void increase()
	{
		this.order = null;
		this.order = "ASC";
	}
	
	private void decrease()
	{
		this.order = null;
		this.order = "DESC";
	}
	
	
	
	
	public class TableNameDecidedState
	{
		private SelectSQLProcessor root;
		
		public TableNameDecidedState(SelectSQLProcessor root)
		{
			this.root = root;
		}
		
		public ColumnNameDecidedState selectColumn(String... columnNames)
		{
			this.root.selectColumn(columnNames);
			return this.root.columnNameDecidedState;
		}
		
		public ColumnNameDecidedState selectColumn(int... columnIDs) throws SQLException, InterruptedException, IOException
		{
			this.root.selectColumn(columnIDs);
			return this.root.columnNameDecidedState;
		}

		public EveryColumnNameDecidedState selectEveryColumn()
		{
			this.root.selectEveryColumn();
			return this.root.everyColumnNameDecidedState;
		}
	}
	
	
	public class ColumnNameDecidedState
	{
		private SelectSQLProcessor root;
		
		public ColumnNameDecidedState(SelectSQLProcessor root)
		{
			this.root = root;
		}
		
		public ColumnNameDecidedState selectColumn(String... columnNames)
		{
			this.root.selectColumn(columnNames);
			return this;
		}
		
		public ColumnNameDecidedState selectColumn(int... columnIDs) throws SQLException, InterruptedException, IOException
		{
			this.root.selectColumn(columnIDs);
			return this;
		}
		
		public WhereColumnDecidedState whereColumn(String columnName)
		{
			this.root.whereColumn(columnName);
			return this.root.whereColumnDecidedState;
		}
		
		public WhereColumnDecidedState whereColumn(int columnID)
		{
			this.root.whereColumn(columnID);
			return this.root.whereColumnDecidedState;
		}
		
		public FilterDecidedState filter(String condition)
		{
			this.root.filter(condition);
			return this.root.filterDecidedState;
		}
	}
	
	
	
	public class EveryColumnNameDecidedState
	{
		private SelectSQLProcessor root;
		
		public EveryColumnNameDecidedState(SelectSQLProcessor root)
		{
			this.root = root;
		}
		
		public WhereColumnDecidedState whereColumn(String columnName)
		{
			this.root.whereColumn(columnName);
			return this.root.whereColumnDecidedState;
		}
		
		
		public WhereColumnDecidedState whereColumn(int columnID)
		{
			this.root.whereColumn(columnID);
			return this.root.whereColumnDecidedState;
		}
		
		public FilterDecidedState filter(String condition)
		{
			this.root.filter(condition);
			return this.root.filterDecidedState;
		}
	}
	
	
	
	public class FilterDecidedState
	{
		private SelectSQLProcessor root;
		
		public FilterDecidedState(SelectSQLProcessor root)
		{
			this.root = root;
		}
		
		public OrderByColumnNameDecidedState orderBy(String... columnNames)
		{
			this.root.orderBy(columnNames);
			return this.root.orderByColumnNameDecidedState;
		}

		public OrderByColumnNameDecidedState orderBy(int... columnIDs)
		{
			this.root.orderBy(columnIDs);
			return this.root.orderByColumnNameDecidedState;
		}
	}
	
	
	
	public class WhereColumnDecidedState
	{
		private SelectSQLProcessor root;
		
		public WhereColumnDecidedState(SelectSQLProcessor root)
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
		private SelectSQLProcessor root;
		
		public WhereOperatorDecidedState(SelectSQLProcessor root)
		{
			this.root = root;
		}
		
		public TableRecord[] execute() throws SQLException, InterruptedException, IOException, ClassNotFoundException
		{
			return this.root.execute();
		}
		
		public OrderByColumnNameDecidedState orderBy(String... columnNames)
		{
			this.root.orderBy(columnNames);
			return this.root.orderByColumnNameDecidedState;
		}

		public OrderByColumnNameDecidedState orderBy(int... columnIDs)
		{
			this.root.orderBy(columnIDs);
			return this.root.orderByColumnNameDecidedState;
		}
	}
	

	public class OrderByColumnNameDecidedState
	{
		private SelectSQLProcessor root;
		
		public OrderByColumnNameDecidedState(SelectSQLProcessor root)
		{
			this.root = root;
		}
		
		public OrderByColumnNameDecidedState orderBy(String... columnNames)
		{
			this.root.orderBy(columnNames);
			return this;
		}

		public OrderByColumnNameDecidedState orderBy(int... columnIDs)
		{
			this.root.orderBy(columnIDs);
			return this;
		}
		
		public TableRecord[] execute() throws SQLException, InterruptedException, IOException, ClassNotFoundException
		{
			return this.root.execute();
		}
		
		public DirectionDecidedState increase()
		{
			this.root.increase();
			return this.root.directionDecidedState;
		}
		
		public DirectionDecidedState decrease()
		{
			this.root.decrease();
			return this.root.directionDecidedState;
		}
	}
	
	
	public class DirectionDecidedState
	{
		private SelectSQLProcessor root;
		
		public DirectionDecidedState(SelectSQLProcessor root)
		{
			this.root = root;
		}
		
		public TableRecord[] execute() throws SQLException, InterruptedException, IOException, ClassNotFoundException
		{
			return this.root.execute();
		}
	}
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) throws SQLException, InstanceNotExistException, InterruptedException, IOException, InstanceAlreadyExistException, InvalidConnectionException, ClassNotFoundException, DependencyNotSatisfiedException
	{
		SelectSQLProcessor pro = new SelectSQLProcessor();
		pro.setSQLExecutor(SQLExecutorFactory.getInstance());
		pro.setTableInfoLibrary(TableInfoLibraryFactory.getInstance());
		
//		System.out.println(pro.select("SSD_EXCEPTION_T").column("ORGN_ZIP3_CD").filter("orgn_zip3_cd like '44%'").sort("ORGN_ZIP3_CD").increase().execute().length);
		System.out.println(pro.selectTable("SSD_EXCEPTION_T").selectColumn("ORGN_ZIP3_CD", "dest_zip3_cd").whereColumn("dest_zip3_cd").like("%0").orderBy("ORGN_ZIP3_CD").decrease().execute().length);
		
		TableRecord[] tr = pro.selectTable("SSD_EXCEPTION_T").selectColumn("ORGN_ZIP3_CD", "dest_zip3_cd").whereColumn("dest_zip3_cd").like("%0").orderBy("ORGN_ZIP3_CD").decrease().execute();
		for(int i=0; i<tr.length; ++i)
		{
			String[] cn = tr[i].getEveryColumnName();
			for(int j=0; j<cn.length; ++j)
			{
				System.out.print(cn[j]+": "+tr[i].getTableColumnRecord(cn[j]).getValue()+"    ");
			}
			System.out.println();
		}
	}

}
