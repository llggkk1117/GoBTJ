package com.gene.modules.db.SQLProcessor;

import java.io.IOException;
import java.sql.SQLException;



import com.gene.modules.exceptions.MissingDependancyException;
import com.gene.modules.db.SQLAdapter.TableCreator;
import com.gene.modules.db.SQLAdapter.TableCreatorFactory;
import com.gene.modules.db.SQLAdapter.TableRecordInserter;
import com.gene.modules.db.SQLAdapter.TableRecordInserterFactory;
import com.gene.modules.db.SQLExecutor.OldSQLExecutor;
import com.gene.modules.db.SQLExecutor.SQLExecutorFactory;
import com.gene.modules.db.connectionPoolAdaptor.DBSource.DBProperties;
import com.gene.modules.db.connectionPoolAdaptor.DBSource.DBSource;
import com.gene.modules.db.table.tableInfoLibrary.TableInfoLibrary;
import com.gene.modules.db.table.tableInfoLibrary.TableInfoLibraryFactory;


public class SQLProcessor
{
	private OldSQLExecutor sqlExecutor;
	private TableCreator tableCreator;
	private TableInfoLibrary tableInfoLibrary;
	private TableRecordInserter tableRecordInserter;
	
	private CreateTableSQLProcessor createTableSQLProcessor;
	private DropTableSQLProcessor dropTableSQLProcessor;
	private InsertSQLProcessor insertSQLProcessor;
	private UpdateSQLProcessor updateSQLProcessor;
	private TruncateTableSQLProcessor truncateTableSQLProcessor;
	private DeleteSQLProcessor deleteSQLProcessor;
	private SelectSQLProcessor selectSQLProcessor;
	
	private boolean ready;
	
	public SQLProcessor()
	{
		this.sqlExecutor = null;
		this.tableCreator = null;
		this.tableInfoLibrary = null;
		this.tableRecordInserter = null;
		
		this.createTableSQLProcessor = new CreateTableSQLProcessor();
		this.dropTableSQLProcessor = new DropTableSQLProcessor();
		this.insertSQLProcessor = new InsertSQLProcessor();
		this.updateSQLProcessor = new UpdateSQLProcessor();
		this.truncateTableSQLProcessor = new TruncateTableSQLProcessor();
		this.deleteSQLProcessor = new DeleteSQLProcessor();
		this.selectSQLProcessor = new SelectSQLProcessor();
		
		this.ready = false;
	}
	
	
	public synchronized void setSQLExecutor(OldSQLExecutor sqlExecutor)
	{
		this.sqlExecutor = sqlExecutor;
		this.setSQLExecutorDependancy();
		this.ready = this.isReadyToGo();
	}
	
	private synchronized void setSQLExecutorDependancy()
	{
		this.dropTableSQLProcessor.setSQLExecutor(this.sqlExecutor);
		this.truncateTableSQLProcessor.setSQLExecutor(this.sqlExecutor);
		this.updateSQLProcessor.setSQLExecutor(this.sqlExecutor);
		this.deleteSQLProcessor.setSQLExecutor(this.sqlExecutor);
		this.selectSQLProcessor.setSQLExecutor(this.sqlExecutor);
	}
	
	public synchronized OldSQLExecutor getSQLExecutor()
	{
		return this.sqlExecutor;
	}
	
	
	
	public synchronized void setTableCreator(TableCreator tableCreator)
	{
		this.tableCreator = tableCreator;
		this.setTableCreatorDependancy();
		this.ready = this.isReadyToGo();
	}
	
	private synchronized void setTableCreatorDependancy()
	{
		this.createTableSQLProcessor.setTableCreator(this.tableCreator);
	}
	
	public synchronized TableCreator getTableCreator()
	{
		return this.tableCreator;
	}
	
	
	
	public synchronized void setTableRecordInserter(TableRecordInserter tableRecordInserter)
	{
		this.tableRecordInserter = tableRecordInserter;
		this.setTableRecordInserterDependancy();
		this.ready = this.isReadyToGo();
	}
	
	private synchronized void setTableRecordInserterDependancy()
	{
		this.insertSQLProcessor.setTableRecordInserter(this.tableRecordInserter);
	}
	
	public synchronized TableRecordInserter getTableRecordInserter()
	{
		return this.tableRecordInserter;
	}

	
	
	public synchronized void setTableInfoLibrary(TableInfoLibrary tableInfoLibrary)
	{
		this.tableInfoLibrary = tableInfoLibrary;
		this.setTableInfoLibraryDependancy();
	}
	
	private synchronized void setTableInfoLibraryDependancy()
	{
		this.createTableSQLProcessor.setTableInfoLibrary(this.tableInfoLibrary);
		this.dropTableSQLProcessor.setTableInfoLibrary(this.tableInfoLibrary);
		this.truncateTableSQLProcessor.setTableInfoLibrary(this.tableInfoLibrary);
		this.insertSQLProcessor.setTableInfoLibrary(this.tableInfoLibrary);
		this.updateSQLProcessor.setTableInfoLibrary(this.tableInfoLibrary);
		this.deleteSQLProcessor.setTableInfoLibrary(this.tableInfoLibrary);
		this.selectSQLProcessor.setTableInfoLibrary(this.tableInfoLibrary);
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
		else if(this.tableCreator == null)
		{
			isReady = false;
		}
		else if(this.tableRecordInserter == null)
		{
			isReady = false;
		}
		else if(!this.createTableSQLProcessor.isReadyToGo())
		{
			isReady = false;
		}
		else if(!this.deleteSQLProcessor.isReadyToGo())
		{
			isReady = false;
		}
		else if(!this.dropTableSQLProcessor.isReadyToGo())
		{
			isReady = false;
		}
		else if(!this.insertSQLProcessor.isReadyToGo())
		{
			isReady = false;
		}
		else if(!this.selectSQLProcessor.isReadyToGo())
		{
			isReady = false;
		}
		else if(!this.truncateTableSQLProcessor.isReadyToGo())
		{
			isReady = false;
		}
		else if(!this.updateSQLProcessor.isReadyToGo())
		{
			isReady = false;
		}
		
		return isReady;
	}
	
	
	
	
	
