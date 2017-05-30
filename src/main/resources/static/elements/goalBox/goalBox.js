function GoalBox(container_id,goalData) {
	// Parent constructor
	this.container = $(container_id);
	this.goal = goalData;
	this.subgoals_expanded = false;
	this.showSubgoalsAfterDraw = false;
	this.prependPath = true;
	if(goalData) {
		this.draw();
	}
};

//Inheritance
GoalBox.prototype.getGoal = function(goalTag, projectName) {
	GLOBAL.serverComm.goalGetFromTag(goalTag,projectName,this.goalReceivedCallback,this);
}

GoalBox.prototype.updateGoal = function() {
	GLOBAL.serverComm.goalGet(this.goal.id,this.goalReceivedCallback,this);
}

GoalBox.prototype.goalReceivedCallback = function(goalDto) {
	this.goal = goalDto;
	this.draw();
}

GoalBox.prototype.draw = function() {
	this.container.load("/elements/goalBox/goalBox.html",this.goalBoxLoaded.bind(this));
}

GoalBox.prototype.goalBoxLoaded = function() {
	
	/* change container id using this goal ID*/
	var container_id = "goal_div" + this.goal.id;
	$("#goal_id").attr('id', container_id);
	
	this.id_tag = '#' + container_id + ' '
	
	$(this.id_tag + '#new_subgoal_description_input').markdown({
		autofocus:false,
		savable:false,
		hiddenButtons: ["cmdHeading", "cmdImage"],
		resize: "vertical"
	});
	
	$(this.id_tag + '#edit_input',this.container).markdown({
		autofocus:false,
		savable:false,
		hiddenButtons: ["cmdHeading", "cmdImage"],
		resize: "vertical"
	});
	
	/* TITLE */
	if(this.prependPath) {
		$(this.id_tag + "#goaltag",this.container).append(getProjectLink(this.goal.projectName));

		if(this.goal.parentGoalsTags) {
			var nparents = this.goal.parentGoalsTags.length;
			for(var ix=0; ix < nparents; ix++) {
				// cycle from last to first as the first parent is the immediate parent
				var parentTag = this.goal.parentGoalsTags[nparents - ix - 1];
				$(this.id_tag + "#goaltag",this.container).append(getGoalPageLink(parentTag,this.goal.projectName));
			}
		}	
	}	

	var labelsAppend = "";
	if(this.goal.state == "PROPOSED") {
		labelsAppend = " <label class='label label-info'>proposed</label>";
	}

	/* ATTACHMENT/DETACHMENT LOGIC */
	switch(this.goal.attachedState) {
		case "ATTACHED":
			labelsAppend = labelsAppend + " <label class='label label-success'>attached</label>";
			
			/* detach only if has a parent */
			if(this.goal.parentGoalTag) {
				$(this.id_tag + "#detach_form_container",this.container).show();

				$(this.id_tag + "#detach_btn",this.container).click(this.detachBtnClicked.bind(this));
				$(this.id_tag + "#detach_save_btn",this.container).click(this.detachBtnSaveClicked.bind(this));	
			}
			
			break;

		case "DETACHED":
			labelsAppend = labelsAppend + " <label class='label label-warning'>detached</label>";
			labelsAppend = labelsAppend + " <label class='label label-warning'>available budget: "+this.goal.currentBudget+"</label>";
			
			$(this.id_tag + "#detach_form_container",this.container).show();
			$(this.id_tag + "#detach_btn",this.container).html("reattach");
			$(this.id_tag + "#detach_input",this.container).hide();
			$(this.id_tag + "#detach_btn",this.container).click(this.detachBtnClicked.bind(this));
			$(this.id_tag + "#detach_save_btn",this.container).click(this.detachBtnSaveClicked.bind(this));

			switch(this.goal.increaseBudgetState) {
				case "NOT_PROPOSED":
					$(this.id_tag + "#increase_budget_form_container",this.container).show();
					$(this.id_tag + "#increase_budget_btn",this.container).click(this.increaseBtnClicked.bind(this));
					$(this.id_tag + "#increase_budget_save_btn",this.container).click(this.increaseBtnSaveClicked.bind(this));
					break;

				case "PROPOSED":
					$(this.id_tag + "#increase_budget_decision_container",this.container).show();
					var decBox = new DecisionBoxSmall($(this.id_tag + "#increase_budget_decision_container",this.container),this.goal.increaseBudgetDec);
					decBox.updateVoteAndDraw();
					break;
			}
			
			break;

		case "PROPOSED_DETACH":
			labelsAppend = labelsAppend + " <label class='label label-success'>detach proposed</label>";
			$(this.id_tag + "#increase_budget_decision_container",this.container).show();
			var decBox = new DecisionBoxSmall($(this.id_tag + "#increase_budget_decision_container",this.container),this.goal.increaseBudgetDec);
			decBox.updateVoteAndDraw();
			break;

		case "PROPOSED_REATTACH":
			labelsAppend = labelsAppend + " <label class='label label-success'>reattach proposed</label>";
			$(this.id_tag + "#reattach_decision_container",this.container).show();
			var decBox = new DecisionBoxSmall($(this.id_tag + "#reattach_decision_container",this.container),this.goal.reattachDec);
			decBox.updateVoteAndDraw();
			break;
	}
	
	
	$(this.id_tag + "#goaltag",this.container).append(getGoalPageLink(this.goal.goalTag,this.goal.projectName)+""+labelsAppend);
	
	if(this.goal.description) {
		$(this.id_tag + "#description",this.container).append(markdown.toHTML(this.goal.description));	
	}
	

	/* NEW SUBGOALS AND CONTROL */
	if(isUserLogged()) {

		$(this.id_tag + "#new_subgoal_btn",this.container).show();
		$(this.id_tag + "#new_subgoal_btn",this.container).click(this.newSubgoalBtnClicked.bind(this));
		$(this.id_tag + "#new_subgoal_save_btn",this.container).click(this.newSubgoalSaveBtnClicked.bind(this));

		$(this.id_tag + "#show_control_btn",this.container).show();
		$(this.id_tag + "#show_control_btn",this.container).click(this.showControlBtnClicked.bind(this));

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
			var decBox = new DecisionBoxSmall($(this.id_tag + "#state_decision_div",this.container),applicable_decision);
			decBox.updateVoteAndDraw();
		}	
	}
	
	/* SUBGOALS */
	if(this.goal.subGoalsTags.length > 0) {
		$(this.id_tag + "#show_subgoals_btn",this.container).show();
		$(this.id_tag + "#n_subgoals_label",this.container).show();
		$(this.id_tag + "#n_subgoals_label",this.container).html(this.goal.subGoalsTags.length+" subgoals");
		
		$(this.id_tag + "#show_subgoals_btn",this.container).click(this.showSubGoals.bind(this));
		$(this.id_tag + "#n_subgoals_label",this.container).click(this.showSubGoals.bind(this));
	}
	
	/* EDITION PROPOSALS */
	if(this.goal.editionProps.length > 0) {
		$(this.id_tag + "#edition_proposed_label", this.container).html("show " + this.goal.editionProps.length + " editions proposed");
		$(this.id_tag + "#edition_proposed_label", this.container).show();
		$(this.id_tag + "#edition_proposed_label", this.container).click(this.showEditionProposals.bind(this));
		for(var ix in this.goal.editionProps) {
			$(this.id_tag + "#edition_proposals", this.container).append("<div class=edition_proposal id=edition_proposal"+ix+"_div></div>");
			var editProp = new EditionProposalBox($(this.id_tag + "#edition_proposal"+ix+"_div",this.container),this.goal.editionProps[ix]);
		}
	}
	
	/* CREATE EDITION PROPOSAL */
	if(isUserLogged()) {
		$(this.id_tag + "#edit_form_container",this.container).show();
		$(this.id_tag + "#edit_input",this.container).val(this.goal.description);	
		$(this.id_tag + "#edit_btn",this.container).click(this.editBtnClicked.bind(this));
		$(this.id_tag + "#edit_save_btn",this.container).click(this.editBtnSaveClicked.bind(this));
	}
	
	/* EDITION HISTORY */
	if(this.goal.editionsHistory.length > 0) {
		$(this.id_tag + "#edition_history_btn_container",this.container).show();
		$(this.id_tag + "#edition_history_btn_container",this.container).click(this.editionHistoryBtnClicked.bind(this));
		
		for(var ix in this.goal.editionsHistory) {
			var thisEdition = this.goal.editionsHistory[ix];
			$(this.id_tag + "#edition_history_container", this.container).append("<hr>");
			$(this.id_tag + "#edition_history_container", this.container).append(
				"<div class=edition_history>"+
					getUserPageLink(thisEdition.proposerUsername)+
					" proposed '"+LimitStrSize(thisEdition.edition,80)+"' "+
					getTimeStrSince(thisEdition.creationDate)+" ago"+
					" and it was "+thisEdition.state+". <a href='/v/decision/"+thisEdition.acceptDec.id+"'>(see decision)</a></div>");
		}
	}


	/* PARENT PROPOSALS */
	if(isUserLogged()) {
		$(this.id_tag + "#order_form_container",this.container).show();
		$(this.id_tag + "#order_input",this.container).val(this.goal.subGoalPosition);	
		$(this.id_tag + "#order_btn",this.container).click(this.orderBtnClicked.bind(this));
		$(this.id_tag + "#order_save_btn",this.container).click(this.orderBtnSaveClicked.bind(this));
		
		if(this.goal.parentState == "PROPOSED") {
			$(this.id_tag + "#propose_parent_decision_container",this.container).show();
			var propDecBox = new DecisionBoxSmall($(this.id_tag + "#propose_parent_decision_container",this.container),this.goal.proposeParent, getLoggedUsername());
			propDecBox.updateVoteAndDraw();
			
		} else {
			$(this.id_tag + "#propose_parent_form_container",this.container).show();
			$(this.id_tag + "#propose_parent_btn",this.container).click(this.proposeBtnClicked.bind(this));
			
			$(this.id_tag + "#parent_goal_input",this.container).autocomplete({
						serviceUrl: '/1/goals/suggestions',
						minChars: 0,
						maxHeight: 200,
						params: { projectName:  this.goal.projectName } 
			});
			
			$(this.id_tag + "#propose_parent_save_btn",this.container).click(this.proposeSaveBtnClicked.bind(this));	
		}
	}

	if(this.showSubgoalsAfterDraw) {
		this.showSubGoals();
	}
}

