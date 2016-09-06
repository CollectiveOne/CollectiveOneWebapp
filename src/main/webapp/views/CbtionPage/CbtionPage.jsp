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
<title>CoProject - Contribution</title>
</head>

<script type="text/javascript">
	var JSP_cbtionId = "<s:property value="cbtionId" />";
</script>

<%@ include file="../Common/Base/BodyOver.jsp"%>

<div id="content_pane">
	<div id="cbtion_container">
		<div id="project_div">
			<p class="p_header">project:</p>
		</div>
		<div id="goals_div">
			<p class="p_header">goals:</p>
		</div>
		<div id="title_div">
			<p class="p_header">contribution title:</p>
		</div>
		<div id="description_div">
			<p class="p_header">description:</p>
		</div>
		<div id="product_div">
			<p class="p_header">product:</p>
		</div>
		<div id="creator_div">
			<p class="p_header">creator:</p>
		</div>
		<div id="state_div">
			<p class="p_header">state:</p>
		</div>
		<div id="bids_portion_div">
			<p class="p_header">bids:</p>
			<div id="new_bid_btn">
				<p>+bid to this contribution</p>
			</div>
			<div id="new_bid_div">
				<div id="newbid_username_div"></div>
				<div id="newbid_ppoints_div">
					<input type="number" id=newbid_ppoints_in placeholder="pps">
				</div>
				<div id="newbid_description_div">
					<textarea id=newbid_description_in placeholder="description"></textarea>
				</div>
				<div id="newbid_delivery_date_div">
					<input type="text" placeholder="delivery" id="newbid_datepicker">
				</div>
				<div id="newbid_submit_div">
					<p>submit</p>
				</div>
			</div>
			<div id="bids_div"></div>
		</div>
	</div>
</div>

<%@ include file="../Common/Base/BodyBelow.jsp"%>

</html>
