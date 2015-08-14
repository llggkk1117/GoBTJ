package com.gene.modules.db.tableManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;






import com.gene.modules.check.Check;
import com.gene.modules.db.tableManagement.info.TableInfo;
import com.gene.modules.exceptions.MissingDependancyException;



public class TableInfoLibrary
{
	private TableInfoLoader tableInfoLoader;
	private HashMap<String, TableInfo> tableInfoRegistry;
	
	public TableInfoLibrary()
	{
		this.tableInfoRegistry = new HashMap<String, TableInfo>();
	}
	
	public void setTableInfoLoader(TableInfoLoader tableInfoLoader)
	{
		Check.notNull(tableInfoLoader);
		this.tableInfoLoader = tableInfoLoader;
	}
	
	public TableInfoLoader getTableInfoLoader()
	{
		return this.tableInfoLoader;
	}
	
	
	public TableInfo load(String tableName) throws SQLException, InterruptedException, IOException, ClassNotFoundException  
	{
		Check.notBlank(tableName);
		Check.notNull(MissingDependancyException.class, this.tableInfoLoader);
		
		tableName = tableName.toLowerCase();
		TableInfo tableInfo = this.tableInfoLoader.getTableInfo(tableName);

		if(tableInfo != null)
		{
			this.tableInfoRegistry.put(tableName, tableInfo);
		}
		
		return tableInfo;
	}
	
	
	
	
	public synchronized boolean checkTableAlreadyExists(String tableName) throws SQLException, InterruptedException, ClassNotFoundException
	{
		Check.notBlank(tableName);
		Check.notNull(MissingDependancyException.class, this.tableInfoLoader);
		
		boolean exist = (this.tableInfoRegistry.get(tableName.toLowerCase()) != null);
		if(!exist)
		{
			exist = this.tableInfoLoader.checkTableExist(tableName);
		}
		
		return exist;
	}
	
	
	
	
	public synchronized boolean add(TableInfo tableInfo) throws SQLException, InterruptedException, ClassNotFoundException
	{
		Check.notNull(tableInfo);
		
		boolean added = false;
		if(!this.checkTableAlreadyExists(tableInfo.getTableName()))
		{
			this.tableInfoRegistry.put(tableInfo.getTableName().toLowerCase(), tableInfo);
			added = true;
		}
		
		return added;
	}
	

	
	/**
	 * 
	 * @param tableName
	 * @return can be null
	 * @throws SQLException
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	public synchronized TableInfo getTableInfo(String tableName) throws SQLException, InterruptedException, IOException, ClassNotFoundException 
	{
		Check.notBlank(tableName);
		
		tableName = tableName.toLowerCase();
		TableInfo tableInfo = this.tableInfoRegistry.get(tableName);
		if(tableInfo == null)
		{
			tableInfo = this.load(tableName);
		}
		
		return tableInfo;
	}
	
	
	
	
	public synchronized boolean removeTableInfo(String tableName)
	{
		Check.notBlank(tableName);
		
		boolean removed = (this.tableInfoRegistry.remove(tableName.toLowerCase()) != null);
		
		return removed;
	}
}
