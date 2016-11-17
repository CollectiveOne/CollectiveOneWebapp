function ArgumentBox(container_id,argumentData) {
	// Parent constructor
	this.container = $(container_id);
	this.argument = argumentData;
	this.isBacked = false;
};

//Inheritance
ArgumentBox.prototype.updateArgument = function() {
	GLOBAL.serverComm.argumentGet(this.argument.id,this.argumentReceivedCallback,this);
}

ArgumentBox.prototype.argumentReceivedCallback = function(argumentDto) {
	this.argument = argumentDto;
	this.draw();
}

ArgumentBox.prototype.draw = function() {
	this.container.load("/elements/argumentBox/argumentBox.html",this.argumentBoxLoaded.bind(this));
}

ArgumentBox.prototype.argumentBoxLoaded = function() {
	$("#description_div",this.container).append(this.argument.description);
	$("#creator_div",this.container).append("...by "+getUserPageLink(this.argument.creatorUsername));
	$("#backers_div",this.container).append(""+this.argument.nbackers+" backers");
	$("#back_btn",this.container).click(this.backThis.bind(this));
	
	this.updateBacked();
}

ArgumentBox.prototype.updateBacked = function() {
	if(isUserLogged()) {
		GLOBAL.serverComm.argumentIsBacked(this.argument.id,this.argumentIsBackedCallback,this);
	}
}

ArgumentBox.prototype.argumentIsBackedCallback = function(isbacked) {
	this.isBacked = isbacked;
	if(isbacked) {
		$("#back_btn",this.container).html("backed");
		$("#back_btn",this.container).removeClass("btn-info").removeClass("btn-success").addClass("btn-success");
	} else {
		$("#back_btn",this.container).html("back");
		$("#back_btn",this.container).removeClass("btn-info").removeClass("btn-success").addClass("btn-info");
	}
}

ArgumentBox.prototype.backThis = function() {
	if(!this.isBacked) {
		GLOBAL.serverComm.argumentBack(this.argument.id,true,this.argumentBackedCallback,this);
	} else {
		GLOBAL.serverComm.argumentBack(this.argument.id,false,this.argumentBackedCallback,this);
	}
}

ArgumentBox.prototype.argumentBackedCallback = function() {
	this.updateArgument();
}

