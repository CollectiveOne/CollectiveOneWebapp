function DecisionBoxSmall(container_id,decisionData, voter) {
	// Parent constructor
	this.container = $(container_id);
	this.decision = decisionData;
	this.voter = voter || null;
	this.thesis = null;
	this.init();
};

//Inheritance
DecisionBoxSmall.prototype.init = function() {
}

DecisionBoxSmall.prototype.updateDecision = function() {
	GLOBAL.serverComm.decisionGet(this.decision.id,this.decisionReceivedCallback,this);
}

DecisionBoxSmall.prototype.decisionReceivedCallback = function(decisionDto) {
	this.decision = decisionDto;
}

DecisionBoxSmall.prototype.draw = function() {
	this.container.load("../elements/DecisionBoxSmall/DecisionBoxSmall.html",this.DecisionBoxSmallLoaded.bind(this));
}

DecisionBoxSmall.prototype.DecisionBoxSmallLoaded = function() {
	
	this.updateVoteStatus();
	
	var verdictStr = [];
	if(this.decision.state == "IDLE" || this.decision.state == "OPEN") {

		$("#dec_left_div #current_state",this.container).html(this.decision.fromState);
		$("#dec_left_div #candidate_state",this.container).html(this.decision.toState+"?");

		// if decision is still open
		$("#dec_center_div #vote_div",this.container).html(	"<div id=accept_div>" +
														"	<p>Yes</p>" +
														"</div>" +
														"<div id=reject_div>" +
														"	<p>No</p>" +
														"</div>");

		$("#accept_div",this.container).click(this.voteAccept.bind(this));
		$("#reject_div",this.container).click(this.voteReject.bind(this));
	
		var statusStr = [];
		
		if(this.decision.state == "OPEN") {
			var verdictStr = [];
			if(this.decision.verdict == 1) verdictStr = "Yes";
			else verdictStr = "No";
			
			var elapsedHrs = (1-this.decision.elapsedFactor)*this.decision.verdictHours
			var remHrs = this.decision.verdictHours - elapsedHrs;
			
			statusStr = "Verdict:"+verdictStr+" in < "+floatToChar(remHrs,1)+" hr";
			
		} else if (this.decision.state == "IDLE") {
			statusStr = "Idle";
		}
		
		$("#dec_right_div #verdict_status",this.container).html(statusStr);
		
	} else {
		$("#dec_left_div #current_state_div",this.container).html("<p>CLOSED</p>");
		$("#dec_left_div #candidate_state_div",this.container).html("");

		var verdictStr = [];
		if(this.decision.verdict == 1) verdictStr = "yes";
		else verdictStr = "no";
		
		$("#dec_right_div #verdict_status",this.container).html(verdictStr+", closed");
	}
}


DecisionBoxSmall.prototype.updateVoteStatus = function() {
	if(this.voter != null) {
		GLOBAL.serverComm.decisionGetVote(this.decision.id,this.voter.id,this.voteStatusReceivedCallback,this);
	}
}

DecisionBoxSmall.prototype.voteAccept = function() {
	GLOBAL.serverComm.decisionVote(1,this.decision.id, this.updateVoteStatus, this);
};

DecisionBoxSmall.prototype.voteReject = function() {
	GLOBAL.serverComm.decisionVote(0,this.decision.id, this.updateVoteStatus, this);
};

DecisionBoxSmall.prototype.voteStatusReceivedCallback = function(thesisDto) {
	this.thesis = thesisDto;
	this.drawVoteStatus();
}

DecisionBoxSmall.prototype.drawVoteStatus = function() {
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