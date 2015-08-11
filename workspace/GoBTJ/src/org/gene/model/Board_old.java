package org.gene.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;


public class Board_old
{
	@Expose  private Long id;
	@Expose private String boardName;
	@Expose private List<Article_old> articles;

	public Board_old(){}
	
	public Board_old(Long id, String boardName)
	{
		this.setId(id);
		this.setBoardName(boardName);
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
	
	public String getBoardName()
	{
		return boardName;
	}
	
	public void setBoardName(String boardName)
	{
		if(this.boardName==null && boardName!=null)
		{
			this.boardName = boardName;
		}
	}
	
	public List<Article_old> getArticles()
	{
		return this.articles;
	}
	
	public void addArticle(Article_old article)
	{
		if(article!=null)
		{
			if(this.articles==null)
			{
				this.articles = new ArrayList<Article_old>();
			}
			this.articles.add(article);
		}
	}
	
	public void addArticles(List<Article_old> articles)
	{
		if(articles!=null)
		{
			if(this.articles==null)
			{
				this.articles = articles;
			}
			else
			{
				this.articles.addAll(articles);
			}
		}
	}
}
