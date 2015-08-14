package com.gene.modules.db.SQLProcessor;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

import com.gene.modules.exceptions.MissingDependancyException;
import com.gene.modules.check.Check;
import com.gene.modules.db.SQLAdapter.TableCreator;
import com.gene.modules.db.SQLExecutor.SQLExecutorFactory;




public class CreateTableSQLProcessor
{
	private TableCreator tableCreator;
	private TableInfoLibrary tableInfoLibrary;
	private TableInfo tableInfo;
	
	private TableNameDecidedState tableNameDecidedState;
	private ColumnInfoDecidedState columnInfoDecidedState;
	
	public CreateTableSQLProcessor()
	{
		this.tableInfo = null;
		this.tableCreator = null;
		this.tableInfo = null;
		
		this.tableNameDecidedState = new TableNameDecidedState(this);
		this.columnInfoDecidedState = new ColumnInfoDecidedState(this);
	}
	
	public synchronized void setTableCreator(TableCreator tableCreator)
	{
		if(tableCreator == null)
		{
			throw new IllegalArgumentException();
		}
		
		this.tableCreator = tableCreator;
	}
	
	public synchronized TableCreator getTableCreator()
	{
		return this.tableCreator;
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
		
		if(this.tableCreator == null)
		{
			isReady = false;
		}
		
		return isReady;
	}
	
	
	
	public synchronized TableNameDecidedState createTable(String tableName)
	{
		Check.notBlankWithMessage(tableName);
		
		if(!this.isReadyToGo())
		{
			throw new MissingDependancyException();
		}
		
		this.tableInfo = null;
		this.tableInfo = new TableInfo(tableName);
		
		return this.tableNameDecidedState;
	}
	
	
	private synchronized void createColumn(String columnName, String dataType, boolean isNotNull, boolean isPrimaryKey)
	{
		Check.notBlankWithMessage(columnName);
		
		String data_type = dataType;
		
		if(!CreateTableSQLProcessor.isCorrectDataType(data_type))
		{
			final int DEFAULT_CHAR_LENGTH = 20;
			final String DEFAULT_DATA_TYPE= "VARCHAR2 ("+DEFAULT_CHAR_LENGTH+" CHAR)";
			
			data_type = DEFAULT_DATA_TYPE;
		}
		
		this.tableInfo.addColumn(columnName, data_type, isNotNull, isPrimaryKey);
	}
	
	
	private static synchronized boolean isCorrectDataType(String dataType)
	{
		boolean isCorrectDataType = false;
		
		if(!Check.isNotBlank(dataType))
		{
			String data_type = dataType.toUpperCase().replace(" ", "");
			
			if(Pattern.compile("VARCHAR2\\(([0-9]+)(CHAR)?\\)").matcher(data_type).matches())
			{
				String size = data_type.toUpperCase().replace("VARCHAR2", "").replace("(", "").replace(")", "");
				boolean valid = false;
				if(size.indexOf("CHAR") > 0)
				{
					final int MAX_SIZE = 128;
					final int MIN_SIZE = 0;
					
					boolean isInteger = false;
					int num = 0;
					try
					{
						num = Integer.parseInt(size.replace("CHAR", ""));
						isInteger = true;
					}
					catch(Exception e){}
					
					if(isInteger)
					{
						if((num >= MIN_SIZE)&&(num <= MAX_SIZE))
						{
							valid = true;
						}
					}
				}
				else
				{
					final int MAX_SIZE = 255;
					final int MIN_SIZE = 0;
					
					boolean isInteger = false;
					int num = 0;
					try
					{
						num = Integer.parseInt(size);
						isInteger = true;
					}
					catch(Exception e){}
					
					if(isInteger)
					{
						if((num >= MIN_SIZE)&&(num <= MAX_SIZE))
						{
							valid = true;
						}
					}
				}
				
				isCorrectDataType = valid;
			}
			else if(Pattern.compile("NUMBER(\\((([0-9]+)|(((\\*)|([0-9]+)),((\\*)|([0-9]+))))\\))?").matcher(data_type).matches())
			{
				final int MAX_SIZE = 38;
				final int MIN_SIZE = -38; 
				
				String size = data_type.toUpperCase().replace("NUMBER", "").replace("(", "").replace(")", "");
				String[] sizeElementsTemp = size.split(",");
				int[] sizeElements = new int[sizeElementsTemp.length];
				
				boolean isAllInteger = true;
				for(int i=0; i<sizeElementsTemp.length; ++i)
				{
					try
					{
						sizeElements[i] = Integer.parseInt(sizeElementsTemp[i]);
					}
					catch(Exception e)
					{
						isAllInteger = false;
						break;
					}
				}
				
				boolean valid = false;
				if(isAllInteger)
				{
					valid = true;
					for(int i=0; i<sizeElements.length; ++i)
					{
						if((sizeElements[i] > MAX_SIZE)||(sizeElements[i] < MIN_SIZE))
						{
							valid = false;
							break;
						}
					}
				}
				
				isCorrectDataType = valid;
			}
		}
			
		return isCorrectDataType;
	}
	
	
	private synchronized int execute() throws SQLException, InterruptedException, NumberFormatException, ClassNotFoundException 
	{
		Check.checkNotBlank(MissingDependancyException.class, this.tableCreator);
		Check.notBlankWithMessage(MissingDependancyException.class, this.tableInfo.getTableName());
		Check.checkTrue(MissingDependancyException.class, (this.tableInfo.getColumnNames().length == 0));

		int result = this.tableCreator.createTable(this.tableInfo);
		if((this.tableInfoLibrary != null)&&(result == 0))
		{
			this.tableInfoLibrary.add(this.tableInfo);
		}
		
		return result;
	}
	
	
	
	
	public class TableNameDecidedState
	{
		private CreateTableSQLProcessor root;
		
