ProjectSelector = function(container_id,drawnCallBackFunc, callBackObj, updatedCallBackFunc) {
	this.container = $(container_id);
	this.projectsReceivedCallBackFun = drawnCallBackFunc;
	this.projectsReceivedCallBackObj = callBackObj;

	if(updatedCallBackFunc) {
		this.container.on('change', updatedCallBackFunc.bind(callBackObj));	
	}
}

ProjectSelector.prototype.fill = function() {
	
	var activeProjects = GLOBAL.sessionData.activeProjectsController.getActiveProjectsNames();
	if(activeProjects.length > 0) {
		this.projectListReceivedCallback(activeProjects)
	} else {
		GLOBAL.serverComm.projectListGet(this.projectListReceivedCallback,this);	
	}
	
}

ProjectSelector.prototype.projectListReceivedCallback = function(projectList) {
	this.container.empty();
	
	for(var ix in projectList) {
		this.container.append($("<option value="+projectList[ix]+">"+projectList[ix]+"</option>"));
	} 

	this.projectsReceivedCallBackFun.call(this.projectsReceivedCallBackObj);

}