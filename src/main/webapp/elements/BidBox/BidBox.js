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
		case "CONSIDERING":
			$("#bid_value_div",this.container).hide();
			$("#bid_delivered_div",this.container).hide();
			$("#show_reviews_btn",this.container).hide();
			
			if(GLOBAL.sessionData.userLogged) {
				if(GLOBAL.sessionData.userLogged.username == this.bid.creatorDto.username) {
					$("#bidder_update_div",this.container).show();
					$("#bidder_offer_btn",this.container).click(this.offerNowClick.bind(this));
				}
			}

			break;

		case "OFFERED":
			var decBox = new DecisionBoxSmall($("#bid_decision_div",this.container),this.bid.assignDec, GLOBAL.sessionData.userLogged);
			decBox.draw();
			$("#show_reviews_btn",this.container).hide();
			break;
			
		case "ASSIGNED":
			var decBox = new DecisionBoxSmall($("#bid_decision_div",this.container),this.bid.acceptDec, GLOBAL.sessionData.userLogged);
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
			break;			
	}
	
}

BidBox.prototype.offerNowClick = function() {
	$("#bid_value_form",this.container).show();
	$("#bid_delivered_form",this.container).show();
	$("#offer_delivery_date_in",this.container).datepicker();
	$("#bidder_offer_save_btn",this.container).show();

	$("#bidder_offer_save_btn",this.container).click(this.offerSave.bind(this));
}

BidBox.prototype.offerSave = function() {
	var offerData = {
		bidId: this.bid.id,
		creatorUsername: GLOBAL.sessionData.userLogged.username,
	}

	if($("#offer_delivery_date_in",this.container).val() == "") offerData.deliveryDate = 0;
	else offerData.deliveryDate = deliveryDate = Date.parse($("#offer_delivery_date_in",this.container).val());

	if($("#offer_ppoints_in",this.container).val() == "") offerData.ppoints = 0;
	else offerData.ppoints = $("#offer_ppoints_in",this.container).val();

	GLOBAL.serverComm.bidOffer(offerData, this.offerSentCallback, this)
}

BidBox.prototype.offerSentCallback = function() {
	this.udpateBid();
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


