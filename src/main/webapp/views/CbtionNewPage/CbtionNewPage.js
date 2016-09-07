$(document).ready(function() {

	GLOBAL = new Object();
	GLOBAL.cbtionNewPage = new CbtionNewPage("#content_pane");
	CopDocReadyCommon(GLOBAL.cbtionNewPage.init,GLOBAL.cbtionNewPage);
	
});

function CbtionNewPage(container_id) {
	// Parent constructor
	Page.call(this,container_id);
};

// Inheritance
CbtionNewPage.prototype = Page.prototype;

CbtionNewPage.prototype.init = function() {
	this.draw();
}
CbtionNewPage.prototype.draw = function() {

	if (GLOBAL.sessionData.userLogged) {
		this.container.empty();

		this.append_project_selector('project_select',this.projectSelectorDrawn,this);
		this.append_input('title', 'Title', '');
		this.append_text_area('description', 'Description','');
		this.append_text_area('product', 'Product','');
		
		this.container.append(	"<div class=field id=goalTag_div>"+
									"<p class=field_name_p>Goal</p>"+
									"<input type=text class=field_in id=goalTag_selector>"+
								"</div>");
		
		// append create button
		this.container.append($('<button id="create_btn">Create</button>'));

		$('#create_btn').click(this.cbtionNew.bind(this));
		$('#project_select').on('change', this.projectSelectorUpdated.bind(this));

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
