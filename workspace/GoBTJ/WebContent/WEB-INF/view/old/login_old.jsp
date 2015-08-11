<!DOCTYPE html>
<html>
<head>
	<script src="js/json2.js"></script>
	<script src="js/jquery-1.11.0.js"></script>

	<link rel="stylesheet" type="text/css" href="resource/bootstrap-3.3.4/dist/css/bootstrap.css">
	<!--
	<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
	-->
	<link rel="stylesheet" href="//netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.min.css">
	<link rel="stylesheet" href="http://bootsnipp.com/dist/bootsnipp.min.css?ver=70eabcd8097cd299e1ba8efe436992b7">
	<link rel="stylesheet" href="//code.jquery.com/ui/1.11.3/themes/smoothness/jquery-ui.css">
	<link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400">
	<link rel="stylesheet" href="css/font-awesome.css" >
	
	<script src="http://code.jquery.com/jquery-1.11.2.min.js"></script>
	<script src="http://code.jquery.com/ui/1.11.3/jquery-ui.min.js"></script>
  
	<script>
		function isJsonString(str)
		{
			var result = false;
		    try
		    {
		        JSON.parse(str);
		        result = true;
		    }
		    catch(e){}
		    return result;
		}
		
		function isUndefined(obj)
		{
			var result = false;
			if(typeof obj == 'undefined')
			{
				result = true;
			}
			return result;
		}
	</script>
	
	<script>
		$(document).ready(
				function()
				{
					// receiving json from controller
					var jsonStringReceived = '${jsonStringReceived}';
					console.log("jsonStringReceived: "+jsonStringReceived);
					if(jsonStringReceived) // if it is not undefined, null, 0, or empty string
					{
						var jsonObjectReceived = null;
						try
						{
							jsonObjectReceived = JSON.parse(jsonStringReceived);	
						}
						catch(e){}
						
						if(jsonObjectReceived && !jsonObjectReceived.loginSuccess)
						{
							var displayMessage = jsonObjectReceived.errorMessage;
							document.getElementById("_display").innerHTML = displayMessage;
							$( "#_loginWindow" ).effect( "shake", {direction:"left", distance:5, /*times:10*/}, 300);
						}
					}
					
					
					// sending json to controller
					document.getElementById("_submitButton").addEventListener("click", 
							function myFunction()
							{
								var username = document.getElementById("_username").value;
								var password = document.getElementById("_password").value;
								document.getElementById("_jsonStringForSending").value = JSON.stringify({emailAddress: username, password: password});
							}
					);
					
					
				}
		);
	</script>
</head>

<body style="padding-top:10%; padding-bottom:40px; background-color:#efefef; -webkit-font-smoothing:antialiased; font:normal 14px Roboto,arial,sans-serif;">
	
	<div id="outer" style="width:100%">  
		<div id="_loginWindow" class="form-login" style="background-color: #E1E1E1; border-radius: 15px; border-color:#c0c0c0; border-width: 5px; box-shadow:0 1px 0 #c3c3c3; width:258px; margin: 0 auto;">
			<div style="height: 10px;"></div>
			<h4 id="_display" style="border:0 solid #ffffff; border-bottom-width:1px; padding-bottom:10px; text-align: center;">Welcome</h4>
			<form action="${pageContext.request.contextPath}/login.module" method="post">
				<div align="center">
					<input id="_jsonStringForSending" type="hidden" name="jsonStringForSending">
					<input type="text" id="_username" class="form-control input-sm chat-input" placeholder="email address" style="width:218px; height:30px; border-radius: 10px;" required autofocus/><br />
					<input type="password" id="_password" class="form-control input-sm chat-input" placeholder="password" style="width:218px; height:30px; border-radius: 10px;" required/><br />
				</div>
				<div style="text-align:center;">
					<button id="_submitButton" class="btn btn-primary btn-md" type="submit">login <i class="fa fa-sign-in"></i></button>
				</div>
			</form>
			<div style="height: 20px;"></div>
		</div>
	</div>


</body>
</html>