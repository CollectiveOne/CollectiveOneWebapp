$(document).ready(function() {
	GLOBAL.projectPage = new ProjectPage("#content_pane");
	docReadyCommon(GLOBAL.projectPage.init,GLOBAL.projectPage,false);
});

function ProjectPage(container_id) {
	this.project;
};

ProjectPage.prototype.init = function() {
	GLOBAL.projectPage.updateProject(GLOBAL.REQ_projectName);
}

ProjectPage.prototype.updateProject = function(projectName) {
	GLOBAL.serverComm.projectGet(projectName,this.ProjectReceivedCallback,this);
}

ProjectPage.prototype.ProjectReceivedCallback = function(projectDto) {
	this.project = projectDto;
	this.fillProject();
}

ProjectPage.prototype.fillProject = function() {
	$("#project_name h2",this.container).append(this.project.name);
	$("#project_description",this.container).append(markdown.toHTML(this.project.description));
	
	this.updateGoals();
	this.updateContributors();
	
}

ProjectPage.prototype.updateGoals = function() {
	GLOBAL.serverComm.goalsOfProjectGet(this.project.name,this.goalsOfProjectGetReceivedCallback,this);
}

ProjectPage.prototype.goalsOfProjectGetReceivedCallback = function(goals) {
	for ( var ix in goals) {
		$("#project_goals").append("<div class=goalBox_div id=goalbox"+ix+"_div></div>");
		var goalBox = new GoalBox($("#goalbox"+ix+"_div"),goals[ix]);
	}
}

ProjectPage.prototype.updateContributors = function() {
	GLOBAL.serverComm.projectContributorsGet(this.project.name,this.contributorsOfProjectGetReceivedCallback,this);
}

ProjectPage.prototype.contributorsOfProjectGetReceivedCallback = function(data) {

	var usernamesAndData = data.usernamesAndData;
	var ppsTot = data.ppsTot;
	
	for(var ix in usernamesAndData) {
		var thisUserData = usernamesAndData[ix];
		$("#contributors_table_body").append(
				"<tr>" +
				"	<td>"+getUserPageLink(thisUserData.username)+"</td>"+
				"	<td>"+thisUserData.pps+"</td>"+
				"	<td>"+thisUserData.nCbtionsCreated+"</td>"+
				"	<td>"+thisUserData.nCbtionsDone+"</td>"+
				"	<td>"+thisUserData.nCbtionsDoneRecently+"</td>"+
				"	<td>"+floatToChar(thisUserData.pps/thisUserData.nCbtionsDone,2)+"</td>"+
				"</tr>");
	}
}

