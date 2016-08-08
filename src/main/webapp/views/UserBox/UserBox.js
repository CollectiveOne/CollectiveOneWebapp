function UserBox(container_id,userData,projectName) {
	// Parent constructor
	this.container = $(container_id);
	this.user = userData;
	this.projectName = projectName;
	this.data = [];
};

//Inheritance
UserBox.prototype.updateUser = function() {
	GLOBAL.serverComm.userGet(this.user.username,this.userReceivedCallback,this);
}

UserBox.prototype.userReceivedCallback = function(userDto) {
	this.user = userDto;
	GLOBAL.serverComm.userDataIn(this.user.username,this.projectName,this.userDataReceivedCallback,this);
}

UserBox.prototype.userDataReceivedCallback = function(dataIn) {
	this.ppointsInProject = dataIn.ppoints;
	this.draw();
}

UserBox.prototype.draw = function() {
	this.container.load("UserBox/UserBox.html",this.userBoxLoaded.bind(this));
}

UserBox.prototype.userBoxLoaded = function() {
	var href = 'UserPage.action?username=' + this.user.username;
	var text = this.user.username;
	$("#username",this.container).append($('<a href='+href+'>'+text+'</a>'));
	$("#ppoints_div",this.container).append("<p>"+floatToChar(this.ppointsInProject,0)+" pp's in "+this.projectName+"</p>");
}