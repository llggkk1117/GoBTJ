package org.gene.modules.database.dbConnectionPool.dbSource;

import java.util.ArrayList;
import java.util.List;

public class DefaultDBSources
{
	public static List<DBSource> dbSources;
	static
	{
		dbSources = new ArrayList<DBSource>();
		dbSources.add(new DBSourceBean("jdbc:postgresql://localhost:5432/llggkk_gobtj", "llggkk", "wnsla486"));
		dbSources.add(new DBSourceBean("jdbc:postgresql://localhost:5432/gobtj", "postgres", "1234"));
	}
}
