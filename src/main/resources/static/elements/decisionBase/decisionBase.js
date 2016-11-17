function DecisionBase(container_id,decisionData) {
	// Parent constructor
	this.container = $(container_id);
	this.decision = decisionData;
};

DecisionBase.prototype.update = function() {
	GLOBAL.serverComm.decisionGet(this.decision.id,this.decisionReceivedCallback,this);
}

DecisionBase.prototype.decisionReceivedCallback = function(decisionDto) {
	this.decision = decisionDto;
	this.updateVoteAndDraw();
}

DecisionBase.prototype.updateVoteAndDraw = function() {
	this.updateVoteStatus();
	this.draw();
}

DecisionBase.prototype.updateVoteStatus = function() {
	GLOBAL.serverComm.decisionGetVote(this.decision.id, this.voteStatusReceivedCallback,this);
}

DecisionBase.prototype.voteAccept = function() {
	GLOBAL.serverComm.decisionVote(1,this.decision.id, this.updateVoteStatus, this);
};

DecisionBase.prototype.voteReject = function() {
	GLOBAL.serverComm.decisionVote(0,this.decision.id, this.updateVoteStatus, this);
};

DecisionBase.prototype.voteStatusReceivedCallback = function(thesisDto) {
	/* empty object received if logged user has not voted*/
	if(thesisDto.id) {
		this.thesis = thesisDto;
		this.drawVoteStatus();
	}
}

DecisionBase.prototype.drawVoteStatus = function() {
	if(this.thesis) {
		if(this.thesis.value == 1) {
			$("#accept_div",this.container).removeClass("btn-info").removeClass("btn-success").addClass("btn-success");
			$("#reject_div",this.container).removeClass("btn-info").removeClass("btn-success").addClass("btn-info");
		} else if(this.thesis.value == 0) {
			$("#accept_div",this.container).removeClass("btn-info").removeClass("btn-success").addClass("btn-info");
			$("#reject_div",this.container).removeClass("btn-info").removeClass("btn-success").addClass("btn-success");
		}	
	}
}