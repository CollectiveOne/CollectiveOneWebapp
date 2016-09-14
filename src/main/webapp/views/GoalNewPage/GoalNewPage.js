$(document).ready(function() {

	GLOBAL = new Object();
	GLOBAL.goalNewPage = new GoalNewPage("#content_pane");
	CopDocReadyCommon(GLOBAL.goalNewPage.init,GLOBAL.goalNewPage);
	
});

function GoalNewPage(container_id) {
	// Parent constructor
	Page.call(this,container_id);
};

// Inheritance
GoalNewPage.prototype = Page.prototype;

GoalNewPage.prototype.init = function() {
	this.draw();
}
GoalNewPage.prototype.draw = function() {

	if (GLOBAL.sessionData.userLogged) {
		this.container.empty();

		this.append_project_selector('project_select',this.projectSelectorDrawn,this);
		this.container.append(	"<div class=field id=superGoalTag_div>"+
				"<p class=field_name_p>Parent Goal</p>"+
				"<input type=text class=field_in id=superGoalTag_selector>"+
			"</div>");
		this.append_input('goalTag', 'New Goal Tag', '');
		this.append_text_area('description', 'Description','');
		
		
		
		$('#superGoalTag_selector',this.container).autocomplete({
			serviceUrl: '../json/GoalGetSuggestions.action'
		});
		
		// append create button
		this.container.append($('<button id="create_btn">Create</button>'));

		$('#create_btn').click(this.goalNew.bind(this));
		$('#project_select').on('change', this.projectSelectorUpdated.bind(this));

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
		parentGoalsTags: [$("#superGoalTag_div #superGoalTag_selector", this.container).val()],
		goalTag: $("#goalTag_in", this.container).val(),
		description: $("#description_in", this.container).val()
	};
	
	GLOBAL.serverComm.goalNew(data,this.goalNewCallback,this);
}

GoalNewPage.prototype.goalNewCallback = function(goalDto) {
	window.location = 'GoalListPage.action';
}
