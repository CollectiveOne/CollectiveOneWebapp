<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%@ include file="../Common/Base/CommonHead.jsp"%>
<script type="text/javascript" src="GoalPage/GoalPage.js"></script>
<link rel="stylesheet" type="text/css" href="GoalPage/GoalPage.css" />
<script type="text/javascript" src="../elements/GoalBox/GoalBox.js"></script>
<link rel="stylesheet" type="text/css" href="../elements/GoalBox/GoalBox.css" />
<script type="text/javascript" src="../elements/CbtionList/CbtionList.js"></script>
<link rel="stylesheet" type="text/css" href="../elements/CbtionList/CbtionList.css"/>
<script type="text/javascript" src="../elements/FilterElement/FilterElement.js"></script>
<link rel="stylesheet" type="text/css" href="../elements/FilterElement/FilterElement.css"/>
<title>CollectiveOne - Goal</title>
</head>

<%@ include file="../Common/Base/BodyOver.jsp"%>

<script type="text/javascript">
	var JSP_goalTag = "<s:property value="goalTag" />";
	var JSP_projectName = "<s:property value="projectName" />";
</script>

<div id="content_pane">
	<div id="goal_container">
	</div>
	<div id="cbtions_div">
		<div id="cbtions_container" class="cp_box_gray_thick"></div>
	</div>
</div>

<%@ include file="../Common/Base/BodyBelow.jsp"%>

</html>
