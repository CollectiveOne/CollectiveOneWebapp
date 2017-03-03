function ActiveProjectsController(containerId,callbackFun,callbackObj) {
	this.container = $(containerId);
	this.callbackFun = callbackFun;
	this.callbackObj = callbackObj;

	this.updateData();
};

ActiveProjectsController.prototype = {
		
	updateData: function() {
		/* first get the user logged */
		GLOBAL.serverComm.activeProjectsGet(this.activeProjectsGetCallback,this);
	},
	
	activeProjectsGetCallback: function(activeProjects) {
		this.activeProjects = activeProjects;
		this.draw();
		
		/* at this point the active projects are ready, so call the page init */
		if(this.callbackFun) this.callbackFun.call(this.callbackObj);
	},
	
	draw: function() {
		$("#active_projects_list",this.container).empty();

		for(var ix in this.activeProjects) {
			var activeProject = this.activeProjects[ix];
			var activeProjectBtnId = activeProject.projectName+"_active";
			
			$("#active_projects_list",this.container)
				.append("<label class='label' id="+
						activeProjectBtnId+" project_name="+activeProject.projectName+">"+
						activeProject.projectName+"</button>")

			$("#"+activeProjectBtnId,this.container).click(this.activeProjectClicked);

			if(activeProject.active) {
				$("#"+activeProjectBtnId,this.container).addClass("label-success");
			} else {
				$("#"+activeProjectBtnId,this.container).addClass("label-info");
			}
		}
	},

	activeProjectClicked: function() {
		/* I wasnt able to know which button was clicked and at the same time kept alive the "this"" object */
		var clickedProject = $(this).attr("project_name")
		var ixActiveProject;
		var activeProjects = GLOBAL.sessionData.activeProjectsController.activeProjects;

		/* loof at the active project list in session and find the clicked project */
		for(var ix in activeProjects) {
			if(activeProjects[ix].projectName == clickedProject) {
				ixActiveProject = ix;
				break;
			}
		}	

		/* change active session project status */
		if(activeProjects[ixActiveProject].active) {
			GLOBAL.serverComm.activeProjectChange(clickedProject,false,
				GLOBAL.sessionData.activeProjectsController.changeProjectCallback,
				GLOBAL.sessionData.activeProjectsController);

		} else {
			GLOBAL.serverComm.activeProjectChange(clickedProject,true,
				GLOBAL.sessionData.activeProjectsController.changeProjectCallback,
				GLOBAL.sessionData.activeProjectsController);
		}
	},

	changeProjectCallback: function(activeProjects) {	
		this.activeProjectsGetCallback(activeProjects);
	},
	
	getActiveProjectsNames: function() {
		/* helper function to return the list of current active projects */
		var activeProjectsNames = [];
		var activeProjects = GLOBAL.sessionData.activeProjectsController.activeProjects;

		for(var ix in activeProjects) {
			if(activeProjects[ix].active) {
				activeProjectsNames.push(activeProjects[ix].projectName);
			}
		}	

		return activeProjectsNames;
	}
}
