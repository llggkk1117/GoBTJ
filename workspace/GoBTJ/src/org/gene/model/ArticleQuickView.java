package org.gene.model;

import java.util.Date;

public class ArticleQuickView
{
	private Integer articleNumber;
	private String title;
	private String author;
	private Date lastUpdateTime;
	private Integer viewCount;
	
	public Integer getArticleNumber()
	{
		return articleNumber;
	}
	
	public void setArticleNumber(Integer articleNumber)
	{
		this.articleNumber = articleNumber;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getAuthor()
	{
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public Integer getViewCount() {
		return viewCount;
	}
	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}
}
