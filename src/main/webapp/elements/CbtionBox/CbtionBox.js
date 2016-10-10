function CbtionBox(container_id,cbtionData) {
	// Parent constructor
	this.container = $(container_id);
	this.cbtion = cbtionData;
	this.conf = { showReviews: true };
	this.init();
};

//Inheritance
CbtionBox.prototype.init = function() {
}

CbtionBox.prototype.updateDecision = function() {
	GLOBAL.serverComm.cbtionGet(this.decision.id,this.cbtionReceivedCallback,this);
}

CbtionBox.prototype.cbtionReceivedCallback = function(cbtionDto) {
	this.cbtion = cbtionDto;
}

CbtionBox.prototype.draw = function() {
	this.container.load("../elements/CbtionBox/CbtionBox.html",this.CbtionBoxLoaded.bind(this));
}

CbtionBox.prototype.CbtionBoxLoaded = function() {
	
	$("#promotion_center_div",this.container).append(this.cbtion.relevance);
	$("#promotion_up_div",this.container).click(this.promoteUpClick.bind(this));
	$("#promotion_down_div",this.container).click(this.promoteDownClick.bind(this));

	$("#title_div",this.container).append("<a href=CbtionPage.action?cbtionId="+ this.cbtion.id+">"+this.cbtion.title+"</a>");
	$("#description_div",this.container).append("<p>"+this.cbtion.description+"</p>");
	
	$("#product_div",this.container).append("<p>"+this.cbtion.product+"</p>");
	
	if(this.cbtion.parentGoalsTags) {
		var nparents = this.cbtion.parentGoalsTags.length;
		for(var ix=0; ix < nparents; ix++) {
			// cycle from last to first as the first parent is the immediate parent
			var parentTag = this.cbtion.parentGoalsTags[nparents - ix - 1];
			$("#goal_div",this.container).append("<a href=GoalPage.action?goalTag="+parentTag+">&#x0371 "+parentTag+"</a>");
		}
	}
	if(this.cbtion.goalTag)	$("#goal_div",this.container).append("<a href=../views/GoalPage.action?goalTag="+this.cbtion.goalTag+">&#x0371 "+this.cbtion.goalTag+"</a>");

	$("#project_div",this.container).append("<a href=ProjectPage.action?projectName="+this.cbtion.projectName+">"+this.cbtion.projectName+"</a>");
	$("#state_div",this.container).append("<p>"+this.cbtion.state+"</p>");
	
	switch(this.cbtion.state) {
		case "PROPOSED":
			$("#creator_div",this.container).append("<p>created by </p>");	
			$("#creator_div",this.container).append("<a href=UserPage.action?username="+this.cbtion.creatorUsername+">"+this.cbtion.creatorUsername+"</a>");
			$("#creator_div",this.container).append("<p> "+getTimeStrSince(this.cbtion.creationDate)+" ago</p>");
			var openDec = new DecisionBoxSmall($("#status_desc_div",this.container),this.cbtion.openDec, GLOBAL.sessionData.userLogged);		
			openDec.draw();
			break;

		case "ACCEPTED":
			$("#status_desc_div",this.container).append("<p>contributed by "+
				"<a href=UserPage.action?username="+this.cbtion.contributorUsername+">"+this.cbtion.contributorUsername+"</a><br />"+
				" for "+this.cbtion.assignedPpoints+" pps</p>");

			$("#status_desc_div p",this.container).addClass("status_accepted_p");
			if(this.conf.showReviews) this.showReviews();
			
			break;

	}
}

CbtionBox.prototype.promoteUpClick = function() {
	GLOBAL.serverComm.cbtionPromote(this.cbtion.id,true,this.promotionSentCallback,this);
}

CbtionBox.prototype.promoteDownClick = function() {
	GLOBAL.serverComm.cbtionPromote(this.cbtion.id,false,this.promotionSentCallback,this);
}

CbtionBox.prototype.promotionSentCallback = function() {
	
}

CbtionBox.prototype.showReviews = function() {
	$("#cbtion_reviews_div",this.container).show();
	GLOBAL.serverComm.reviewsGetOfCbtion(this.cbtion.id,this.reviewsReceivedCallback,this);
}

CbtionBox.prototype.reviewsReceivedCallback = function(data) {
	var reviews = data.reviewDtos;

	for(var ix in reviews) {
		$("#cbtion_reviews_div",this.container).append("<div id=cbtion_review_container"+ix+" class=cbtion_review_container></div>")
		var review = new ReviewItem($("#cbtion_review_container"+ix,this.container),reviews[ix]);
		review.draw();
	}
}

