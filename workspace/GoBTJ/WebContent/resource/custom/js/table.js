function getDateTime()
{
	var now = new Date(); 
	var year = now.getFullYear();
	var month = now.getMonth()+1; 
	var day = now.getDate();
	var hour = now.getHours();
	var minute = now.getMinutes();
	var second = now.getSeconds(); 
	if(month.toString().length == 1) {
		var month = '0'+month;
	}
	if(day.toString().length == 1) {
		var day = '0'+day;
	}   
	if(hour.toString().length == 1) {
		var hour = '0'+hour;
	}
	if(minute.toString().length == 1) {
		var minute = '0'+minute;
	}
	if(second.toString().length == 1) {
		var second = '0'+second;
	}   
	// var dateTime = year+'/'+month+'/'+day+' '+hour+':'+minute+':'+second;   
	var dateTime = year+month+day+hour+minute+second;
	return dateTime;
}






function createPopWindow(param)
{
	var windowId = param.windowId ? param.windowId : "_default"+getDateTime();
	var popId = windowId+"Pop";
	var implId = windowId+"Impl";
	
	$("body").append('<div id="'+windowId+'"></div>');
	
	$("#"+windowId).html(
			'<a id="'+popId+'" href="#'+implId+'" style="display: none;"></a>'+
			'<div id="'+implId+'" style="width:98%; display:none;">'+
				param.implementation+
			'</div>'
	);
	
	
	var configuration = null;
	if(param.configuration)
	{
		configuration = param.configuration;
	}
	else
	{
		configuration =
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
			}
	}
	
	$("#"+popId).fancybox(configuration);
	
	return windowId;
}




function createTable(tableId, configuration)
{
	$("#"+tableId).attr("cellspacing", "0");
	$("#"+tableId).css("width", "100%");
	$("#"+tableId).addClass("table table-striped table-bordered dataTable no-footer");
	
	var table = $("#"+tableId).dataTable(configuration);
	
	$("#"+tableId+"_filter").html(
			'<label for="searchBox">'+
				'<span class="glyphicon glyphicon-search"></span>'+
			'</label>'+
			'<input type="search" id="searchBox" class="form-control input-sm chat-input" placeholder="Search.." aria-controls="'+tableId+'">'
	);
}



function setDataTextAlign(tableId, alignInfo)
{
	var arrayLength = alignInfo.length;
	for (var i = 0; i < arrayLength; i++)
	{
		$("#"+tableId+" tbody tr td:nth-child("+(i+1)+")").css("text-align", (alignInfo[i] ? alignInfo[i] : "left"));
	}
}


function setColumnNameTextAlign(tableId, alignInfo)
{
	var arrayLength = alignInfo.length;
	for (var i = 0; i < arrayLength; i++)
	{
		$("#"+tableId+" thead tr th:nth-child("+(i+1)+")").css("text-align", (alignInfo[i] ? alignInfo[i] : "left"));
	}
}

function addBehaviorWhenCellClicked(tableId, behavior)
{
	$("#"+tableId).dataTable().$('td').click(
			function ()
			{
				behavior(tableId, this);
			}
	);
}


var viewed = {};
function addBehaviorWhenCellClickedOnce(tableId, behavior)
{
	if(!viewed[tableId])
	{
		viewed[tableId] = [];
	}
	
	$("#"+tableId).dataTable().$('td').click(
			function ()
			{
				var cell = getClickedCellInfo(tableId, this);
				var positionStr = "["+cell.position[0]+"]";
				var index = $.inArray(positionStr, viewed[tableId]);
				if(index === -1)
				{
					viewed[tableId].push(positionStr);
					behavior(tableId, this);
				}
			}
	);
}




function addBehaviorWhenRowClicked(tableId, behavior)
{
	$("#"+tableId).dataTable().$('tr').click(
			function ()
			{
				behavior(tableId, this);
			}
	);
}




function getClickedCellInfo(tableId, cellObject)
{
	var table = $('#'+tableId).dataTable();
	
	var text = table.fnGetData(cellObject);
	var position = table.fnGetPosition(cellObject);
	var row = table.fnGetData(position[0]);
	
	return {text: text, position: position, row: row};
}




function updateCellText(tableId, cellObject, value)
{
	var table = $('#'+tableId).dataTable();
	var cell = table.cell(cellObject);
	cell.data(value).draw();
}
