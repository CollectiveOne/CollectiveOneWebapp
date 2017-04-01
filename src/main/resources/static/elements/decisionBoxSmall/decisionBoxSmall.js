function DecisionBoxSmall(container_id,decisionData) {
	DecisionBase.call(this,container_id,decisionData);
};

//Inheritance
DecisionBoxSmall.prototype = DecisionBase.prototype;

DecisionBoxSmall.prototype.draw = function() {
	this.container.load("/elements/decisionBoxSmall/decisionBoxSmall.html",this.DecisionBoxSmallLoaded.bind(this));
}

DecisionBoxSmall.prototype.DecisionBoxSmallLoaded = function() {
	this.drawVoteStatus();
	
	$("#dec_more_div",this.container).attr('href','/v/decision/'+this.decision.id);
	
	var verdictStr = [];

	switch(this.decision.state) {
		case "IDLE":
		case "OPEN":
			if((this.decision.fromState) && (this.decision.toState)) {
				$("#dec_left_div",this.container).append("Now <span id=state>"+this.decision.fromState+"</span>, switch to <span id=state>"+this.decision.toState+"?");
				if(this.decision.toState == "DELETED") {
					$("#decision_small_div",this.container).addClass("dangerousDecisions");
				}
			} else {
				$("#dec_left_div",this.container).append(LimitStrSize(this.decision.description,80));
			}
			$("#dec_center_div #vote_div",this.container).show();

			$("#accept_div",this.container).click(this.voteAccept.bind(this));
			$("#reject_div",this.container).click(this.voteReject.bind(this));
			break;

		case "CLOSED_ACCEPTED":
		case "CLOSED_DENIED":
		case "CLOSED_EXTERNALLY":
			$("#dec_center_div #vote_div",this.container).hide();
			break;
	}

	if(this.decision.state == "IDLE" || this.decision.state == "OPEN") {
		
	} else {
		
	}
}
