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
	$("#state",this.container).append("<p>"+this.goal.state+"</p>");
}