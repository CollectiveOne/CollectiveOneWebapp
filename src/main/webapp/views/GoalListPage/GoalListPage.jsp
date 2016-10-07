<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
	<%@ include file="../Common/Base/CommonHead.jsp"%>
	<script type="text/javascript" src="GoalListPage/GoalListPage.js"></script>
	<link rel="stylesheet" type="text/css" href="GoalListPage/GoalListPage.css" />
	<script type="text/javascript" src="../elements/GoalBox/GoalBox.js"></script>
	<link rel="stylesheet" type="text/css" href="../elements/GoalBox/GoalBox.css" />
	<script type="text/javascript" src="../elements/FilterElement/FilterElement.js"></script>
	<link rel="stylesheet" type="text/css" href="../elements/FilterElement/FilterElement.css"/>
	<title>CollectiveOne - Search Goals</title>
</head>

<script type="text/javascript">
	var JSP_projectFilter = "<s:property value="projectFilter" />";
</script>

<%@ include file="../Common/Base/BodyOver.jsp"%>
<div id="content_pane">
	<div id="filter_container"></div>
	<div id="list_of_elements"></div>
</div>
<%@ include file="../Common/Base/BodyBelow.jsp"%>

</html>
