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
	<link rel="stylesheet" href="resource/fancyBox-2.1.5/source/helpers/jquery.fancybox-buttons.css" type="text/css" media="screen" />
	<script type="text/javascript" src="resource/fancyBox-2.1.5/source/helpers/jquery.fancybox-buttons.js"></script>
	<script type="text/javascript" src="resource/fancyBox-2.1.5/source/helpers/jquery.fancybox-media.js"></script>
	<link rel="stylesheet" href="resource/fancyBox-2.1.5/source/helpers/jquery.fancybox-thumbs.css" type="text/css" media="screen" />
	<script type="text/javascript" src="resource/fancyBox-2.1.5/source/helpers/jquery.fancybox-thumbs.js"></script>
	<!-- fancyBox END -->




	<link rel="stylesheet" type="text/css" href="http://fonts.googleapis.com/earlyaccess/nanumgothic.css">
	<script src="js/json2.js"></script>
	
	
	<script>
		function ajaxPostRequest(serverURL, jsonObject, handleData)
		{
			var json_string = JSON.stringify(jsonObject); //JSON.stringify({boardId:1});
			$.ajax(	{
						url: serverURL, //"${pageContext.request.contextPath}/board2.module",
						type: "POST",
						contentType: "text/plain; charset=UTF-8", // sending format
						dataType: "text", // expected format for response
						data: json_string,
						success: function(data, textStatus, jqXHR) //called when successful
						{
							// http://zzznara2.tistory.com/94
							var returnData = decodeURIComponent(data);
							console.log(returnData);
							var returnDataObject = jQuery.parseJSON(returnData);
							handleData(returnDataObject);
						},
						complete: function(jqXHR, textStatus){}, //called when complete
						error: function(jqXHR, textStatus, errorThrown){}, //called when there is an error
			});
		}
	</script>
	
	<script>
		$(document).ready(
				function()
				{
					// https://jqueryui.com/dialog/#default
					var articleDialog = $("#_articleDialog").dialog(
							{
								autoOpen: false,
								resizable: false,
								modal: true,
								width:$(window).width()*0.85,
								height: "auto",
								show: {effect: "fade", duration: 500},
								hide: {effect: "fade", duration: 500},
								
								buttons:
									{
										"Close": 
											function()
											{
												$( this ).dialog( "close" );
											},
									},
								
								open: 
									function(event, ui)
									{
										// removing x button
										// http://stackoverflow.com/questions/896777/how-to-remove-close-button-on-the-jquery-ui-dialog
										$(".ui-dialog-titlebar-close", $(this).parent()).hide();
										
										// chaning title bar color
										// http://stackoverflow.com/questions/17735205/jquery-dialog-titlebar-color-change
										$(".ui-dialog > .ui-widget-header").css("background", "linear-gradient(180deg, #C0C0C0, #A2A2A2)");
										$(".ui-dialog > .ui-widget-header").css("border-color", "#A1A1A1");
									},
							}
					);

					
					
					ajaxPostRequest("${pageContext.request.contextPath}/board.process", {boardId:1},
							function(data)
							{
								console.log(data);
								var dataSet = data.board.articleList;
								console.log(dataSet);
								// $('#demo').html( '<table cellpadding="0" cellspacing="0" border="0" class="display" id="articleListTable"></table>' );
								var viewed = [];
								var oTable = $('#articleListTable').dataTable(
									{
										"data": dataSet,
										"columns": 
												[
													{ "title": "글 번호" },
													{ "title": "제목" },
													{ "title": "작성자" },
													{ "title": "조회", "class": "center" },
													{ "title": "작성일시", "class": "center" }
												],
										"order": [[0, 'desc']],
										
										/* Table Row Click Event Binding START */
										// This callback allows you to 'post process' each row after it have been generated for each table draw, but before it is rendered into the document
										// https://datatables.net/reference/option/rowCallback
										/*
										"rowCallback":
												function( row, data )
												{
													console.log(row+"\n"+data+"\n"+data.DT_RowId);
													if ($.inArray(data.DT_RowId, selected) !== -1)
													{
														$(row).addClass('selected');
													}
												},
										*/

										
										// http://legacy.datatables.net/usage/callbacks
										// http://stackoverflow.com/questions/25346476/datatables-jquery-make-each-row-clickable-or-linkable
										/*
										"fnRowCallback":
												function (nRow, aData, iDisplayIndex)
												{
													// Bind click event
													$(nRow).click(
															function()
															{
																alert(nRow+"\n"+aData+"\n"+aData[0]+"\n"+iDisplayIndex);
															}
													);
													return nRow;	
												},
										*/
										/* Table Row Click Event Binding END */
									}
							    );
								
								/* Table Click Event Binding START */
								// http://stackoverflow.com/questions/7522586/jquery-datatables-make-whole-row-a-link
								oTable.$('td').click(
										function ()
										{
											// var element = oTable.fnGetData(this);
											var position = oTable.fnGetPosition(this);
											var row = oTable.fnGetData(position[0]);
											ajaxPostRequest("${pageContext.request.contextPath}/article.process", {boardName:"Temporary", articleNumber: row[0]},
													function(data)
													{
														var article = data.article;
														$("#_title").html(article.title);
														$("#_contents").html(article.contents);
														$("#_authorName").html(article.authorName);
														$("#_articleDialogContainer").css("width", $(window).width()*0.8);
														// $("#_articleDialog").dialog("open");
														articleDialog.dialog("open");
														
														/*
														$('#articleDisplay').html(
																'<div id="article" class="container">'+
																	'<h1>Hello World!</h1>'+
																	'<p>Resize the browser window to see the effect.</p>'+
																	'<div class="row">'+
																		'<div class="col-sm-4" style="background-color: lavender;">'+
																			'<p>'+contents+'</p>'+
																			'<p>Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>'+
																		'</div>'+
																		'<div class="col-sm-4" style="background-color: lavenderblush;">'+
																			'<p>'+article.authorName+'</p>'+
																		'</div>'+
																	'</div>'+
																'</div>'
														);
														*/
													}
											);
											
											
											/* View Count Processing START */
											var articleNumber = row[0];
											var index = $.inArray(articleNumber, viewed);
											if( index === -1)
											{
												viewed.push(articleNumber);
												// increase viewCount
												alert(viewed);
											}
											/* View Count Processing END */
										}
								);
								
								
								/*
								$('#articleListTable tbody').on('click', 'tr', 
										function ()
										{
											var id = this.id;
											var index = $.inArray(id, selected);
											console.log("id: ->"+id+"<- selected: "+selected+" index: "+index);
											if ( index === -1 )
											{
												selected.push( id );
											}
											else
											{
												selected.splice( index, 1 );
											}
											$(this).toggleClass('selected');
										}
								);
								*/
								
								
								$('#articleListTable tbody').on('click', 'tr', 
										function ()
										{
											$("#articleListTable tbody tr").removeClass("selected");
											$(this).addClass("selected");
											
											/*
											if($(this).hasClass("even"))
											{
												$('#article').html(
														'<h1>Hello World!</h1>'+
														'<p>Resize the browser window to see the effect.</p>'+
														'<div class="row">'+
															'<div class="col-sm-4" style="background-color: lavender;">'+
																'<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, seddo eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>'+
																'<p>Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>'+
															'</div>'+
															'<div class="col-sm-4" style="background-color: lavenderblush;">'+
																'<p>Sed ut perspiciatis unde omnis iste natus error sit</p>'+
															'</div>'+
														'</div>'
													);	
											}
											else
											{
												$('#article').html('');
											}
											*/
										}
								);
								/* Table Click Event Binding END */
								
								
								/* Table Modification START */
								//$("#articleListTable_filter input").addClass("form-control input-sm chat-input");
								
								$("#articleListTable_filter").html(
											'<label for="searchBox">'+
												'<span class="glyphicon glyphicon-search"></span>'+
											'</label>'+
											'<input type="search" id="searchBox" class="form-control input-sm chat-input" placeholder="Search.." aria-controls="articleListTable">'
								);
								/*
								$("#articleListTable_length").html(
											'<label for="optionBox">Show:</label>'+
											'<select name="articleListTable_length" aria-controls="articleListTable" class="form-control" id="optionBox">'+
												'<option value="10">10</option>'+
												'<option value="25">25</option>'+
												'<option value="50">50</option>'+
												'<option value="100">100</option>'+
											'</select>'
								);
								*/
								/* Table Modification END */
							}
							/* End of ajax dataHandler function */ 
					);
					/* End of ajax post call */
					
					
				}
		);
	</script>
	
</head>

<body style="font-family: Nanum Gothic, sans-serif;">

	<div id="_articleDialog" title="Article">
		<!-- http://www.w3schools.com/bootstrap/bootstrap_grid_basic.asp -->
  		<div id="_articleDialogContainer" class="container" style="width:50%">
			<h1 id="_title">Hello World!</h1>
			<div class="row">
				<div class="col-xs-6" style="background-color: lavender;">
					<p id="_contents"></p>
				</div>
				<div class="col-xs-6" style="background-color: lavenderblush;">
					<p id="_authorName"></p>
				</div>
			</div>
		</div>
	</div>


	<table id="articleListTable" class="table table-striped table-bordered dataTable no-footer" cellspacing="0" width="100%" style="width: 100%;">
		<!--
        <thead>
            <tr>
                <th>Column 1</th>
                <th>Column 2</th>
                <th>Column 3</th>
                <th>Column 4</th>
                <th>Column 5</th>
            </tr>
        </thead>
 
        <tfoot>
            <tr>
                <th>Column 1</th>
                <th>Column 2</th>
                <th>Column 3</th>
                <th>Column 4</th>
                <th>Column 5</th>
            </tr>
        </tfoot>
        -->
    </table>
    
</body>
</html>