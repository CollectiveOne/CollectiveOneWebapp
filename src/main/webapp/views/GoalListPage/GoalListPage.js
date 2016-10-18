$(document).ready(function() {

	GLOBAL = new Object();
	GLOBAL.goalListPage = new GoalListPage("#content_pane");
	CopDocReadyCommon(GLOBAL.goalListPage.draw,GLOBAL.goalListPage);

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

GoalListPage.prototype.draw = function() {

	var filters = {
			projectNames : GLOBAL.sessionData.activeProjectsController.getActiveProjectsNames(),
			stateNames: ["PROPOSED","ACCEPTED"],
			creatorUsernames: [],
			keyw : '',
			sortBy: "CREATIONDATEDESC",
			page : 1,
			nperpage : 15
	};
	
	customElements = { 
			stateNames: ["PROPOSED","ACCEPTED","NOT_ACCEPTED","DELETED"],
			sortBy: [ { text:"New first", value:"CREATIONDATEDESC" },
			          { text:"Old first", value:"CREATIONDATEASC" },
			]
	};
	
	this.filter = new FilterElement("#filter_container", 
			GLOBAL.serverComm.goalListGet, 
			this.GoalsReceivedCallback, 
			this, 
			customElements, 
			filters);

	this.filter.updateData();

}

GoalListPage.prototype.GoalsReceivedCallback = function(data) {
	this.goals = data.goalDtos;
	this.drawGoals();
	
	this.filter.resSet = data.resSet;
	this.filter.updateResSet();

	UpdateBtns();
}

GoalListPage.prototype.drawGoals = function() {

	this.filter.updateResSet();
	$("#list_of_elements", this.container).empty();

	for ( var ix in this.goals) {
		$("#list_of_elements", this.container).append("<div class=goalBox_div id=goalbox"+ix+"_div></div>");
		var goalBox = new GoalBox($("#goalbox"+ix+"_div"),this.goals[ix]);
	}
}

