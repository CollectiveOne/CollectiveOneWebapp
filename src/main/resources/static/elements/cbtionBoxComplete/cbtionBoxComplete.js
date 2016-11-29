function CbtionBoxComplete(container_id,cbtionData) {
	// Parent constructor
	this.container = $(container_id);
	this.cbtion = cbtionData;
	this.bidOffer = false;
}

CbtionBoxComplete.prototype.update = function() {
	GLOBAL.serverComm.cbtionGet(this.cbtion.id,this.cbtionDataReceived,this);
}


CbtionBoxComplete.prototype.cbtionDataReceived = function(cbtionDto) {
	this.cbtion = cbtionDto;
	this.draw();	
}


CbtionBoxComplete.prototype.draw = function() {
	this.container.load("/elements/cbtionBoxComplete/cbtionBoxComplete.html",this.cbtionBoxLoaded.bind(this));
}

CbtionBoxComplete.prototype.cbtionBoxLoaded = function() {
	
	$("#promotion_center_div",this.container).append(this.cbtion.relevance);
	$("#promotion_up_div",this.container).click(this.promoteUpClick.bind(this));
	$("#promotion_down_div",this.container).click(this.promoteDownClick.bind(this));

	$("#title_div",this.container).append("<a href=/views/cbtionPageR/"+ this.cbtion.id+">"+this.cbtion.title+"</a>");
	$("#description_div",this.container).append(this.cbtion.description);
	
	$("#product_text",this.container).append(this.cbtion.product);
	
	if(this.cbtion.parentGoalsTags) {
		var nparents = this.cbtion.parentGoalsTags.length;
		for(var ix=0; ix < nparents; ix++) {
			// cycle from last to first as the first parent is the immediate parent
			var parentTag = this.cbtion.parentGoalsTags[nparents - ix - 1];
			$("#goal_div",this.container).append(getGoalPageLink(parentTag,this.cbtion.projectName));
		}
	}
	
	if(this.cbtion.goalTag)	$("#goal_div",this.container).append(getGoalPageLink(this.cbtion.goalTag,this.cbtion.projectName));
	
	$("#project_div",this.container).append(getProjectLink(this.cbtion.projectName));
	$("#creator_div",this.container).append(getUserPageLink(this.cbtion.creatorUsername));
	$("#creator_div",this.container).append(" "+getTimeStrSince(this.cbtion.creationDate)+" ago");
	$("#state_div",this.container).append("Current state: "+this.cbtion.state);
	
	switch(this.cbtion.state) {
		case "PROPOSED":
			if(isUserLogged()) {
				var openDec = new DecisionBoxSmall($("#status_desc_div",this.container),this.cbtion.openDec);		
				openDec.updateVoteAndDraw();	
			}
			break;

		case "OPEN":
			if(isUserLogged()) {
				$("#new_bid_btn",this.container).show();
				var deleteDec = new DecisionBoxSmall($("#status_desc_div",this.container),this.cbtion.deleteDec);		
				deleteDec.updateVoteAndDraw();
			}
			break;

		case "ACCEPTED":
			$("#status_desc_div",this.container).append("contributed by "+
				getUserPageLink(this.cbtion.creatorUsername)+
				" for "+this.cbtion.assignedPpoints+" pps");

			$("#cbtion_div #status_desc_div p",this.container).css("font-size","10px");
			break;

	}

	$("#new_bid_btn",this.container).click(this.newBidClick.bind(this));

	$("#newbid_just_considering_btn",this.container).click(this.bidConsideringClick.bind(this));
	$("#newbid_offer_now_btn",this.container).click(this.bidOfferClick.bind(this));
	
	$("#newbid_submit_div",this.container).click(this.bidNew.bind(this));
	$("#newbid_datepicker",this.container).datepicker();
		
	if(this.cbtion.ncomments > 0) {
		$("#show_comments_btn",this.container).append("show comments ("+this.cbtion.ncomments+")")
	} else {
		$("#show_comments_btn",this.container).append("show comments")
	}
	
	$("#show_comments_btn",this.container).click(this.showCommentsClick.bind(this));

	if(isUserLogged()) {
		if(getLoggedUsername() == this.cbtion.creatorUsername) {
			if(this.cbtion.state == "PROPOSED") {
				$("#edit_cbtion_btn",this.container).show();	
				$("#edit_cbtion_btn",this.container).click(this.editClick.bind(this));	
			}
		}
	}

	this.updateBids();
	
}

