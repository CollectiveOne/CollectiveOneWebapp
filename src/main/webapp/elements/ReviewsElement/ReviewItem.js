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
	this.container.load("../elements/ReviewsElement/ReviewItem.html",this.reviewBoxLoaded.bind(this));
}

ReviewItem.prototype.reviewBoxLoaded = function() {
	
	switch(this.review.rate) {
		case "MUCHWORSE":
			$("#review_rate", this.container).append("<p>much worse</p>");
			$("#review_rate", this.container).addClass("much_worse_btn rate_subbox double_line");
			break;
			
		case "WORSE":
			$("#review_rate", this.container).append("<p>worse</p>");
			$("#review_rate", this.container).addClass("worse_btn rate_subbox single_line");
			break;
		
		case "ASEXPECTED":
			$("#review_rate", this.container).append("<p>as expected</p>");
			$("#review_rate", this.container).addClass("as_btn rate_subbox double_line");
			break;
			
		case "BETTER":
			$("#review_rate", this.container).append("<p>better</p>");
			$("#review_rate", this.container).addClass("better_btn rate_subbox single_line");
			break;
			
		case "MUCHBETTER":
			$("#review_rate", this.container).append("<p>much better</p>");
			$("#review_rate", this.container).addClass("much_better_btn rate_subbox double_line");
			break;
	}
	
	
	$("#review_description", this.container).append("<p>"+this.review.description+"</p>");
	$("#review_author", this.container).append("<p>...by "+this.review.creatorUsername+"</p>");
	
}