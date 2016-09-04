<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
	<%@ include file="../Common/Base/CommonHead.jsp"%>
	<script type="text/javascript" src="DecisionListPage/DecisionListPage.js"></script>
	<link rel="stylesheet" type="text/css" href="DecisionListPage/DecisionListPage.css" />
	<script type="text/javascript" src="../elements/DecisionBox/DecisionBox.js"></script>
	<link rel="stylesheet" type="text/css" href="../elements/DecisionBox/DecisionBox.css" />
	<script type="text/javascript" src="../elements/FilterElement/FilterElement.js"></script>
	<link rel="stylesheet" type="text/css" href="../elements/FilterElement/FilterElement.css"/>
	<script type="text/javascript" src="../elements/ArgumentBox/ArgumentBox.js"></script>
	<link rel="stylesheet" type="text/css" href="../elements/ArgumentBox/ArgumentBox.css" />
	<title>CoProject - Search Decisions</title>
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
