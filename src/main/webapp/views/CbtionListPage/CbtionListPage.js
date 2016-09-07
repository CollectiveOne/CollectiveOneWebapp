$(document).ready(function() {

	GLOBAL = new Object();
	GLOBAL.cbtionListPage = new CbtionListPage("#content_pane");
	CopDocReadyCommon(GLOBAL.cbtionListPage.init,GLOBAL.cbtionListPage);

});

function CbtionListPage(container_id) {

	// Parent constructor
	Page.call(this,container_id);

	// Array with Cbtion objects
	this.cbtions = {};
	this.filter = {};
};

//Inheritance
CbtionListPage.prototype = Page.prototype;

CbtionListPage.prototype.init = function() {
	// Common after sessionUpdate code
	CopSessionReadyCommon();
	
	var filters = {
			projectNames : [],
			stateNames: ["PROPOSED","OPEN","ASSIGNED"],
			creatorUsernames: [],
			keyw : '',
			page : 1,
			nperpage : 15
	};
	
	this.filter = new FilterElement("#filter_container", 
			GLOBAL.serverComm.cbtionListGet, 
			this.CbtionsReceivedCallback, 
			this, 
			["PROPOSED","OPEN","ASSIGNED","ACCEPTED","NOTOPENED"], 
			filters);

	this.filter.updateData();
}

CbtionListPage.prototype.CbtionsReceivedCallback = function(data) {
	this.cbtions = data.cbtionDtos;
	this.drawCbtions();

	this.filter.resSet = data.resSet;
	this.filter.updateResSet();
}

CbtionListPage.prototype.drawCbtions = function() {

	$("#list_of_elements", this.container).empty();

	for ( var ix in this.cbtions) {
		$("#list_of_elements", this.container).append(this.create_div_jQ("cbtionbox",ix));
		var cbtionBox = new CbtionBox(this.get_div_jQ("cbtionbox",ix),this.cbtions[ix]);
		cbtionBox.draw();
	}
}

