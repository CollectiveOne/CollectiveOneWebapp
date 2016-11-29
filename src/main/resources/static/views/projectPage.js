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
	$("#project_description",this.container).append(this.project.description);
	
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

	var usernamesAndPpps = data.usernamesAndPps;
	var ppsTot = data.ppsTot;
	
	var chartData = []; 
	var chartLabels = [];
	var chartColors = [];
	
	for(var ix in usernamesAndPpps) {
		chartData.push(usernamesAndPpps[ix].pps);
		chartLabels.push(usernamesAndPpps[ix].username+"("+usernamesAndPpps[ix].pps+")");
		chartColors.push("#5bc0de");
	}
	
	var ctx =  $("#contributors_chart");
	var myDoughnutChart = new Chart(ctx, {
	    type: 'bar',
	    data: {	datasets: [{
	    			data: chartData,
	    			backgroundColor: chartColors
	    		}],
	    		labels: chartLabels 
	    },
	    options: {
	    		legend: {
	    			display: false
	    		},
	    		cutoutPercentage: 50
	    		}
	});

}

