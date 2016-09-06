function ReviewsElement(container_id,confData) {
	// Parent constructor
	this.container = $(container_id);
	this.confData = confData;
	this.reviewsData = [];
	this.rateSelected = null;
};

//Inheritance
ReviewsElement.prototype.updateData = function() {
	GLOBAL.serverComm.reviewsGetOfBid(this.confData.bidId,this.reviewsReceivedCallback,this);
}

ReviewsElement.prototype.reviewsReceivedCallback = function(data) {
	this.reviewsData.reviews = data.reviewDtos;
	this.draw();
}

ReviewsElement.prototype.draw = function() {
	this.container.load("../elements/ReviewsElement/ReviewsElement.html",this.reviewsBoxLoaded.bind(this));
}

ReviewsElement.prototype.reviewsBoxLoaded = function() {

	$("#new_btn_div", this.container).click(this.newReviewClicked.bind(this));

	$("#review_rate_boxes #much_worse", this.container).click(this.muchWorseClicked.bind(this));
	$("#review_rate_boxes #worse", this.container).click(this.worseClicked.bind(this));
	$("#review_rate_boxes #as", this.container).click(this.asExpectedClicked.bind(this));
	$("#review_rate_boxes #better", this.container).click(this.betterClicked.bind(this));
	$("#review_rate_boxes #much_better", this.container).click(this.muchBetterClicked.bind(this));

	$("#review_new_save", this.container).click(this.newReviewSaveClicked.bind(this));

	$("#reviews_list_div", this.container).empty();
	for ( var ix in this.reviewsData.reviews ) {
		$("#reviews_list_div", this.container).append("<div id=review_no"+ix+" class=review_item_div></div>");

		var reviewItem = new ReviewItem("#review_no"+ix, this.reviewsData.reviews[ix]);
		reviewItem.draw();
	}

}

ReviewsElement.prototype.newReviewClicked = function() {
	if(GLOBAL.sessionData.userLogged != null) {
		$("#review_new_form", this.container).toggle();	
	} else {
		showOutput("please login to add a review","DarkRed")
	}
}

ReviewsElement.prototype.muchWorseClicked = function() {
	this.rateSelected = "MUCHWORSE";
	$(".rate_subbox").removeClass('rate_box_selected');
	$("#review_rate_boxes #much_worse", this.container).addClass("rate_box_selected");
}

ReviewsElement.prototype.worseClicked = function() {
	this.rateSelected = "WORSE";
	$(".rate_subbox").removeClass('rate_box_selected');
	$("#review_rate_boxes #worse", this.container).addClass("rate_box_selected");
}

ReviewsElement.prototype.asExpectedClicked = function() {
	this.rateSelected = "ASEXPECTED";
	$(".rate_subbox").removeClass('rate_box_selected');
	$("#review_rate_boxes #as", this.container).addClass("rate_box_selected");
}

ReviewsElement.prototype.betterClicked = function() {
	this.rateSelected = "BETTER";
	$(".rate_subbox").removeClass('rate_box_selected');
	$("#review_rate_boxes #better", this.container).addClass("rate_box_selected");
}

ReviewsElement.prototype.muchBetterClicked = function() {
	this.rateSelected = "MUCHBETTER";
	$(".rate_subbox").removeClass('rate_box_selected');
	$("#review_rate_boxes #much_better", this.container).addClass("rate_box_selected");
}

ReviewsElement.prototype.newReviewSaveClicked = function() {
	if(this.rateSelected == null) {
		showOutput("please select one of the rate options","DarkRed")
	} else {
		var reviewDto = {
				creatorUsername : GLOBAL.sessionData.userLogged.username,
				rate: this.rateSelected,
				description: $("#review_new_description",this.container).val() 
		}

		$("#review_new_form",this.container).hide();	
		GLOBAL.serverComm.reviewNew(reviewDto,this.confData.bidId,this.reviewNewCallback,this);
	}
	
}

ReviewsElement.prototype.reviewNewCallback = function() {
	this.updateData();	
}
