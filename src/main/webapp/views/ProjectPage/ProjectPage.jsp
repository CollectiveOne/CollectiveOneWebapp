<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%@ include file="../Common/Base/CommonHead.jsp"%>
<script type="text/javascript" src="ProjectPage/ProjectPage.js"></script>
<link rel="stylesheet" type="text/css" href="ProjectPage/ProjectPage.css" />
<title>CollectiveOne - Project</title>
</head>

<script type="text/javascript">
	var JSP_projectName = "<s:property value="projectName" />";
</script>

<%@ include file="../Common/Base/BodyOver.jsp"%>

<div id="content_pane">
</div>

<%@ include file="../Common/Base/BodyBelow.jsp"%>

</html>
