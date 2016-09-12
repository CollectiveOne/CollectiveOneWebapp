<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%@ include file="../Common/Base/CommonHead.jsp"%>
<script type="text/javascript" src="CbtionPage/CbtionPage.js"></script>
<link rel="stylesheet" type="text/css" href="CbtionPage/CbtionPage.css" />
<script type="text/javascript" src="../elements/BidBox/BidBox.js"></script>
<link rel="stylesheet" type="text/css" href="../elements/BidBox/BidBox.css" />
<script type="text/javascript" src="../elements/ReviewsElement/ReviewsElement.js"></script>
<link rel="stylesheet" type="text/css" href="../elements/ReviewsElement/ReviewsElement.css" />
<script type="text/javascript" src="../elements/ReviewsElement/ReviewItem.js"></script>
<link rel="stylesheet" type="text/css" href="../elements/ReviewsElement/ReviewItem.css" />
<script type="text/javascript" src="../elements/CbtionBoxComplete/CbtionBoxComplete.js"></script>
<link rel="stylesheet" type="text/css" href="../elements/CbtionBoxComplete/CbtionBoxComplete.css" />
<link rel="stylesheet" type="text/css" href="../external/jquery-ui-datepicker.css"/>
<link rel="stylesheet" href="../external/jquery.rateyo.css"/>
<script type="text/javascript" src="../external/jquery.rateyo.js"></script>

<title>CoProject - Contribution</title>
</head>

<script type="text/javascript">
	var JSP_cbtionId = "<s:property value="cbtionId" />";
</script>

<%@ include file="../Common/Base/BodyOver.jsp"%>

<div id="content_pane">
	<div id="cbtion_container">
	</div>
</div>

<%@ include file="../Common/Base/BodyBelow.jsp"%>

</html>
