<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix ="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page import="java.util.*" %>
<%@ page import="org.gene.modules.utils.JsonUtils" %>
<%@ page import="org.gene.model.Board_old" %>
<%@ page import="org.gene.controller.old.BoardController_old.BoardPageResponse" %>
<%@ page import="org.gene.controller.old.BoardController_old.ArticleQuickViewLine" %>



<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
	<title>게시판</title>

	<script src="js/json2.js"></script>
	<script src="js/jquery-1.11.0.js"></script>
	
</head>
<body>

	<p id="_jsonStringReceived" style="display:none;">${jsonStringReceived}</p>
	인사말: <div id="_display" style="display:inline-block;"></div>
	
	<br/>

	<%
		String json_string = (String) request.getAttribute("jsonStringReceived");
		System.out.println("json_string in board.jsp: "+json_string);
		BoardPageResponse boardPageResponse = JsonUtils.jsonToObject(json_string, BoardPageResponse.class);
		List<ArticleQuickViewLine> articleQuickViewLines = boardPageResponse.getArticleQuickViewLines();
		pageContext.setAttribute("articleQuickViewLines", articleQuickViewLines);

		// http://www.java2s.com/Tutorial/Java/0380__JSTL/UseForEachtoLoopThroughArrayList.htm
		// http://www.w3schools.com/bootstrap/bootstrap_tables.asp
		// http://www.w3schools.com/bootstrap/bootstrap_forms_inputs.asp
	%>


	<!-- 리스트 시작 -->
	<div class="kboard-list">
		<table>
			<thead>
				<tr>
					<td class="kboard-list-uid">번호</td>
					<td class="kboard-list-title">제목</td>
					<td class="kboard-list-user">작성자</td>
					<td class="kboard-list-date">작성일</td>
					<td class="kboard-list-view">조회</td>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="articleQuickViewLine" items="${articleQuickViewLines}">
					<tr>
						<td>${articleQuickViewLine.articleNumber}</td>
						<td>${articleQuickViewLine.title}</td>
						<td>${articleQuickViewLine.author}</td>
						<!--
						<td>${articleQuickViewLine.lastUpdateTime}</td>
						-->
						<!-- http://www.tutorialspoint.com/jsp/jstl_format_formatdate_tag.htm -->
						<td><fmt:formatDate pattern="yyyy-MM-dd hh:mm:ss aa" value="${articleQuickViewLine.lastUpdateTime}" /></td>
						<td>${articleQuickViewLine.viewCount}</td>
					</tr>
				</c:forEach>
				
				
				<!--
				<tr class="kboard-list-notice">
					<td class="kboard-list-uid">공지사항</td>
					<td class="kboard-list-title"><div class="cut_strings">
							<a
								href="http://btjusca.org/wordpress/home/%ea%b3%b5%ec%a7%80%ec%82%ac%ed%95%ad/?uid=43&mod=document">교회
								및 대외관계 자료- 교단별 및 목회자 중심(update)</a>
						</div></td>
					<td class="kboard-list-user">관리자</td>
					<td class="kboard-list-date">2014.04.30</td>
					<td class="kboard-list-view">49</td>
				</tr>
				-->
				
			
				
			</tbody>
		</table>
	</div>
	<!-- 리스트 끝 -->


</body>
</html>
