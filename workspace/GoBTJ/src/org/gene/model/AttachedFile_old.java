package org.gene.model;

import com.google.gson.annotations.Expose;



public class AttachedFile_old
{
	@Expose private Long id;
	@Expose private Long articleId;
	@Expose private String filePath;
	
	public AttachedFile_old(){}
	
	public AttachedFile_old(Long id, Long articleId, String filePath)
	{
		this.setId(id);
		this.setArticleId(articleId);
		this.setFilePath(filePath);
	}
	
	public Long getId()
	{
		return id;
	}
	
	public void setId(Long id)
	{
		if(this.id==null && id!=null)
		{
			this.id = id;
		}
	}
	
	public Long getArticleId()
	{
		return articleId;
	}
	
	public void setArticleId(Long articleId)
	{
		if(this.articleId==null && articleId!=null)
		{
			this.articleId = articleId;	
		}
	}
	
	public String getFilePath()
	{
		return filePath;
	}
	
	public void setFilePath(String filePath)
	{
		if(this.filePath==null && filePath!=null)
		{
			this.filePath = filePath;
		}
	}
}
