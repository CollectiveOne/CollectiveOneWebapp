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
			projects : [],
			creators : [],
			states: ["IDLE","OPEN"],
			keyw : [],
			page : 1,
			nPerPage : 10
	};
	
	this.filter = new FilterElement("#filter_container", 
			GLOBAL.serverComm.decisionListGet, 
			this.decisionsReceivedCallback, 
			this, 
			["IDLE","OPEN","CLOSED_ACCEPTED","CLOSED_DENIED","CLOSED_EXTERNALLY"], 
			filters);

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

	for ( var ix in this.decisions) {
		$("#list_of_elements", this.container).append(this.create_div_jQ("decisionbox",ix));
		var decisionBox = new DecisionBox(this.get_div_jQ("decisionbox",ix),this.decisions[ix]);
		decisionBox.draw();
	}
}

