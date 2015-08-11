// https://api.jquery.com/category/deferred-object/
// http://tutorials.jenkov.com/jquery/deferred-objects.html
// https://www.google.com/webhp?sourceid=chrome-instant&ion=1&espv=2&ie=UTF-8#q=javascript+post+response

function ajaxPostRequest(serverURL, jsonObject)
{
	var json_string = JSON.stringify(jsonObject); //JSON.stringify({boardId:1});
	var deferredObject = $.Deferred();
	$.ajax(
		{
			url: serverURL, //"${pageContext.request.contextPath}/board2.module",
			type: "POST",
			contentType: "text/plain; charset=UTF-8", // sending format
			dataType: "text", // expected format for response
			data: json_string,
			success: 
				function(data, textStatus, jqXHR) //called when successful
				{
					// http://zzznara2.tistory.com/94
					var returnData = decodeURIComponent(data);
					console.log(returnData);
					var returnDataObject = jQuery.parseJSON(returnData);
					deferredObject.resolve(returnDataObject);
				},
			complete: function(jqXHR, textStatus){}, //called when complete
			error:
				function(jqXHR, textStatus, errorThrown) //called when there is an error
				{
					deferredObject.reject();
				}, 
		}
	);
	
	return deferredObject.promise();
}



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
