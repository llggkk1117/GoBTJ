<!DOCTYPE html>
<html>
<head>
	<script src="resource/custom/js/post.js"></script>
</head>

<body>
	
<%
	// New location to be redirected
	String site = new String(request.getContextPath()+"/login");
	response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
	response.setHeader("Location", site);
%>
	
</body>
</html>