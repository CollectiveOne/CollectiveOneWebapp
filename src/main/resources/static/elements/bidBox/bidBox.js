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
	this.container.load("/elements/bidBox/bidBox.html",this.bidBoxLoaded.bind(this));
}

BidBox.prototype.bidBoxLoaded = function() {
	
	var userBox = new UserBox($("#bid_user_div",this.container),this.bid.creatorDto,this.projectName);
	userBox.updateUser();

	$("#bid_value_div",this.container).append(floatToChar(this.bid.ppoints,2));
	$("#bid_state_div",this.container).append(this.bid.state);
	
	$("#bid_times_created_div",this.container).append("created "+getTimeStrSince(this.bid.creationDate)+" ago");
	$("#bid_description_div",this.container).append(this.bid.description);

	if(this.bid.doneState == "DONE") {
		$("#bid_delivered_div",this.container).append("done "+getTimeStrSince(this.bid.doneDate)+" ago");
		$("#bid_done_description",this.container).append(this.bid.doneDescription);
	} else {
		$("#bid_delivered_div",this.container).append($("to be delivered in "+getTimeStrUntil(this.bid.deliveryDate)));
	}
		
	
	switch(this.bid.state) {
		case "CONSIDERING":
			this.enableBidderControl();
			this.enableOffer();
			break;

		case "OFFERED":
			$("#bid_value_div",this.container).show();
			this.enableBidderControl();
			this.enableDone();
			if(isUserLogged()) {
				var decBox = new DecisionBoxSmall($("#bid_decision_div",this.container),this.bid.assignDec, GLOBAL.sessionData.userLogged);
				decBox.updateVoteAndDraw();	
			}
			break;
			
		case "ASSIGNED":
			$("#bid_value_div",this.container).show();
			this.enableBidderControl();
			this.enableDone();
			if(isUserLogged()) {
				var decBox = new DecisionBoxSmall($("#bid_decision_div",this.container),this.bid.acceptDec, GLOBAL.sessionData.userLogged);
				decBox.updateVoteAndDraw();
			}
			break;

		case "NOT_ASSIGNED":
		case "ACCEPTED":
		case "NOT_ACCEPTED":
			$("#bid_value_div",this.container).show();
			$("#show_reviews_btn",this.container).show();
			$("#show_reviews_btn",this.container).click(this.reviewsExpandClick.bind(this));
			break;
			
		case "SUPERSEEDED":
			break;	
			
		default:
			break;			
	}
	
}

BidBox.prototype.enableBidderControl = function() {
	if(isUserLogged()) {
		if(getLoggedUsername() == this.bid.creatorDto.username) {
			$("#bidder_update_div",this.container).show();
		}
	}
}

BidBox.prototype.enableOffer = function() {
	$("#bidder_offer_btn",this.container).toggle();
	$("#bidder_offer_btn",this.container).click(this.offerNowClick.bind(this));
}

BidBox.prototype.enableDone = function() {
	if(this.bid.doneState != "DONE") {
		$("#bidder_done_btn",this.container).show();
		$("#bidder_done_btn",this.container).click(this.markDoneClick.bind(this));
		$("#bid_markdone_save_btn",this.container).click(this.doneSaveClick.bind(this));	
	}
}

BidBox.prototype.markDoneClick = function() {
	$("#mark_done_form",this.container).toggle();
}

BidBox.prototype.doneSaveClick = function() {
	GLOBAL.serverComm.bidMarkDone(this.bid.id, $("#bid_markdone_in",this.container).val(),this.markDoneSaveCallback,this);
}

BidBox.prototype.markDoneSaveCallback = function() {
	this.udpateBid();
}


BidBox.prototype.offerNowClick = function() {
	$("#bid_description_div",this.container).hide();
	$("#bidder_offer_btn",this.container).hide();
	
	$("#bid_value_form",this.container).show();
	$("#bid_description_form",this.container).show();
	$("#offer_description_in",this.container).val(this.bid.description);
	$("#bid_delivered_form",this.container).show();
	$("#offer_delivery_date_in",this.container).datepicker();
	$("#bidder_offer_save_btn",this.container).show();

	$("#bidder_offer_save_btn",this.container).click(this.offerSave.bind(this));
}

BidBox.prototype.offerSave = function() {
	var bidData = { 
		id: this.bid.id,
		description:$("#offer_description_in",this.container).val(),
	}; 

	if($("#offer_delivery_date_in",this.container).val() == "") bidData.deliveryDate = 0;
	else bidData.deliveryDate = deliveryDate = Date.parse($("#offer_delivery_date_in",this.container).val());

	if($("#offer_ppoints_in",this.container).val() == "") bidData.ppoints = 0;
	else bidData.ppoints = $("#offer_ppoints_in",this.container).val();

	GLOBAL.serverComm.bidOffer(bidData,this.offerSentCallback,this);

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


