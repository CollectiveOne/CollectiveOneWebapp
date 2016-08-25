function DecisionBox(container_id,decisionData, voter) {
	// Parent constructor
	this.container = $(container_id);
	this.decision = decisionData;
	this.voter = voter || null;
	this.thesis = null;
	this.init();
};

//Inheritance
DecisionBox.prototype.init = function() {
}

DecisionBox.prototype.updateDecision = function() {
	GLOBAL.serverComm.decisionGet(this.decision.id,this.decisionReceivedCallback,this);
}

DecisionBox.prototype.decisionReceivedCallback = function(decisionDto) {
	this.decision = decisionDto;
}

DecisionBox.prototype.draw = function() {
	this.container.load("../elements/DecisionBox/DecisionBox.html",this.decisionBoxLoaded.bind(this));
}

DecisionBox.prototype.decisionBoxLoaded = function() {
	
	$("#accept_div",this.container).click(this.voteAccept.bind(this));
	$("#reject_div",this.container).click(this.voteReject.bind(this));
	
	this.updateVoteStatus();
	
	$("#state",this.container).append($("<p>"+this.decision.state+"</p>"));
	var verdictStr = [];

	if(this.decision.verdict == 1) verdictStr = "ACCEPT";
	else verdictStr = "REJECT";
	$("#verdict",this.container).append($("<p>"+verdictStr+"</p>"));
	$("#verdictTime",this.container).append("<p>"+floatToChar((1-this.decision.elapsedFactor)*this.decision.verdictHours,0)+" of "+this.decision.verdictHours+" hr</p>");
	$("#description",this.container).append($("<p>"+this.decision.description+"</p>"));
	$("#clarity",this.container).append($("<p>c: "+floatToChar(this.decision.clarity*100,1)+"</p>"));
	$("#stability",this.container).append($("<p>s: "+floatToChar(this.decision.stability*100,1)+"</p>"));
	$("#acceptToRejectRatio",this.container).append($("<p>r: "+floatToChar(this.decision.log_l1l0,2)+"</p>"));
	$("#votedRatio",this.container).append($("<p>v: "+floatToChar(this.decision.ppsCum/this.decision.ppsTot*100,2)+"%</p>"));
}

DecisionBox.prototype.updateVoteStatus = function() {
	if(this.voter != null) {
		GLOBAL.serverComm.decisionGetVote(this.decision.id,this.voter.id,this.voteStatusReceivedCallback,this);
	}
}

DecisionBox.prototype.voteAccept = function() {
	GLOBAL.serverComm.decisionVote(1,this.decision.id, this.updateVoteStatus, this);
};

DecisionBox.prototype.voteReject = function() {
	GLOBAL.serverComm.decisionVote(0,this.decision.id, this.updateVoteStatus, this);
};

DecisionBox.prototype.voteStatusReceivedCallback = function(thesisDto) {
	this.thesis = thesisDto;
	this.drawVoteStatus();
}

DecisionBox.prototype.drawVoteStatus = function() {
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