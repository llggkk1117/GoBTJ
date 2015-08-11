package org.gene.model;

import java.util.List;

import com.google.gson.annotations.Expose;

public class Board
{
	@Expose private String boardName;
	@Expose private List<String[]> articleList;
	
	public String getBoardName()
	{
		return boardName;
	}
	
	public void setBoardName(String boardName)
	{
		this.boardName = boardName;
	}
	
	public List<String[]> getArticleList()
	{
		return articleList;
	}
	
	public void setArticleList(List<String[]> articleList)
	{
		this.articleList = articleList;
	}
}