CbtionBoxComplete.prototype.editClick = function () {
	$("#edit_cbtion_btn",this.container).hide();	
	
	$("#edit_cbtion_save_btn",this.container).show();	
	$("#edit_cbtion_save_btn",this.container).click(this.editSaveClick.bind(this));

	var currentTitle = $("#title_div a", this.container).html();
	var currentDescription = $("#description_div", this.container).html();
	var currentProduct = $("#product_text", this.container).html();

	$("#title_div", this.container).empty();
	$("#description_div", this.container).empty();
	$("#product_text", this.container).empty();

	$("#title_div", this.container).append("<input type=text id=title_edit_input class=form-control></input");
	$("#title_edit_input", this.container).val(currentTitle);

	$("#description_div", this.container).append("<textarea id=description_edit_input rows=5 class=form-control></textarea>");
	$("#description_edit_input", this.container).val(currentDescription);

	$("#product_div", this.container).append("<input type=text id=product_edit_input class=form-control></input");
	$("#product_edit_input", this.container).val(currentProduct);

}

CbtionBoxComplete.prototype.editSaveClick = function () {
	var cbtionEditData = {
		id: this.cbtion.id,
		title: $("#title_edit_input", this.container).val(),
		description: $("#description_edit_input", this.container).val(),
		product: $("#product_edit_input", this.container).val()
	}

	GLOBAL.serverComm.cbtionEdit(cbtionEditData,this.editedCallback,this);
}

CbtionBoxComplete.prototype.editedCallback = function () { 
	this.update();
}

CbtionBoxComplete.prototype.newBidClick = function () {
	if(this.cbtion.state == "OPEN") {
		$("#new_bid_form_container",this.container).toggle();
		$("#newbid_username_div",this.container).html(("bidder: "+GLOBAL.sessionData.userLogged.username));	
	} else {
		showOutput("contribution is not open");
	}
	
}

CbtionBoxComplete.prototype.bidConsideringClick = function() {
	this.bidOffer = false;
	$("#newbid_just_considering_btn",this.container).removeClass("btn-info").addClass("btn-success");
	$("#newbid_offer_now_btn",this.container).removeClass("btn-success").addClass("btn-info");

	$("#newbid_ppoints_div",this.container).hide();
	$("#newbid_delivery_date_div",this.container).hide();
}

CbtionBoxComplete.prototype.bidOfferClick = function() {
	this.bidOffer = true;
	$("#newbid_just_considering_btn",this.container).removeClass("btn-success").addClass("btn-info");
	$("#newbid_offer_now_btn",this.container).removeClass("btn-info").addClass("btn-success");

	$("#newbid_ppoints_div",this.container).show();
	$("#newbid_delivery_date_div",this.container).show();
}


CbtionBoxComplete.prototype.showCommentsClick = function() {
	$("#comments_box_container",this.container).toggle();
	var commentsBox = new CommentsBox($("#comments_box_container",this.container),{cbtionId: this.cbtion.id});
	commentsBox.updateData();
}

CbtionBoxComplete.prototype.promoteUpClick = function() {
	GLOBAL.serverComm.cbtionPromote(this.cbtion.id,true,this.update,this);
}

CbtionBoxComplete.prototype.promoteDownClick = function() {
	GLOBAL.serverComm.cbtionPromote(this.cbtion.id,false,this.update,this);
}

CbtionBoxComplete.prototype.bidNew = function (){
	var bidData = { 
		cbtionId:this.cbtion.id,
		description:$("#newbid_description_in",this.container).val(),
		offer: this.bidOffer
	}; 

	if($("#newbid_datepicker",this.container).val() == "") bidData.deliveryDate = 0;
	else bidData.deliveryDate = deliveryDate = Date.parse($("#newbid_datepicker",this.container).val());

	if($("#newbid_ppoints_in",this.container).val() == "") bidData.ppoints = 0;
	else bidData.ppoints = $("#newbid_ppoints_in",this.container).val();

	GLOBAL.serverComm.bidNew(bidData,this.newBidSavedCallback,this);
}

CbtionBoxComplete.prototype.newBidSavedCallback = function() {
	$("#new_bid_form_container").hide();
	this.updateBids();
}

CbtionBoxComplete.prototype.updateBids = function(bidDtos) {
	GLOBAL.serverComm.bidsOfCbtionGet(this.cbtion.id,this.updateBidsCallback,this);
}

CbtionBoxComplete.prototype.updateBidsCallback = function(bidDtos) {
	this.cbtion.bids = bidDtos;
	this.drawBids();
}

CbtionBoxComplete.prototype.drawBids = function() {
	
	$("#bids_div",this.container).empty();
	var nb = this.cbtion.bids.length;
	
	for(var ix=0; ix<nb ; ix++ ) {
		
		$("#bids_div",this.container).append($("<div class=bid_div id=bid_"+ix+"_div></div>"));
		
		var bidBox = new BidBox("#bid_"+ix+"_div",this.cbtion.bids[ix], this.cbtion.projectName);
		bidBox.draw();
		
	}
}
