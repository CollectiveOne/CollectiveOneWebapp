function CbtionBox(container_id,cbtionData) {
	// Parent constructor
	this.container = $(container_id);
	this.cbtion = cbtionData;
	this.conf = { showReviews: true };
};

//Inheritance
CbtionBox.prototype.update = function() {
	GLOBAL.serverComm.cbtionGet(this.cbtion.id,this.cbtionDataReceived,this);
}

CbtionBox.prototype.cbtionDataReceived = function(cbtionDto) {
	this.cbtion = cbtionDto;
	this.draw();	
}

CbtionBox.prototype.draw = function() {
	this.container.load("/elements/cbtionBox/cbtionBox.html",this.CbtionBoxLoaded.bind(this));
}

CbtionBox.prototype.CbtionBoxLoaded = function() {
	
	$("#promotion_center_div",this.container).append(this.cbtion.relevance);
	$("#promotion_up_div",this.container).click(this.promoteUpClick.bind(this));
	$("#promotion_down_div",this.container).click(this.promoteDownClick.bind(this));

	$("#title_div",this.container).append("<a href=/views/cbtionPageR/"+ this.cbtion.id+">"+this.cbtion.title+"</a>");
	$("#description_div",this.container).append(this.cbtion.description);
	
	$("#product_div",this.container).append(this.cbtion.product);
	
	if(this.cbtion.parentGoalsTags) {
		var nparents = this.cbtion.parentGoalsTags.length;
		for(var ix=0; ix < nparents; ix++) {
			// cycle from last to first as the first parent is the immediate parent
			var parentTag = this.cbtion.parentGoalsTags[nparents - ix - 1];
			$("#goal_div",this.container).append(getGoalPageLink(parentTag,this.cbtion.projectName));
		}
	}
	if(this.cbtion.goalTag)	$("#goal_div",this.container).append(getGoalPageLink(this.cbtion.goalTag,this.cbtion.projectName));

	$("#project_div",this.container).append(getProjectLink(this.cbtion.projectName));
	$("#state_div",this.container).append(this.cbtion.state);
	
	switch(this.cbtion.state) {
		case "PROPOSED":
			$("#creator_div",this.container).append("created by ");	
			$("#creator_div",this.container).append(getUserPageLink(this.cbtion.creatorUsername));
			$("#creator_div",this.container).append(" "+getTimeStrSince(this.cbtion.creationDate)+" ago");
			if(isUserLogged()) {
				var openDec = new DecisionBoxSmall($("#status_desc_div",this.container),this.cbtion.openDec, GLOBAL.sessionData.userLogged);		
				openDec.updateVoteAndDraw();
			}
			break;

		case "ACCEPTED":
			$("#status_desc_div",this.container).append("contributed by "+
				getUserPageLink(this.cbtion.contributorUsername)+"<br />"+
				" for "+this.cbtion.assignedPpoints+" pps");

			$("#status_desc_div p",this.container).addClass("status_accepted_p");
			if(this.conf.showReviews) this.showReviews();
			
			break;

	}
}

CbtionBox.prototype.promoteUpClick = function() {
	GLOBAL.serverComm.cbtionPromote(this.cbtion.id,true,this.update,this);
}

CbtionBox.prototype.promoteDownClick = function() {
	GLOBAL.serverComm.cbtionPromote(this.cbtion.id,false,this.update,this);
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

