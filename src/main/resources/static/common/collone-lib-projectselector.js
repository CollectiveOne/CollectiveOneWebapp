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
		/* if one or more projects is selected as active */
		this.projectListReceivedCallback(activeProjects)
	} else {
		if(isUserLogged()) {
			/* if user is logged, use active projects */
			this.projectListReceivedCallback(GLOBAL.sessionData.activeProjectsController.getPossibleActiveProjectsNames());
		} else {
			GLOBAL.serverComm.projectNamesListGet(this.projectListReceivedCallback,this);
		}
	}
}
ProjectSelector.prototype.projectListReceivedCallback = function(projectList) {
	this.container.empty();
	
	for(var ix in projectList) {
		this.container.append($("<option value="+projectList[ix]+">"+projectList[ix]+"</option>"));
	} 

	/* automatically select if GLOBAL set (this allows for server to choose selected project)*/
	if(GLOBAL.projectSelected) {
		this.container.val(GLOBAL.projectSelected);
	}		

	this.projectsReceivedCallBackFun.call(this.projectsReceivedCallBackObj);

}