GoalBox.prototype.newSubgoalBtnClicked = function() {
	$(this.id_tag + "#new_subgoal_form",this.container).toggle();
}

GoalBox.prototype.showControlBtnClicked = function() {
	$(this.id_tag + "#goal_control_div",this.container).toggle();
}

GoalBox.prototype.showEditionProposals = function() {
	$(this.id_tag + "#edition_proposals",this.container).toggle();
}

GoalBox.prototype.editBtnClicked = function() {
	$(this.id_tag + "#edit_form_container",this.container).addClass("control_form_container_large");
	$(this.id_tag + "#edit_form",this.container).toggle();
}

GoalBox.prototype.detachBtnClicked = function() {
	$(this.id_tag + "#detach_form",this.container).toggle();
}

GoalBox.prototype.increaseBtnClicked = function() {
	$(this.id_tag + "#increase_budget_form",this.container).toggle();
}

GoalBox.prototype.proposeBtnClicked = function() {
	$(this.id_tag + "#propose_parent_form",this.container).toggle();
}

GoalBox.prototype.editionHistoryBtnClicked = function() {
	$(this.id_tag + "#edition_history_container",this.container).toggle();
}

GoalBox.prototype.orderBtnClicked = function() {
	$(this.id_tag + "#order_form",this.container).toggle();
}

