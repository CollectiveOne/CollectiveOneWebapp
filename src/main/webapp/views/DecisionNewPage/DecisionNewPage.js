$(document).ready(function() {

	GLOBAL = new Object();
	GLOBAL.decisionNewPage = new DecisionNewPage("#content_pane");
	CopDocReadyCommon(GLOBAL.decisionNewPage.init,GLOBAL.decisionNewPage);
	
});

function DecisionNewPage(container_id) {
	// Parent constructor
	Page.call(this,container_id);
};

// Inheritance
DecisionNewPage.prototype = Page.prototype;

DecisionNewPage.prototype.init = function() {
	this.draw();
}
DecisionNewPage.prototype.draw = function() {

	if (GLOBAL.sessionData.userLogged) {
		this.container.empty();

		this.append_project_selector('project_select');
		this.append_text_area('decisionDescription', 'Description', '');
		
		// append create button
		this.container.append($('<button id="create_btn">Create</button>'));

		$('#create_btn').click(this.decisionNew.bind(this));

	} else {
		this.container.empty();
		this.container
				.append($("<p>please login to be able to create a new decision</p>"));
	}
}

DecisionNewPage.prototype.decisionNew = function() {
	var data = {
		creatorUsername: GLOBAL.sessionData.userLogged.username, 
		projectName: $("#project_select", this.container).val(),
		description: $("#decisionDescription_in", this.container).val(),
	};
	
	GLOBAL.serverComm.decisionNew(data,this.decisionNewCallback,this);
}

DecisionNewPage.prototype.decisionNewCallback = function(goalDto) {
	window.location = 'DecisionListPage.action';
}
