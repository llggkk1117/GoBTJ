package com.gene.modules.db.SQLProcessor;

import java.io.IOException;
import java.sql.SQLException;

import com.gene.modules.exceptions.DependencyNotSatisfiedException;
import com.gene.modules.exceptions.InstanceAlreadyExistException;
import com.gene.modules.exceptions.InstanceNotExistException;
import com.gene.modules.exceptions.InvalidConnectionException;
import com.gene.modules.exceptions.MissingDependancyException;
import com.gene.modules.check.Check;
import com.gene.modules.db.SQLExecutor.OldSQLExecutor;
import com.gene.modules.db.SQLExecutor.SQLExecutorFactory;
import com.gene.modules.db.table.tableInfoLibrary.TableInfoLibrary;
import com.gene.modules.db.table.tableInfoLibrary.TableInfoLibraryFactory;


public class DropTableSQLProcessor
{
	private OldSQLExecutor sqlExecutor;
	private TableInfoLibrary tableInfoLibrary;
	
	private String tableName;
	private boolean cascadeConstraints;
	
	private TableNameDecidedState tableNameDecidedState;
	private CascadeConstraintsDecidedState cascadeConstraintsDecidedState;
	
	
	public DropTableSQLProcessor()
	{
		this.sqlExecutor = null;
		this.tableInfoLibrary = null;
		this.tableName = null;
		this.cascadeConstraints = false;
		
		this.tableNameDecidedState = new TableNameDecidedState(this);
		this.cascadeConstraintsDecidedState = new CascadeConstraintsDecidedState(this);
	}
	
	public synchronized void setSQLExecutor(OldSQLExecutor sqlExecutor)
	{
		if(sqlExecutor == null)
		{
			throw new IllegalArgumentException();
		}
		
		this.sqlExecutor = sqlExecutor;
	}
	
	
	public synchronized OldSQLExecutor getSQLExecutor()
	{
		return this.sqlExecutor;
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
	
	
	
	public TableNameDecidedState dropTable(String tableName)
	{
		Check.notBlankWithMessage(tableName);
		
		this.tableName = tableName.toUpperCase();
		
		return this.tableNameDecidedState;
	}
	
	
	/**
	 * This method generates 'drop' sql statement based on member variables (tableName, cascadeConstraints) 
	 * in this class. 
	 * @return 'drop' sql statement; never be null
	 */
	private synchronized String generateSQL()
	{
		Check.notBlankWithMessage(MissingDependancyException.class, this.tableName);

		String sql = "DROP TABLE " + this.tableName + (this.cascadeConstraints ? " CASCADE CONSTRAINTS" : "");

		return sql;
	}
	
	
	
	private synchronized Boolean varify() throws SQLException, InterruptedException, ClassNotFoundException
	{
		Boolean valid = null;
		if(this.tableInfoLibrary != null)
		{
			valid = this.tableInfoLibrary.checkTableAlreadyExists(this.tableName);
		}
		
		return valid;
	}
	
	
	private synchronized int execute() throws NumberFormatException, SQLException, InterruptedException, IOException, ClassNotFoundException
	{
		if(this.sqlExecutor == null)
		{
			throw new MissingDependancyException();
		}
		
		Boolean valid = this.varify();
		System.out.println("valid: "+valid);
		
		
		int result = -1;
		if((valid == null)||valid)
		{
			String sql = this.generateSQL();
			System.out.println(sql);
			result = (Integer) this.sqlExecutor.execute(sql);
			
			
			if(this.tableInfoLibrary != null)
			{
				this.tableInfoLibrary.removeTableInfo(this.tableName);
			}
		}
		
		return result;
	}
	
	
	private synchronized void cascadeConstraints()
	{
		this.cascadeConstraints = true;
	}
	
	
	
	public class TableNameDecidedState
	{
		private DropTableSQLProcessor root;
		
		public TableNameDecidedState(DropTableSQLProcessor root)
		{
			this.root = root;
		}
		
		public synchronized CascadeConstraintsDecidedState cascadeConstraints()
		{
			this.root.cascadeConstraints();
			return this.root.cascadeConstraintsDecidedState;
		}
		
		public synchronized int execute() throws NumberFormatException, SQLException, InterruptedException, IOException, ClassNotFoundException
		{
			return this.root.execute();
		}
	}
	
	
	public class CascadeConstraintsDecidedState
	{
		private DropTableSQLProcessor root;
		
		public CascadeConstraintsDecidedState(DropTableSQLProcessor root)
		{
			this.root = root;
		}
		
		public synchronized int execute() throws NumberFormatException, SQLException, InterruptedException, IOException, ClassNotFoundException
		{
			return this.root.execute();
		}
	}
	
	
	
	
	public static void main(String[] args) throws IOException, InstanceAlreadyExistException, InvalidConnectionException, SQLException, InterruptedException, ClassNotFoundException, InstanceNotExistException, DependencyNotSatisfiedException
	{
		TableInfoLibrary tableInfoLibrary = TableInfoLibraryFactory.getInstance();
		DropTableSQLProcessor dropTableSQLProcessor = new DropTableSQLProcessor();
		dropTableSQLProcessor.setSQLExecutor(SQLExecutorFactory.getInstance());
		dropTableSQLProcessor.setTableInfoLibrary(tableInfoLibrary);
		
		System.out.println(dropTableSQLProcessor.dropTable("temptemp4").cascadeConstraints().execute());
		System.out.println(dropTableSQLProcessor.dropTable("temp1").cascadeConstraints().execute());
		System.out.println(dropTableSQLProcessor.dropTable("temp2").cascadeConstraints().execute());
		System.out.println(dropTableSQLProcessor.dropTable("temp11").cascadeConstraints().execute());
	}
}
