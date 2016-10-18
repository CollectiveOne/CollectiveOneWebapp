function Page(container_id) {
	this.container = $(container_id);
	this.projectsRecivedCallBackFun = null;
	this.projectsRecivedCallBackObj = null;
};

Page.prototype.fillProjectSelector = function(container,callBackFunc, callBackObj) {
	
	container.append($("<select class=select>"));
	
	this.projectsRecivedCallBackFun = callBackFunc;
	this.projectsRecivedCallBackObj = callBackObj;
	
	var activeProjects = GLOBAL.sessionData.activeProjectsController.getActiveProjectsNames();
	if(activeProjects.length > 0) {
		this.projectListReceivedCallback(activeProjects)
	} else {
		GLOBAL.serverComm.projectListGet(this.projectListReceivedCallback,this);	
	}
	
}

Page.prototype.projectListReceivedCallback = function(projectList) {
	$("#project_select").empty();
	
	for(var ix in projectList) {
		$("#project_select").append($("<option value="+projectList[ix]+">"+projectList[ix]+"</option>"));
	} 

	this.projectsRecivedCallBackFun.call(this.projectsRecivedCallBackObj);

}



