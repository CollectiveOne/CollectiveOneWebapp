function CbtionBoxComplete(container_id,cbtionData) {
	// Parent constructor
	this.container = $(container_id);
	this.cbtion = cbtionData;
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
	
	$("#product_div",this.container).append("<p>"+this.cbtion.product+"</p>");
	
	if(this.cbtion.parentGoalsTags) {
		for(var ix in this.cbtion.parentGoalsTags) {
			var this_parent = this.cbtion.parentGoalsTags[ix];
			$("#goal_div",this.container).append("<a href=../views/GoalPage.action?goalTag="+this_parent+">@"+this_parent+"</a> &#x2192 ");
		}
	}
	if(this.cbtion.goalTag)	$("#goal_div",this.container).append("<a href=../views/GoalPage.action?goalTag="+this.cbtion.goalTag+">@"+this.cbtion.goalTag+"</a>");
	
	$("#project_div",this.container).append("<a href=ProjectPage.action?projectName="+this.cbtion.projectName+">"+this.cbtion.projectName+"</a>");
	$("#creator_div",this.container).append("<a href=UserPage.action?username="+this.cbtion.creatorUsername+">"+this.cbtion.creatorUsername+"</a>");
	$("#creator_div",this.container).append("<p> "+getTimeStrSince(this.cbtion.creationDate)+" ago</p>");
	$("#state_div",this.container).append("<p>Current state: "+this.cbtion.state+"</p>");
	
	switch(this.cbtion.state) {
		case "PROPOSED":
			var openDec = new DecisionBoxSmall($("#status_desc_div",this.container),this.cbtion.openDec, GLOBAL.sessionData.userLogged);		
			openDec.draw();
			break;

		case "ACCEPTED":
			$("#status_desc_div",this.container).append("<p>contributed by "+
				"<a href=UserPage.action?username="+this.cbtion.contributorUsername+">"+this.cbtion.contributorUsername+"</a>"+
				" for "+this.cbtion.assignedPpoints+" pps</p>");

			$("#cbtion_div #status_desc_div p",this.container).css("font-size","10px");
			break;

	}
	
	
	$("#new_bid_btn",this.container).click(function (){
		if(GLOBAL.sessionData.userLogged) {
			$("#new_bid_form_container",this.container).toggle();
			$("#newbid_username_div",this.container).html(("<p>"+GLOBAL.sessionData.userLogged.username+"</p>"));
		} else {
			$("#new_bid_div",this.container).hide();
			showOutput("plase login to bid to this contribution","DarkRed");
		}
	});

	$("#newbid_datepicker",this.container).datepicker();
	$("#newbid_submit_div",this.container).click(this.bidNew.bind(this));
	
	if(this.cbtion.ncomments > 0) {
		$("#show_comments_btn",this.container).append("<p>show comments ("+this.cbtion.ncomments+")</p>")
	} else {
		$("#show_comments_btn",this.container).append("<p>show comments</p>")
	}
	
	$("#show_comments_btn",this.container).click(this.showCommentsClick.bind(this));

	this.updateBids();
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
	
	if($("#newbid_ppoints_in",this.container).attr('value') != "") {
		if($("#newbid_description_in",this.container).attr('value') != "") {
			if($("#newbid_datepicker",this.container).attr('value') != "") {
				var bidData = { 
					cbtionId:this.cbtion.id,
					ppoints:$("#newbid_ppoints_in",this.container).attr('value'),
					description:$("#newbid_description_in",this.container).attr('value'),
					deliveryDate:Date.parse($("#newbid_datepicker",this.container).attr('value')),
					creatorDto: GLOBAL.sessionData.userLogged
				}; 

				GLOBAL.serverComm.bidNew(bidData,this.newBidSavedCallback,this);
			} else {
				showOutput("please select a delivery date","darkred");
			}
		} else {
			showOutput("please add a description","darkred");
		} 
	} else {
		showOutput("please chose yout bid price in pps","darkred");
	}

	
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
