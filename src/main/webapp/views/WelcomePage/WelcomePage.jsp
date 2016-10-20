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
	<div id="presentation_container">
		<iframe src="https://docs.google.com/presentation/d/1Sma1CKVq1plvPrvglOs5qHB1ldjzkHHE4g_yJBRu_0Q/embed?start=false&loop=false&delayms=3000" 
			frameborder="0" width="960" height="569" allowfullscreen="true" mozallowfullscreen="true" 
			webkitallowfullscreen="true">
		</iframe>
	</div>
	
	<div id="under_presentation">
		<div class="row_container">
			<p>Take a look at the white paper draft</p>
				<a id=link_to_gdoc_container href="https://docs.google.com/document/d/1C9dCnPbKEZzCqaLYjmhsYIv0t0ehlaRbzwRaMZtzNNA" target='_blank'>
					<img id="logo" src="../images/GDocs.png">
				</a>
		</div>
		<div class="row_container">
			<p>Join the conversation at </p>
			<a id=link_to_reddit_container href="https://www.reddit.com/r/CollectiveOne/" target='_blank'>
				<img id="logo" src="../images/Reddit.png">
			</a>
			<a id=link_to_slack_container href="https://collectiveone.slack.com/" target='_blank'>
				<img id="logo" src="../images/Slack.png">
			</a>
		</div>
		<div class="row_container">
			<p>Contact us at <a href="mailto:contact@collectiveone.org" target="_top">contact@collectiveone.org</a></p>
		</div>
	</div>
</div>

<%@ include file="../Common/Base/BodyBelow.jsp"%>

</html>
