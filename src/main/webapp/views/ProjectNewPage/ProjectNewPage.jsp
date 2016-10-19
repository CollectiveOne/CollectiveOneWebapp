<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%@ include file="../Common/Base/CommonHead.jsp"%>
<script type="text/javascript" src="ProjectNewPage/ProjectNewPage.js"></script>
<link rel="stylesheet" type="text/css" href="Common/Base/NewElementPage.css" />
<title>CollectiveOne - New Project</title>
</head>

<script type="text/javascript">
</script>

<%@ include file="../Common/Base/BodyOver.jsp"%>

<div id="content_pane">
	<h3>Create new project</h3>
	
	<div class="field" id="title">
		<p class="field_name_p">Name</p>
		<input type="text" class="field_in" id="name_in">
	</div>
	<div class="field" id="description">
		<p class="field_name_p">Description</p>
		<textarea class="textarea_in" id="description_in"></textarea>
	</div>
		
	<button id="create_btn">Create</button>
</div>

<%@ include file="../Common/Base/BodyBelow.jsp"%>

</html>
