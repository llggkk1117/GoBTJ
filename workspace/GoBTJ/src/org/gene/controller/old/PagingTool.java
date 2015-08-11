package org.gene.controller.old;

public class PagingTool
{
	private static final int MIN_NUM_OF_ELEMENTS_IN_PAGE = 10;
	private static final int MAX_NUM_OF_ELEMENTS_IN_PAGE = 50;
	public static PageInfo getElementIndexRangeInPage(int pageIndex, int numOfElementsInPage, int totalNumOfElements)
	{
		PageInfo pageInfo = null;
		if(pageIndex>=0 && numOfElementsInPage>0 && totalNumOfElements>0)
		{
			if(numOfElementsInPage < MIN_NUM_OF_ELEMENTS_IN_PAGE)
			{
				numOfElementsInPage = MIN_NUM_OF_ELEMENTS_IN_PAGE;
			}
			
			if(numOfElementsInPage > MAX_NUM_OF_ELEMENTS_IN_PAGE)
			{
				numOfElementsInPage = MAX_NUM_OF_ELEMENTS_IN_PAGE;
			}
			
			int numOfElementsInLastPage = totalNumOfElements%numOfElementsInPage;
			int lastPageIndex = (totalNumOfElements/numOfElementsInPage-1)+(numOfElementsInLastPage>0 ? 1 : 0);
			pageIndex = pageIndex>lastPageIndex ? lastPageIndex : pageIndex;
			int startingElementIndex = pageIndex*numOfElementsInPage;
			int endingElementIndex = startingElementIndex + (pageIndex==lastPageIndex ?  numOfElementsInLastPage : numOfElementsInPage) - 1;
			
			pageInfo = new PageInfo();
			pageInfo.setCurrentPageIndex(pageIndex);
			pageInfo.setLastPageIndex(lastPageIndex);
			pageInfo.setStartingElementIndex(startingElementIndex);
			pageInfo.setEndingElementIndex(endingElementIndex);
		}
		return pageInfo;
	}
}


class PageInfo
{
	private int startingElementIndex;
	private int endingElementIndex;
	private int currentPageIndex;
	private int lastPageIndex;
	
	public int getStartingElementIndex() {
		return startingElementIndex;
	}
	public void setStartingElementIndex(int startingElementIndex) {
		this.startingElementIndex = startingElementIndex;
	}
	public int getEndingElementIndex() {
		return endingElementIndex;
	}
	public void setEndingElementIndex(int endingElementIndex) {
		this.endingElementIndex = endingElementIndex;
	}
	public int getCurrentPageIndex() {
		return currentPageIndex;
	}
	public void setCurrentPageIndex(int currentPageIndex) {
		this.currentPageIndex = currentPageIndex;
	}
	public int getLastPageIndex() {
		return lastPageIndex;
	}
	public void setLastPageIndex(int lastPageIndex) {
		this.lastPageIndex = lastPageIndex;
	}
}