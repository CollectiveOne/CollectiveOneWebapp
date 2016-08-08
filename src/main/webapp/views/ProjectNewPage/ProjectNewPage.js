$(document).ready(function() {

	GLOBAL = new Object();
	GLOBAL.projectNewPage = new ProjectNewPage("#content_pane");
	
	CopDocReadyCommon(GLOBAL.projectNewPage.init,GLOBAL.projectNewPage);
	
});

function ProjectNewPage(container_id) {
	// Parent constructor
	Page.call(this,container_id);
};

// Inheritance
ProjectNewPage.prototype = Page.prototype;

ProjectNewPage.prototype.init = function() {
	this.data = {
			creatorUsername: [], 
			name: [],
			description: []
	};
	
	this.draw();
}
ProjectNewPage.prototype.draw = function() {

	if (GLOBAL.sessionData.userLogged) {
		this.container.empty();

		this.append_input('name', 'Name', '');
		this.append_text_area('description', 'Description','');

		// append create button
		this.container.append($('<button id="create_btn">Create</button>'));

		$('#create_btn').click(this.ProjectNew.bind(this));

	} else {
		this.container.empty();
		this.container
				.append($("<p>please login to be able to create a new project</p>"));
	}
}

ProjectNewPage.prototype.ProjectNew = function() {
	this.data.creatorUsername = GLOBAL.sessionData.userLogged.username;
	this.data.name = $("#name_in", this.container).val();
	this.data.description = $("#description_in", this.container).val();
	
	GLOBAL.serverComm.projectNew(this.data,this.projectNewCallback,this);
}

ProjectNewPage.prototype.projectNewCallback = function(projectDto) {
	window.location = 'ProjectPage.action?projectName='+this.data.name;
}
