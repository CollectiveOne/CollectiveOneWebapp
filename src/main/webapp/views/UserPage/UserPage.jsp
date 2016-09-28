<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%@ include file="../Common/Base/CommonHead.jsp"%>
<script type="text/javascript" src="UserPage/UserPage.js"></script>
<link rel="stylesheet" type="text/css" href="UserPage/UserPage.css" />
<script type="text/javascript" src="../elements/CbtionList/CbtionList.js"></script>
<link rel="stylesheet" type="text/css" href="../elements/CbtionList/CbtionList.css"/>
<script type="text/javascript" src="../elements/FilterElement/FilterElement.js"></script>
<link rel="stylesheet" type="text/css" href="../elements/FilterElement/FilterElement.css"/>
<script type="text/javascript" src="../elements/ReviewsElement/ReviewItem.js"></script>
<link rel="stylesheet" type="text/css" href="../elements/ReviewsElement/ReviewItem.css" />
<link rel="stylesheet" href="../external/jquery.rateyo.css"/>
<script type="text/javascript" src="../external/jquery.rateyo.js"></script>
<title>CoProject - User</title>
</head>

<script type="text/javascript">
	var JSP_username = "<s:property value="username" />";
</script>

<%@ include file="../Common/Base/BodyOver.jsp"%>

<div id="content_pane">
	<div id="user_container" class=cp_box_gray_thick>
		<div id="username_div"></div>
		<div id="projects_contributed_div" class=cp_box_gray_slim>
			<p class=marker>Projects</p>
		</div>
		<div id="accepted_contributions_div" class=cp_box_gray_slim>
			<p class=marker>Contributions</p>
			<div id="contributions_list"></div>
		</div>
	</div>
</div>

<%@ include file="../Common/Base/BodyBelow.jsp"%>

</html>
