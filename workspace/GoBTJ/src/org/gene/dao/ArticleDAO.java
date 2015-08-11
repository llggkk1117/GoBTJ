package org.gene.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.gene.model.Article;
import org.gene.model.Article_old;
import org.gene.modules.utils.DateUtils;



public class ArticleDAO extends AbstractDAO
{
	public static Integer increaseViewCount(Long articleId) throws ClassNotFoundException, InterruptedException, SQLException
	{
		Integer increasedViewCount = null;
		if(articleId!=null && articleId>0)
		{
			String sql1 = 
					"update articles"
					+ " set view_count=(select view_count from articles where id='?')+1"
					+ " where id='?'";
			String sql2 = "select view_count from articles where id='?'";
			
			db.openSession();
			db.execute(sql1, articleId, articleId);
			db.commit();
			@SuppressWarnings("unchecked")
			List<String[]> result = (List<String[]>) db.execute(sql2, articleId);
			db.closeSession();
			if(result.size()==2)
			{
				increasedViewCount = Integer.parseInt(result.get(1)[0]);
			}
		}
		
		return increasedViewCount;
	}
	
	public static Long lookUpArticleId(String boardName, int articleNumber) throws ClassNotFoundException, InterruptedException, SQLException
	{
		Long articleId = null;
		if(boardName!=null && !"".equals(boardName) && articleNumber>0)
		{
			String sql = "select a.id from articles a, boards b where a.board_id=b.id and b.board_name='?' and a.article_number='?'";
			db.openSession();
			@SuppressWarnings("unchecked")
			List<String[]> result = (List<String[]>) db.execute(sql, boardName, articleNumber);
			db.closeSession();
			if(result.size()==2)
			{
				articleId = Long.parseLong(result.get(1)[0]);
			}
		}
		return articleId;
	}
	
	
	public static Article retrieveArticle(Long articleId) throws ClassNotFoundException, InterruptedException, SQLException
	{
		Article article = null;
		if(articleId!=null && articleId>0)
		{
			article = retrieveArticleItself(articleId);
			if(article!=null)
			{
				List<Article> comments = retrieveArticleComments(articleId);
				if(comments!=null)
				{
					article.setComments(comments);
				}
				List<String> attachedFilePaths = retrieveArticleAttachedFilePaths(articleId);
				if(attachedFilePaths!=null)
				{
					article.setAttachedFilePaths(attachedFilePaths);
				}
			}
		}
		
		return article;
	}
	
	
	private static Article retrieveArticleItself(Long articleId) throws ClassNotFoundException, InterruptedException, SQLException
	{
		Article article = null;
		if(articleId!=null && articleId>0)
		{
			String sql = "select a.article_number, a.title, a.contents, m.korean_name, m.english_name, a.view_count, a.last_update_time"
						+ " from articles a, members m"
						+ " where a.member_id=m.id and a.id='?'";
			db.openSession();
			@SuppressWarnings("unchecked")
			List<String[]> result = (List<String[]>) db.execute(sql, articleId);
			db.closeSession();
			
			if(result.size()==2)
			{
				String[] tuple = result.get(1);
				article = new Article();
				article.setArticleNumber(Integer.parseInt(tuple[0]));
				article.setTitle(tuple[1]);
				article.setContents(tuple[2]);
				String authorName = (tuple[3]!=null && !"".equals(tuple[3]))? tuple[3] : tuple[4];
				article.setAuthorName(authorName);
				article.setViewCount(Integer.parseInt(tuple[5]));
				article.setLastUpdateTime(DateUtils.convertToDate(tuple[6]));
			}
		}
		
		return article;
	}
	
	
	private static List<Article> retrieveArticleComments(Long articleId) throws ClassNotFoundException, InterruptedException, SQLException
	{
		List<Article> comments = null;
		if(articleId!=null && articleId>0)
		{
			String sql = "select a.contents, m.korean_name, m.english_name, a.last_update_time, a.id"
						+ " from articles a, members m"
						+ " where a.member_id=m.id and a.comment_of='?'";
			db.openSession();
			@SuppressWarnings("unchecked")
			List<String[]> result = (List<String[]>) db.execute(sql, articleId);
			db.closeSession();
			
			if(result.size()>=2)
			{
				comments = new ArrayList<Article>();
				for(int i=1; i<result.size(); ++i)
				{
					String[] tuple = result.get(i);
					Article comment = new Article();
					comment.setContents(tuple[0]);
					String authorName = (tuple[1]!=null && !"".equals(tuple[1]))? tuple[1] : tuple[2];
					comment.setAuthorName(authorName);
					comment.setLastUpdateTime(DateUtils.convertToDate(tuple[3]));
					Long commentId = Long.parseLong(tuple[4]);
					List<Article> cmts = retrieveArticleComments(commentId);
					if(cmts!=null)
					{
						comment.setComments(cmts);
					}
					comments.add(comment);
				}
			}
		}
		
		return comments;
	}
	
	
	private static List<String> retrieveArticleAttachedFilePaths(Long articleId) throws ClassNotFoundException, InterruptedException, SQLException
	{
		List<String> attachedFilePaths = null;
		if(articleId!=null && articleId>0)
		{
			String sql = "select file_path from attached_files where article_id='?'";
			db.openSession();
			@SuppressWarnings("unchecked")
			List<String[]> result = (List<String[]>) db.execute(sql, articleId);
			db.closeSession();
			if(result.size()>=2)
			{
				attachedFilePaths = new ArrayList<String>();
				for(int i=1; i<result.size(); ++i)
				{
					String[] tuple = result.get(i);
					attachedFilePaths.add(tuple[0]);
				}
			}
		}
		return attachedFilePaths;
	}

	
	
	
//	public static Article2 retrieveArticle(Long articleId) throws ClassNotFoundException, InterruptedException, SQLException
//	{
//		Article2 article = null;
//		if(articleId>0)
//		{
//			String sql = "select board_id, member_id, article_number, title, contents, view_count, last_updated_time from articles where id='?' and comment_of is NULL";
//			db.openSession();
//			@SuppressWarnings("unchecked")
//			List<String[]> result = (List<String[]>) db.execute(sql, articleId);
//			db.closeSession();
//			
//			if(result.size()==2)
//			{
//				String[] tuple = result.get(1);
//				article = new Article2();
//				article.setId(articleId);
//				article.setBoardId(Long.parseLong(tuple[0]));
//				article.setMemberId(Long.parseLong(tuple[1]));
//				article.setArticleNumber(Integer.parseInt(tuple[2]));
//				article.setTitle(tuple[3]);
//				article.setContents(tuple[4]);
//				article.setViewCount(Integer.parseInt(tuple[5]));
//				article.setLastUpdateTime(DateUtils.convertToDate(tuple[6]));
//			}
//		}
//		
//		return article;
//	}
	
	
	
//	public static List<Article2> retrieveComments(Long articleId) throws ClassNotFoundException, InterruptedException, SQLException
//	{
//		List<Article2> comments = null;
//		if(articleId!=null && articleId>0)
//		{
//			String sql = "select id, board_id, member_id, contents, last_updated_time from articles where comment_of='?'";
//			db.openSession();
//			@SuppressWarnings("unchecked")
//			List<String[]> result = (List<String[]>) db.execute(sql, articleId);
//			db.closeSession();
//			
//			if(result.size()>=2)
//			{
//				comments = new ArrayList<Article2>();
//				for(int i=1; i<result.size(); ++i)
//				{
//					String[] tuple = result.get(i);
//					Article2 comment = new Article2();
//					Long commentId = Long.parseLong(tuple[0]); 
//					comment.setId(commentId);
//					comment.setBoardId(Long.parseLong(tuple[1]));
//					comment.setMemberId(Long.parseLong(tuple[2]));
//					comment.setContents(tuple[3]);
//					comment.setCommentOf(articleId);
//					comment.setLastUpdateTime(DateUtils.convertToDate(tuple[4]));
//					
//					List<Article2> subComments = retrieveComments(commentId);
//					comment.addComments(subComments);
//
//					comments.add(comment);
//				}
//			}
//		}
//		
//		return comments;
//	}
	
	
	
