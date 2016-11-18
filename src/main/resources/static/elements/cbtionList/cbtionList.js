function CbtionList(container_id,conf) {
	// Parent constructor
	this.container = $(container_id);
	this.conf = conf;
};

//Inheritance
CbtionList.prototype.init = function() {
	this.draw();
}

CbtionList.prototype.draw = function(data) {
	this.container.load("/elements/cbtionList/cbtionList.html",this.boxLoaded.bind(this));
}

CbtionList.prototype.boxLoaded = function(data) {

	var activeProjects = [];
	if(GLOBAL.sessionData.activeProjectsController) {
		activeProjects = GLOBAL.sessionData.activeProjectsController.getActiveProjectsNames();
	}
	
	var filters = {
			projectNames : activeProjects,
			stateNames: ["PROPOSED","OPEN","ASSIGNED"],
			contributorUsername: [],
			creatorUsernames: [],
			contributorUsername: "",
			goalTag: "",
			goalSubgoalsFlag: true,
			keyw : '',
			sortBy: "CREATIONDATEDESC",
			page : 1,
			nperpage : 15
	};

	var customElements = { 
			stateNames: ["PROPOSED","OPEN","ASSIGNED","ACCEPTED","NOTOPENED","DELETED"],
			sortBy: [ { text:"New first", value:"CREATIONDATEDESC" },
			          { text:"Old first", value:"CREATIONDATEASC" },
			          { text:"More relevant", value:"RELEVANCEDESC" },
			          { text:"Less relevant", value:"RELEVANCEASC" },
			]
	};

	if(this.conf.projectNames) {
		filters.projectNames = this.conf.projectNames;
	}
	
	if(this.conf.contributorUsername) {
		filters.contributorUsername = this.conf.contributorUsername;
	}
	
	if(this.conf.goalTag) {
		filters.goalTag = this.conf.goalTag;
	}

	if(this.conf.maxheight) {
		$("#list_of_elements",this.container).css("max-height",this.conf.maxheight);
	}

	if(this.conf.stateNames) {
		filters.stateNames = this.conf.stateNames;
	}
	
	this.filter = new FilterElement($("#filter_container",this.container), 
			GLOBAL.serverComm.cbtionListGet, 
			this.CbtionsReceivedCallback, 
			this, 
			customElements, 
			filters,
			"cbtions");

	this.updateData();
}

CbtionList.prototype.updateData = function(data) {
	this.filter.updateData();
}

CbtionList.prototype.CbtionsReceivedCallback = function(data) {
	this.cbtions = data.cbtionDtos;
	this.drawCbtions();

	this.filter.resSet = data.resSet;
	this.filter.updateResSet();
}

CbtionList.prototype.drawCbtions = function() {

	$("#list_of_elements", this.container).empty();

	if(this.cbtions.length > 0) {
		for ( var ix in this.cbtions) {
			$("#list_of_elements", this.container).append("<div id=cbtionbox"+ix+"></div>");
			var cbtionBox = new CbtionBox("#cbtionbox"+ix,this.cbtions[ix]);
			cbtionBox.draw();
		}
	} else {
		$("#list_of_elements", this.container).append(
		"<div class='alert alert-info'>"+
  			"No results found"+
		"</div>");
	}
	
}
