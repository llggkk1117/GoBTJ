package org.gene.controller;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.log4j.Logger;
import org.gene.dao.ArticleDAO;
import org.gene.dao.BoardDAO;
import org.gene.model.Article;
import org.gene.model.Board;
import org.gene.modules.utils.JsonUtils;



import com.google.gson.annotations.Expose;

@Controller
public class BoardController extends AbstractController
{
	final static Logger logger = Logger.getLogger(BoardController.class);
	
	public class BoardRequest
	{
		@Expose private Long boardId;
		
		public Long getBoardId()
		{
			return boardId;
		}
		
		public void setBoardId(Long boardId)
		{
			this.boardId = boardId;
		}
	}
	
	public class BoardResponse
	{
		@Expose private Board board;

		public Board getBoard()
		{
			return board;
		}

		public void setBoard(Board board)
		{
			this.board = board;
		}
	}
	
	// select * from (select row_number() over(order by last_update_time desc) as id, article_number, contents, last_update_time from articles where article_number is not null) t ;
	// select * from (select row_number() over(order by last_update_time desc) as id, article_number, contents, last_update_time from articles) t where t.id=4;
	
	@RequestMapping(value="/board.process", method=RequestMethod.POST)
	@ResponseBody
	public String showBoardPost(HttpServletRequest request, @RequestBody String json) throws ClassNotFoundException, InterruptedException, SQLException, UnsupportedEncodingException
	{
//		if(logger.isDebugEnabled()){logger.debug("/board post envoked");}
		// if(logger.isInfoEnabled()){logger.info("");}
		
		BoardRequest boardRequest = JsonUtils.jsonToObject(json, BoardRequest.class);
		Long boardId = boardRequest.getBoardId();
    	Board board = BoardDAO.retrieveBoard(boardId);
    	BoardResponse boardResponse = new BoardResponse();
    	boardResponse.setBoard(board);
    	String result = JsonUtils.objectToJson(boardResponse);
    	System.out.println(result);
    	result = encode(result);
		return result;
	}
	
	
	@RequestMapping(value="/board", method=RequestMethod.POST)
	public String forwardToBoard(HttpServletRequest request)
	{
		String view = "board";
		return view;
	}
	

	
	public class ArticleRequest
	{
		@Expose private String boardName;
		@Expose private int articleNumber;
		public String getBoardName() {
			return boardName;
		}
		public void setBoardName(String boardName) {
			this.boardName = boardName;
		}
		public int getArticleNumber() {
			return articleNumber;
		}
		public void setArticleNumber(int articleNumber) {
			this.articleNumber = articleNumber;
		}
	}
	
	
	public class ArticleResponse
	{
		@Expose private Article article;

		public Article getArticle()
		{
			return article;
		}

		public void setArticle(Article article)
		{
			this.article = article;
		}
	}
	
	
	@RequestMapping(value="/article.process", method=RequestMethod.POST)
	@ResponseBody
	public String getArticlePost(HttpServletRequest request, @RequestBody String json) throws ClassNotFoundException, InterruptedException, SQLException, UnsupportedEncodingException
	{
		System.out.println("article.process");
		ArticleRequest articleRequest = JsonUtils.jsonToObject(json, ArticleRequest.class);
		String boardName = articleRequest.getBoardName();
		int articleNumber = articleRequest.getArticleNumber();
		Long articleId = ArticleDAO.lookUpArticleId(boardName, articleNumber);
		Article article = ArticleDAO.retrieveArticle(articleId);
		ArticleResponse articleResponse =  new ArticleResponse();
		articleResponse.setArticle(article);
		String result = JsonUtils.objectToJson(articleResponse);
		System.out.println(result);
    	result = encode(result);
		return result;
	}
	
	
	
	public class ViewCountIncreseRequest
	{
		@Expose private String boardName;
		@Expose private int articleNumber;
		public String getBoardName() {
			return boardName;
		}
		public void setBoardName(String boardName) {
			this.boardName = boardName;
		}
		public int getArticleNumber() {
			return articleNumber;
		}
		public void setArticleNumber(int articleNumber) {
			this.articleNumber = articleNumber;
		}
	}
	
	
	public class ViewCountIncreseResponse
	{
		@Expose private int increasedViewCount;

		public int getIncreasedViewCount() {
			return increasedViewCount;
		}

		public void setIncreasedViewCount(int increasedViewCount) {
			this.increasedViewCount = increasedViewCount;
		}
	}
	
	
	
	@RequestMapping(value="/increaseViewCount.process", method=RequestMethod.POST)
	@ResponseBody
	public String increaseViewCountPost(HttpServletRequest request, @RequestBody String json) throws ClassNotFoundException, InterruptedException, SQLException, UnsupportedEncodingException
	{
		System.out.println("increaseViewCount.process");
		ViewCountIncreseRequest viewCountIncreseRequest = JsonUtils.jsonToObject(json, ViewCountIncreseRequest.class);
		String boardName = viewCountIncreseRequest.getBoardName();
		int articleNumber = viewCountIncreseRequest.getArticleNumber();
		Long articleId = ArticleDAO.lookUpArticleId(boardName, articleNumber);
		Integer increasedViewCount = ArticleDAO.increaseViewCount(articleId);
		ViewCountIncreseResponse viewCountIncreseResponse =  new ViewCountIncreseResponse();
		viewCountIncreseResponse.setIncreasedViewCount(increasedViewCount);
		String result = JsonUtils.objectToJson(viewCountIncreseResponse);
		System.out.println(result);
    	result = encode(result);
		return result;
	}
}
