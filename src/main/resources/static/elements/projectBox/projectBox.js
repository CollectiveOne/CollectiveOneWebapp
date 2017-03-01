function ProjectBox(container_id,projectData,projectName) {
	// Parent constructor
	this.container = $(container_id);
	this.project = projectData;
	this.projectName = projectName;
	this.data = [];
};

//Inheritance
ProjectBox.prototype = {
		updateProject: function() {
			GLOBAL.serverComm.projectGet(this.project.projectName,this.projectReceivedCallback,this);
		} ,

		projectReceivedCallback: function(projectDto) {
			this.project = projectDto;
			this.draw();
		},

		draw: function() {
			this.container.load("/elements/projectBox/projectBox.html",this.projectBoxLoaded.bind(this));
		},

		projectBoxLoaded: function() {
			$("#projectname_div",this.container).html(getProjectLink(this.project.name));	
			$("#description_div",this.container).html(markdown.toHTML(this.project.description));
		}
	
}