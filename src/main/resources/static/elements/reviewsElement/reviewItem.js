function ReviewItem(container_id,data) {
	// Parent constructor
	this.container = $(container_id);
	this.review = data;
};

//Inheritance
ReviewItem.prototype.updateData = function() {
	GLOBAL.serverComm.reviewGet(this.review.id,this.reviewReceivedCallback,this);
}

ReviewItem.prototype.reviewReceivedCallback = function(reviewDtos) {
	this.review = reviewDto;
	this.draw();
}

ReviewItem.prototype.draw = function() {
	this.container.load("/elements/reviewsElement/reviewItem.html",this.reviewBoxLoaded.bind(this));
}

ReviewItem.prototype.reviewBoxLoaded = function() {
	
	$("#review_stars_container", this.container).rateYo({
        rating: this.review.rate,
        readOnly: true,
        starWidth: "15px"
    });
	$("#review_description", this.container).append(this.review.description);
	var creationDate = new Date(this.review.creationDate);
	$("#creation_info", this.container).append("made by "+getUserPageLink(this.review.creatorUsername)+" on "+creationDate.toDateString());
	
}