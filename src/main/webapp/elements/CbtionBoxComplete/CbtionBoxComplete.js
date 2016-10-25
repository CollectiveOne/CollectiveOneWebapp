function CbtionBoxComplete(container_id,cbtionData) {
	// Parent constructor
	this.container = $(container_id);
	this.cbtion = cbtionData;
	this.bidOffer = false;
}

CbtionBoxComplete.prototype.draw = function() {
	this.container.load("../elements/CbtionBoxComplete/CbtionBoxComplete.html",this.cbtionBoxLoaded.bind(this));
}

CbtionBoxComplete.prototype.cbtionBoxLoaded = function() {
	
	$("#promotion_center_div",this.container).append(this.cbtion.relevance);
	$("#promotion_up_div",this.container).click(this.promoteUpClick.bind(this));
	$("#promotion_down_div",this.container).click(this.promoteDownClick.bind(this));

	$("#title_div",this.container).append("<a href=CbtionPage.action?cbtionId="+ this.cbtion.id+">"+this.cbtion.title+"</a>");
	$("#description_div",this.container).append("<p>"+this.cbtion.description+"</p>");
	
	$("#product_div",this.container).append("<p id=product_text>"+this.cbtion.product+"</p>");
	
	if(this.cbtion.parentGoalsTags) {
		var nparents = this.cbtion.parentGoalsTags.length;
		for(var ix=0; ix < nparents; ix++) {
			// cycle from last to first as the first parent is the immediate parent
			var parentTag = this.cbtion.parentGoalsTags[nparents - ix - 1];
			$("#goal_div",this.container).append(getGoalPageLink(parentTag,this.cbtion.projectName));
		}
	}
	
	if(this.cbtion.goalTag)	$("#goal_div",this.container).append(getGoalPageLink(this.cbtion.goalTag,this.cbtion.projectName));
	
	$("#project_div",this.container).append("<a href=ProjectPage.action?projectName="+this.cbtion.projectName+">"+this.cbtion.projectName+"</a>");
	$("#creator_div",this.container).append(getUserPageLink(this.cbtion.creatorUsername));
	$("#creator_div",this.container).append("<p> "+getTimeStrSince(this.cbtion.creationDate)+" ago</p>");
	$("#state_div",this.container).append("<p>Current state: "+this.cbtion.state+"</p>");
	
	switch(this.cbtion.state) {
		case "PROPOSED":
			if(GLOBAL.sessionData.userLogged) {
				var openDec = new DecisionBoxSmall($("#status_desc_div",this.container),this.cbtion.openDec, GLOBAL.sessionData.userLogged);		
				openDec.draw();	
			}
			break;

		case "OPEN":
			if(GLOBAL.sessionData.userLogged) {
				var deleteDec = new DecisionBoxSmall($("#status_desc_div",this.container),this.cbtion.deleteDec, GLOBAL.sessionData.userLogged);		
				deleteDec.draw();
			}
			break;

		case "ACCEPTED":
			$("#status_desc_div",this.container).append("<p>contributed by "+
				getUserPageLink(this.cbtion.creatorUsername)+
				" for "+this.cbtion.assignedPpoints+" pps</p>");

			$("#cbtion_div #status_desc_div p",this.container).css("font-size","10px");
			break;

	}
	
	if(GLOBAL.sessionData.userLogged) $("#new_bid_btn",this.container).show();

	$("#new_bid_btn",this.container).click(this.newBidClick.bind(this));

	$("#newbid_just_considering_btn",this.container).click(this.bidConsideringClick.bind(this));
	$("#newbid_offer_now_btn",this.container).click(this.bidOfferClick.bind(this));
	
	$("#newbid_submit_div",this.container).click(this.bidNew.bind(this));
	$("#newbid_datepicker",this.container).datepicker();
		
	if(this.cbtion.ncomments > 0) {
		$("#show_comments_btn",this.container).append("<p>show comments ("+this.cbtion.ncomments+")</p>")
	} else {
		$("#show_comments_btn",this.container).append("<p>show comments</p>")
	}
	
	$("#show_comments_btn",this.container).click(this.showCommentsClick.bind(this));

	if(GLOBAL.sessionData.userLogged) {
		if(GLOBAL.sessionData.userLogged.username == this.cbtion.creatorUsername) {
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
	var currentDescription = $("#description_div p", this.container).html();
	var currentProduct = $("#product_div #product_text", this.container).html();

	$("#title_div", this.container).empty();
	$("#description_div", this.container).empty();
	$("#product_div", this.container).empty();

	$("#title_div", this.container).append("<input type=text id=title_edit_input></input");
	$("#title_edit_input", this.container).val(currentTitle);

	$("#description_div", this.container).append("<textarea id=description_edit_input></textarea>");
	$("#description_edit_input", this.container).val(currentDescription);

	$("#product_div", this.container).append("<input type=text id=product_edit_input></input");
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

CbtionBoxComplete.prototype.editedCallback = function (data) { 
	this.cbtion = data.cbtionDto;
	this.draw();
}

CbtionBoxComplete.prototype.newBidClick = function () {
	if(this.cbtion.state == "OPEN") {
		$("#new_bid_form_container",this.container).toggle();
		$("#newbid_username_div",this.container).html(("<p>bidder: "+GLOBAL.sessionData.userLogged.username+"</p>"));	
	} else {
		showOutput("contribution is not open");
	}
	
}

CbtionBoxComplete.prototype.bidConsideringClick = function() {
	this.bidOffer = false;
	$("#newbid_just_considering_btn",this.container).removeClass("cp_btn_light").addClass("cp_btn_dark");
	$("#newbid_offer_now_btn",this.container).removeClass("cp_btn_dark").addClass("cp_btn_light");

	$("#newbid_ppoints_div",this.container).hide();
	$("#newbid_delivery_date_div",this.container).hide();
}

CbtionBoxComplete.prototype.bidOfferClick = function() {
	this.bidOffer = true;
	$("#newbid_just_considering_btn",this.container).removeClass("cp_btn_dark").addClass("cp_btn_light");
	$("#newbid_offer_now_btn",this.container).removeClass("cp_btn_light").addClass("cp_btn_dark");

	$("#newbid_ppoints_div",this.container).show();
	$("#newbid_delivery_date_div",this.container).show();
}


CbtionBoxComplete.prototype.showCommentsClick = function() {
	$("#comments_box_container",this.container).toggle();
	var commentsBox = new CommentsBox($("#comments_box_container",this.container),{cbtionId: this.cbtion.id});
	commentsBox.updateData();
}

CbtionBoxComplete.prototype.promoteUpClick = function() {
	GLOBAL.serverComm.cbtionPromote(this.cbtion.id,true,this.cbtionReceivedCallback,this);
}

CbtionBoxComplete.prototype.promoteDownClick = function() {
	GLOBAL.serverComm.cbtionPromote(this.cbtion.id,false,this.cbtionReceivedCallback,this);
}

CbtionBoxComplete.prototype.bidNew = function (){
	var bidData = { 
		cbtionId:this.cbtion.id,
		description:$("#newbid_description_in",this.container).val(),
		creatorDto: GLOBAL.sessionData.userLogged
	}; 

	if($("#newbid_datepicker",this.container).val() == "") bidData.deliveryDate = 0;
	else bidData.deliveryDate = deliveryDate = Date.parse($("#newbid_datepicker",this.container).val());

	if($("#newbid_ppoints_in",this.container).val() == "") bidData.ppoints = 0;
	else bidData.ppoints = $("#newbid_ppoints_in",this.container).val();

	GLOBAL.serverComm.bidNew(bidData,this.bidOffer,this.newBidSavedCallback,this);
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
		
		/* specific to show two bids in a row */
		if(ix % 2 == 1) $("#bids_div",this.container).append($("<div class=bid_div_spacer></div>"));
		
		$("#bids_div",this.container).append($("<div class=bid_div id=bid_"+ix+"_div></div>"));
		
		var bidBox = new BidBox("#bid_"+ix+"_div",this.cbtion.bids[ix], this.cbtion.projectName);
		bidBox.draw();
		
	}
}
