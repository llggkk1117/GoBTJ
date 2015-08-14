package com.gene.modules.db.SQLProcessor;



import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;

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


public class TruncateTableSQLProcessor
{
	private OldSQLExecutor sqlExecutor;
	private TableInfoLibrary tableInfoLibrary;

	private String tableName;
	
	private TableNameDecidedState tableNameDecidedState;
	
	public TruncateTableSQLProcessor()
	{
		this.tableNameDecidedState = new TableNameDecidedState(this);
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
	
	
	
	public synchronized TableNameDecidedState truncateTable(String tableName)
	{
		if(StringUtils.isBlank(tableName))
		{
			throw new IllegalArgumentException();
		}

		this.tableName = tableName.toUpperCase();
		
		return this.tableNameDecidedState;
	}
	
	
	private synchronized String generateSQL()
	{
		if(Check.anyBlankExists(this.tableName))
		{
			throw new IllegalArgumentException();
		}
		
		String sql = "TRUNCATE TABLE " + this.tableName;

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
		if((valid == null) || valid)
		{
			String sql = this.generateSQL();
			System.out.println(sql);
			
			this.sqlExecutor.openSession();
			String[][] tempResult = this.sqlExecutor.execute(sql);
			this.sqlExecutor.commit();
			this.sqlExecutor.closeSession();
			
			if((Check.NoBlankExists(tempResult[0][0]))&&(Check.isAllNumeric(tempResult[0][0])))
			{
				result = Integer.parseInt(tempResult[0][0]);
			}
		}
		
		return result;
	}
	
	
	
	public class TableNameDecidedState
	{
		private TruncateTableSQLProcessor root;
		
		public TableNameDecidedState(TruncateTableSQLProcessor root)
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
		TruncateTableSQLProcessor truncateTableSQLProcessor = new TruncateTableSQLProcessor();
		truncateTableSQLProcessor.setSQLExecutor(SQLExecutorFactory.getInstance());
		truncateTableSQLProcessor.setTableInfoLibrary(tableInfoLibrary);
		
		System.out.println(truncateTableSQLProcessor.truncateTable("temptemp4").execute());
	}
}

