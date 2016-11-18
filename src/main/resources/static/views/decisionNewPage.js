$(document).ready(function() {

	GLOBAL.goalNewPage = new GoalNewPage("#content_pane");
	docReadyCommon(GLOBAL.goalNewPage.init,GLOBAL.goalNewPage,true);
	
});

function GoalNewPage(container_id) {
	this.container = $(container_id);
};

GoalNewPage.prototype = {
		
		init: function() {
			/* Link the project selector with the goalTag autocomplete so that the suggested goals are coherent with the project selected */
			this.projectSelector = new ProjectSelector($("#project_select",this.container),this.projectSelectorDrawn,this,this.projectSelectorUpdated);
			this.projectSelector.fill();
		},

}