	public CreateTableSQLProcessor.TableNameDecidedState createTable(String tableName)
	{
		if(!ready)
		{
			throw new MissingDependancyException();
		}
		
		return this.createTableSQLProcessor.createTable(tableName);
	}
	
	
	public DropTableSQLProcessor.TableNameDecidedState dropTable(String tableName)
	{
		if(!ready)
		{
			throw new MissingDependancyException();
		}
		
		return this.dropTableSQLProcessor.dropTable(tableName);
	}
	
	
	public TruncateTableSQLProcessor.TableNameDecidedState truncateTable(String tableName)
	{
		if(!ready)
		{
			throw new MissingDependancyException();
		}
		
		return this.truncateTableSQLProcessor.truncateTable(tableName);
	}
	
	public InsertSQLProcessor.PuttingColumnAndValueState insert(String tableName) throws SQLException, InterruptedException, IOException, ClassNotFoundException 
	{
		if(!ready)
		{
			throw new MissingDependancyException();
		}
		
		return this.insertSQLProcessor.insertInto(tableName);
	}
	
	
	public UpdateSQLProcessor.SetColumn update(String tableName)
	{
		if(!ready)
		{
			throw new MissingDependancyException();
		}
		
		return this.updateSQLProcessor.updateTable(tableName);
	}
	
	
	public DeleteSQLProcessor.TableNameDecidedState deleteFrom(String tableName)
	{
		if(!ready)
		{
			throw new MissingDependancyException();
		}
		
		return this.deleteSQLProcessor.deleteFrom(tableName);
	}
	
	
	public SelectSQLProcessor.TableNameDecidedState selectTable(String tableName) throws SQLException, InterruptedException, IOException, ClassNotFoundException
	{
		if(!ready)
		{
			throw new MissingDependancyException();
		}
		
		return this.selectSQLProcessor.selectTable(tableName);
	}
	
	
	
	public static void main(String[] args) throws InterruptedException, ClassNotFoundException, SQLException, IOException  
	{
		String dbPropertisFileName = "settings/database.properties";
		DBSource dbConnectionInfo = new DBProperties(dbPropertisFileName);
		
		OldSQLExecutor sqlExecutor = SQLExecutorFactory.getInstance(dbConnectionInfo.getDriver(), dbConnectionInfo.getURL(), dbConnectionInfo.getUserName(), dbConnectionInfo.getPassword());
    	TableInfoLibrary tableInfoLibrary = TableInfoLibraryFactory.getInstance(dbConnectionInfo.getDriver(), dbConnectionInfo.getURL(), dbConnectionInfo.getUserName(), dbConnectionInfo.getPassword());
    	TableCreator tableCreator = TableCreatorFactory.getInstance(dbConnectionInfo.getDriver(), dbConnectionInfo.getURL(), dbConnectionInfo.getUserName(), dbConnectionInfo.getPassword());
    	TableRecordInserter tableRecordInserter = TableRecordInserterFactory.getInstance(dbConnectionInfo.getDriver(), dbConnectionInfo.getURL(), dbConnectionInfo.getUserName(), dbConnectionInfo.getPassword());
    	
		SQLProcessor sqlProcessor = new SQLProcessor();
		sqlProcessor.setSQLExecutor(sqlExecutor);
		sqlProcessor.setTableCreator(tableCreator);
		sqlProcessor.setTableInfoLibrary(tableInfoLibrary);
		sqlProcessor.setTableRecordInserter(tableRecordInserter);
		
		System.out.println(sqlProcessor.isReadyToGo());
		
		
//		int result = -100;
//		result = p.truncateTable("temptemp90").execute();
//		System.out.println(result);
//		result = p.dropTable("temptemp90").cascadeConstraints().execute();
//		System.out.println(result);
//		result = p.createTable("temptemp90").createColumn("col1", "varchar2", true, true).createColumn("col2", "number", false, false).execute();
//		System.out.println(result);
//		result = p.insert("temptemp90").column(1, "aa").column(2, 123).force().execute();
//		System.out.println(result);
//		result = p.update("temptemp90").setColumn("col1").is("bb").whereColumn("col1").sameAs("aa").execute();
//		System.out.println(result);
//		result = p.insert("temptemp90").column("col1", "bb").force().execute();
//		System.out.println(result);
////		result = p.deleteFrom("temptemp90").where(2).sameAs(123).execute();
////		System.out.println(result);
////		result = p.deleteFrom("temptemp90").filter("'aa' = 'aa'").filter("rownum > 1").execute();
////		System.out.println(result);
//		TableRecord[] tr = p.selectTable("temptemp90").selectColumn(1,2).where("col1").sameAs("bb").orderBy(2).decrease().execute();
//		for(int i=0; i<tr.length; ++i)
//		{
//			System.out.println(tr[i].getTableColumnRecord("col1").getValue());
//			System.out.println(tr[i].getTableColumnRecord(2).getValue());
//		}
	}
}
