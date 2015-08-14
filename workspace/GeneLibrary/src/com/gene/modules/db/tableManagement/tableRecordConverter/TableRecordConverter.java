package com.gene.modules.db.tableManagement.tableRecordConverter;





import com.gene.modules.db.tableManagement.data.Record;
import com.gene.modules.db.tableManagement.info.TableInfo;
import com.gene.modules.db.tableManagement.tableRecordConverter.externalData.Data;


public class TableRecordConverter
{
	public static synchronized Record convertToTableRecord(TableInfo tableInfo, Data externalData)
	{
		Record record = new Record();
		
		// modify START
		record.putColumn("fiscal_yr", externalData.getDataField("fiscal_year").getValue());
		record.putColumn("postal_qtr", externalData.getDataField("postal_quarter").getValue());
		record.putColumn("orgn_zip3_cd", externalData.getDataField("origin_zip_code").getValue());
		record.putColumn("dest_zip3_cd", externalData.getDataField("dest_zip_code").getValue());

		String targetMailClassColumn = null;
		int targetMailClassIndex = 0;
		String[] columnNames = tableInfo.getColumnNames();
		for(int i=0; i<columnNames.length; ++i)
		{
			if(columnNames[i].toLowerCase().startsWith(externalData.getDataField("mail_class_code").getValue().toLowerCase()))
			{
				targetMailClassColumn = columnNames[i];
				targetMailClassIndex = i;
				break;
			}
		}
		record.putColumn(targetMailClassColumn.toLowerCase(), externalData.getDataField("service_standard").getValue());
		
		for(int i=0; i<columnNames.length; ++i)
		{
			if((columnNames[i].toLowerCase().endsWith("_svc_std_nbr"))&&(i != targetMailClassIndex))
			{
				record.putColumn(columnNames[i], "-1");
			}
		}
		
		record.putColumn("updt_user_id", "None");
		record.putColumn("last_updt_dtm", "None");
		// modify END
		
		
		return record;
	}
}
