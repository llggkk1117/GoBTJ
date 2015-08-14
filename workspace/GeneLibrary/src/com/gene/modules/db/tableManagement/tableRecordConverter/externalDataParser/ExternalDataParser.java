package com.gene.modules.db.tableManagement.tableRecordConverter.externalDataParser;

import com.gene.modules.db.tableManagement.tableRecordConverter.externalData.Data;




public interface ExternalDataParser
{
	public Data parse(String line);	
}
