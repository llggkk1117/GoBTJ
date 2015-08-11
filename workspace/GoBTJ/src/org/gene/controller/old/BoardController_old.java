package org.gene.controller.old;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.annotations.Expose;

import org.gene.controller.AbstractController;
import org.gene.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;






@Controller
public class BoardController_old extends AbstractController
{
	public class BoardPageRequest
	{
		@Expose private Long boardId;
		@Expose private Integer numOfArticlesInPage;
		@Expose private Integer pageNumber;
		
		public Long getBoardId()
		{
			return boardId;
		}
		
		public void setBoardId(Long boardId)
		{
			this.boardId = boardId;
		}
		
		public Integer getNumOfArticlesInPage()
		{
			return numOfArticlesInPage;
		}
		
		public void setNumOfArticlesInPage(Integer numOfArticlesInPage)
		{
			this.numOfArticlesInPage = numOfArticlesInPage;
		}
		
		public Integer getPageNumber()
		{
			return pageNumber;
		}
		
		public void setPageNumber(Integer pageNumber)
		{
			this.pageNumber = pageNumber;
		}
	}
	
	
	
	public class BoardPageResponse extends AbstractResponse
	{
		@Expose private Long boardId;
		@Expose private String boardName;
		@Expose private Integer numOfArticlesInPage;
		@Expose private Integer currentPageNumber;
		@Expose private Integer lastPageNumber;
		@Expose private List<ArticleQuickViewLine> articleQuickViewLines;
		
		public Long getBoardId() {
			return boardId;
		}
		public void setBoardId(Long boardId) {
			this.boardId = boardId;
		}
		public String getBoardName() {
			return boardName;
		}
		public void setBoardName(String boardName) {
			this.boardName = boardName;
		}
		public Integer getNumOfArticlesInPage() {
			return numOfArticlesInPage;
		}
		public void setNumOfArticlesInPage(Integer numOfArticlesInPage) {
			this.numOfArticlesInPage = numOfArticlesInPage;
		}
		public Integer getCurrentPageNumber() {
			return currentPageNumber;
		}
		public void setCurrentPageNumber(Integer currentPageNumber) {
			this.currentPageNumber = currentPageNumber;
		}
		public Integer getLastPageNumber() {
			return lastPageNumber;
		}
		public void setLastPageNumber(Integer lastPageNumber) {
			this.lastPageNumber = lastPageNumber;
		}
		public List<ArticleQuickViewLine> getArticleQuickViewLines() {
			return articleQuickViewLines;
		}
		public void setArticleQuickViewLines(List<ArticleQuickViewLine> articleQuickViewLines)
		{
			this.articleQuickViewLines = articleQuickViewLines;
		}
	}
	
	

	public class ArticleQuickViewLine
	{
		@Expose private Integer articleNumber;
		@Expose private String title;
		@Expose private String author;
		@Expose private Date lastUpdateTime;
		@Expose private Integer viewCount;
		
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
		public String getAuthor() {
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
	
	
	
	
	@RequestMapping(value="/board.module", method=RequestMethod.POST)
	public String showBoardPost(HttpServletRequest request) throws ClassNotFoundException, InterruptedException, SQLException
	{
		String view = null;
		User user = (User) request.getSession().getAttribute("user");
        if (user==null)
        {
        	view = "login";
        }
        else
        {
//        	BoardPageRequest boardPageRequest = receiveObject(request, BoardPageRequest.class);
//    		if(boardPageRequest != null)
//    		{
//    			BoardPageResponse boardPageResponse = new BoardPageResponse();
//    			Long boardId = boardPageRequest.getBoardId();
//    			boardPageResponse.setBoardId(boardId);
//    			Board_old board = BoardDAO.retrieveBoard(boardId);
//    			boardPageResponse.setBoardName(board.getBoardName());
//    			boardPageResponse.setNumOfArticlesInPage(boardPageRequest.getNumOfArticlesInPage());
//    			boardPageResponse.setCurrentPageNumber(boardPageRequest.getPageNumber());
//    			
//    			List<Integer> articleNumbers = ArticleDAO.getArticleNumbersInBoard(boardId);
//    			PageInfo pageInfo = PagingTool.getElementIndexRangeInPage(boardPageRequest.getPageNumber()-1, boardPageRequest.getNumOfArticlesInPage(), articleNumbers.size());
//    			boardPageResponse.setLastPageNumber(pageInfo.getLastPageIndex()+1);
//    			int startingArticleNum = articleNumbers.get(pageInfo.getStartingElementIndex());
//    			int endingArticleNum = articleNumbers.get(pageInfo.getEndingElementIndex());
//    			List<Article_old> articles = ArticleDAO.retrieveArticles(boardId, startingArticleNum, endingArticleNum);
//    			List<ArticleQuickViewLine> articleQuickViewLines = new ArrayList<ArticleQuickViewLine>();
//    			for(int i=0; i<articles.size(); ++i)
//    			{
//    				ArticleQuickViewLine articleQuickViewLine = new ArticleQuickViewLine();
//    				articleQuickViewLine.setArticleNumber(articles.get(i).getArticleNumber());
//    				articleQuickViewLine.setTitle(articles.get(i).getTitle());
//    				User author = UserDAO.retrieveUser(articles.get(i).getMemberId());
//    				String displayName = author.getKoreanName()!=null ? author.getKoreanName() : author.getEnglishName();
//    				articleQuickViewLine.setAuthor(displayName);
//    				articleQuickViewLine.setLastUpdateTime(articles.get(i).getLastUpdateTime());
//    				articleQuickViewLine.setViewCount(articles.get(i).getViewCount());
//    				articleQuickViewLines.add(articleQuickViewLine);
//    			}
//    			boardPageResponse.setArticleQuickViewLines(articleQuickViewLines);
//    			boardPageResponse.setSuccess(true);
//    			
//    			System.out.println(JsonUtils.objectToJson(boardPageResponse));
//    			
//    			view = "board";
//				sendObject(request, boardPageResponse);
//    		}
        }
		
		return view;
	}
	
	
	@RequestMapping(value="/board.module", method=RequestMethod.GET)
	public String showBoardGet(HttpServletRequest request)
	{
		request.getSession().removeAttribute("user");
		String view = "login";
		return view;
	}
}