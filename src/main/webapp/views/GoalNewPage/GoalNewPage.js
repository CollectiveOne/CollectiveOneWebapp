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

		this.append_project_selector('project_select');
		this.append_input('goalTag', 'Goal Tag', '');
		this.append_text_area('description', 'Description','');
		
		// append create button
		this.container.append($('<button id="create_btn">Create</button>'));

		$('#create_btn').click(this.goalNew.bind(this));

	} else {
		this.container.empty();
		this.container
				.append($("<p>please login to be able to create a new goal</p>"));
	}
}

GoalNewPage.prototype.goalNew = function() {
	var data = {
		creatorUsername: GLOBAL.sessionData.userLogged.username, 
		projectName: $("#project_select", this.container).val(),
		goalTag: $("#goalTag_in", this.container).val(),
		description: $("#description_in", this.container).val()
	};
	
	GLOBAL.serverComm.goalNew(data,this.goalNewCallback,this);
}

GoalNewPage.prototype.goalNewCallback = function(goalDto) {
	window.location = 'GoalListPage.action';
}