		public TableNameDecidedState(CreateTableSQLProcessor root)
		{
			this.root = root;
		}
		
		public synchronized ColumnInfoDecidedState createColumn(String columnName, String dataType, boolean isNotNull, boolean isPrimaryKey)
		{
			this.root.createColumn(columnName, dataType, isNotNull, isPrimaryKey);
			return this.root.columnInfoDecidedState;
		}
	}
	
	
	
	public class ColumnInfoDecidedState
	{
		private CreateTableSQLProcessor root;
		
		public ColumnInfoDecidedState(CreateTableSQLProcessor root)
		{
			this.root = root;
		}
		
		public synchronized ColumnInfoDecidedState createColumn(String columnName, String dataType, boolean isNotNull, boolean isPrimaryKey)
		{
			this.root.createColumn(columnName, dataType, isNotNull, isPrimaryKey);
			return this;
		}
		
		public synchronized int execute() throws SQLException, InterruptedException, NumberFormatException, ClassNotFoundException 
		{
			return this.root.execute();
		}
	}
	
	
	
	
	
	public static void main(String[] args) throws InterruptedException, ClassNotFoundException, SQLException, IOException 
	{
		TableCreator tableCreator = new TableCreator();
		tableCreator.setSQLExecutor(SQLExecutorFactory.getInstance());

		TableInfoLibrary tableInfoLibrary = TableInfoLibraryFactory.getInstance();
		
		
		CreateTableSQLProcessor createTableSQLProcessor = new CreateTableSQLProcessor();
		createTableSQLProcessor.setTableCreator(tableCreator);
		createTableSQLProcessor.setTableInfoLibrary(tableInfoLibrary);
		
		System.out.println(createTableSQLProcessor.createTable("temptemp4").createColumn("column1", null, true, true).createColumn("column2", "number", true, true).createColumn("column3", "varchar2(5 char)", false, false).execute());
		TableInfo temp = tableInfoLibrary.getTableInfo("temptemp4");
		System.out.println(temp.getTableName());
	}
}
