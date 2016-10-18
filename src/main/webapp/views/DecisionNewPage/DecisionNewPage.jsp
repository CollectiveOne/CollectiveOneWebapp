<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%@ include file="../Common/Base/CommonHead.jsp"%>
<script type="text/javascript" src="DecisionNewPage/DecisionNewPage.js"></script>
<link rel="stylesheet" type="text/css" href="DecisionNewPage/DecisionNewPage.css" />
<link rel="stylesheet" type="text/css" href="Common/Base/NewElementPage.css" />
<title>CollectiveOne - New Decision</title>
</head>

<script type="text/javascript">
</script>

<%@ include file="../Common/Base/BodyOver.jsp"%>

<div id="content_pane">
	<h3>Create new decision</h3>
	<div class="select_div" id="project_selector_div">
		<p class="field_name_p">Project</p>
		<select class="select" id="project_select"></select>
	</div>
	<div class="field" id="description">
		<p class="field_name_p">Description</p>
		<textarea class="textarea_in" id="decisionDescription_in"></textarea>
	</div>
	<button id="create_btn">Create</button>
</div>

<%@ include file="../Common/Base/BodyBelow.jsp"%>

</html>
