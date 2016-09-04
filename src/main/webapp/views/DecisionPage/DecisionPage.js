$(document).ready(function() {
	
	GLOBAL = new Object();
	GLOBAL.decisionPage = new DecisionPage("#content_pane");
	CopDocReadyCommon(GLOBAL.decisionPage.init,GLOBAL.decisionPage);
});

function DecisionPage(container_id) {
	// Parent constructor
	Page.call(this,container_id);
	
	this.decision;
};

//Inheritance
DecisionPage.prototype = Page.prototype;

DecisionPage.prototype.init = function() {
	GLOBAL.decisionPage.updateDecision(JSP_decisionId);
}

DecisionPage.prototype.updateDecision = function(decisionId) {
	GLOBAL.serverComm.decisionGet(decisionId,this.decisionReceivedCallback,this);
}

DecisionPage.prototype.decisionReceivedCallback = function(decisionDto) {
	this.decision = decisionDto;
	var decisionBox = new DecisionBox($("#decision_container"),this.decision);
	decisionBox.draw();
}

