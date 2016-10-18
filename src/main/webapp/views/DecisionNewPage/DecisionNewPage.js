$(document).ready(function() {

	GLOBAL = new Object();
	GLOBAL.decisionNewPage = new DecisionNewPage("#content_pane");
	CopDocReadyCommon(GLOBAL.decisionNewPage.draw,GLOBAL.decisionNewPage);
	
	GLOBAL.decisionNewPage.init();
});

function DecisionNewPage(container_id) {
	// Parent constructor
	Page.call(this,container_id);
};

// Inheritance
DecisionNewPage.prototype = Page.prototype;

DecisionNewPage.prototype.init = function() {
	$('#create_btn').click(this.decisionNew.bind(this));
}

DecisionNewPage.prototype.draw = function() {

	if (GLOBAL.sessionData.userLogged) {
		this.fillProjectSelector($("#content_pane #project_select"),this.projectSelectorDrawn,this);
	} else {
		this.container.empty();
		this.container
				.append($("<p>please login to be able to create a new decision</p>"));
	}
}

DecisionNewPage.prototype.projectSelectorDrawn = function() {
	// nop
}

DecisionNewPage.prototype.decisionNew = function() {
	var data = {
		creatorUsername: GLOBAL.sessionData.userLogged.username, 
		projectName: $("#project_select", this.container).val(),
		description: $("#decisionDescription_in", this.container).val(),
	};
	
	GLOBAL.serverComm.decisionNew(data,this.decisionNewCallback,this);
}

DecisionNewPage.prototype.decisionNewCallback = function(data) {
	window.location = 'DecisionPage.action?decisionId='+data.decisionDto.id;
}
