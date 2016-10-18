$(document).ready(function() {

	GLOBAL = new Object();
	GLOBAL.goalNewPage = new GoalNewPage("#content_pane");
	CopDocReadyCommon(GLOBAL.goalNewPage.draw,GLOBAL.goalNewPage);

	GLOBAL.goalNewPage.init();
	
});

function GoalNewPage(container_id) {
	// Parent constructor
	Page.call(this,container_id);
};

// Inheritance
GoalNewPage.prototype = Page.prototype;

GoalNewPage.prototype.init = function() {
	$('#create_btn').click(this.goalNew.bind(this));
	$('#project_select').on('change', this.projectSelectorUpdated.bind(this));
}

GoalNewPage.prototype.draw = function() {

	if (GLOBAL.sessionData.userLogged) {
		this.fillProjectSelector($("#content_pane #project_select"),this.projectSelectorDrawn,this);
	} else {
		this.container.empty();
		this.container
				.append($("<p>please login to be able to create a new goal</p>"));
	}
}

GoalNewPage.prototype.projectSelectorUpdated = function() {
	$('#superGoalTag_selector',this.container).autocomplete().clear();
	$('#superGoalTag_selector',this.container).autocomplete().setOptions({params: {projectName: $("#project_select", this.container).val()}});
}

GoalNewPage.prototype.projectSelectorDrawn = function() {
	$('#superGoalTag_selector',this.container).autocomplete({
		serviceUrl: '../json/GoalGetSuggestions.action',
		params: {projectName: $("#project_select", this.container).val()}
	});
}

GoalNewPage.prototype.goalNew = function() {
	var data = {
		creatorUsername: GLOBAL.sessionData.userLogged.username, 
		projectName: $("#project_select", this.container).val(),
		parentGoalTag: $("#superGoalTag_div #superGoalTag_selector", this.container).val(),
		goalTag: $("#goalTag_in", this.container).val(),
		description: $("#description_in", this.container).val()
	};
	
	GLOBAL.serverComm.goalNew(data,this.goalNewCallback,this);
}

GoalNewPage.prototype.goalNewCallback = function(data) {
	window.location = "../views/GoalPage.action?projectName="+data.goalDto.projectName+"&goalTag="+data.goalDto.goalTag;
}
