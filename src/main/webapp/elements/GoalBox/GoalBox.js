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
	$("#goaltag",this.container).append("<a href=GoalPage.action?goalTag="+this.goal.goalTag+">"+this.goal.goalTag+"</a>");
	$("#description",this.container).append("<p>"+this.goal.description+"</p>");
	
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
		var decBox = new DecisionBoxSmall($("#bottom_div",this.container),applicable_decision, GLOBAL.sessionData.userLogged);
		decBox.draw();
	}
	
	if(this.goal.subGoalsTags.length > 0) {
		$("#show_subgoals_btn",this.container).show();
		$("#show_subgoals_btn",this.container).click(this.showSubGoals.bind(this));
	}
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