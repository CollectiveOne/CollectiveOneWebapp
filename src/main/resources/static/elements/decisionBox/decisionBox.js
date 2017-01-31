function DecisionBox(container_id,decisionData, voter) {
	DecisionBase.call(this,container_id,decisionData, voter);
	this.argumentsExpanded = false;
	this.titleAsLink = true;
	this.expandArguments = false;
};

DecisionBox.prototype = DecisionBase.prototype;

DecisionBox.prototype.draw = function() {
	this.container.load("/elements/decisionBox/decisionBox.html",this.decisionBoxLoaded.bind(this));
}

DecisionBox.prototype.decisionBoxLoaded = function() {

	if(isUserLogged()) {
		this.drawVoteStatus();
		$("#right-bottom",this.container).show()
	}
	
	$("#accept_div",this.container).click(this.voteAccept.bind(this));
	$("#reject_div",this.container).click(this.voteReject.bind(this));
	
	this.updateVoteStatus();
	
	if(this.titleAsLink) {
		$("#dedicated_page",this.container).show();
		$("#dedicated_page",this.container).attr("href","/views/decisionPageR/"+ this.decision.id);	
	} else {
		$("#dedicated_page",this.container).hide();
	}
	
	$("#description",this.container).html(markdown.toHTML(this.decision.description));	
		
	switch(this.decision.type) {
		case "GENERAL":
			$("#from_to_state",this.container).hide();
			break;
		case "CBTION":
			$("#from_to_state",this.container).append("changes the state of contribution <a href=/views/cbtionPageR/"+
			this.decision.affectedCbtionId+">"+this.decision.cbtionTitle+"</a> from "+this.decision.fromState+" to "+this.decision.toState+"");	
			break;
		case "BID":
			$("#from_to_state",this.container).append("modifies bid to contribution <a href=/views/cbtionPageR/"+
			this.decision.affectedCbtionId+">"+this.decision.cbtionTitle+"</a> made by "+getUserPageLink(this.decision.affectedBidCreatorUsername)+"");	
			break;
		case "GOAL":
			var goalLinkStr = getGoalPageLink(this.decision.affectedGoalTag,this.decision.projectName);
			$("#from_to_state",this.container).append("modifies goal "+goalLinkStr);
			break;
	}
	
	var stateStr = '';
	
	switch(this.decision.state) {
		case "IDLE":
		case "OPEN":
			$("#right_div #state",this.container).removeClass("btn-info").addClass("btn-success");
			stateStr = this.decision.state;
			break;

		case "CLOSED_ACCEPTED":
		case "CLOSED_DENIED":
		case "CLOSED_EXTERNALLY":
			$("#right_div #state",this.container).removeClass("btn-info").addClass("label label-danger");
			$("#right-bottom",this.container).hide();
			stateStr = "CLOSED";
			break;
	}

	$("#project_div",this.container).append("In "+getProjectLink(this.decision.projectName)+" "+getGoalPageLink(this.decision.goalTag,this.decision.projectName));
	$("#state",this.container).append(stateStr);

	if(this.decision.state != "IDLE") {

		$("#metrics",this.container).show();
		$("#verdictTime_div",this.container).show();
		$("#verdict_div",this.container).show();
		
		var clarityRatio = floatToChar(this.decision.clarity*100,1);
		$("#clarity",this.container).append(clarityRatio);
		$("#clarity_bar",this.container).css('width', clarityRatio+'%').attr('aria-valuenow', clarityRatio);  
		
		var stabilityRatio = floatToChar(this.decision.stability*100,1);
		$("#stability",this.container).append(stabilityRatio);
		$("#stability_bar",this.container).css('width', stabilityRatio+'%').attr('aria-valuenow', stabilityRatio);  

		var yesRatio = floatToChar(this.decision.pest*100,1);
		$("#yesRatio",this.container).append(yesRatio+"%");
		$("#yesRatio_bar",this.container).css('width', yesRatio+'%').attr('aria-valuenow', yesRatio);  

		var votedRatio = floatToChar(this.decision.ppsCum/this.decision.ppsTot*100,1);
		$("#votedRatio",this.container).append(votedRatio+"%");
		$("#votedRatio_bar",this.container).css('width', votedRatio+'%').attr('aria-valuenow', votedRatio);  

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
				+floatToChar((1-this.decision.elapsedFactor)*this.decision.verdictHours,1)+" hr";
				break;

			case "CLOSED_ACCEPTED":
			case "CLOSED_DENIED":
			case "CLOSED_EXTERNALLY":
				verdictPre = "Final verdict: ";
				verdictTime = "taken "+getTimeStrSince(this.decision.actualVerdictDate)+" ago";
				break;
		}
		
		$("#verdict_div",this.container).append(""+verdictPre+"<label id=verdict class=label>"+verdictStr+"</label>");
		$("#verdictTime_div",this.container).append(""+verdictTime+"");

		if(this.decision.verdict == 1) {
			$("#right_div #verdict",this.container).addClass("label-success");
		} else {
			$("#right_div #verdict",this.container).addClass("label-danger");
		}
	}
	
	// Arguments portion expansion

	$("#arguments_expand_div",this.container).append("show arguments ("+this.decision.narguments+")");
	
	$("#arguments_expand_div",this.container).click(this.argumentsExpandClick.bind(this));
	$("#arguments_no_new_btn",this.container).click(this.argumentNoExpand.bind(this));
	$("#arguments_yes_new_btn",this.container).click(this.argumentYesExpand.bind(this));

	$("#arg_no_new_save",this.container).click(this.argumentNoSave.bind(this));
	$("#arg_yes_new_save",this.container).click(this.argumentYesSave.bind(this));

	if(this.expandArguments) {
		this.argumentsExpandClick();
	}
}

