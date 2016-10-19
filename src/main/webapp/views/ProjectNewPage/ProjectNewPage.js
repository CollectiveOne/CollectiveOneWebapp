$(document).ready(function() {

	GLOBAL = new Object();
	GLOBAL.projectNewPage = new ProjectNewPage("#content_pane");
	
	CopDocReadyCommon(GLOBAL.projectNewPage.draw,GLOBAL.projectNewPage);
	
	GLOBAL.projectNewPage.init();
	
});

function ProjectNewPage(container_id) {
	// Parent constructor
	Page.call(this,container_id);
};

// Inheritance
ProjectNewPage.prototype = Page.prototype;

ProjectNewPage.prototype.init = function() {
	/* assign events here otherwise they are reasigned when the page is redrawn */
	$('#create_btn').click(this.projectNew.bind(this));
}

ProjectNewPage.prototype.draw = function() {

	if (GLOBAL.sessionData.userLogged) {
		
	} else {
		this.container.empty();
		this.container
				.append($("<p>please login to be able to create a new project</p>"));
	}
}

ProjectNewPage.prototype.projectNew = function() {
	this.data = {
			creatorUsername : GLOBAL.sessionData.userLogged.username,
			name : $("#name_in", this.container).val(),
			description : $("#description_in", this.container).val()
	}
	
	GLOBAL.serverComm.projectNew(this.data,this.projectNewCallback,this);
}

ProjectNewPage.prototype.projectNewCallback = function(projectDto) {
	window.location = 'ProjectPage.action?projectName='+this.data.name;
}
