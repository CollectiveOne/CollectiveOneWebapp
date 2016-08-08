<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
	<%@ include file="../Common/Base/CommonHead.jsp"%>
	<script type="text/javascript" src="CbtionListPage/CbtionListPage.js"></script>
	<link rel="stylesheet" type="text/css" href="CbtionListPage/CbtionListPage.css" />
	<title>CoProject - Search Contributions</title>
</head>

<script type="text/javascript">
	var JSP_projectFilter = "<s:property value="projectFilter" />";
</script>

<%@ include file="../Common/Base/BodyOver.jsp"%>
<div id="content_pane">
	<div id="filter_bar">
		<div id="filter_btn">
			<p id="filter_bar_p" class="botton_text">filter</p>
		</div>
		<div id="page_btns">
			<div id="next_page"><p>&gt</p></div>
			<div id="back_page"><p>&lt</p></div>
			<div id="page_set"></div>
		</div>
	</div>
	<div id="filter_contents">
		<div id="filter_featured_projects"></div>
		<div id="filter_cbtion_state"></div>
	</div>
	<div id="list_of_elements"></div>
</div>
<%@ include file="../Common/Base/BodyBelow.jsp"%>

</html>
