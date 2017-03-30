$(document).ready(function() {
	GLOBAL.decisionListPage = new DecisionListPage("#content_pane");
	docReadyCommon(GLOBAL.decisionListPage.draw,GLOBAL.decisionListPage,true);
});

function DecisionListPage(container_id) {
	this.decisions = {};
	this.filter = {};
};

DecisionListPage.prototype.draw = function() {
	
	$("#decisions-nav-btn").addClass("active");
	
	var filters = {
			projectNames : GLOBAL.sessionData.activeProjectsController.getActiveProjectsNames(),
			stateNames: ["IDLE","OPEN"],
			creatorUsernames: [],
			keyw : '',
			sortBy: "CREATIONDATEDESC",
			page : 1,
			nperpage : 10
	};
	
	var customElements = { 
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
			"decisions",
			true, 
			true, 
			"/v/decisionNew");

	this.filter.updateData();
}

DecisionListPage.prototype.decisionsReceivedCallback = function(data) {
	this.decisions = data.decisionDtos;
	this.drawDecisions();
	this.filter.resSet = data.resSet;
	this.filter.updateResSet();
}

DecisionListPage.prototype.drawDecisions = function() {

	this.filter.updateResSet();
	$("#list_of_elements", this.container).empty();
	
	if(this.decisions.length > 0) {
		for ( var ix in this.decisions) {
			$("#list_of_elements", this.container).append("<div class=decisionbox_div id=decisionbox"+ix+"_div></div>");
			var decisionBox = new DecisionBox($("#decisionbox"+ix+"_div"),this.decisions[ix]);
			decisionBox.draw();
		}
	} else {
		$("#list_of_elements", this.container).append(
		"<div class='alert alert-info'>"+
  			"No results found"+
		"</div>");
	}
}

