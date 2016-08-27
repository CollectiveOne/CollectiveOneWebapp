function GoalBox(container_id,goalData) {
	// Parent constructor
	this.container = $(container_id);
	this.goal = goalData;
	if(goalData) {
		this.draw();
	}
};

//Inheritance
GoalBox.prototype.updateGoal = function() {
	GLOBAL.serverComm.goalGet(this.goal.goalTag,this.goalReceivedCallback,this);
}

GoalBox.prototype.goalReceivedCallback = function(goalDto) {
	this.user = goalDto;
	this.draw();
}

GoalBox.prototype.draw = function() {
	this.container.load("../elements/GoalBox/GoalBox.html",this.goalBoxLoaded.bind(this));
}

GoalBox.prototype.goalBoxLoaded = function() {
	$("#goaltag",this.container).append("<p>"+this.goal.goalTag+"</p>");
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
	
}