GoalBox.prototype.increaseBtnClicked = function() {
	$(this.id_tag + "#increase_budget_form",this.container).toggle();
}



GoalBox.prototype.editBtnSaveClicked = function() {
	GLOBAL.serverComm.goalProposeEdit(this.goal.id, $(this.id_tag + "#edit_input",this.container).val(), this.detachSaveCallback, this);
}

GoalBox.prototype.orderBtnSaveClicked = function() {
	GLOBAL.serverComm.goalProposeOrder(this.goal.id, $(this.id_tag + "#order_input",this.container).val(), this.orderSaveCallback, this);
}

GoalBox.prototype.detachBtnSaveClicked = function() {
	switch(this.goal.attachedState) {
		case "ATTACHED":
			GLOBAL.serverComm.goalProposeDetach(this.goal.id, $(this.id_tag + "#detach_input",this.container).val(), this.detachSaveCallback, this);
			break;

		case "DETACHED":
			GLOBAL.serverComm.goalProposeReattach(this.goal.id, this.detachSaveCallback, this);
			break;
	}
}

GoalBox.prototype.orderSaveCallback = function() {
	$(this.id_tag + "#detach_form",this.container).hide();
	this.updateGoal();
}

GoalBox.prototype.detachSaveCallback = function() {
	$(this.id_tag + "#order_form",this.container).hide();
	this.updateGoal();
}

