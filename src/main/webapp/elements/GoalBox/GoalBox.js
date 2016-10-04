function GoalBox(container_id,goalData) {
	// Parent constructor
	this.container = $(container_id);
	this.goal = goalData;
	this.subgoals_expanded = false;
	if(goalData) {
		this.draw();
	}
};

//Inheritance
GoalBox.prototype.getGoal = function(goalTag) {
	GLOBAL.serverComm.goalGet(goalTag,this.goalReceivedCallback,this);
}

GoalBox.prototype.updateGoal = function() {
	GLOBAL.serverComm.goalGet(this.goal.goalTag,this.goalReceivedCallback,this);
}

GoalBox.prototype.goalReceivedCallback = function(goalDto) {
	this.goal = goalDto;
	this.draw();
}

GoalBox.prototype.draw = function() {
	this.container.load("../elements/GoalBox/GoalBox.html",this.goalBoxLoaded.bind(this));
}

GoalBox.prototype.goalBoxLoaded = function() {
	
	if(this.goal.parentGoalsTags) {
		var nparents = this.goal.parentGoalsTags.length;
		for(var ix=0; ix < nparents; ix++) {
			// cycle from last to first as the first parent is the immediate parent
			var parentTag = this.goal.parentGoalsTags[nparents - ix - 1];
			$("#goaltag",this.container).append("<a href=GoalPage.action?goalTag="+parentTag+">&#x0371 "+parentTag+"</a>");
		}
	}

	var tagSuffix = "";
	if(this.goal.state == "PROPOSED") {
		tagSuffix = " (proposed)";
	}
	
	$("#goaltag",this.container).append("<a href=GoalPage.action?goalTag="+this.goal.goalTag+">"+"&#x0371 "+this.goal.goalTag+"</a>"+tagSuffix);	
	$("#description",this.container).append("<p>"+this.goal.description+"</p>");

	if(GLOBAL.sessionData.userLogged) {

		$("#show_new_subgoal_btn",this.container).show();
		$("#show_new_subgoal_btn",this.container).click(this.showNewSubgoalBtnClicked.bind(this));
		$("#new_subgoal_save_btn",this.container).click(this.newSubgoalSaveBtnClicked.bind(this));

		$("#show_control_btn",this.container).show();
		$("#show_control_btn",this.container).click(this.showControlBtnClicked.bind(this));

		var applicable_decision = [];

		switch(this.goal.state) {
			case "PROPOSED":
				applicable_decision = this.goal.createDec;
				break;

			case "ACCEPTED":
				applicable_decision = this.goal.deleteDec;
				break;

			case "DELETED":
				applicable_decision = this.goal.createDec;
				break;

			default:
				console.log("Unexected goal state " + this.goal.state);

		}

		if(applicable_decision.state == "IDLE" || applicable_decision.state == "OPEN") {
			var decBox = new DecisionBoxSmall($("#state_decision_div",this.container),applicable_decision, GLOBAL.sessionData.userLogged);
			decBox.draw();
		}	
	}
	
	if(this.goal.subGoalsTags.length > 0) {
		$("#show_subgoals_btn",this.container).show();
		$("#show_subgoals_btn",this.container).click(this.showSubGoals.bind(this));
	}

	if(GLOBAL.sessionData.userLogged) {
		if(this.goal.parentState == "PROPOSED") {
			$("#propose_parent_decision_container",this.container).show();
			var propDecBox = new DecisionBoxSmall($("#propose_parent_decision_container",this.container),this.goal.proposeParent, GLOBAL.sessionData.userLogged);
			propDecBox.draw();
		} else {
			$("#propose_parent_form_container",this.container).show();
			$("#propose_parent_btn",this.container).click(this.proposeBtnClicked.bind(this));
			$("#parent_goal_input",this.container).autocomplete({serviceUrl: '../json/GoalGetSuggestions.action'});
			$("#propose_parent_save_btn",this.container).click(this.proposeSaveBtnClicked.bind(this));	
		}
	}
	
}

GoalBox.prototype.showNewSubgoalBtnClicked = function() {
	$("#new_subgoal_form",this.container).toggle();
}

GoalBox.prototype.showControlBtnClicked = function() {
	$("#goal_control_div",this.container).toggle();
}

GoalBox.prototype.proposeBtnClicked = function() {
	$("#propose_parent_form",this.container).toggle();
}

GoalBox.prototype.proposeSaveBtnClicked = function() {
	GLOBAL.serverComm.goalProposeParent(this.goal.id, $("#parent_goal_input",this.container).val(), this.goalProposedSendCallback, this);
}

GoalBox.prototype.goalProposedSendCallback = function() {
	$("#propose_parent_form",this.container).hide();
	this.updateGoal();
}

GoalBox.prototype.newSubgoalSaveBtnClicked = function() {
	var newGoalData = { 
		creatorUsername: GLOBAL.sessionData.userLogged.username,
		projectName: this.goal.projectName,
		parentGoalTag: this.goal.goalTag,
		goalTag: $("#new_subgoal_tag_input",this.container).val(),
		description: $("#new_subgoal_description_input",this.container).val()
	}

	GLOBAL.serverComm.goalNew(newGoalData,this.newSubgoalCreatedCallback,this);
}

GoalBox.prototype.newSubgoalCreatedCallback = function() {
	this.updateGoal();
}

GoalBox.prototype.showSubGoals = function() {
	
	if(!this.subgoals_expanded) {
		this.subgoals_expanded = true;
		$("#subgoals_div",this.container).show();

		for(var ix in this.goal.subGoalsTags) {
			$("#subgoals_div",this.container).append("<div id=subgoal_container_"+ix+" class=subgoal_container></div>");
			var subGoalBox = new GoalBox($("#subgoal_container_"+ix,this.container));
			subGoalBox.getGoal(this.goal.subGoalsTags[ix]);
		}

	} else {
		this.subgoals_expanded = false;
		$("#subgoals_div",this.container).empty();
		$("#subgoals_div",this.container).hide();
		
	}
}