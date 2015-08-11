
function ajaxPostSync(serverURL, jsonObject, handleData)
{
	var result = null;
	var json_string = JSON.stringify(jsonObject); //JSON.stringify({boardId:1});
	$.ajax(	
		{
			url: serverURL, //"${pageContext.request.contextPath}/board2.module",
			type: "POST",
			contentType: "text/plain; charset=UTF-8", // sending format
			dataType: "text", // expected format for response
			data: json_string,
			async: false,
			success: 
				function(data, textStatus, jqXHR) //called when successful
				{
					// http://zzznara2.tistory.com/94
					var returnData = decodeURIComponent(data);
					console.log(returnData);
					result = jQuery.parseJSON(returnData);
				},
			complete: function(jqXHR, textStatus){}, //called when complete
			error: function(jqXHR, textStatus, errorThrown){}, //called when there is an error
		}
	);
	return result;
}