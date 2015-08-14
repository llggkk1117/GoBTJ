package com.gene.modules.db.tableManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

import com.gene.modules.db.SQLExecutor.SQLExecutor;
import com.gene.modules.db.SQLExecutor.SQLExecutorFactory;
import com.gene.modules.db.tableManagement.info.ColumnInfo;
import com.gene.modules.db.tableManagement.info.TableInfo;
import com.gene.modules.db.tableManagement.table.TableData;
import com.gene.modules.db.utils.Constants.DB;


public class TableManager
{
	private static final Logger logger = Logger.getLogger(TableManager.class);
	
	private SQLExecutor sqlExecutor;
	private TableInfo tableInfo;
	private Boolean tableExist;
	
	public TableManager()
	{
		this.sqlExecutor = null;
		this.tableInfo = null;
		this.tableExist = null;
	}
	
	public synchronized void setTableInfo(TableInfo tableInfo)
	{
		this.tableInfo = tableInfo;
	}
	
	public synchronized TableInfo getTableInfo()
	{
		return this.tableInfo;
	}
	
	public synchronized void setSqlExecutor(SQLExecutor sqlExecutor)
	{
		this.sqlExecutor = sqlExecutor;
	}
	
	public synchronized SQLExecutor getSqlExecutor()
	{
		return this.sqlExecutor;
	}
	
	private synchronized boolean checkTableAlreadyExists() throws ClassNotFoundException, SQLException, InterruptedException
	{
		if(this.tableExist == null)
		{
			this.tableExist = !((TableData)this.sqlExecutor.execute(DB.SQL.Oracle.tableExistCheckSQL, this.tableInfo.getTableName())).isEmpty();
		}
		
		return this.tableExist;
	}
	
	private synchronized String getCreatTableSQL()
	{
		String tableName = this.tableInfo.getTableName();
		String[] columnNames = this.tableInfo.getColumnNames();

		String sql_create = "CREATE TABLE "+tableName+" (";
		
		ColumnInfo tableColumn = null;
		for(int i=0; i<columnNames.length; ++i)
		{
			tableColumn = this.tableInfo.getColumnInfo(columnNames[i]);
			if(tableColumn != null)
			{
				sql_create += columnNames[i];
				String dataType = tableColumn.getDataType();
				sql_create += " "+ ((dataType != null) ? dataType.toLowerCase() : "VARCHAR2 (20 CHAR)");
					
				if(tableColumn.isNotNull())
				{
					sql_create += " NOT NULL ENABLE";
				}
				
				if(i < columnNames.length-1)
				{
					sql_create += ", ";
				}
			}
		}
		
		
		//find out how many primary keys exist
		int numOfPrimaryKeys = 0;
		for(int i=0; i<columnNames.length; ++i)
		{
			tableColumn = this.tableInfo.getColumnInfo(columnNames[i]);
			if(tableColumn != null)
			{
				if(tableColumn.isPrimaryKey())
				{
					numOfPrimaryKeys++;
				}
			}
		}
		

		if(numOfPrimaryKeys > 0)
		{
			int numOfPrimaryKeys_temp = numOfPrimaryKeys;
			sql_create += ", constraint "+tableName+"_pk primary key (";
			for(int i=0; i<columnNames.length; ++i)
			{
				tableColumn = this.tableInfo.getColumnInfo(columnNames[i]);
				if(tableColumn != null)
				{
					if(tableColumn.isPrimaryKey())
					{
						sql_create += columnNames[i];
						numOfPrimaryKeys_temp--;
						if(numOfPrimaryKeys_temp > 0)
						{
							sql_create += ", ";
						}
					}
				}
			}
			sql_create+=")";
		}
		
		sql_create += ")";
		
		
		return sql_create.toUpperCase();
	}
	
	
	private synchronized String getDropTableSQL()
	{
		String dropTableSQL = "DROP TABLE "+this.tableInfo.getTableName()+" CASCADE CONSTRAINTS";
		return dropTableSQL;
	}
	

