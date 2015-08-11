package org.gene.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import org.gene.model.Board;
import org.gene.modules.utils.DateUtils;


public class BoardDAO extends AbstractDAO
{
	public static Board retrieveBoard(Long boardId) throws ClassNotFoundException, InterruptedException, SQLException
	{
		Board board = null;
		if(boardId!=null && boardId>0)
		{
			String sql = "select board_name from boards where id='?'";
			db.openSession();
			@SuppressWarnings("unchecked")
			List<String[]> result1 = (List<String[]>) db.execute(sql, boardId);
			db.closeSession();
			if(result1.size()==2)
			{
				board = new Board();
				board.setBoardName(result1.get(1)[0]);
			}
			
			if(board!=null)
			{
				sql = "select a.article_number, a.title, m.korean_name, a.view_count, a.last_update_time"
						+ " from articles a, members m"
						+ " where a.member_id=m.id and a.board_id='?' and a.comment_of is NULL"
						+ " order by a.article_number asc";
				db.openSession();
				@SuppressWarnings("unchecked")
				List<String[]> result2 = (List<String[]>) db.execute(sql, boardId);
				db.closeSession();
				if(result2.size()>=2)
				{
					List<String[] >articleList = new ArrayList<String[]>();
					for(int i=1; i<result2.size(); ++i)
					{
						String[] tuple = result2.get(i);
						String[] row = new String[]{tuple[0], tuple[1], tuple[2], tuple[3], DateUtils.format(tuple[4])};
						articleList.add(row);
					}
					board.setArticleList(articleList);
				}
			}
		}
		return board;
	}
	
	
	/*
	public static Board_old retrieveBoard(Long boardId) throws ClassNotFoundException, InterruptedException, SQLException
	{
		Board_old board = null;
		if(boardId!=null && boardId>0)
		{
			String sql = "select board_name from boards where id='?'";
			db.openSession();
			@SuppressWarnings("unchecked")
			List<String[]> result = (List<String[]>) db.execute(sql, boardId);
			db.closeSession();
			if(result.size()==2)
			{
				board = new Board_old();
				String[] tuple = result.get(1);
				board.setId(boardId);
				board.setBoardName(tuple[0]);
			}
		}
		return board;
	}
	
	
	
	public static Board_old retrieveBoard(String boardName) throws ClassNotFoundException, InterruptedException, SQLException
	{
		Board_old board = null;
		if(boardName!=null && !"".equals(boardName))
		{
			String sql = "select id from boards where board_name='?'";
			db.openSession();
			@SuppressWarnings("unchecked")
			List<String[]> result = (List<String[]>) db.execute(sql, boardName);
			db.closeSession();
			if(result.size()==2)
			{
				board = new Board_old();
				String[] tuple = result.get(1);
				board.setId(Long.parseLong(tuple[0]));
				board.setBoardName(boardName);
			}
		}
		return board;
	}
	*/
	
	
	

	public static void main(String[] args) throws ClassNotFoundException, InterruptedException, SQLException
	{
		
	}
}
