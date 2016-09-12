$(document).ready(function() {
	GLOBAL = new Object();
	GLOBAL.cbtionPage = new CbtionPage("#content_pane");
	CopDocReadyCommon(GLOBAL.cbtionPage.init,GLOBAL.cbtionPage);
});

function CbtionPage(container_id) {
};

//Inheritance
CbtionPage.prototype = Page.prototype;

CbtionPage.prototype.init = function() {
	GLOBAL.serverComm.cbtionGet(JSP_cbtionId,this.cbtionReceivedCallback,this);
}

CbtionPage.prototype.cbtionReceivedCallback = function(cbtionDto) {
	var cbtionBoxComplete = new CbtionBoxComplete("#cbtion_container",cbtionDto);
	cbtionBoxComplete.draw();
}

