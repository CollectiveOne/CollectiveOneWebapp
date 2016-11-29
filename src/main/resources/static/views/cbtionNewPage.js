$(document).ready(function() {

	GLOBAL.cbtionNewPage = new CbtionNewPage("#content_pane");
	docReadyCommon(GLOBAL.cbtionNewPage.init,GLOBAL.cbtionNewPage,true);
	
});

function CbtionNewPage(container_id) {
	this.container = $(container_id);
};

CbtionNewPage.prototype = {
		
		init: function() {
			/* Link the project selector with the goalTag autocomplete so that the suggested goals are coherent with the project selected */
			this.projectSelector = new ProjectSelector($("#project_select",this.container),this.projectSelectorDrawn,this,this.projectSelectorUpdated);
			this.projectSelector.fill();
		},
		
		projectSelectorUpdated: function() {
			$('#goalTag_selector',this.container).autocomplete().clear();
			$('#goalTag_selector',this.container).autocomplete().setOptions({params: {projectName: $("#project_select", this.container).val()}});
		},
		
		projectSelectorDrawn: function() {
			$('#goalTag_selector',this.container).autocomplete({
				serviceUrl: '/rest/goals/getSuggestions',
				minChars: 0,
				maxHeight: 200,
				params: {projectName: $("#project_select", this.container).val()}
			});
		},

}
