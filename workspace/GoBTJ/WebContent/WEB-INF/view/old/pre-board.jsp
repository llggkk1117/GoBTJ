<!DOCTYPE html>
<html>
<head>
	<script src="js/post.js"></script>
	<script src="js/json2.js"></script>
	<script src="js/jquery-1.11.0.js"></script>
	<script>
		$(document).ready(
				function()
				{
					// var json_string = '${jsonStringReceived}';
					// console.log("json_string: "+json_string);
					// post('${pageContext.request.contextPath}/board.module', {jsonStringForSending: '${jsonStringReceived}'});
					
					var object = {jsonStringForSending: '{"boardId":1, "numOfArticlesInPage":10, "pageNumber":1}'};
					post('${pageContext.request.contextPath}/board.module', object);
				}
		);
	</script>

</head>

<body>

this is pre-board

</body>
</html>