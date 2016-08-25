$(document).ready(function() {

	GLOBAL = new Object();
	GLOBAL.goalListPage = new GoalListPage("#content_pane");
	CopDocReadyCommon(GLOBAL.goalListPage.init,GLOBAL.goalListPage);

});

function GoalListPage(container_id) {

	// Parent constructor
	Page.call(this,container_id);

	// Array with Cbtion objects
	this.goals = {};
	this.resSet = {};

	this.filters = {
			keyw : undefined,
			projects : [],
			creators : [],
			states: ["PROPOSED","ACCEPTED"],
			page : 1,
			nPerPage : 15
	};

	this.filters_expanded = false;

};

//Inheritance
GoalListPage.prototype = Page.prototype;

GoalListPage.prototype.init = function() {

	// Common after sessionUpdate code
	CopSessionReadyCommon();

	// Apply JQ effects
	$("#filter_btn", this.container).click(this.filterClick.bind(this));

	$("#next_page", this.container).click(this.nextPageClick.bind(this));
	$("#back_page", this.container).click(this.backPageClick.bind(this));

	if(JSP_projectFilter){
		this.filters.projects.push(JSP_projectFilter);
	}

	this.updateGoalList();

}

GoalListPage.prototype.nextPageClick = function() {
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

GoalListPage.prototype.backPageClick = function() {
	this.filters.page = this.filters.page-1;
	if (this.filters.page < 1) this.filters.page = 1;
	this.updateCbtionList();
}

GoalListPage.prototype.GoalsReceivedCallback = function(goalDtos,resSet) {
	this.goals = goalDtos;
	this.resSet = resSet;
	this.drawGoals();
}

GoalListPage.prototype.updateGoalList = function() {
	GLOBAL.serverComm.goalListGet(this.filters, this.GoalsReceivedCallback,this);
}

GoalListPage.prototype.filterClick = function() {

	if (!this.filters_expanded) {
		// if the filters are not expanded, expand and update the first time
		this.filters_expanded = true;
		$("#filter_contents", this.container).slideDown();
		$("#filter_bar_p", this.container).text("update results");

		// Featured Projects
		GLOBAL.serverComm.projectListGet(this.ProjectListReceivedCallback,this);
		
		// Cbtion state
		$("#filter_goal_state", this.container).empty();
		this.stateFilterDraw("PROPOSED")
		this.stateFilterDraw("ACCEPTED")
		this.stateFilterDraw("DELETED")

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
			
			var statefilter = $("#filter_goal_state", this.container).find("input");
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

	this.updateGoalList();
}


GoalListPage.prototype.ProjectListReceivedCallback = function(projectList) {
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

GoalListPage.prototype.stateFilterDraw = function(state_name) {
	$("#filter_goal_state", this.container).append(
			$("<input type='checkbox' id="+state_name+
					"_input_fe state_id="+state_name+">"
					+state_name+"<br/>"));
	
	if (this.filters.states.includes(state_name)) {
		$("#" + state_name + "_input_fe").attr('checked', true);
	}

}

GoalListPage.prototype.drawGoals = function() {

	this.updateResSet();
	$("#list_of_elements", this.container).empty();

	for ( var ix in this.goals) {
		$("#list_of_elements", this.container).append(this.create_div_jQ("goalbox",ix));
		var goalBox = new GoalBox(this.get_div_jQ("goalbox",ix),this.goals[ix]);
	}
}

GoalListPage.prototype.updateResSet = function() {
	$("#page_set", this.container).html("<p>"+this.resSet[0]+"-"+this.resSet[1]+" of "+this.resSet[2]+"</p>");
}

