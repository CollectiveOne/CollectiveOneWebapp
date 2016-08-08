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
	$("#username_div",this.container).append($("<p id=username>"+this.user.username+"</p>"));
	for(var ix=0; ix < this.user.projectsContributed.length; ix++) {
		var project_name = this.user.projectsContributed[ix];
		GLOBAL.serverComm.userDataIn(this.user.username,project_name,this.userDataInReceivedCallback,this);
	}
}

UserPage.prototype.userDataInReceivedCallback = function(userDataIn) {
	$("#projects_contributed_div",this.container).append(
			$("<p class=projec_contributed>"+userDataIn.projectDto.name
					+" with "+userDataIn.ppoints+ " of "+userDataIn.projectDto.ppsTot+" pp's ("
					+floatToChar(userDataIn.ppoints/userDataIn.projectDto.ppsTot*100, 2)+"%)</p>"));
}


