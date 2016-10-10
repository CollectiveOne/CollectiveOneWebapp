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
	<div id="project_container" class=cp_box_gray_thick>
		<div id="project_name"></div>
		<div id="project_description" class=cp_box_gray_slim></div>
		<div id="project_contributors" class=cp_box_gray_slim>
			<p class=marker>Contributors</p>
			<div id="total_pps_div"></div>
			<div id="contributors_list"></div>
		</div>
	</div>
</div>

<%@ include file="../Common/Base/BodyBelow.jsp"%>

</html>
