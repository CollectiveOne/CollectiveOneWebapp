$(document).ready(function() {
	GLOBAL.userPage = new UserPage("#content_pane");
	docReadyCommon(GLOBAL.userPage.init,GLOBAL.userPage,false);
});

function UserPage(container_id) {
	this.user;
	this.container = $(container_id);
};

UserPage.prototype.init = function() {
	GLOBAL.userPage.updateUser(GLOBAL.REQ_username);
}

UserPage.prototype.updateUser = function(username) {
	GLOBAL.serverComm.userGet(username,this.UserReceivedCallback,this);
}

UserPage.prototype.UserReceivedCallback = function(userDto) {
	this.user = userDto;
	this.fillUser();
}

UserPage.prototype.fillUser = function() {
	
	$("#username_div h2",this.container).append(this.user.username);
	
	this.updateProjectsContributed();
	this.updateContributionsAccepted();
}

UserPage.prototype.updateProjectsContributed = function() {
	GLOBAL.serverComm.userGetProjectsContributed(this.user.username,this.userProjectsContributedCallback,this);
}

UserPage.prototype.userProjectsContributedCallback = function(projectsContributed) {
	
	for(var ix in projectsContributed) {
		var projectContributed = projectsContributed[ix];

		var ppsContributed = projectContributed.ppsContributed;
		var ppsTot = projectContributed.ppsTot;

		var percentage = floatToChar(ppsContributed/ppsTot*100, 2);
		
		$("#projects_contributed_div",this.container).append(
		$("<p class=project_contributed>"+percentage+"% of "+getProjectLink(projectContributed.projectName)+" with "+ppsContributed+ " of "+ppsTot+" pp's</p>"));	
	}
}

UserPage.prototype.updateContributionsAccepted = function() {
	var cbtionList = new CbtionList("#contributions_list", {
		contributorUsername: this.user.username, 
		maxheight: "600px",
		stateNames: ["ACCEPTED"],
		showFilterBtn: true,
		showNewBtn: false,
		newBtnLink: ""
		});

	cbtionList.init();
}



