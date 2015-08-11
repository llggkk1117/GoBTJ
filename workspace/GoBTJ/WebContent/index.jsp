<!DOCTYPE html>
<html>
<head>
	<script type="text/javascript" charset="utf8" src="resource/jquery-1.11.2/jquery-1.11.2.js"></script>

	<script type="text/javascript" charset="utf8" src="resource/custom/js/post.js"></script>
	<script>
		$(document).ready(
				function()
				{
					post('${pageContext.request.contextPath}/login', {});
				}
		);
	</script>

	<!--
	<script type="text/javascript">
		function Utils(){}
		Utils.isArray =
			function(value)
			{
				return (Object.prototype.toString.call(value) === Object.prototype.toString.call([]));
			};
		Utils.isString =
			function(value)
			{
				return (typeof myVar === 'string');
			};
		Utils.isEmpty =
			function(value)
			{
				if(!value)
				{
					return true;
				}
				else if(Utils.isArray(value) || Utils.isString(value))
				{
					return (value.length==0);
				}
				else
				{
					return false;
				}
			}
	</script>
	<script type="text/javascript" charset="utf8" src="resource/custom/js/ajax/ajaxPostSync.js"></script>
	<script>
		// http://stackoverflow.com/questions/2141520/javascript-variable-number-of-arguments-to-function
		// https://developer.mozilla.org/en-US/docs/Web/JavaScript/Introduction_to_Object-Oriented_JavaScript
		// http://www.w3schools.com/js/js_object_methods.asp
		// http://javascript.crockford.com/private.html
		// http://stackoverflow.com/questions/55611/javascript-private-methods
		// http://stackoverflow.com/questions/4775722/check-if-object-is-array
		// https://developer.mozilla.org/en-US/docs/Web/API/XMLHttpRequest/Synchronous_and_Asynchronous_Requests
		// https://xhr.spec.whatwg.org/
		// https://developer.mozilla.org/en-US/docs/Web/API/XMLHttpRequest/Using_XMLHttpRequest
		function PreviewTable(board_name)
		{
			var boardName = board_name;

			var orderByColumn = "id";
			var orderBrDescending = true;
			var currentPageNumber = 1;
			var numOfPages = 1;
			var pageSize = 20;

			var columnNames = [];
			var rowIdsInOrder = [];
			var rows = [];




			var setCurrentPageNumber =
				function(page_number)
				{
					currentPageNumber = page_number;
				};



			var loadColumnNames =
				function()
				{
					if(columnNames.length == 0)
					{
						var result =
							ajaxPostSync("${pageContext.request.contextPath}/previewTable.process",
								{
									request: "columnNames",
									boardName: boardName,
								}
							);
						columnNames = result.columnNames;
					}
				}



			var loadRowIdsInOrderForced =
				function()
				{
					var result =
						ajaxPostSync("${pageContext.request.contextPath}/previewTable.process",
							{
								request: "rowIdsInOrder",
								boardName: boardName,
								orderByColumn: orderByColumn,
								orderBrDescending: orderBrDescending
							}
						);
					rowIdsInOrder = result.rowIdsInOrder;
				}
			var loadRowIdsInOrder =
				function()
				{
					if(rowIdsInOrder.length==0)
					{
						loadRowIdsInOrderForced();
					}
				}



			var sort =
				function(column_name, desc)
				{
					orderByColumn = column_name;
					orderBrDescending = desc;
					loadRowIdsInOrderForced();
				}



			var loadNumOfPages =
				function()
				{
					loadRowIdsInOrder();
					numOfPages = (rowIdsInOrder / pageSize) + (rowIdsInOrder % pageSize > 0 ? 1 : 0);
				};



			var setPageSize =
				function(page_size)
				{
					pageSize = page_size;
					loadNumOfPages();
				};



			var putRow =
				function(rowId, row)
				{
					rows[rowId] = row;
				};
			var getRow =
				function(rowId)
				{
					return rows[rowId];
				};
			var loadRowsByIdForced =
				function(rowIds)
				{
					if(Utils.isArray(rowIds) && rowIds.length > 0)
					{
						// {
						//     rows:
						//         [
						//             {id: 11, value: ["aa", "bb", "cc"]},
						//             {id: 12, value: ["dd", "ee", "ff"]},
						//         ]
						// }
						var result =
							ajaxPostSync("${pageContext.request.contextPath}/previewTable.process",
								{
									request: "rows",
									rowIds: rowIds
								}
							);
						var resultRows = result.rows;
						var i=0;
						for(i=0 ; i<resultRows.length ; ++i)
						{
							putRow(resultRows[i].id, resultRows[i]);
						}
					}
				};
			var getRowIdIndexRange =
				function(pageNumber)
				{
					var startingIndex = (pageNumber-1)*pageSize;
					var endingIndex = startingIndex + pageSize - 1;
					endingIndex = (endingIndex < rows.length) ? endingIndex : (rows.length-1);
					return {startingIndex: startingIndex, endingIndex: endingIndex};
				};
			var getRowIdsInOrderByPageNumber =
				function(pageNumber)
				{
					loadRowIdsInOrder();
					var rowIdIndexRange = getRowIdIndexRange(pageNumber);
					return rowIdsInOrder.slice(rowIdIndexRange.startingIndex, endingIndex+1);
				};
			var retrieveUnloadedRowIds =
				function(rowIds)
				{
					var unloadedRowIds = [];
					var i=0;
					for(i=0; i<rowIds.length; ++i)
					{
						if(!getRow(rowIds[i]))
						{
							unloadedRowIds.push(rowIds[i]);
						}
					}
					return unloadedRowIds;
				}
			var loadRowsById =
				function(rowIds)
				{
					var unloadedRowIds = retrieveUnloadedRowIds(rowIds);
					loadRowsByIdForced(unloadedRowIds);
				};
			var loadRowsByPageNumber =
				function(pageNumber)
				{
					var rowIds = getRowIdsInOrderByPageNumber(pageNumber);
					loadRowsById(rowIds);
				};



			this.renderTable=
				function (selectorId, rowIds)
				{
					var divId = selectorId;
					var tableId = divId+"_table";

					var isFirstTime = ($("#"+divId).has("#"+tableId).length == 0);
					if(isFirstTime)
					{
						$("#"+divId).append('<table class="table table-hover table-condensed" id="'+tableId+'"></table>');
						$("#"+tableId).append("<thead></thead><tbody></tbody>");

						loadColumnNames();
						var tableHead = "<tr>";
						var j=0;
						for(j=0; j<columnNames.length; ++j)
						{
							tableHead += ("<th>"+columnNames[j]+"</th>");
						}
						tableHead += "</tr>";
						$("#"+tableId+" > thead").append(tableHead);
					}
					else
					{
						$("#"+tableId+" > tbody").html("");
					}


					loadRowsById(rowIds);
					var tableBody = "";
					var i=0;
					for(i=0; i<rowIds.length; ++i)
					{
						var currentRow = getRow(rowIds[i]);
						tableBody += "<tr>";
						for(j=0; j<currentRow.length; ++j)
						{
							tableBody += ("<td>"+currentRow.value[j]+"</td>");
						}
						tableBody += "</tr>";
					}
					$("#"+tableId+" > tbody").append(tableBody);


					for(i=0; i<rowIds.length; ++i)
					{
						$("#"+tableId+" > tbody > tr:nth-child("+(i+1)+")").data("rowInfo", {id: rowIds[i]});
					}


					$("#"+tableId+" > tbody > tr").click(
						function()
						{
							// https://developer.mozilla.org/en-US/docs/Web/API/HTMLTableRowElement
							// https://developer.mozilla.org/en-US/docs/Web/API/HTMLTableCellElement
							// https://developer.mozilla.org/en-US/docs/Web/API/Element/innerHTML

							//alert(this.rowIndex);
							//alert(this.cells[1].innerHTML+"\n"+event.pageX);
							//var rowIndex = this.rowIndex;
							//var cells = this.cells;

							var rowId = $("#"+tableId+" > tbody > tr:nth-child("+this.rowIndex+")").data("rowInfo").id;
							alert(rowId);
						}
					);
				}



			this.renderPagination =
				function (selectorId)
				{
					var divId = selectorId;
					var tableId = divId+"_table";
					var paginationId = divId+"_pagination";

					var isFirstTime = ($("#"+divId).has("#"+paginationId).length == 0);
					if(isFirstTime)
					{
						$("#"+divId).append('<ul class="pagination" id="'+paginationId+'"></ul>');
					}
					else
					{
						$("#"+paginationId).html("");
					}

					loadNumOfPages();
					var i=0;
					for(i=0; i<numOfPages; ++i)
					{
						var element = null;
						if(currentPageNumber == (i+1))
						{
							element = '<li class="active">';
						}
						else
						{
							element = '<li>';
						}
						element += '<a href="#">'+(i+1)+'</a></li>';
						$("#"+paginationId).append(element);
					}

					$("#"+paginationId+" > li").click(
						function()
						{
							var pageNumer = $(this).index()+1;
							$("#"+tableId+" > tbody > tr:nth-child("+pageNumer+")").fadeOut(2000);

							$("#"+paginationId+" > li").removeClass("active");
							$("#"+paginationId+" > li:nth-child("+pageNumer+")").addClass("active");
						}
					);
				}
		}
	</script>
	<script>
		$(document).ready(
			function()
			{
				var result = null;
				var result = ajaxPostSync("${pageContext.request.contextPath}/board.process", {boardId:1});
				console.log("-->"+result.board.boardName);
			}
		);
	</script>
	-->

</head>

<body>
</body>
</html>