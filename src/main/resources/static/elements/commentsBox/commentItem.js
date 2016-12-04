function CommentItem(container_id,data) {
	// Parent constructor
	this.container = $(container_id);
	this.comment = data;
	this.expandReplies = false;
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
	this.container.load("/elements/commentsBox/commentItem.html",this.commentBoxLoaded.bind(this));
}

CommentItem.prototype.commentBoxLoaded = function() {
	
	$("#comment_content", this.container).append(this.comment.content);
	
	var creationDate = new Date(this.comment.creationDate);
	$("#comment_creator", this.container).append("by "+getUserPageLink(this.comment.creatorUsername)+" "+getTimeStrSince(creationDate)+" ago");
	
	if(this.comment.nreplies > 0) {
		$("#comment_show_replies", this.container).append("show replies ("+this.comment.nreplies+")");
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
	
	if(this.expandReplies) {
		this.showRepliesClicked();
	}

}

CommentItem.prototype.commentReplyClick = function() {
	if(isUserLogged()) {
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
	this.expandReplies = true;
	this.updateData();	
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

CommentItem.prototype.repliesGetCallback = function(repliesDtos) {
	
	$("#replies_elements",this.container).empty();

	for(var ix in repliesDtos) {
		$("#replies_elements",this.container).append("<div id=reply_no"+ix+" class=reply_container></div>");
		var replyItem = new CommentItem($("#reply_no"+ix,this.container),repliesDtos[ix]);
		replyItem.draw();
	}
}

CommentItem.prototype.promoteUpClick = function() {
	if(isUserLogged()) {
		GLOBAL.serverComm.commentPromote(this.comment.id,true,this.promotionSentCallback,this);	
	}
}

CommentItem.prototype.promoteDownClick = function() {
	if(isUserLogged()) {
		GLOBAL.serverComm.commentPromote(this.comment.id,false,this.promotionSentCallback,this);
	}
}

CommentItem.prototype.promotionSentCallback = function() {
	this.updateData();
}

