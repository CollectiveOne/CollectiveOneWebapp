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
	
	activeProjectsGetCallback: function(data) {
		this.activeProjects = data.activeProjects;
		this.draw();
		
		/* at this point the active projects are ready, so call the page init */
		this.callbackFun.call(this.callbackObj);
	},
	
	draw: function() {
		$(this.container).empty();

		for(var ix in this.activeProjects) {
			var activeProject = this.activeProjects[ix];
			var activeProjectBtnId = activeProject.projectName+"_active";
			
			$(this.container)
				.append("<div id="+activeProjectBtnId+">"+activeProject.projectName+"</div>")

			$("#"+activeProjectBtnId,this.container).click(this.activeProjectClicked);

			$("#"+activeProjectBtnId,this.container).addClass("cp_btn_light");
			if(activeProject.active) {
				$("#"+activeProjectBtnId,this.container).addClass("active_project_btn_selected");
			} else {
				$("#"+activeProjectBtnId,this.container).addClass("active_project_btn");
			}
		}
	},

	activeProjectClicked: function() {
		/* I wasnt able to know which button was clicked and at the same time kept alive the "this"" object */
		var clickedProject = this.innerText;
		var ixActiveProject;
		var activeProjects = GLOBAL.sessionData.activeProjectsController.activeProjects;

		for(var ix in activeProjects) {
			if(activeProjects[ix].projectName == clickedProject) {
				ixActiveProject = ix;
				break;
			}
		}	

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

	getActiveProjectsNames: function() {
		
		var activeProjectsNames = [];
		var activeProjects = GLOBAL.sessionData.activeProjectsController.activeProjects;

		for(var ix in activeProjects) {
			if(activeProjects[ix].active) {
				activeProjectsNames.push(activeProjects[ix].projectName);
			}
		}	

		return activeProjectsNames;
	},

	changeProjectCallback: function() {	
		this.updateData();
	}
}
