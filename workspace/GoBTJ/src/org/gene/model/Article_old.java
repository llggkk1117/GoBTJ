package org.gene.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gson.annotations.Expose;


public class Article_old
{
	@Expose private Long id;
	@Expose private Long boardId; 
	@Expose private Long memberId;
	@Expose private Integer articleNumber;
	@Expose private String title;
	@Expose private String contents;
	@Expose private Integer viewCount;
	@Expose private Long commentOf;
	@Expose private Date lastUpdateTime;
	
	@Expose private User user;
	@Expose private List<Article_old> comments;
	@Expose private List<AttachedFile_old> attachedFiles;
	
	private LinkedHashMap<String, Object> changeHistories;
	
	public Long getId() 
	{
		return id;
	}
	
	public void setId(Long id) 
	{
		if(this.id==null)
		{
			this.id = id;
		}
	}
	
	public Long getBoardId() 
	{
		return boardId;
	}
	
	public void setBoardId(Long boardId) 
	{
		if(this.boardId==null && boardId!=null)
		{
			this.boardId = boardId;
		}
	}
	
	public Long getMemberId() 
	{
		return memberId;
	}
	
	public void setMemberId(Long memberId) 
	{
		if(this.memberId==null && memberId!=null)
		{
			this.memberId = memberId;
		}
	}
	
	public int getArticleNumber() 
	{
		return articleNumber;
	}
	
	public void setArticleNumber(Integer articleNumber) 
	{
		if(this.articleNumber==null && articleNumber!=null)
		{
			this.articleNumber = articleNumber;
		}
	}
	
	public String getTitle() 
	{
		return title;
	}
	
	public void setTitle(String title) 
	{
		if(title!=null)
		{
			 if(this.title!=null)
			 {
				 this.addChangeHistory("title", title);
			 }
			 this.title = title;	
		}
	}
	
	public String getContents()
	{
		return contents;
	}
	
	public void setContents(String contents)
	{
		if(contents!=null)
		{
			 if(this.contents!=null)
			 {
				 this.addChangeHistory("contents", contents);
			 }
			 this.contents = contents;	
		}
	}
	
	public int getViewCount()
	{
		return viewCount;
	}
	
	public void setViewCount(Integer viewCount)
	{
		if(viewCount!=null)
		{
			 if(this.viewCount!=null)
			 {
				 this.addChangeHistory("viewCount", viewCount);
			 }
			 this.viewCount = viewCount;	
		}
	}
	
	public Long getCommentOf()
	{
		return commentOf;
	}

	public void setCommentOf(Long commentOf)
	{
		this.commentOf = commentOf;
	}

	public Date getLastUpdateTime()
	{
		return this.lastUpdateTime;
	}
	
	public void setLastUpdateTime(Date lastUpdatedTime)
	{
		if(lastUpdatedTime!=null)
		{
			 if(this.lastUpdateTime!=null)
			 {
				 this.addChangeHistory("lastUpdatedTime", lastUpdatedTime);
			 }
			 this.lastUpdateTime = lastUpdatedTime;	
		}
	}
	
	
	
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Article_old> getComments()
	{
		return this.comments;
	}
	
	public void addComment(Article_old comment)
	{
		if(comment!=null)
		{
			if(this.comments==null)
			{
				this.comments = new ArrayList<Article_old>();
			}
			this.comments.add(comment);
		}
	}
	
	
	public void addComments(List<Article_old> comments)
	{
		if(comments!=null)
		{
			if(this.comments==null)
			{
				this.comments = comments;
			}
			else
			{
				this.comments.addAll(comments);
			}
		}
	}
	
	
	public List<AttachedFile_old> getAttachedFiles()
	{
		return this.attachedFiles;
	}
	
	public void addAttachedFile(AttachedFile_old attachedFile)
	{
		if(attachedFile!=null)
		{
			if(this.attachedFiles==null)
			{
				this.attachedFiles = new ArrayList<AttachedFile_old>();
			}
			this.attachedFiles.add(attachedFile);
		}
	}
	
	public void addAttachedFiles(List<AttachedFile_old> attachedFiles)
	{
		if(attachedFiles!=null)
		{
			if(this.attachedFiles==null)
			{
				this.attachedFiles = attachedFiles;
			}
			else
			{
				this.attachedFiles.addAll(attachedFiles);
			}
		}
	}
	
	
	
	
	private void addChangeHistory(String fieldName, Object newValue)
	{
		if(fieldName!=null && !"".equals(fieldName))
		{
			if(this.changeHistories==null)
			{
				this.changeHistories = new LinkedHashMap<String, Object>();
			}
			this.changeHistories.put(fieldName, (newValue==null ? "NULL" : newValue));
		}
	}
	
	public List<String> getChangeHistories()
	{
		List<String> result = null;
		if(this.changeHistories!=null)
		{
			result = new ArrayList<String>();
			Iterator<String> key = this.changeHistories.keySet().iterator();
			while(key.hasNext())
			{
				String fieldName = key.next();
				Object newValue = this.changeHistories.get(fieldName);
				String sql = "UPDATE articles SET "+fieldName+"="+newValue+" WHERE id="+this.id;
				result.add(sql);
			}
		}
		return result;
	}
}
