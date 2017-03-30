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
			
			$('#description_editor',this.container).markdown({
				autofocus:false,
				savable:false,
				hiddenButtons: ["cmdHeading", "cmdImage"],
				resize: "vertical"
			});
		},
		
		projectSelectorUpdated: function() {
			$('#goalTag_selector',this.container).autocomplete().clear();
			$('#goalTag_selector',this.container).autocomplete().setOptions({params: {projectName: $("#project_select", this.container).val()}});
		},
		
		projectSelectorDrawn: function() {
			$('#goalTag_selector',this.container).autocomplete({
				serviceUrl: '/1/goals/suggestions',
				minChars: 0,
				maxHeight: 200,
				params: {projectName: $("#project_select", this.container).val()}
			});
		},

}
