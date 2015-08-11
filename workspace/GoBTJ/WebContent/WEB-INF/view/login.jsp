<!DOCTYPE html>
<html>
<head>
	<script src="js/json2.js"></script>
	
	<script type="text/javascript" charset="utf8" src="resource/jquery-1.11.2/jquery-1.11.2.js"></script>
	<script type="text/javascript" charset="utf8" src="resource/jquery-ui-1.11.4/jquery-ui.js"></script>
	<script type="text/javascript" charset="utf8" src="resource/bootstrap-3.3.4/dist/js/bootstrap.js"></script>
	<link rel="stylesheet" type="text/css" href="resource/jquery-ui-1.11.4/jquery-ui.css">
	<link rel="stylesheet" type="text/css" href="resource/bootstrap-3.3.4/dist/css/bootstrap.css">
	
	<link rel="stylesheet" href="//netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.min.css">
	<link rel="stylesheet" href="http://bootsnipp.com/dist/bootsnipp.min.css?ver=70eabcd8097cd299e1ba8efe436992b7">
	<link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400">
	<link rel="stylesheet" href="css/font-awesome.css" >
  
  	<script src="resource/custom/js/ajaxPostRequest.js"></script>
  	<script type="text/javascript" charset="utf8" src="resource/custom/js/post.js"></script>
  	
	<script>
		$(document).ready(
				function()
				{
					$("body").hide();
				    $("body").fadeIn(1000);
				    
					$("#_submitButton").click(
							function()
							{
								var username = $("#_username").val();
								var password = $("#_password").val();
								ajaxPostRequest("${pageContext.request.contextPath}/login.process", {emailAddress: username, password: password},
										function(data)
										{
											var successColor = 'green';
											var failureColor = 'red';
											$("#_display").html('<div style="color:'+(data.loginSuccess ? successColor : failureColor)+'">'+data.displayMessage+'</div>');
											
											if(data.loginSuccess)
											{
												$("body").fadeOut(1500,
														function()
														{
															post('${pageContext.request.contextPath}/board', {});
												    	}
												);
											}
											else
											{
												$("#_loginWindow").effect( "shake", {direction:"left", distance:5/*, times:10*/}, 300);
											}
										}
								);
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
			<form>
				<div align="center">
					<input type="text" id="_username" class="form-control input-sm chat-input" placeholder="email address" style="width:218px; height:30px; border-radius: 10px;" required autofocus/><br />
					<input type="password" id="_password" class="form-control input-sm chat-input" placeholder="password" style="width:218px; height:30px; border-radius: 10px;" required/><br />
				</div>
			</form>
			<div style="text-align:center;">
				<button id="_submitButton" class="btn btn-primary btn-md">login <i class="fa fa-sign-in"></i></button>
			</div>
			<div style="height: 20px;"></div>
		</div>
	</div>

</body>
</html>