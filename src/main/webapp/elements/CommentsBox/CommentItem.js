function CommentItem(container_id,data) {
	// Parent constructor
	this.container = $(container_id);
	this.comment = data;
	this.replies_expanded = false;
};

//Inheritance
CommentItem.prototype.updateData = function() {
	GLOBAL.serverComm.commentGet(this.comment.id,this.commentReceivedCallback,this);
}

CommentItem.prototype.commentReceivedCallback = function(commentDto) {
	this.comment = commentDto;
	this.draw();
}

CommentItem.prototype.draw = function() {
	this.container.load("../elements/CommentsBox/CommentItem.html",this.commentBoxLoaded.bind(this));
}

CommentItem.prototype.commentBoxLoaded = function() {
	
	$("#comment_content", this.container).append("<p>"+this.comment.content+"</p>");
	
	var creationDate = new Date(this.comment.creationDate);
	$("#comment_creator", this.container).append("<p>by <a href=../views/UserPage.action?username="+this.comment.creatorUsername+">"+this.comment.creatorUsername+"</a> "+getTimeStrSince(creationDate)+" ago</p>");
	
	if(this.comment.nreplies > 0) {
		$("#comment_show_replies", this.container).append("<p>show replies ("+this.comment.nreplies+")</p>");
		$("#comment_show_replies", this.container).show();
	} else {
		$("#comment_show_replies", this.container).hide();
	}

	$("#comment_reply", this.container).click(this.commentReplyClick.bind(this));
	$("#new_comment_save", this.container).click(this.newReplySaveClicked.bind(this));
	$("#comment_show_replies", this.container).click(this.showRepliesClicked.bind(this));
	
	$("#comment_promotion_center_div",this.container).append(this.comment.relevance);
	$("#comment_promotion_up_div",this.container).click(this.promoteUpClick.bind(this));
	$("#comment_promotion_down_div",this.container).click(this.promoteDownClick.bind(this));

}

CommentItem.prototype.commentReplyClick = function() {
	if(GLOBAL.sessionData.userLogged != null) {
		$("#new_comment_form", this.container).toggle();
	} else {
		showOutput("please login to reply","DarkRed")
	}
}

CommentItem.prototype.newReplySaveClicked = function() {
	var commentDto = {
			parentId: this.comment.id,
			creatorUsername : GLOBAL.sessionData.userLogged.username,
			content: $("#new_comment_content",this.container).val(),
			cbtionId : this.comment.cbtionId
	}

	$("#new_comment_form",this.container).hide();	
	GLOBAL.serverComm.commentNew(commentDto,this.replyNewCallback,this);
	
}

CommentItem.prototype.replyNewCallback = function() {
	this.updateReplies();	
}

CommentItem.prototype.showRepliesClicked = function() {
	if(!this.replies_expanded) {
		this.replies_expanded = true;
		$("#replies_elements",this.container).show();	
		this.updateReplies();
	} else {
		this.replies_expanded = false;
		$("#replies_elements",this.container).hide();	
	}
}

CommentItem.prototype.updateReplies = function() {
	GLOBAL.serverComm.commentGetReplies(this.comment.id,this.repliesGetCallback,this);
}

CommentItem.prototype.repliesGetCallback = function(data) {
	
	$("#replies_elements",this.container).empty();

	for(var ix in data.repliesDtos) {
		$("#replies_elements",this.container).append("<div id=reply_no"+ix+" class=reply_container></div>");
		var replyItem = new CommentItem($("#reply_no"+ix,this.container),data.repliesDtos[ix]);
		replyItem.draw();
	}
}

CommentItem.prototype.promoteUpClick = function() {
	GLOBAL.serverComm.commentPromote(this.comment.id,true,this.promotionSentCallback,this);
}

CommentItem.prototype.promoteDownClick = function() {
	GLOBAL.serverComm.commentPromote(this.comment.id,false,this.promotionSentCallback,this);
}

CommentItem.prototype.promotionSentCallback = function() {
}

