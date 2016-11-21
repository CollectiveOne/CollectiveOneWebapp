function CommentsBox(container_id,confData) {
	// Parent constructor
	this.container = $(container_id);
	this.confData = confData;
	this.commentsData = [];
};

//Inheritance
CommentsBox.prototype.updateData = function() {
	GLOBAL.serverComm.commentsGetOfCbtion(this.confData.cbtionId,this.commentsReceivedCallback,this);
}

CommentsBox.prototype.commentsReceivedCallback = function(commentDtos) {
	this.commentsData.comments = commentDtos;
	this.draw();
}

CommentsBox.prototype.draw = function() {
	this.container.load("/elements/commentsBox/commentsBox.html",this.commentsBoxLoaded.bind(this));
}

CommentsBox.prototype.commentsBoxLoaded = function() {

	$("#new_comment_btn", this.container).click(this.newCommentClicked.bind(this));
	$("#new_comment_save", this.container).click(this.newCommentSaveClicked.bind(this));
	
	if(isUserLogged()) {
		$("#new_comment_btn", this.container).show();
	}

	$("#comments_list_div", this.container).empty();
	for ( var ix in this.commentsData.comments ) {
		$("#comments_list_container", this.container).append("<div id=comment_no"+ix+" class=comment_item_div></div>");

		var commentItem = new CommentItem($("#comment_no"+ix, this.container),this.commentsData.comments[ix]);
		commentItem.draw();
	}

}

CommentsBox.prototype.newCommentClicked = function() {
	if(GLOBAL.sessionData.userLogged != null) {
		$("#new_comment_form", this.container).toggle();	
	} else {
		showOutput("please login to add a comment","DarkRed")
	}
}

CommentsBox.prototype.newCommentSaveClicked = function() {
	var commentDto = {
			creatorUsername : GLOBAL.sessionData.userLogged.username,
			content: $("#new_comment_content",this.container).val(),
			cbtionId : this.confData.cbtionId
	}

	$("#new_comment_form",this.container).hide();	
	GLOBAL.serverComm.commentNew(commentDto,this.commentNewCallback,this);
	
}

CommentsBox.prototype.commentNewCallback = function() {
	this.updateData();	
}
