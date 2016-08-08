$(document).ready(function() {
	
	GLOBAL = new Object();
	GLOBAL.projectPage = new ProjectPage("#content_pane");
	CopDocReadyCommon(GLOBAL.projectPage.init,GLOBAL.projectPage);
});

function ProjectPage(container_id) {
	// Parent constructor
	Page.call(this,container_id);
	
	this.project;
};

//Inheritance
ProjectPage.prototype = Page.prototype;

ProjectPage.prototype.init = function() {
	GLOBAL.projectPage.updateProject(JSP_projectName);
}

ProjectPage.prototype.updateProject = function(projectName) {
	GLOBAL.serverComm.projectGet(projectName,this.ProjectReceivedCallback,this);
}

ProjectPage.prototype.ProjectReceivedCallback = function(projectDto) {
	this.project = projectDto;
	this.drawProject();
}

ProjectPage.prototype.drawProject = function() {
	$("#name_div",this.container).append($("<p id=project_name>"+this.project.name+"</p>"));
	$("#description_div",this.container).append($("<p id=project_description>"+this.project.description+"</p>"));
	$("#cbtions_div",this.container).append(
			this.create_pap_str('project_cbtions','',this.project.nCbtions+' ',
			'contributions', 'CbtionListPage.action?projectFilter=' + this.project.name,
			''));
	
	(div_type,id,textprev,texthref,href,textafter) 
	
}