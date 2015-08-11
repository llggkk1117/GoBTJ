package org.gene.model;

import java.util.Date;
import java.util.List;

import com.google.gson.annotations.Expose;

public class Article
{
	@Expose private Integer articleNumber;
	@Expose private String title;
	@Expose private String contents;
	@Expose private String authorName;
	@Expose private Integer viewCount;
	@Expose private Date lastUpdateTime;
	@Expose private List<Article> comments;
	@Expose private List<String> attachedFilePaths;
	
	public Integer getArticleNumber() {
		return articleNumber;
	}
	public void setArticleNumber(Integer articleNumber) {
		this.articleNumber = articleNumber;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public Integer getViewCount() {
		return viewCount;
	}
	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public List<Article> getComments() {
		return comments;
	}
	public void setComments(List<Article> comments) {
		this.comments = comments;
	}
	public List<String> getAttachedFilePaths() {
		return attachedFilePaths;
	}
	public void setAttachedFilePaths(List<String> attachedFilePaths) {
		this.attachedFilePaths = attachedFilePaths;
	}
}
