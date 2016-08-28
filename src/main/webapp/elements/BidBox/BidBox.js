function BidBox(container_id, bid, projectName) {
	// Parent constructor
	this.container = $(container_id);
	this.bid = bid;
	this.projectName = projectName;

};

//Inheritance
BidBox.prototype.init = function() {
	// this.udpateBid();
}

BidBox.prototype.udpateBid = function() {
	GLOBAL.serverComm.bidGet(this.bid.id,this.bidReceivedCallback,this);
}

BidBox.prototype.bidReceivedCallback = function(bidDto) {
	this.bid = bidDto;
	this.draw();
}

BidBox.prototype.draw = function() {
	this.container.load("../elements/BidBox/BidBox.html",this.bidBoxLoaded.bind(this));
}

BidBox.prototype.bidBoxLoaded = function() {
	
	var userBox = new UserBox($("#bid_user_div",this.container),this.bid.creatorDto,this.projectName);
	userBox.updateUser();

	
	$("#bid_value_div",this.container).append($("<p id=bid_value_text>bid value</p>"));
	$("#bid_value_div",this.container).append($("<p id=bid_value_num>"+floatToChar(this.bid.ppoints,2)+"</p>"));
	$("#bid_value_div",this.container).append($("<p id=bid_status>"+this.bid.state+"</p>"));
	$("#bid_description_div",this.container).append("<p>"+this.bid.description+"</p>");
	
	var applicable_decision = null;
	
	switch(this.bid.state) {
		case "OFFERED":
			applicable_decision = this.bid.assignDec;
			break;
			
		case "ASSIGNED":
			applicable_decision = this.bid.acceptDec;
			break;

		case "NOT_ASSIGNED":
			applicable_decision = this.bid.assignDec;
			break;
			
		case "ACCEPTED":
			applicable_decision = this.bid.acceptDec;
			break;
			
		case "NOT_ACCEPTED":
			applicable_decision = this.bid.acceptDec;
			break;
			
		case "OVERSEED":
			applicable_decision = this.bid.assignDec;
			break;	
			
		default:
			console.log("Unexected bid state " + bid.state);
			
	}
	
	if(applicable_decision.state == "IDLE" || applicable_decision.state == "OPEN") {
		var decBox = new DecisionBoxSmall($("#bid_decision_div",this.container),applicable_decision, GLOBAL.sessionData.userLogged);
		decBox.draw();
	}
}