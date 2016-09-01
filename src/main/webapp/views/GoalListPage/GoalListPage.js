$(document).ready(function() {

	GLOBAL = new Object();
	GLOBAL.goalListPage = new GoalListPage("#content_pane");
	CopDocReadyCommon(GLOBAL.goalListPage.init,GLOBAL.goalListPage);

});

function GoalListPage(container_id) {

	// Parent constructor
	Page.call(this,container_id);

	// Array with Cbtion objects
	this.goals = {};
	this.filter = {};
};

//Inheritance
GoalListPage.prototype = Page.prototype;

GoalListPage.prototype.init = function() {
	// Common after sessionUpdate code
	CopSessionReadyCommon();
	
	var filters = {
			projects : [],
			creators : [],
			states: ["PROPOSED","ACCEPTED"],
			keyw : [],
			page : 1,
			nPerPage : 15
	};
	
	this.filter = new FilterElement("#filter_container", 
			GLOBAL.serverComm.goalListGet, 
			this.GoalsReceivedCallback, 
			this, 
			["PROPOSED","ACCEPTED","DELETED"], 
			filters);

	this.filter.updateData();

}

GoalListPage.prototype.GoalsReceivedCallback = function(data) {
	this.goals = data.goalDtos;
	this.drawGoals();
	
	this.filter.resSet = data.resSet;
	this.filter.updateResSet();
}

GoalListPage.prototype.drawGoals = function() {

	this.filter.updateResSet();
	$("#list_of_elements", this.container).empty();

	for ( var ix in this.goals) {
		$("#list_of_elements", this.container).append(this.create_div_jQ("goalbox",ix));
		var goalBox = new GoalBox(this.get_div_jQ("goalbox",ix),this.goals[ix]);
	}
}