DecisionBox.prototype.argumentsExpandClick = function() {

	if(this.argumentsExpanded) {
		$("#discussion_div",this.container).hide();
		this.argumentsExpanded = false;
	} else {
		this.updateArguments();
		this.argumentsExpanded = true;
	}
	
}

DecisionBox.prototype.updateArguments = function() {
	GLOBAL.serverComm.argumentsGetOfDecision(this.decision.id,this.argumentsReceivedCallback,this);
}

DecisionBox.prototype.argumentsReceivedCallback = function(data) {

	this.decision.argumentsNo = data.argumentNoDtos;
	this.decision.argumentsYes = data.argumentYesDtos;

	this.drawArguments();
}

DecisionBox.prototype.drawArguments = function() {

	$("#discussion_div",this.container).show();

	$("#args_no_div", this.container).empty();
	for ( var ix in this.decision.argumentsNo ) {
		$("#args_no_div", this.container).append("<div id=argument_no"+ix+" class=argument_container></div>");
		var argumentBox = new ArgumentBox($("#argument_no"+ix,this.container),this.decision.argumentsNo[ix]);
		argumentBox.draw();
	}
	
	$("#args_yes_div", this.container).empty();
	for ( var ix in this.decision.argumentsYes ) {
		$("#args_yes_div", this.container).append("<div id=argument_yes"+ix+" class=argument_container></div>");
		var argumentBox = new ArgumentBox($("#argument_yes"+ix,this.container),this.decision.argumentsYes[ix]);
		argumentBox.draw();
	}	

	if(isUserLogged()) {
		$("#arguments_yes_new_btn", this.container).show();
		$("#arguments_no_new_btn", this.container).show();

		$("#arg_yes_new_description", this.container).markdown({
			autofocus:false,
			savable:false,
			hiddenButtons: ["cmdHeading", "cmdImage"],
			resize: "vertical"
		});

		$("#arg_no_new_description", this.container).markdown({
			autofocus:false,
			savable:false,
			hiddenButtons: ["cmdHeading", "cmdImage"],
			resize: "vertical"
		});
	}
}

DecisionBox.prototype.argumentNoExpand = function() {
	if(GLOBAL.sessionData.userLogged != null) {
		$("#arg_no_new_form",this.container).toggle();	
	} else {
		showOutput("please login to add arguments","DarkRed")
	}
	
}

DecisionBox.prototype.argumentYesExpand = function() {
	if(GLOBAL.sessionData.userLogged != null) {
		$("#arg_yes_new_form",this.container).toggle();
	} else {
		showOutput("please login to add arguments","DarkRed")
	}
}

DecisionBox.prototype.argumentNoSave = function() {
	argDto = {
		creatorUsername : GLOBAL.sessionData.userLogged.username,
		description : $("#arg_no_new_description", this.container).val(),
		decisionId : this.decision.id,
		tendency: "FORNO"
	}

	$("#arg_no_new_form",this.container).hide();	
	GLOBAL.serverComm.argumentNew(argDto,this.argumentNewCallback,this);	
}

DecisionBox.prototype.argumentYesSave = function() {
	argDto = {
		creatorUsername : GLOBAL.sessionData.userLogged.username,
		description : $("#arg_yes_new_description", this.container).val(),
		decisionId : this.decision.id,
		tendency: "FORYES"
	}
	$("#arg_yes_new_description", this.container).val("");
	$("#arg_yes_new_form",this.container).hide();
	GLOBAL.serverComm.argumentNew(argDto,this.argumentNewCallback,this);		
}

DecisionBox.prototype.argumentNewCallback = function() {
	this.updateArguments();
}
