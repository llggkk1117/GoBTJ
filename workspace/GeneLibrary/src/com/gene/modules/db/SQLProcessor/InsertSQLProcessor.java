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
import com.gene.modules.db.SQLAdapter.TableRecordInserter;
import com.gene.modules.db.SQLExecutor.SQLExecutorFactory;
import com.gene.modules.db.table.tableInfo.TableInfo;
import com.gene.modules.db.table.tableInfoLibrary.TableInfoLibrary;
import com.gene.modules.db.table.tableInfoLibrary.TableInfoLibraryFactory;
import com.gene.modules.db.table.tableRecord.TableColumnRecord;
import com.gene.modules.db.table.tableRecord.TableRecord;



public class InsertSQLProcessor
{
	private TableRecordInserter tableRecordInserter;
	private TableInfoLibrary tableInfoLibrary;
	
	private TableRecord tableRecord;
	private boolean insertForce;
	
	private PuttingColumnAndValueState puttingColumnAndValueState;
	private PuttingColumnAndValueDoneState puttingColumnAndValueDoneState;
	private ForceCommandDoneState forceCommandDoneState;
	
	public InsertSQLProcessor()
	{
		this.insertForce = false;
		this.puttingColumnAndValueState = new PuttingColumnAndValueState(this);
		this.puttingColumnAndValueDoneState = new PuttingColumnAndValueDoneState(this);
		this.forceCommandDoneState = new ForceCommandDoneState(this);
	}
	
	public synchronized void setTableRecordInserter(TableRecordInserter tableRecordInserter)
	{
		if(tableRecordInserter == null)
		{
			throw new IllegalArgumentException();
		}
		
		this.tableRecordInserter = tableRecordInserter;
	}
	
	public synchronized TableRecordInserter getTableRecordInserter()
	{
		return this.tableRecordInserter;
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
		
		if(this.tableRecordInserter == null)
		{
			isReady = false;
		}
		
		return isReady;
	}
	
	
	
	public PuttingColumnAndValueState insertInto(String tableName) throws SQLException, InterruptedException, IOException, ClassNotFoundException  
	{
		if(StringUtils.isBlank(tableName))
		{
			throw new IllegalArgumentException();
		}
		
		this.tableRecord = null;
		this.tableRecord = new TableRecord(tableName.toUpperCase());
		if(this.tableInfoLibrary != null)
		{
			TableInfo tableInfo = this.tableInfoLibrary.getTableInfo(tableName.toUpperCase());
			if(tableInfo != null)
			{
				this.tableRecord.setTableInfo(tableInfo);
			}
		}

		return this.puttingColumnAndValueState;
	}
	
	
	private synchronized void putColumnAndValue(String columnName, Object value)
	{
		if(Check.anyBlankExists(columnName))
		{
			throw new IllegalArgumentException();
		}
		if(value == null)
		{
			throw new IllegalArgumentException();
		}
		
		
		String column_name = columnName.toUpperCase();
		if(this.tableRecord.getTableColumnRecord(column_name) != null)
		{
			this.tableRecord.removeTableColumnRecord(column_name);
		}
		
		this.tableRecord.putTableColumnRecord(column_name, value+"");
	}
	
	
	private synchronized void putColumnAndValue(int columnID, Object value)
	{
		TableInfo tableInfo = this.tableRecord.getTableInfo();
		if(tableInfo == null)
		{
			throw new MissingDependancyException();
		}
		
		int maxColumnID = tableInfo.getColumnNames().length-1;
		if((columnID < 0)||(columnID > maxColumnID))
		{
			throw new IllegalArgumentException();
		}
		
		String columnName = tableInfo.getColumnInfo(columnID).getColumnName();
		this.putColumnAndValue(columnName, value);
	}
	
	
	private synchronized void force()
	{
		this.insertForce = true;
	}
	
	
	private synchronized int execute() throws SQLException, InterruptedException, ClassNotFoundException 
	{
		if(this.tableRecordInserter == null)
		{
			throw new MissingDependancyException();
		}
		
		int result = -1;
		if((Check.NoBlankExists(this.tableRecord.getTableName()))&&(!this.tableRecord.isEmpty()))
		{
			if(this.insertForce)
			{
				result = this.tableRecordInserter.insertForce(this.tableRecord);				
			}
			else
			{
				result = this.tableRecordInserter.insert(this.tableRecord);
			}
		}
		
		return result;
	}
	
	
	
	
	public class PuttingColumnAndValueState
	{
		private InsertSQLProcessor root;

		public PuttingColumnAndValueState(InsertSQLProcessor root) 
		{
			this.root = root;
		}
		
		public synchronized PuttingColumnAndValueDoneState columnAndValue(String columnName, Object value)
		{
			this.root.putColumnAndValue(columnName, value);
			return this.root.puttingColumnAndValueDoneState;
		}
		
		public synchronized PuttingColumnAndValueDoneState columnAndValue(int columnID, Object value)
		{
			this.root.putColumnAndValue(columnID, value);
			return this.root.puttingColumnAndValueDoneState;
		}
	}
	
	
	
	public class PuttingColumnAndValueDoneState
	{
		private InsertSQLProcessor root;

		public PuttingColumnAndValueDoneState(InsertSQLProcessor root) 
		{
			this.root = root;
		}
		
		public synchronized PuttingColumnAndValueDoneState columnAndValue(String columnName, Object value)
		{
			this.root.putColumnAndValue(columnName, value);
			return this;
		}
		
		public synchronized PuttingColumnAndValueDoneState columnAndValue(int columnID, Object value)
		{
			this.root.putColumnAndValue(columnID, value);
			return this;
		}
		
		public synchronized int execute() throws SQLException, InterruptedException, ClassNotFoundException 
		{
			return this.root.execute();
		}
		
		public synchronized ForceCommandDoneState force()
		{
			this.root.force();
			return this.root.forceCommandDoneState;
		}
	}
	
	

	public class ForceCommandDoneState
	{
		private InsertSQLProcessor root;

		public ForceCommandDoneState(InsertSQLProcessor root) 
		{
			this.root = root;
		}
		
		public synchronized int execute() throws SQLException, InterruptedException, ClassNotFoundException 
		{
			return this.root.execute();
		}
	}
	
	
	
	public static void main(String[] args) throws IOException, InstanceAlreadyExistException, InvalidConnectionException, SQLException, InterruptedException, ClassNotFoundException, DependencyNotSatisfiedException, InstanceNotExistException
	{
		TableInfoLibrary tableInfoLibrary = TableInfoLibraryFactory.getInstance();
		
		TableRecordInserter tableRecordInserter =  new TableRecordInserter();
		tableRecordInserter.setSQLExecutor(SQLExecutorFactory.getInstance());
		
		InsertSQLProcessor insertSQLProcessor = new InsertSQLProcessor();
		insertSQLProcessor.setTableInfoLibrary(tableInfoLibrary);
		insertSQLProcessor.setTableRecordInserter(tableRecordInserter);
		
//		System.out.println(insertSQLProcessor.insert("temptemp4").column("column1", "13").column("column2", "14").column("column3", "10").force().execute());
		System.out.println(insertSQLProcessor.insertInto("temptemp4").columnAndValue("column1", "13").columnAndValue("column2", "10").force().execute());
		
//		System.out.println(insertSQLProcessor.insertInto("temptemp4").columnAndValue("column1", 13));
	}
}

