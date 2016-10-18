<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%@ include file="../Common/Base/CommonHead.jsp"%>
<script type="text/javascript" src="CbtionNewPage/CbtionNewPage.js"></script>
<link rel="stylesheet" type="text/css" href="CbtionNewPage/CbtionNewPage.css" />
<link rel="stylesheet" type="text/css" href="Common/Base/NewElementPage.css" />
<title>CollectiveOne - New Contribution</title>
</head>

<script type="text/javascript">
</script>

<%@ include file="../Common/Base/BodyOver.jsp"%>

<div id="content_pane">
	<h3>Create new contribution</h3>
	
	<div class="select_div" id="project_selector_div">
		<p class="field_name_p">Project</p>
		<select class="select" id="project_select"></select>
	</div>
	<div class="field" id="title">
		<p class="field_name_p">Title</p>
		<input type="text" class="field_in" id="title_in">
	</div>
	<div class="field" id="description">
		<p class="field_name_p">Description</p>
		<textarea class="textarea_in" id="description_in"></textarea>
	</div>
	<div class="field" id="product">
		<p class="field_name_p">Product</p>
		<textarea class="textarea_in" id="product_in"></textarea>
	</div>
	<div class="field" id="goalTag_div">
		<p class="field_name_p">Goal</p>
		<input type="text" class="field_in" id="goalTag_selector">
	</div>
	
	<button id="create_btn">Create</button>
</div>

<%@ include file="../Common/Base/BodyBelow.jsp"%>

</html>
