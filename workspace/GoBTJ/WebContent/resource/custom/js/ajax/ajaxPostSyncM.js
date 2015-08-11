// https://developer.mozilla.org/en-US/docs/Web/API/XMLHttpRequest/Synchronous_and_Asynchronous_Requests
// https://developer.mozilla.org/en-US/docs/Web/API/XMLHttpRequest/Using_XMLHttpRequest
// https://xhr.spec.whatwg.org/

function ajaxPostSynM(serverURL, jsonObject)
{
	var json_string = JSON.stringify(jsonObject);
	var request = new XMLHttpRequest();
	request.open("POST", serverURL, false);
	request.setRequestHeader("Content-Type", "text/plain;charset=UTF-8");
	request.send(json_string);
	var responseText = request.responseText;
	var responseTextDecoded = decodeURIComponent(responseText);
	var responseObject = jQuery.parseJSON(responseTextDecoded);
	return responseObject;
}