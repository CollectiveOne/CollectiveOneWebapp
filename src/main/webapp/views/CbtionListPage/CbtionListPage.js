$(document).ready(function() {

	GLOBAL = new Object();
	GLOBAL.cbtionListPage = new CbtionListPage("#content_pane");
	CopDocReadyCommon(GLOBAL.cbtionListPage.draw,GLOBAL.cbtionListPage);

});

function CbtionListPage(container_id) {

	// Parent constructor
	Page.call(this,container_id);

	// Array with Cbtion objects
	this.cbtionList = [];
};

//Inheritance
CbtionListPage.prototype = Page.prototype;

CbtionListPage.prototype.draw = function() {
	this.cbtionList = new CbtionList("#content_pane",{});
	 
}
