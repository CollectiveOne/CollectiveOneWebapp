$(document).ready(function() {
	
	GLOBAL = new Object();
	GLOBAL.goalPage = new GoalPage("#content_pane");
	CopDocReadyCommon(GLOBAL.goalPage.init,GLOBAL.goalPage);
});

function GoalPage(container_id) {
	// Parent constructor
	Page.call(this,container_id);
};

//Inheritance
GoalPage.prototype = Page.prototype;

GoalPage.prototype.init = function() {
	this.updateGoal(JSP_goalTag, JSP_projectName);
}

GoalPage.prototype.updateGoal = function(goalTag, projectName) {
	GLOBAL.serverComm.goalGet(goalTag,projectName,this.goalReceivedCallback,this);
}

GoalPage.prototype.goalReceivedCallback = function(goalDto) {
	this.goal = goalDto;
	var goalBox = new GoalBox($("#goal_container"),this.goal);
	goalBox.draw();
	
	this.updateCbtions();
}

GoalPage.prototype.updateCbtions = function(goalDto) {
	this.cbtionList = new CbtionList($("#cbtions_container",this.container),{ 
			goalTag: this.goal.goalTag,
			projectNames: [this.goal.projectName],
			maxheight: "600px"
		});
}