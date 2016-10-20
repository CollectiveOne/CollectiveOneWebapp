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
	
	<div class="row_container">
		<div id="link_to_reddit_container"><a href="https://www.reddit.com/r/CollectiveOne/" target='_blank'><img id="logo" src="../images/Reddit.png"></a></div>
		<div id="link_to_slack_container"><a href="https://collectiveone.slack.com/" target='_blank'><img id="logo" src="../images/Slack.png"></a></div>
		<div id="join_the_conversation"><p>Join the conversation at </p></div>
	</div>
	<div class="row_container">
		<div id="learn_more">
			<p>Take a look at the <a href="https://docs.google.com/document/d/1C9dCnPbKEZzCqaLYjmhsYIv0t0ehlaRbzwRaMZtzNNA/edit?usp=sharing" target='_blank'>white paper draft</a></p>
		</div>
	</div>
	<div class="row_container">
		<div id="contact_us">
			<p>Contact us at <a href="mailto:contact@collectiveone.org" target="_top">contact@collectiveone.org</a></p>
		</div>
	</div>
</div>

<%@ include file="../Common/Base/BodyBelow.jsp"%>

</html>
