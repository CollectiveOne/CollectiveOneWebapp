$(document).ready(function() {
	
	GLOBAL = new Object();
	GLOBAL.userPage = new UserPage("#content_pane");
	CopDocReadyCommon(GLOBAL.userPage.init,GLOBAL.userPage);
});

function UserPage(container_id) {
	// Parent constructor
	Page.call(this,container_id);
	
	this.user;
};

//Inheritance
UserPage.prototype = Page.prototype;

UserPage.prototype.init = function() {
	GLOBAL.userPage.updateUser(JSP_username);
}

UserPage.prototype.updateUser = function(username) {
	GLOBAL.serverComm.userGet(username,this.UserReceivedCallback,this);
}

UserPage.prototype.UserReceivedCallback = function(userDto) {
	this.user = userDto;
	this.drawUser();
}

UserPage.prototype.drawUser = function() {
	this.container.load("../views/UserPage/UserPage.html",this.fillUser.bind(this));
}


UserPage.prototype.fillUser = function() {
	
	$("#username_div",this.container).append($("<p id=username>"+this.user.username+"</p>"));
	
	this.updateProjectsContributed();
	this.updateContributionsAccepted();
}

UserPage.prototype.updateProjectsContributed = function() {
	GLOBAL.serverComm.userGetProjectsContributed(this.user.username,this.userProjectsContributedCallback,this);
}

UserPage.prototype.userProjectsContributedCallback = function(data) {
	
	var projectsContributed = data.projectsContributedDtos;

	for(var ix in projectsContributed) {
		var projectContributed = projectsContributed[ix];

		var ppsContributed = projectContributed.ppsContributed;
		var ppsTot = projectContributed.ppsTot;

		var percentage = floatToChar(ppsContributed/ppsTot*100, 2);
		var projectLink = "<a href=../views/ProjectPage.action?projectName="+projectContributed.projectName+">"+projectContributed.projectName+"</a>";

		$("#projects_contributed_div",this.container).append(
		$("<p class=project_contributed>"+percentage+"% of "+projectLink+" with "+ppsContributed+ " of "+ppsTot+" pp's</p>"));	
	}


	
}

UserPage.prototype.updateContributionsAccepted = function() {
	this.cbtionList = new CbtionList("#contributions_list", {
		contributorUsername: this.user.username, 
		maxheight: "600px",
		stateNames: ["ACCEPTED"]
		});
}



