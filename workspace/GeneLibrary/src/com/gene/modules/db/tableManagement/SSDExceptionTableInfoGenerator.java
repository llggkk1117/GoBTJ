package com.gene.modules.db.tableManagement;

import com.gene.modules.db.tableManagement.info.TableInfo;


public class SSDExceptionTableInfoGenerator
{
	private static TableInfo ssdTableScheme;
	
	public static TableInfo getSSDExceptionTableInfo()
	{
		if(ssdTableScheme == null)
		{
			ssdTableScheme = new TableInfo("SSD_EXCEPTION_T");
			ssdTableScheme.addColumnInfo("ORGN_ZIP3_CD", "varchar2 (9 char)", true, true);
			ssdTableScheme.addColumnInfo("DEST_ZIP3_CD", "varchar2 (9 char)", true, true);
			ssdTableScheme.addColumnInfo("PRI_SVC_STD_NBR", "number", false, false);
			ssdTableScheme.addColumnInfo("STD_SVC_STD_NBR", "number", false, false);
			ssdTableScheme.addColumnInfo("FCM_SVC_STD_NBR", "number", false, false);
			ssdTableScheme.addColumnInfo("PKG_SVC_STD_NBR", "number", false, false);
			ssdTableScheme.addColumnInfo("PER_SVC_STD_NBR", "number", false, false);
			ssdTableScheme.addColumnInfo("PMG_SVC_STD_NBR", "number", false, false);
			ssdTableScheme.addColumnInfo("FISCAL_YR", "varchar2 (2 char)", false, false);
			ssdTableScheme.addColumnInfo("POSTAL_QTR", "number", false, false);
			ssdTableScheme.addColumnInfo("UPDT_USER_ID", "varchar2 (32 char)", false, false);
			ssdTableScheme.addColumnInfo("LAST_UPDT_DTM", "varchar2 (32 char)", true, false);
			
			TableHoldPolicy tableHoldPolicy = new TableHoldPolicy();
			tableHoldPolicy.setTableHoldPeriod(TableHoldPolicy.PeriodType.DAY, 1);
//			tableHoldPolicy.setTableHoldPeriod(TableHoldPolicy.PeriodType.DAY, 3);
			tableHoldPolicy.setLastUpdateTimeColumnName("LAST_UPDT_DTM");
			tableHoldPolicy.setLastUpdateTimeFormatColumnFormat("yyyy-MM-dd H:mm:ss:SSS");
			
			ssdTableScheme.setTableHoldPolicy(tableHoldPolicy);
		}
		
		return ssdTableScheme;
	}
}
