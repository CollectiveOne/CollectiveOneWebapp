$(document).ready(function() {
	GLOBAL.ProjectListPage = new ProjectListPage("#content_pane");
	docReadyCommon(GLOBAL.ProjectListPage.draw,GLOBAL.ProjectListPage,true);
});

function ProjectListPage(container_id) {
	this.projects = {};
	this.filter = {};
};

ProjectListPage.prototype.draw = function() {
	
	$("#projects-nav-btn").addClass("active");
	
	var filters = {
			projectNames : [],
			stateNames: [],
			creatorUsernames: [],
			keyw : '',
			sortBy: "CREATIONDATEASC",
			page : 1,
			nperpage : 10
	};
	
	var customElements = { 
			stateNames: [],
			sortBy: [ { text:"Old first", value:"CREATIONDATEASC" },
					  { text:"New first", value:"CREATIONDATEDESC" },
			]
	};
	
	this.filter = new FilterElement("#filter_container", 
			GLOBAL.serverComm.projectListGet, 
			this.projectsReceivedCallback, 
			this, 
			customElements, 
			filters,
			"projects",
			true, 
			true, 
			"/v/projectNew");

	this.filter.updateData();
}

ProjectListPage.prototype.projectsReceivedCallback = function(data) {
	this.projects = data.projectDtos;
	this.drawProjects();
	this.filter.resSet = data.resSet;
	this.filter.updateResSet();
}

ProjectListPage.prototype.drawProjects = function() {

	this.filter.updateResSet();
	$("#list_of_elements", this.container).empty();
	
	if(this.projects.length > 0) {
		for ( var ix in this.projects) {
			$("#list_of_elements", this.container).append("<div class=projectbox_div id=projectbox"+ix+"_div></div>");
			var projectBox = new ProjectBox($("#projectbox"+ix+"_div"),this.projects[ix]);
			projectBox.draw();
		}
	} else {
		$("#list_of_elements", this.container).append(
		"<div class='alert alert-info'>"+
  			"No results found"+
		"</div>");
	}
}