	public synchronized void createTable() throws ClassNotFoundException, SQLException, InterruptedException
	{
		if(!this.checkTableAlreadyExists())
		{	
			logger.trace("Creating table: "+this.tableInfo.getTableName());
			
			String createTableSQL = this.getCreatTableSQL(); 
			String grantSelectSQL = "GRANT SELECT ON "+this.tableInfo.getTableName()+" TO PUBLIC";
			
			this.sqlExecutor.execute(createTableSQL);
			this.sqlExecutor.execute(grantSelectSQL);
			
			this.tableExist = true;
		}
	}
	
	
	public synchronized void dropTable() throws ClassNotFoundException, SQLException, InterruptedException
	{
		if(this.checkTableAlreadyExists())
		{	
			logger.trace("Dropping table: "+this.tableInfo.getTableName());
			
			String dropTableSQL = this.getDropTableSQL(); 
			this.sqlExecutor.execute(dropTableSQL);
			this.tableExist = false;
		}
	}
	
	
	private synchronized Boolean isTableHoldPeriodPassed() throws ClassNotFoundException, SQLException, InterruptedException
	{
		TableHoldPolicy tableHoldPolicy = this.tableInfo.getTableHoldPolicy();
		
		String[][] result_lastUpdateTime = null;
		if(this.checkTableAlreadyExists())
		{
			String sql_lastUpdateTime = "select max("+tableHoldPolicy.getLastUpdateTimeColumnName()+") from "+this.tableInfo.getTableName();
			result_lastUpdateTime = (String[][]) this.sqlExecutor.execute(sql_lastUpdateTime);
		}
		
		Boolean tableHoldPeriodPassed = null;
		if((result_lastUpdateTime != null)&&(result_lastUpdateTime.length > 1))
		{
			Date lastUpdateTime = null;
			try
			{
				lastUpdateTime = (new SimpleDateFormat(tableHoldPolicy.getLastUpdateTimeFormatColumnFormat())).parse(result_lastUpdateTime[1][0]);
			}
			catch(Exception e){}
			
			if(lastUpdateTime != null)
			{
				Date now =Calendar.getInstance().getTime();
				Calendar gc = new GregorianCalendar();
				gc.setTime(lastUpdateTime);
				gc.add(tableHoldPolicy.getTableHoldPeriodType(), tableHoldPolicy.getTableHoldPeriod());
				Date latestEffectiveTime = gc.getTime();
				tableHoldPeriodPassed = (now.compareTo(latestEffectiveTime) > 0);
				System.out.println("latestEffectiveTime: "+latestEffectiveTime);
			}
		}
		
		return tableHoldPeriodPassed;
	}
	

	
	public synchronized void initializeTable() throws ClassNotFoundException, SQLException, InterruptedException
	{
		if((this.checkTableAlreadyExists())&&(!Boolean.FALSE.equals(this.isTableHoldPeriodPassed())))
		{
			this.dropTable();
		}
		
		if(!this.checkTableAlreadyExists())
		{
			this.createTable();
		}
	}
	
	
	public static void main(String[] args) throws ClassNotFoundException, InterruptedException, SQLException, IOException
	{
		SQLExecutor sqlExecutor = SQLExecutorFactory.getInstance();
		TableInfo tableInfo = SSDExceptionTableInfoGenerator.getSSDExceptionTableInfo();
		
		TableManager tableManager = new TableManager();
		tableManager.setSqlExecutor(sqlExecutor);
		tableManager.setTableInfo(tableInfo);
		
		tableManager.dropTable();
		tableManager.createTable();
		sqlExecutor.execute("Insert into SSD_EXCEPTION_T (ORGN_ZIP3_CD,DEST_ZIP3_CD,PRI_SVC_STD_NBR,STD_SVC_STD_NBR,FCM_SVC_STD_NBR,PKG_SVC_STD_NBR,PER_SVC_STD_NBR,PMG_SVC_STD_NBR,FISCAL_YR,POSTAL_QTR,UPDT_USER_ID,LAST_UPDT_DTM) values ('12345','65498',9,-1,3,-1,-1,-1,'13',2,'SSD_EXCEPTION_BATCH','2013-04-15 5:55:06:911')");
		Thread.sleep(1000);
		tableManager.initializeTable();
	}
}
