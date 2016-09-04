function ArgumentBox(container_id,argumentData) {
	// Parent constructor
	this.container = $(container_id);
	this.argument = argumentData;
};

//Inheritance
ArgumentBox.prototype.updateArgument = function() {
	GLOBAL.serverComm.argumentGet(this.argument.id,this.argumentReceivedCallback,this);
}

ArgumentBox.prototype.argumentReceivedCallback = function(data) {
	this.argument = data.argumentDto;
	this.draw();
}

ArgumentBox.prototype.draw = function() {
	this.container.load("../elements/ArgumentBox/ArgumentBox.html",this.argumentBoxLoaded.bind(this));
}

ArgumentBox.prototype.argumentBoxLoaded = function() {
	$("#description_div",this.container).append("<p>"+this.argument.description+"</p>");
	$("#creator_div",this.container).append("<p>...by "+this.argument.creatorUsername+"</p>");
	$("#backers_div",this.container).append("<p>"+this.argument.nbackers+" backers</p>");
	
	this.updateBacked();
}

ArgumentBox.prototype.updateBacked = function() {
	if(GLOBAL.sessionData.userLogged != null) {
		GLOBAL.serverComm.argumentIsBacked(this.argument.id,GLOBAL.sessionData.userLogged.id,this.argumentIsBackedCallback,this);
	}
}

ArgumentBox.prototype.argumentIsBackedCallback = function(data) {
	if(!data.isbacked) {
		$("#back_container_div",this.container).append("<div id=back_div><p>back this</p></div>");
		$("#back_div",this.container).click(this.backThis.bind(this));
		$("#back_div",this.container).css("background-color","#cccccc");
		$("#back_div p",this.container).css("color","#545454");
	} else {
		$("#back_container_div",this.container).append("<div id=back_div><p>dont back</p></div>");
		$("#back_div",this.container).click(this.unBackThis.bind(this));
		$("#back_div",this.container).css("background-color","DarkGreen");
		$("#back_div p",this.container).css("color","white");
	}

}

ArgumentBox.prototype.backThis = function() {
	if(GLOBAL.sessionData.userLogged != null) {
		GLOBAL.serverComm.argumentBack(this.argument.id,this.argumentBackedCallback,this);
	} else {
		showOutput("please login to back this argument","DarkRed")
	}
}

ArgumentBox.prototype.unBackThis = function() {
	if(GLOBAL.sessionData.userLogged != null) {
		GLOBAL.serverComm.argumentUnBack(this.argument.id,this.argumentBackedCallback,this);
	} else {
		showOutput("please login to back this argument","DarkRed")
	}
}

ArgumentBox.prototype.argumentBackedCallback = function() {
	this.updateArgument();
}