GoalBox.prototype.increaseBtnSaveClicked = function() {
	GLOBAL.serverComm.goalProposeIncreaseBudget(this.goal.id, $(this.id_tag + "#increase_budget_input",this.container).val(), this.increaseSaveCallback, this);
}

GoalBox.prototype.increaseSaveCallback = function() {
	$(this.id_tag + "#increase_budget_form",this.container).hide();
	this.updateGoal();
}


GoalBox.prototype.proposeSaveBtnClicked = function() {
	GLOBAL.serverComm.goalProposeParent(this.goal.id, $(this.id_tag + "#parent_goal_input",this.container).val(), this.goalProposedSendCallback, this);
}

GoalBox.prototype.goalProposedSendCallback = function() {
	$(this.id_tag + "#propose_parent_form",this.container).hide();
	this.updateGoal();
}



GoalBox.prototype.newSubgoalSaveBtnClicked = function() {
	var goalTag = $(this.id_tag + "#new_subgoal_tag_input",this.container).val().trim();
	var description = $(this.id_tag + "#new_subgoal_description_input",this.container).val();

	GLOBAL.serverComm.goalExist(goalTag,this.goal.projectName,this.checkGoalExistCallback,this)
}

GoalBox.prototype.checkGoalExistCallback = function(exist) {

	var goalTag = $(this.id_tag + "#new_subgoal_tag_input",this.container).val().trim();
	var description = $(this.id_tag + "#new_subgoal_description_input",this.container).val();

	/* validation */
	var errors = false;
	var errorContainer = $(this.id_tag + "#new_subgoal_error_div",this.container);

	errorContainer.empty();
	
	if(exist) {
		errorContainer.show();
		if(errors) errorContainer.append("<br \>")
		errorContainer.append("goal +"+goalTag+" already exist in this project");
		errors = true;
	}
	
	if(!goalTag) {
		errorContainer.show();
		if(errors) errorContainer.append("<br \>")
		errorContainer.append("subgoal tag cannot be empty");
		errors = true;
	}

	if(!/^[a-zA-Z0-9-]+$/.test(goalTag)) {
		errorContainer.show();
		if(errors) errorContainer.append("<br \>")
		errorContainer.append("subgoal tag can contain only characters, numbers or bars '-'");
		errors = true;
	}
	
	if(!description) {
		 errorContainer.show();
		 if(errors) errorContainer.append("<br \>")
		 errorContainer.append("description cannot be empty");
		 errors = true;
	}
	
	if(!errors) {
		var newGoalData = { 
			projectName: this.goal.projectName,
			parentGoalTag: this.goal.goalTag,
			goalTag: goalTag,
			description: description
		}

		GLOBAL.serverComm.goalNew(newGoalData,this.newSubgoalCreatedCallback,this);
	}
}

GoalBox.prototype.newSubgoalCreatedCallback = function() {
	this.showSubgoalsAfterDraw = true;
	this.updateGoal();
}

GoalBox.prototype.showSubGoals = function() {
	
	if(!this.subgoals_expanded) {
		this.subgoals_expanded = true;
		$(this.id_tag + "#subgoals_div",this.container).show();

		for(var ix in this.goal.subGoalsTags) {
			$(this.id_tag + "#subgoals_div",this.container).append("<div id=subgoal_container_"+ix+" class=subgoal_container></div>");
			var subGoalBox = new GoalBox($(this.id_tag + "#subgoal_container_"+ix,this.container));
			subGoalBox.prependPath = false;
			subGoalBox.getGoal(this.goal.subGoalsTags[ix], this.goal.projectName);
		}

	} else {
		this.subgoals_expanded = false;
		$(this.id_tag + "#subgoals_div",this.container).empty();
		$(this.id_tag + "#subgoals_div",this.container).hide();
		
	}
}