function CbtionBox(container_id,cbtionData) {
	// Parent constructor
	this.container = $(container_id);
	this.cbtion = cbtionData;
	this.init();
};

//Inheritance
CbtionBox.prototype.init = function() {
}

CbtionBox.prototype.updateDecision = function() {
	GLOBAL.serverComm.cbtionGet(this.decision.id,this.cbtionReceivedCallback,this);
}

CbtionBox.prototype.cbtionReceivedCallback = function(cbtionDto) {
	this.cbtion = cbtionDto;
}

CbtionBox.prototype.draw = function() {
	this.container.load("../elements/CbtionBox/CbtionBox.html",this.CbtionBoxLoaded.bind(this));
}

CbtionBox.prototype.CbtionBoxLoaded = function() {
	
	$(".cbtion_div #promotion_center_div",this.container).append(this.cbtion.relevance);
	$(".cbtion_div #promotion_up_div",this.container).click(this.promoteUpClick.bind(this));
	$(".cbtion_div #promotion_down_div",this.container).click(this.promoteDownClick.bind(this));

	$(".cbtion_div #title_div",this.container).append("<a href=CbtionPage.action?cbtionId="+ this.cbtion.id+">"+this.cbtion.title+"</a>");
	$(".cbtion_div #description_div",this.container).append("<p>"+this.cbtion.description+"</p>");
	$(".cbtion_div #product_div",this.container).append("<p>"+this.cbtion.product+"</p>");
	
	$(".cbtion_div #project_div",this.container).append("<a href=ProjectPage.action?projectName="+this.cbtion.projectName+">"+this.cbtion.projectName+"</a>");
	$(".cbtion_div #creator_div",this.container).append("<a href=UserPage.action?username="+this.cbtion.creatorUsername+">"+this.cbtion.creatorUsername+"</a>");
	$(".cbtion_div #creator_div",this.container).append("<p> "+getTimeStrSince(this.cbtion.creationDate)+" ago</p>");
	$(".cbtion_div #state_div",this.container).append("<p>"+this.cbtion.state+"</p>");
	
	switch(this.cbtion.state) {
		case "PROPOSED":
			var openDec = new DecisionBoxSmall($("#status_desc_div",this.container),this.cbtion.openDec, GLOBAL.sessionData.userLogged);		
			openDec.draw();
			break;

		case "ACCEPTED":
			$(".cbtion_div #status_desc_div",this.container).append("<p>contributed by "+
				"<a href=UserPage.action?username="+this.cbtion.contributorUsername+">"+this.cbtion.contributorUsername+"</a>"+
				" for "+this.cbtion.assignedPpoints+" pps</p>");

			$(".cbtion_div #status_desc_div p",this.container).css("font-size","10px");
			break;

	}
}

CbtionBox.prototype.promoteUpClick = function() {
	GLOBAL.serverComm.cbtionPromote(this.cbtion.id,true,this.cbtionReceivedCallback,this);
}

CbtionBox.prototype.promoteDownClick = function() {
	GLOBAL.serverComm.cbtionPromote(this.cbtion.id,false,this.cbtionReceivedCallback,this);
}
