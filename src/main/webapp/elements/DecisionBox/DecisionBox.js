function DecisionBox(container_id,decisionData, voter) {
	DecisionBase.call(this,container_id,decisionData, voter);
};

DecisionBox.prototype = DecisionBase.prototype;

DecisionBox.prototype.draw = function() {
	this.container.load("../elements/DecisionBox/DecisionBox.html",this.decisionBoxLoaded.bind(this));
}

DecisionBox.prototype.decisionBoxLoaded = function() {
	
	$("#accept_div",this.container).click(this.voteAccept.bind(this));
	$("#reject_div",this.container).click(this.voteReject.bind(this));
	
	this.updateVoteStatus();
	
	$("#description",this.container).append($("<p>"+this.decision.description+"</p>"));
	
	if(this.decision.fromState != null && this.decision.toState != null) {
		$("#from_to_state",this.container).append($("<p>Changes the state from "
			+this.decision.fromState
			+" to "+this.decision.toState+"</p>"));	
	} else {
		$("#from_to_state",this.container).hide();
	}
	
	var stateStr = '';
	
	switch(this.decision.state) {
		case "IDLE":
		case "OPEN":
			stateStr = this.decision.state;
			break;

		case "CLOSED_ACCEPTED":
		case "CLOSED_DENIED":
		case "CLOSED_EXTERNALLY":
			$("#center_div",this.container).hide();
			stateStr = "CLOSED";
			break;
	}

	$("#state_div",this.container).append($("<p>Currenlty <span id=state>"+stateStr+"</span></p>"));

	switch(this.decision.state) {
		case "IDLE":
		case "OPEN":
			$("#right_div #state",this.container).css("color","DarkGreen");
			break;

		case "CLOSED_ACCEPTED":
		case "CLOSED_DENIED":
		case "CLOSED_EXTERNALLY":
			$("#right_div #state",this.container).css("color","DarkRed");
			stateStr = "CLOSED";
			break;
	}
	
	if(this.decision.state != "IDLE") {
		$("#clarity",this.container).append($("<p>clarity: "+floatToChar(this.decision.clarity*100,1)+"</p>"));
		$("#stability",this.container).append($("<p>stability: "+floatToChar(this.decision.stability*100,1)+"</p>"));
		$("#votedRatio",this.container).append($("<p>voted ratio: "+floatToChar(this.decision.ppsCum/this.decision.ppsTot*100,2)+"%</p>"));
		$("#acceptToRejectRatio",this.container).append($("<p>yes ratio: "+floatToChar(this.decision.log_l1l0,2)+"</p>"));
		
		var verdictStr = [];
		if(this.decision.verdict == 1) {
			verdictStr = "YES"; 
		} else {
			verdictStr = "NO";
		}
		
		var verdictPre = ''; 
		var verdictTime = '';
		var verdictTimePost = '';

		switch(this.decision.state) {
			case "IDLE":
			case "OPEN":
				verdictPre = "Temporary verdict is ";
				verdictTime = "to be closed in less than "
				+getNiceTimeStr((1-this.decision.elapsedFactor)*this.decision.verdictHours*3600);
				break;

			case "CLOSED_ACCEPTED":
			case "CLOSED_DENIED":
			case "CLOSED_EXTERNALLY":
				verdictPre = "Final verdict: ";
				verdictTime = "taken "+getTimeStrSince(this.decision.actualVerdictDate)+" ago";
				break;
		}
		
		$("#verdict_div",this.container).append($("<p>"+verdictPre+"<span id=verdict>"+verdictStr+"</span></p>"));
		$("#verdictTime_div",this.container).append("<p>"+verdictTime+"</p>");

		if(this.decision.verdict == 1) {
			$("#right_div #verdict",this.container).css("color","DarkGreen");
		} else {
			$("#right_div #verdict",this.container).css("color","DarkRed");
		}
	}
}