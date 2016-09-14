<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%@ include file="../Common/Base/CommonHead.jsp"%>
<script type="text/javascript" src="GoalPage/GoalPage.js"></script>
<link rel="stylesheet" type="text/css" href="GoalPage/GoalPage.css" />
<script type="text/javascript" src="../elements/GoalBox/GoalBox.js"></script>
<link rel="stylesheet" type="text/css" href="../elements/GoalBox/GoalBox.css" />
<title>CoProject - Goal</title>
</head>

<%@ include file="../Common/Base/BodyOver.jsp"%>

<script type="text/javascript">
	var JSP_goalTag = "<s:property value="goalTag" />";
</script>

<div id="content_pane">
	<div id="goal_container">
	</div>
</div>

<%@ include file="../Common/Base/BodyBelow.jsp"%>

</html>
