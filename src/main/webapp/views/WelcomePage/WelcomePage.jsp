<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%@ include file="../Common/Base/CommonHead.jsp"%>
<script type="text/javascript" src="WelcomePage/WelcomePage.js"></script>
<link rel="stylesheet" type="text/css" href="WelcomePage/WelcomePage.css" />
<title>CollectiveOne - Welcome</title>
</head>

<%@ include file="../Common/Base/BodyOver.jsp"%>

<div id="content_pane">
	
	<div id="preferred_browser_div" class="cp_box_gray_slim">
		<p>The preferred browsers for this page are Chrome or Firefox</p>
	</div>
	
	<div id="presentation_container">
		<iframe width="800" height="450" src="https://www.youtube.com/embed/QT0T2EvtLok" frameborder="0" allowfullscreen></iframe>
	</div>
	
	<div id="under_presentation">
		<div class="row_container">
			<div id="first_box" class="cp_box_gray_slim">
				<p>Take a look at the white paper draft</p>
				<a id=link_to_gdoc_container href="https://docs.google.com/document/d/1C9dCnPbKEZzCqaLYjmhsYIv0t0ehlaRbzwRaMZtzNNA" target='_blank'>
					<img id="logo" src="../images/GDocs.png">
				</a>
			</div>
			
			<div id="second_box" class="cp_box_gray_slim">
				<p>Join the conversation at </p>
				<a id=link_to_reddit_container href="https://www.reddit.com/r/CollectiveOne/" target='_blank'>
					<img id="logo" src="../images/Reddit.png">
				</a>
				<a id=link_to_slack_container href="https://collectiveone.slack.com/" target='_blank'>
					<img id="logo" src="../images/Slack.png">
				</a>
			</div>
			
			<div id="third_box" class="cp_box_gray_slim">
				<p>Contact us at</p>
				 <a href="mailto:contact@collectiveone.org" target="_top">contact@collectiveone.org</a>
			</div>
		</div>
	</div>
</div>

<%@ include file="../Common/Base/BodyBelow.jsp"%>

</html>
