function ActivityBox(container_id,activityData) {
	// Parent constructor
	this.container = $(container_id);
	this.activity = activityData;
};

ActivityBox.prototype.draw = function() {
	this.container.load("/elements/activityBox/activityBox.html",this.activityBoxLoaded.bind(this));
}

ActivityBox.prototype.activityBoxLoaded = function() {
	$("#activity_div",this.container).append("In "+getProjectLink(this.activity.projectName)+" "+getTimeStrSince(this.activity.creationDate)+" ago - "+this.activity.eventPretty);
}

