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

ReviewsElement.prototype.reviewsReceivedCallback = function(reviewDtos) {
	this.reviewsData.reviews = reviewDtos;
	this.draw();
}

ReviewsElement.prototype.draw = function() {
	this.container.load("/elements/reviewsElement/reviewsElement.html",this.reviewsBoxLoaded.bind(this));
}

ReviewsElement.prototype.reviewsBoxLoaded = function() {

	$("#new_btn_div", this.container).click(this.newReviewClicked.bind(this));

	if(isUserLogged()) {
		$("#new_btn_div", this.container).show();
	}

	$(".rateyo").rateYo({
          precision: 0
          }).on("rateyo.set", this.newRateYoSet.bind(this))
          	.on("rateyo.change", this.newRateYoChanged.bind(this));
	
	$("#review_new_save", this.container).click(this.newReviewSaveClicked.bind(this));

	$("#reviews_list_div", this.container).empty();
	for ( var ix in this.reviewsData.reviews ) {
		$("#reviews_list_div", this.container).append("<div id=review_no"+ix+" class=review_item_div></div>");

		var reviewItem = new ReviewItem("#review_no"+ix, this.reviewsData.reviews[ix]);
		reviewItem.draw();
	}

}

ReviewsElement.prototype.newRateYoChanged = function (e,data) {
	var str = '';

	if(data.rating <= 1.5) {
		str	= "much worse than expected";
	} else if(1.5 < data.rating && data.rating <= 2.5) {
		str	= "worse than expected";
	} else if(2.5 < data.rating && data.rating <= 3.5) {
		str	= "broadly as expected";
	} else if(3.5 < data.rating && data.rating <= 4.5) {
		str	= "better than expected";
	} else if(4.5 < data.rating && data.rating <= 5.5) {
		str	= "much better than expected";
	}

	$("#stars_status_num",this.container).html(data.rating);
	$("#stars_status_txt",this.container).html(str);
}

ReviewsElement.prototype.newRateYoSet = function (e,data) {
	this.rateSelected = data.rating;
}

ReviewsElement.prototype.newReviewClicked = function() {
	if(GLOBAL.sessionData.userLogged != null) {
		$("#review_new_form", this.container).toggle();	
	} else {
		showOutput("please login to add a review","DarkRed")
	}
}

ReviewsElement.prototype.newReviewSaveClicked = function() {
	if(this.rateSelected == null) {
		showOutput("please select one of the rate options","DarkRed")
	} else {
		var reviewDto = {
				bidId : this.confData.bidId,
				creatorUsername : GLOBAL.sessionData.userLogged.username,
				rate: this.rateSelected,
				description: $("#review_new_description",this.container).val() 
		}

		$("#review_new_form",this.container).hide();	
		GLOBAL.serverComm.reviewNew(reviewDto,this.reviewNewCallback,this);
	}
	
}

ReviewsElement.prototype.reviewNewCallback = function() {
	this.updateData();	
}