	public static Article_old retrieveArticle(String boardName, Integer articleNumber) throws ClassNotFoundException, InterruptedException, SQLException
	{
		Article_old article = null;
		if(boardName!=null && !"".equals(boardName) && articleNumber!=null && articleNumber>0)
		{
			String sql = "select a.id, a.board_id, a.member_id, a.article_number, a.title, a.contents, a.view_count, a.last_updated_time"
						+ " from articles a, boards b"
						+ " where a.board_id=b.id and b.board_name='?' and a.article_number=? and a.comment_of is NULL";
			db.openSession();
			@SuppressWarnings("unchecked")
			List<String[]> result = (List<String[]>) db.execute(sql, boardName, articleNumber);
			db.closeSession();
			
			if(result.size()==2)
			{
				String[] tuple = result.get(1);
				article = new Article_old();
				article.setId(Long.parseLong(tuple[0]));
				article.setBoardId(Long.parseLong(tuple[1]));
				article.setMemberId(Long.parseLong(tuple[2]));
				article.setArticleNumber(Integer.parseInt(tuple[3]));
				article.setTitle(tuple[4]);
				article.setContents(tuple[5]);
				article.setViewCount(Integer.parseInt(tuple[6]));
				article.setLastUpdateTime(DateUtils.convertToDate(tuple[7]));
			}
		}
		
		return article;
	}
	
	
	
	
	
