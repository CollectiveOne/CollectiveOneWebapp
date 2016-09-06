function BidBox(container_id, bid, projectName) {
	// Parent constructor
	this.container = $(container_id);
	this.bid = bid;
	this.projectName = projectName;
	this.reviewsElement == null;
	this.reviewsExpanded = false;
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
	$("#bid_state_div",this.container).append($("<p id=bid_state>"+this.bid.state+"</p>"));
	
	$("#bid_times_created_div",this.container).append($("<p id=bid_times_created_p>created "+getTimeStrSince(this.bid.creationDate)+" ago</p>"));
	$("#bid_description_div",this.container).append("<p>"+this.bid.description+"</p>");
	$("#bid_delivered_div",this.container).append($("<p id=bid_delivered_p>...to be delivered in "+getTimeStrUntil(this.bid.deliveryDate)+"</p>"));
	
	switch(this.bid.state) {
		case "OFFERED":
			applicable_decision = this.bid.assignDec;
			var decBox = new DecisionBoxSmall($("#bid_bottom_div",this.container),this.bid.assignDec, GLOBAL.sessionData.userLogged);
			decBox.draw();
			$("#show_reviews_btn",this.container).hide();
			break;
			
		case "ASSIGNED":
			applicable_decision = this.bid.acceptDec;
			var decBox = new DecisionBoxSmall($("#bid_bottom_div",this.container),this.bid.acceptDec, GLOBAL.sessionData.userLogged);
			decBox.draw();
			$("#show_reviews_btn",this.container).hide();
			break;

		case "NOT_ASSIGNED":
		case "ACCEPTED":
		case "NOT_ACCEPTED":
			$("#show_reviews_btn",this.container).show();
			$("#show_reviews_btn",this.container).click(this.reviewsExpandClick.bind(this));
			break;
			
		case "SUPERSEEDED":
			break;	
			
		default:
			console.log("Unexected bid state " + bid.state);
			
	}
	
}

BidBox.prototype.reviewsExpandClick = function() {

	if(!this.reviewsExpanded) {
		if(this.reviewsElement == null) {
			this.reviewsElement = new ReviewsElement("#reviews_container",{bidId: this.bid.id});
		}
		
		this.reviewsElement.updateData();
		
		$("#reviews_container",this.container).show();
		this.reviewsExpanded = true;
		
	} else {
		$("#reviews_container",this.container).hide();
		this.reviewsExpanded = false;
	}
	
}


