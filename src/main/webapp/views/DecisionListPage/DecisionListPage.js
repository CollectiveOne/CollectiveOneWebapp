$(document).ready(function() {

	GLOBAL = new Object();
	GLOBAL.decisionListPage = new DecisionListPage("#content_pane");
	CopDocReadyCommon(GLOBAL.decisionListPage.draw,GLOBAL.decisionListPage);

});

function DecisionListPage(container_id) {

	// Parent constructor
	Page.call(this,container_id);

	// Array with Cbtion objects
	this.decisions = {};
	this.filter = {};
};

//Inheritance
DecisionListPage.prototype = Page.prototype;

DecisionListPage.prototype.draw = function() {
	
	var filters = {
			projectNames : GLOBAL.sessionData.activeProjectsController.getActiveProjectsNames(),
			stateNames: ["IDLE","OPEN"],
			creatorUsernames: [],
			keyw : '',
			sortBy: "CREATIONDATEDESC",
			page : 1,
			nperpage : 10
	};
	
	customElements = { 
			stateNames: ["IDLE","OPEN","CLOSED_ACCEPTED","CLOSED_DENIED","CLOSED_EXTERNALLY"],
			sortBy: [ { text:"New first", value:"CREATIONDATEDESC" },
			          { text:"Old first", value:"CREATIONDATEASC" },
			]
	};
	
	this.filter = new FilterElement("#filter_container", 
			GLOBAL.serverComm.decisionListGet, 
			this.decisionsReceivedCallback, 
			this, 
			customElements, 
			filters,
			"decisions");

	this.filter.updateData();
}

DecisionListPage.prototype.decisionsReceivedCallback = function(data) {
	this.decisions = data.decisionDtos;
	this.drawDecisions();
	
	this.filter.resSet = data.resSet;
	this.filter.updateResSet();

	UpdateBtns();
}

DecisionListPage.prototype.drawDecisions = function() {

	this.filter.updateResSet();
	$("#list_of_elements", this.container).empty();

	for ( var ix in this.decisions) {
		$("#list_of_elements", this.container).append("<div class=decisionbox_div id=decisionbox"+ix+"_div></div>");
		var decisionBox = new DecisionBox($("#decisionbox"+ix+"_div"),this.decisions[ix]);
		decisionBox.draw();
	}
}

