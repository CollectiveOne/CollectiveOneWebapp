$(document).ready(function() {
	GLOBAL.userPage = new UserPage("#content_pane");
	docReadyCommon(GLOBAL.userPage.init,GLOBAL.userPage,false);
});

function UserPage(container_id) {
	this.user;
	this.container = $(container_id);
};

UserPage.prototype.init = function() {
	$('#user_profile_textarea',this.container).markdown({
		autofocus:false,
		savable:false,
		hiddenButtons: ["cmdHeading", "cmdImage"],
		resize: "vertical"
	});

	if(isUserLogged()) {
		if(getLoggedUsername() == GLOBAL.REQ_username) {
			$("#edit_profile_btn").show();
			$("#edit_profile_btn").click(this.editProfileBtnClicked.bind(this));
			$("#edit_profile_save").click(this.editProfileSaveBtnClicked.bind(this));
		}
	}

	GLOBAL.userPage.updateUser(GLOBAL.REQ_username);
}

UserPage.prototype.updateUser = function(username) {
	GLOBAL.serverComm.userGet(username,this.UserReceivedCallback,this);
}

UserPage.prototype.UserReceivedCallback = function(userDto) {
	this.user = userDto;
	this.fillUserNameAndProfile();
	this.fillUserProjectsAndCbtions();
}

UserPage.prototype.fillUserNameAndProfile = function() {
	
	$("#user_profile_html").show();	
	$("#edit_profile_form").hide();
	
	$("#username_div h2",this.container).html(this.user.username);
	
	if(this.user.profile) {
		$("#user_profile_html").html(markdown.toHTML(this.user.profile));
	} else {
		$("#user_profile_html").html("Apparently, this user prefers to keep an air of mystery about them.");
	}
}

UserPage.prototype.fillUserProjectsAndCbtions = function() {
	this.updateProjectsContributed();
	this.updateContributionsAccepted();
}


UserPage.prototype.editProfileBtnClicked = function() {
	$("#user_profile_html").toggle();	
	$("#edit_profile_form").toggle();

	if(this.user.profile) {
		$("#user_profile_textarea").html(this.user.profile);	
	}
}

UserPage.prototype.editProfileSaveBtnClicked = function() {
	var newProfile = $("#user_profile_textarea").val();
	GLOBAL.serverComm.userUpdateProfile(this.user.username,newProfile,this.editProfileSaveCallback,this);
}

UserPage.prototype.editProfileSaveCallback = function() {
	GLOBAL.serverComm.userGet(this.user.username,this.UserReceivedAfterProfileCallback,this);
}

UserPage.prototype.UserReceivedAfterProfileCallback = function(userDto) {
	this.user = userDto;
	this.fillUserNameAndProfile();
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
	var cbtionList = new CbtionList("#accepted_contributions_list", {
		contributorUsername: this.user.username, 
		maxheight: "600px",
		stateNames: ["ACCEPTED"],
		showFilterBtn: true,
		showNewBtn: false,
		newBtnLink: ""
		});

	cbtionList.init();
	
	var assignedCbtionList = new CbtionList("#assigned_contributions_list", {
		assigneeUsername: this.user.username, 
		maxheight: "600px",
		stateNames: ["ASSIGNED"],
		showFilterBtn: true,
		showNewBtn: false,
		newBtnLink: ""
		});

	assignedCbtionList.init();
}



