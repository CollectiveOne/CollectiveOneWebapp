$(document).ready(function() {
	
	GLOBAL = new Object();
	GLOBAL.projectPage = new ProjectPage("#content_pane");
	CopDocReadyCommon(GLOBAL.projectPage.init,GLOBAL.projectPage);
});

function ProjectPage(container_id) {
	// Parent constructor
	Page.call(this,container_id);
	
	this.project;
};

//Inheritance
ProjectPage.prototype = Page.prototype;

ProjectPage.prototype.init = function() {
	GLOBAL.projectPage.updateProject(JSP_projectName);
}

ProjectPage.prototype.updateProject = function(projectName) {
	GLOBAL.serverComm.projectGet(projectName,this.ProjectReceivedCallback,this);
}

ProjectPage.prototype.ProjectReceivedCallback = function(projectDto) {
	this.project = projectDto;
	this.drawProject();
}

ProjectPage.prototype.drawProject = function() {
	$("#project_name",this.container).append($("<p>"+this.project.name+"</p>"));
	$("#project_description",this.container).append($("<p>"+this.project.description+"</p>"));
	
	this.updateContributors();
}

ProjectPage.prototype.updateContributors = function() {
	GLOBAL.serverComm.projectContributorsGet(this.project.name,this.contributorsOfProjectGetReceivedCallback,this);
}

ProjectPage.prototype.contributorsOfProjectGetReceivedCallback = function(data) {
	var usernamesAndPpps = data.usernamesAndPps;
	var ppsTot = data.ppsTot;
	for(var ix in usernamesAndPpps) {
		var username = usernamesAndPpps[ix].username;
		var pps = usernamesAndPpps[ix].pps;
		var percentage = pps/ppsTot*100;
		$("#contributors_list",this.container).append("<p><a href=../views/UserPage.action?username="+username+">"+username+"</a> with ("+floatToChar(percentage,2)+" %)</p>");
	}
}

