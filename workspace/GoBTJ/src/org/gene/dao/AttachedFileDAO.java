package org.gene.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.gene.model.AttachedFile_old;

public class AttachedFileDAO extends AbstractDAO
{
	public static List<AttachedFile_old> retrieveAttachedFiles(Long articleId) throws ClassNotFoundException, InterruptedException, SQLException
	{
		List<AttachedFile_old> attachedFiles = null;
		if(articleId!=null && articleId>0)
		{
			String sql = "select id, article_id, file_name, file_path from attached_files where article_id='?'";
			db.openSession();
			@SuppressWarnings("unchecked")
			List<String[]> result = (List<String[]>) db.execute(sql, articleId);
			db.closeSession();
			
			if(result.size()>=2)
			{
				attachedFiles = new ArrayList<AttachedFile_old>();
				for(int i=1; i<result.size(); ++i)
				{
					String[] tuple = result.get(i);
					AttachedFile_old attachedFile = new AttachedFile_old();
					attachedFile.setId(Long.parseLong(tuple[0]));
					attachedFile.setArticleId(Long.parseLong(tuple[1]));
					attachedFile.setFilePath(tuple[2]);
					attachedFile.setFilePath(tuple[3]);
					
					attachedFiles.add(attachedFile);
				}
			}
		}
		
		return attachedFiles;
	}
}