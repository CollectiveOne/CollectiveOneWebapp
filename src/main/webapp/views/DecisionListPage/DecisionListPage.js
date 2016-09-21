$(document).ready(function() {

	GLOBAL = new Object();
	GLOBAL.decisionListPage = new DecisionListPage("#content_pane");
	CopDocReadyCommon(GLOBAL.decisionListPage.init,GLOBAL.decisionListPage);

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

DecisionListPage.prototype.init = function() {
	// Common after sessionUpdate code
	CopSessionReadyCommon();
	
	var filters = {
			projectNames : [],
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
			filters);

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
		$("#list_of_elements", this.container).append(this.create_div_jQ("decisionbox",ix));
		var decisionBox = new DecisionBox(this.get_div_jQ("decisionbox",ix),this.decisions[ix]);
		decisionBox.draw();
	}
}

