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
	this.updateGoal(JSP_goalTag);
}

GoalPage.prototype.updateGoal = function(goalTag) {
	GLOBAL.serverComm.goalGet(goalTag,this.goalReceivedCallback,this);
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
			maxheight: "600px"
		});
}
