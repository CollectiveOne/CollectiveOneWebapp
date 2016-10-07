<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%@ include file="../Common/Base/CommonHead.jsp"%>
<script type="text/javascript" src="DecisionPage/DecisionPage.js"></script>
<link rel="stylesheet" type="text/css" href="DecisionPage/DecisionPage.css" />
<script type="text/javascript" src="../elements/DecisionBox/DecisionBox.js"></script>
<link rel="stylesheet" type="text/css" href="../elements/DecisionBox/DecisionBox.css" />
<script type="text/javascript" src="../elements/ArgumentBox/ArgumentBox.js"></script>
<link rel="stylesheet" type="text/css" href="../elements/ArgumentBox/ArgumentBox.css" />
<title>CollectiveOne - Decision</title>
</head>

<%@ include file="../Common/Base/BodyOver.jsp"%>

<script type="text/javascript">
	var JSP_decisionId = "<s:property value="decisionId" />";
</script>

<div id="content_pane">
	<div id="decision_container">
	</div>
</div>

<%@ include file="../Common/Base/BodyBelow.jsp"%>

</html>
