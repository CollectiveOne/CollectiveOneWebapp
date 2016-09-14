$(document).ready(function() {
	
	GLOBAL = new Object();
	GLOBAL.goalPage = new GoalPage("#content_pane");
	CopDocReadyCommon(GLOBAL.goalPage.init,GLOBAL.goalPage);
});

function GoalPage(container_id) {
	// Parent constructor
	Page.call(this,container_id);
	
	this.goal;
};

//Inheritance
GoalPage.prototype = Page.prototype;

GoalPage.prototype.init = function() {
	GLOBAL.goalPage.updateGoal(JSP_goalTag);
}

GoalPage.prototype.updateGoal = function(goalTag) {
	GLOBAL.serverComm.goalGet(goalTag,this.goalReceivedCallback,this);
}

GoalPage.prototype.goalReceivedCallback = function(goalDto) {
	this.goal = goalDto;
	var goalBox = new GoalBox($("#goal_container"),this.goal);
	goalBox.draw();
}

