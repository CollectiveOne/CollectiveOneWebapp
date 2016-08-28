$(document).ready(function() {

	GLOBAL = new Object();
	GLOBAL.cbtionListPage = new CbtionListPage("#content_pane");
	CopDocReadyCommon(GLOBAL.cbtionListPage.init,GLOBAL.cbtionListPage);

});

function CbtionListPage(container_id) {

	// Parent constructor
	Page.call(this,container_id);

	// Array with Cbtion objects
	this.cbtions = {};
	this.resSet = {};

	this.filters = {
			keyw : undefined,
			issues_types : [],
			projects : [],
			creators : [],
			states: ["OPEN","ASSIGNED"],
			page : 1,
			nPerPage : 15
	};

	this.filters_expanded = false;

};

//Inheritance
CbtionListPage.prototype = Page.prototype;

CbtionListPage.prototype.init = function() {

	// Common after sessionUpdate code
	CopSessionReadyCommon();

	// Apply JQ effects
	$("#filter_btn", this.container).click(this.filterClick.bind(this));

	$("#next_page", this.container).click(this.nextPageClick.bind(this));
	$("#back_page", this.container).click(this.backPageClick.bind(this));

	if(JSP_projectFilter){
		this.filters.projects.push(JSP_projectFilter);
	}

	this.updateCbtionList();

}

CbtionListPage.prototype.nextPageClick = function() {
	this.filters.page = this.filters.page+1;

	if(this.resSet) {
		var page_max = Math.ceil(this.resSet[2]/this.filters.nPerPage);
		if(this.filters.page > page_max) {
			showOutput("no more data available","green");
			this.filters.page = page_max;
		}
	}

	this.updateCbtionList();
}

CbtionListPage.prototype.backPageClick = function() {
	this.filters.page = this.filters.page-1;
	if (this.filters.page < 1) this.filters.page = 1;
	this.updateCbtionList();
}

CbtionListPage.prototype.CbtionsReceivedCallback = function(cbtionDtos,resSet) {
	this.cbtions = cbtionDtos;
	this.resSet = resSet;
	this.drawCbtions();
}

CbtionListPage.prototype.updateCbtionList = function() {
	GLOBAL.serverComm.cbtionListGet(this.filters, this.CbtionsReceivedCallback,this);
}

CbtionListPage.prototype.filterClick = function() {

	if (!this.filters_expanded) {
		// if the filters are not expanded, expand and update the first time
		this.filters_expanded = true;
		$("#filter_contents", this.container).slideDown();
		$("#filter_bar_p", this.container).text("update results");

		// Featured Projects
		GLOBAL.serverComm.projectListGet(this.ProjectListReceivedCallback,this);
		
		// Cbtion state
		$("#filter_cbtion_state", this.container).empty();
		this.stateFilterDraw("OPEN")
		this.stateFilterDraw("ASSIGNED")
		this.stateFilterDraw("ACCEPTED")

	} else {
		// if filters are expanded, collapse and update the cbtions list
		this.filters_expanded = false;

		if(!JSP_projectFilter) {
			// JSP parameter override project filters
			this.filters.projects = [];
			
			var featuredprojects = $("#filter_featured_projects", this.container).find("input");
			var npf = featuredprojects.length;
			// JSP parameter override project filters
			for (var i = 0; i < npf; i++) {
				if ($(featuredprojects[i]).is(":checked")) {
					this.filters.projects.push($(featuredprojects[i]).attr(
					"project_name"));
				}
			}
			
			this.filters.states = [];
			
			var statefilter = $("#filter_cbtion_state", this.container).find("input");
			var nsf = statefilter.length;
			// JSP parameter override project filters
			for (var i = 0; i < nsf; i++) {
				if ($(statefilter[i]).is(":checked")) {
					this.filters.states.push($(statefilter[i]).attr(
					"state_id"));
				}
			}
		}
		
		$("#filter_contents", this.container).slideUp();
		$("#filter_bar_p", this.container).text("filter");
	}

	this.updateCbtionList();
}


CbtionListPage.prototype.ProjectListReceivedCallback = function(projectList) {
	np = projectList.length;

	$("#filter_featured_projects", this.container).empty();

	for (var i = 0; i < np; i++) {
		var pname = projectList[i];
		$("#filter_featured_projects", this.container).append(
				$("<input type='checkbox' id=" + pname
						+ "_input_fe project_name=" + pname + ">"
						+ pname + "</><br/>"));
		if (this.filters.projects.includes(pname)) {
			$("#" + pname + "_input_fe").attr('checked', true);
		}
	}
}

CbtionListPage.prototype.stateFilterDraw = function(state_name) {
	$("#filter_cbtion_state", this.container).append(
			$("<input type='checkbox' id="+state_name+
					"_input_fe state_id="+state_name+">"
					+state_name+"<br/>"));
	
	if (this.filters.states.includes(state_name)) {
		$("#" + state_name + "_input_fe").attr('checked', true);
	}

}

CbtionListPage.prototype.drawCbtions = function() {

	this.updateResSet();
	$("#list_of_elements", this.container).empty();

	for ( var ix in this.cbtions) {
		$("#list_of_elements", this.container).append(this.create_div_jQ("cbtionbox",ix));
		var cbtionBox = new CbtionBox(this.get_div_jQ("cbtionbox",ix),this.cbtions[ix]);
		cbtionBox.draw();
	}
}

CbtionListPage.prototype.updateResSet = function() {
	$("#page_set", this.container).html("<p>"+this.resSet[0]+"-"+this.resSet[1]+" of "+this.resSet[2]+"</p>");
}
