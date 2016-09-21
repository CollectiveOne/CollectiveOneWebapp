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
}