	public static List<Article_old> retrieveArticles(Long boardId, Integer startingArticleNum, Integer endingArticleNum) throws ClassNotFoundException, InterruptedException, SQLException
	{
		List<Article_old> articles = null;
		if(boardId!=null && boardId>0 && startingArticleNum!=null && startingArticleNum>0 && startingArticleNum>=endingArticleNum)
		{
			String sql = "select id, board_id, member_id, article_number, title, contents, view_count, last_updated_time"
						+ " from articles"
						+ " where board_id='?' and article_number<='?' and article_number>='?' and comment_of is NULL"
						+ " order by article_number desc";
			db.openSession();
			@SuppressWarnings("unchecked")
			List<String[]> result = (List<String[]>) db.execute(sql, boardId, startingArticleNum, endingArticleNum);
			db.closeSession();
			if(result.size()>=2)
			{
				articles = new ArrayList<Article_old>();
				for(int i=1; i<result.size(); ++i)
				{
					String[] tuple = result.get(i);
					Article_old article = new Article_old();
					article.setId(Long.parseLong(tuple[0]));
					article.setBoardId(Long.parseLong(tuple[1]));
					article.setMemberId(Long.parseLong(tuple[2]));
					article.setArticleNumber(Integer.parseInt(tuple[3]));
					article.setTitle(tuple[4]);
					article.setContents(tuple[5]);
					article.setViewCount(Integer.parseInt(tuple[6]));
					article.setLastUpdateTime(DateUtils.convertToDate(tuple[7]));
					articles.add(article);
				}
			}
		}
		
		return articles;
	}
	
	
	

