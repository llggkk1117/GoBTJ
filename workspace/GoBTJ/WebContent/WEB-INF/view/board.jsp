<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Insert title here</title>
	
	<!-- jQuery -->
	<!-- http://jqueryhouse.com/50-jquery-bootstrap-plugins-for-your-next-projects/ -->
	<script type="text/javascript" charset="utf8" src="resource/jquery-1.11.2/jquery-1.11.2.js"></script>
	<script type="text/javascript" charset="utf8" src="resource/jquery-ui-1.11.4/jquery-ui.js"></script>
	<link rel="stylesheet" type="text/css" href="resource/jquery-ui-1.11.4/jquery-ui.css">
	
	
	<!-- Bootstrap -->
	<!--
	<script type="text/javascript" charset="utf8" src="resource/jquery-1.11.2/jquery-1.11.2.js"></script>
	-->
	<script type="text/javascript" charset="utf8" src="resource/bootstrap-3.3.4/dist/js/bootstrap.js"></script>
	<link rel="stylesheet" type="text/css" href="resource/bootstrap-3.3.4/dist/css/bootstrap.css">
	
	
	<!-- DataTable Core -->
	<!--
	<script type="text/javascript" charset="utf8" src="resource/DataTables-1.10.5/media/js/jquery.js"></script>
	<link rel="stylesheet" type="text/css" href="resource/DataTables-1.10.5/media/css/jquery.dataTables.css">
	-->
	<script type="text/javascript" charset="utf8" src="resource/DataTables-1.10.5/media/js/jquery.dataTables.js"></script>
	
	
	<!-- DataTable Bootstrap -->
	<!--
	https://datatables.net/examples/styling/bootstrap.html
	https://www.datatables.net/manual/styling/bootstrap
	-->
	<script type="text/javascript" charset="utf8" src="resource/DataTables-1.10.5-BootstrapSkin/dataTables.bootstrap.js"></script>
	<link rel="stylesheet" type="text/css" href="resource/DataTables-1.10.5-BootstrapSkin/dataTables.bootstrap.css">


	
	<!-- fancyBox START -->
	<!-- Add jQuery library -->
	<!--
	<script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
	-->
	
	<!-- Add mousewheel plugin (this is optional) -->
	<script type="text/javascript" src="resource/fancyBox-2.1.5/lib/jquery.mousewheel-3.0.6.pack.js"></script>
	
	<!-- Add fancyBox -->
	<link rel="stylesheet" href="resource/fancyBox-2.1.5/source/jquery.fancybox.css?v=2.1.5" type="text/css" media="screen" />
	<script type="text/javascript" src="resource/fancyBox-2.1.5/source/jquery.fancybox.pack.js?v=2.1.5"></script>
	
	<!-- Optionally add helpers - button, thumbnail and/or media -->
	<link rel="stylesheet" href="resource/fancyBox-2.1.5/source/helpers/jquery.fancybox-buttons.css?v=1.0.5" type="text/css" media="screen" />
	<script type="text/javascript" src="resource/fancyBox-2.1.5/source/helpers/jquery.fancybox-buttons.js?v=1.0.5"></script>
	<script type="text/javascript" src="resource/fancyBox-2.1.5/source/helpers/jquery.fancybox-media.js?v=1.0.6"></script>
	<link rel="stylesheet" href="resource/fancyBox-2.1.5/source/helpers/jquery.fancybox-thumbs.css?v=1.0.7" type="text/css" media="screen" />
	<script type="text/javascript" src="resource/fancyBox-2.1.5/source/helpers/jquery.fancybox-thumbs.js?v=1.0.7"></script>
	<!-- fancyBox END -->


	<link rel="stylesheet" type="text/css" href="http://fonts.googleapis.com/earlyaccess/nanumgothic.css">
	<script src="js/json2.js"></script>
	<script src="resource/custom/js/ajaxPostRequest.js"></script>
	<script src="resource/custom/js/table.js"></script>
	
	<script>	
		$(document).ready(
				function()
				{
					ajaxPostRequest("${pageContext.request.contextPath}/board.process", {boardId:1},
							function(data)
							{
								console.log(data);
								var dataSet = data.board.articleList;
								console.log(dataSet);
								
								
								var windowId = createPopWindow(
										{
											windowId: "_articleWindow",
											configuration:
												{
													maxWidth: '90%',
													maxHeight: '90%',
													width: '65%',
													height: 'auto',
													fitToView: false,
													autoSize: false,
													closeClick: false,
													openEffect: 'fade',
													closeEffect: 'fade',
												},
											implementation:
												'<h1 id="_title"></h1>'+
												'<div class="row">'+
													'<div class="col-xs-6" style="background-color: lavender;">'+
														'<p id="_contents"></p>'+
													'</div>'+
													'<div class="col-xs-6" style="background-color: lavenderblush;">'+
														'<p id="_authorName"></p>'+
													'</div>'+
												'</div>',
										}
								);
								
								
								var tableId = "_articleListTable";
								createTable(tableId,
										{
											// https://datatables.net/reference/option/columnDefs
											// https://datatables.net/reference/option/columns
											"columnDefs": 
												[
													{"title": "번호", "targets": 0, "width": "7%"},
													{"title": "제목", "targets": 1},
													{"title": "작성자", "width": "10%", "targets": 2},
													{"title": "조회", "width": "10%", "targets": 3},
													{"title": "작성일시", "width": "15%", "targets": 4},
												],
											"data": dataSet,
											"order": [[0, 'desc']],
										}
								);
								
								setColumnNameTextAlign(tableId, ["center", "center", "center", "center", "center"]);
								setDataTextAlign(tableId, ["center", "center", "center", "center", "center"]);
								
								addBehaviorWhenRowClicked(tableId,
										function(tableId, clickedRow)
										{
											$("#"+tableId+" tbody tr").removeClass("selected");
											$(clickedRow).addClass("selected");
										}
								);
								
								addBehaviorWhenCellClicked(tableId,
										function(tableId, clickedCellObject)
										{
											var cell = getClickedCellInfo(tableId, clickedCellObject);
											ajaxPostRequest("${pageContext.request.contextPath}/article.process", {boardName:"Temporary", articleNumber: cell.row[0]},
													function(data)
													{
														var article = data.article;
														$("#"+windowId+"Impl #_title").html(article.title);
														$("#"+windowId+"Impl #_contents").html(article.contents);
														$("#"+windowId+"Impl #_authorName").html(article.authorName);
														$("#"+windowId+"Pop").click();
													}
											);
										}
								);
								
								addBehaviorWhenCellClickedOnce(tableId,
										function(tableId, clickedCellObject)
										{
											var cellInfo = getClickedCellInfo(tableId, clickedCellObject);
											ajaxPostRequest("${pageContext.request.contextPath}/increaseViewCount.process", {boardName:"Temporary", articleNumber: cellInfo.row[0]},
													function(data)
													{
														var increasedViewCount = data.increasedViewCount;
														$(clickedCellObject).parent().find("td:nth-child(4)").html(increasedViewCount);
													}
											);
										}
								);
							}
					);
				}
		);
	</script>
	
</head>

<body style="font-family: Nanum Gothic, sans-serif;">

	<table id="_articleListTable"></table>
	
</body>
</html>