
function ajaxPostAsync(serverURL, jsonObject, handleData)
{
	var json_string = JSON.stringify(jsonObject); //JSON.stringify({boardId:1});
	$.ajax(
		{
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
		}
	);
}