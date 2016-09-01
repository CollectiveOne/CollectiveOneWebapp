function DecisionBase(container_id,decisionData, voter) {
	// Parent constructor
	this.container = $(container_id);
	this.decision = decisionData;
	this.voter = voter || null;
	this.thesis = null;
};

DecisionBase.prototype.updateDecision = function() {
	GLOBAL.serverComm.decisionGet(this.decision.id,this.decisionReceivedCallback,this);
}

DecisionBase.prototype.decisionReceivedCallback = function(decisionDto) {
	this.decision = decisionDto;
}

DecisionBase.prototype.updateVoteStatus = function() {
	if(this.voter != null) {
		GLOBAL.serverComm.decisionGetVote(this.decision.id,this.voter.id,this.voteStatusReceivedCallback,this);
	}
}

DecisionBase.prototype.voteAccept = function() {
	GLOBAL.serverComm.decisionVote(1,this.decision.id, this.updateVoteStatus, this);
};

DecisionBase.prototype.voteReject = function() {
	GLOBAL.serverComm.decisionVote(0,this.decision.id, this.updateVoteStatus, this);
};

DecisionBase.prototype.voteStatusReceivedCallback = function(thesisDto) {
	this.thesis = thesisDto;
	this.drawVoteStatus();
}

DecisionBase.prototype.drawVoteStatus = function() {
	if(this.thesis == null) {
		$("#accept_div",this.container).css("background-color","#D1DBBD");
		$("#reject_div",this.container).css("background-color","#D1DBBD");
	} else if(this.thesis.value == 1) {
		$("#accept_div",this.container).css("background-color","red");
		$("#accept_div p",this.container).css("color","white");
		
		$("#reject_div",this.container).css("background-color","#D1DBBD");
		$("#reject_div p",this.container).css("color","#888844");
		
	} else if(this.thesis.value == 0) {
		$("#accept_div",this.container).css("background-color","#D1DBBD");
		$("#accept_div p",this.container).css("color","#888844");
		
		$("#reject_div",this.container).css("background-color","red");
		$("#reject_div p",this.container).css("color","white");
	}
}