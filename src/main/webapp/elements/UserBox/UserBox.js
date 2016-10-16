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
	GLOBAL.serverComm.userProjectPpsGet(this.user.username,this.projectName,this.userDataReceivedCallback,this);
}

UserBox.prototype.userDataReceivedCallback = function(data) {
	this.data = data;
	this.draw();
}

UserBox.prototype.draw = function() {
	this.container.load("../elements/UserBox/UserBox.html",this.userBoxLoaded.bind(this));
}

UserBox.prototype.userBoxLoaded = function() {
	var pps = this.data.projectContributedDto.ppsContributed;
	var ppsTot = this.data.projectContributedDto.ppsTot;
	var percentage = pps/ppsTot*100;

	$("#username",this.container).append($(getUserPageLink(this.user.username)));
	$("#ppoints_div",this.container).append("<p>"+floatToChar(percentage,0)+"% in "+this.projectName+"</p>");
}