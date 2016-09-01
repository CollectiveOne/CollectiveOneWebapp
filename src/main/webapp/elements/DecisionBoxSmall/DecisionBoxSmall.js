function DecisionBoxSmall(container_id,decisionData, voter) {
	DecisionBase.call(this,container_id,decisionData, voter);
};

//Inheritance
DecisionBoxSmall.prototype = DecisionBase.prototype;

DecisionBoxSmall.prototype.draw = function() {
	this.container.load("../elements/DecisionBoxSmall/DecisionBoxSmall.html",this.DecisionBoxSmallLoaded.bind(this));
}

DecisionBoxSmall.prototype.DecisionBoxSmallLoaded = function() {
	
	var verdictStr = [];

	switch(this.decision.state) {
		case "IDLE":
		case "OPEN":
			$("#dec_left_div #current_state_div",this.container).html("<p>Now <span id=state>"+this.decision.fromState+"</span><p>");
			$("#dec_left_div #candidate_state_div",this.container).html("<p>switch to <span id=state>"+this.decision.toState+"?</span></p>");

			// if decision is still open
			$("#dec_center_div #vote_div",this.container).show();

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

			break;

		case "CLOSED_ACCEPTED":
		case "CLOSED_DENIED":
		case "CLOSED_EXTERNALLY":
			$("#dec_center_div #vote_div",this.container).hide();
		
			$("#dec_left_div #current_state_div",this.container).html("<p>CLOSED</p>");
			$("#dec_left_div #candidate_state_div",this.container).html("");

			var verdictStr = [];
			if(this.decision.verdict == 1) verdictStr = "yes";
			else verdictStr = "no";

			$("#dec_right_div #verdict_status",this.container).html(verdictStr+", closed");

			break;
	}

	if(this.decision.state == "IDLE" || this.decision.state == "OPEN") {

		
		
	} else {
		
	}
}