	public static List<Article_old> retrieveArticles(String boardName, Integer startingArticleNum, Integer endingArticleNum) throws ClassNotFoundException, InterruptedException, SQLException
	{
		List<Article_old> articles = null;
		if(boardName!=null && !"".equals(boardName) && startingArticleNum!=null && startingArticleNum>0 && startingArticleNum>=endingArticleNum)
		{
			String sql = "select a.id, a.board_id, a.member_id, a.article_number, a.title, a.contents, a.view_count, a.last_updated_time"
						+ " from articles a, boards b"
						+ " where a.board_id=b.id and b.board_name='?' and a.article_number<=? and a.article_number>=? and a.comment_of is NULL"
						+ " order by a.article_number desc";
			db.openSession();
			@SuppressWarnings("unchecked")
			List<String[]> result = (List<String[]>) db.execute(sql, boardName, startingArticleNum, endingArticleNum);
			db.closeSession();
			if(result.size()>=2)
			{
				articles = new ArrayList<Article_old>();
				for(int i=1; i<result.size(); ++i)
				{
					String[] tuple = result.get(i);
					Article_old article = new Article_old();
					article.setId(Long.parseLong(tuple[0]));
					article.setBoardId(Long.parseLong(tuple[1]));
					article.setMemberId(Long.parseLong(tuple[2]));
					article.setArticleNumber(Integer.parseInt(tuple[3]));
					article.setTitle(tuple[4]);
					article.setContents(tuple[5]);
					article.setViewCount(Integer.parseInt(tuple[6]));
					article.setLastUpdateTime(DateUtils.convertToDate(tuple[7]));
					articles.add(article);
				}
			}
		}
		
		return articles;
	}
	
	
	
	
	public static Integer getTotalNumOfArticlesInBoard(Long boardId) throws ClassNotFoundException, InterruptedException, SQLException
	{
		Integer totalNumOfArticles = null;
		if(boardId!=null && boardId>0)
		{
			String sql = "select count(*) from articles where board_id='?' and article_number is not null";
			db.openSession();
			@SuppressWarnings("unchecked")
			List<String[]> result = (List<String[]>) db.execute(sql, boardId);
			db.closeSession();
			if(result.size()==2)
			{
				String[] tuple = result.get(1);
				totalNumOfArticles = Integer.parseInt(tuple[0]);
			}
		}
			
		return totalNumOfArticles;
	}
	
	
	
	public static Integer getTotalNumOfArticlesInBoard(String boardName) throws ClassNotFoundException, InterruptedException, SQLException
	{
		Integer totalNumOfArticles = null;
		if(boardName!=null && !"".equals(boardName))
		{
			String sql = "select count(*) from articles a, boards b where a.board_id=b.id and b.board_name='?' and article_number is not null";
			db.openSession();
			@SuppressWarnings("unchecked")
			List<String[]> result = (List<String[]>) db.execute(sql, boardName);
			db.closeSession();
			if(result.size()==2)
			{
				String[] tuple = result.get(1);
				totalNumOfArticles = Integer.parseInt(tuple[0]);
			}
		}
			
		return totalNumOfArticles;
	}
	
	
	
	
	public static List<Integer> getArticleNumbersInBoard(Long boardid) throws ClassNotFoundException, InterruptedException, SQLException
	{
		List<Integer> articleNumbers = null;
		if(boardid!=null && boardid>0)
		{
			String sql = "select article_number from articles where board_id='?' and article_number is not null order by article_number desc";
			db.openSession();
			@SuppressWarnings("unchecked")
			List<String[]> result = (List<String[]>) db.execute(sql, boardid);
			db.closeSession();
			if(result.size()>=2)
			{
				articleNumbers = new ArrayList<Integer>();
				for(int i=1; i<result.size(); ++i)
				{
					String[] tuple = result.get(i);
					articleNumbers.add(Integer.parseInt(tuple[0]));
				}
			}
		}
			
		return articleNumbers;
	}
	
	
	
	
	public static List<Integer> getArticleNumbersInBoard(String boardName) throws ClassNotFoundException, InterruptedException, SQLException
	{
		List<Integer> articleNumbers = null;
		if(boardName!=null && !"".equals(boardName))
		{
			String sql = "select a.article_number from articles a, boards b where a.board_id=b.id and b.board_name='?' and a.article_number is not null order by a.article_number desc";
			db.openSession();
			@SuppressWarnings("unchecked")
			List<String[]> result = (List<String[]>) db.execute(sql, boardName);
			db.closeSession();
			if(result.size()>=2)
			{
				articleNumbers = new ArrayList<Integer>();
				for(int i=1; i<result.size(); ++i)
				{
					String[] tuple = result.get(i);
					articleNumbers.add(Integer.parseInt(tuple[0]));
				}
			}
		}
			
		return articleNumbers;
	}
	
	
	
	public static void main(String[] args) throws ClassNotFoundException, InterruptedException, SQLException
	{
//		List<Article> comments = retrieveComments(1L);
//		Article article = retrieveArticle(1L);
//		System.out.println(article);
		
//		System.out.println(getTotalNumOfArticlesInBoard("Temporary"));
		
		System.out.println(lookUpArticleId("Temporary", 2));
		System.out.println(retrieveArticle(1L).getAttachedFilePaths().get(0));
	}
}
