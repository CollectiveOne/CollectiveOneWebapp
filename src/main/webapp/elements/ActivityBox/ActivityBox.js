function ActivityBox(container_id,activityData) {
	// Parent constructor
	this.container = $(container_id);
	this.activity = activityData;
};

ActivityBox.prototype.draw = function() {
	this.container.load("../elements/ActivityBox/ActivityBox.html",this.activityBoxLoaded.bind(this));
}

ActivityBox.prototype.activityBoxLoaded = function() {
	var eventPretty = [];

	switch(this.activity.type) {
		case "CBTION":
			eventPretty = "contribution <a href=CbtionPage.action?cbtionId="+this.activity.cbtionDto.id+">"+this.activity.cbtionDto.title+"</a> was "+this.activity.event;
			break;

		case "BID":
			switch(this.activity.event) {
				case "created":
					eventPretty = "<a href=UserPage.action?username="+this.activity.bidDto.creatorDto.username+">"+this.activity.bidDto.creatorDto.username+"</a>"+
						" made a new bid on <a href=CbtionPage.action?cbtionId="+this.activity.bidDto.cbtionId+">"+this.activity.bidDto.cbtionTitle+"</a>";
					break;

				case "assigned":
				case "accepted":
					eventPretty = "bid from <a href=UserPage.action?username="+this.activity.bidDto.creatorDto.username+">"+this.activity.bidDto.creatorDto.username+"</a>"+
						" to <a href=CbtionPage.action?cbtionId="+this.activity.bidDto.cbtionId+">"+this.activity.bidDto.cbtionTitle+"</a> was "+this.activity.event;
					break;
			}
			
			break;

		case "GOAL":
			var goalLinkStr = "<a href=../views/GoalPage.action?goalTag="+this.activity.goalDto.goalTag+">"+this.activity.goalDto.goalTag+"</a>"
			switch(this.activity.event) {
				case "proposed":
					eventPretty = "goal "+goalLinkStr+" was "+this.activity.event+" by <a href=UserPage.action?username="+this.activity.goalDto.creatorUsername+">"+this.activity.goalDto.creatorUsername+"</a>";
					break;
				default:
					eventPretty = "goal "+goalLinkStr+" - "+this.activity.event;
					break;
			}
			break;

		case "DECISION":
			switch(this.activity.event) {
				case "created":	
					eventPretty = "decision <a href=DecisionPage?decisionId="+this.activity.decisionDto.id+">"+this.activity.decisionDto.description+"</a> was "+this.activity.event+" by <a href=UserPage.action?username="+this.activity.decisionDto.creatorUsername+">"+this.activity.decisionDto.creatorUsername+"</a>";
					break;

				case "accepted":
				case "rejected":
					eventPretty = "decision <a href=DecisionPage?decisionId="+this.activity.decisionDto.id+">"+this.activity.decisionDto.description+"</a> was "+this.activity.event;
					break;
			}
			break;

		case "ARGUMENT":
			switch(this.activity.event) {
				case "created":	
						eventPretty = "<a href=UserPage.action?username="+this.activity.argumentDto.creatorUsername+">"+
						this.activity.argumentDto.creatorUsername+"</a> added an argument to decision '<a href=DecisionPage?decisionId="+
						this.activity.argumentDto.decisionId+">"+this.activity.argumentDto.decisionDescription+"</a>'";
						break;
			}
			break;
	}
	
	var projectLink = "<a href=ProjectPage.action?projectName="+this.activity.projectName+">"+this.activity.projectName+"</a>"; 
	$("#activity_div",this.container).append("<p>In "+projectLink+" "+getTimeStrSince(this.activity.creationDate)+" ago - "+eventPretty+"</p>");
	
}

