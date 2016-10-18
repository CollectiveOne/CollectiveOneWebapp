$(document).ready(function() {

	GLOBAL = new Object();
	GLOBAL.cbtionNewPage = new CbtionNewPage("#content_pane");
	CopDocReadyCommon(GLOBAL.cbtionNewPage.draw,GLOBAL.cbtionNewPage);
	
	GLOBAL.cbtionNewPage.init();
});

function CbtionNewPage(container_id) {
	// Parent constructor
	Page.call(this,container_id);
};

// Inheritance
CbtionNewPage.prototype = Page.prototype;

CbtionNewPage.prototype.init = function() {
	/* assign events here otherwise they are reasigned when the page is redrawn */
	$('#create_btn').click(this.cbtionNew.bind(this));
	$('#project_select').on('change', this.projectSelectorUpdated.bind(this));	
}

CbtionNewPage.prototype.draw = function() {

	if (GLOBAL.sessionData.userLogged) {
		this.fillProjectSelector($("#content_pane #project_select"),this.projectSelectorDrawn,this);

	} else {
		this.container.empty();
		this.container
				.append($("<p>please login to be able to create a new contribution</p>"));
	}
}

CbtionNewPage.prototype.projectSelectorUpdated = function() {
	$('#goalTag_selector',this.container).autocomplete().clear();
	$('#goalTag_selector',this.container).autocomplete().setOptions({params: {projectName: $("#project_select", this.container).val()}});
}

CbtionNewPage.prototype.projectSelectorDrawn = function() {
	$('#goalTag_selector',this.container).autocomplete({
		serviceUrl: '../json/GoalGetSuggestions.action',
		params: {projectName: $("#project_select", this.container).val()}
	});
}

CbtionNewPage.prototype.cbtionNew = function() {
	var data = {
		creatorUsername: GLOBAL.sessionData.userLogged.username, 
		projectName: $("#project_select", this.container).val(),
		title: $("#title_in", this.container).val(),
		description: $("#description_in", this.container).val(),
		product: $("#product_in", this.container).val(),
		goalTag: $("#goalTag_div #goalTag_selector", this.container).val()
	};
	
	GLOBAL.serverComm.cbtionNew(data,this.cbtionNewCallback,this);
}

CbtionNewPage.prototype.cbtionNewCallback = function(cbtionDto) {
	window.location = 'CbtionPage.action?cbtionId='+cbtionDto.id;
}
