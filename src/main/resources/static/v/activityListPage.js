$(document).ready(function() {
	GLOBAL.activityListPage = new ActivityListPage("#content_pane");
	docReadyCommon(GLOBAL.activityListPage.init,GLOBAL.activityListPage,true);

});

function ActivityListPage(container_id) {
	this.activityItems = {};
	this.filter = {};
};

ActivityListPage.prototype.init = function() {
	
	$("#recact-nav-btn").addClass("active");
	
	var filters = {
			projectNames : GLOBAL.sessionData.activeProjectsController.getActiveProjectsNames(),
			stateNames: [],
			creatorUsernames: [],
			keyw : '',
			sortBy: "CREATIONDATEDESC",
			page : 1,
			nperpage : 30
	};

	var customElements = { 
			stateNames: [],
			sortBy: [ { text:"New first", value:"CREATIONDATEDESC" },
			          { text:"Old first", value:"CREATIONDATEASC" } ]
	};

	this.filter = new FilterElement("#filter_container", 
			GLOBAL.serverComm.activityListGet, 
			this.activityItemsReceivedCallback, 
			this, 
			customElements, 
			filters,
			"activities",
			true, 
			false, 
			"");

	this.filter.updateData();
}

ActivityListPage.prototype.activityItemsReceivedCallback = function(data) {
	this.activityItems = data.activityDtos;
	this.drawActivityItems();

	this.filter.resSet = data.resSet;
	this.filter.updateResSet();
}

ActivityListPage.prototype.drawActivityItems = function() {

	$("#list_of_elements", this.container).empty();

	for ( var ix in this.activityItems) {
		$("#list_of_elements", this.container).append("<div class=itembox_div id=itembox"+ix+"_div></div>");
		var activityBox = new ActivityBox($("#itembox"+ix+"_div"),this.activityItems[ix]);
		activityBox.draw();
	}
}

