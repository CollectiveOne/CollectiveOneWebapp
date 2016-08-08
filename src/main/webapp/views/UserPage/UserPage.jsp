<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%@ include file="../Common/Base/CommonHead.jsp"%>
<script type="text/javascript" src="UserPage/UserPage.js"></script>
<link rel="stylesheet" type="text/css" href="UserPage/UserPage.css" />
<title>CoProject - User</title>
</head>

<script type="text/javascript">
	var JSP_username = "<s:property value="username" />";
</script>

<%@ include file="../Common/Base/BodyOver.jsp"%>

<div id="content_pane">
	<div id="user_container">
		<div id="username_div">
			<p class="p_header">username:</p>
		</div>
		<div id="projects_contributed_div">
			<p class="p_header">contributor of:</p>
		</div>
		
	</div>
</div>

<%@ include file="../Common/Base/BodyBelow.jsp"%>

</html>
