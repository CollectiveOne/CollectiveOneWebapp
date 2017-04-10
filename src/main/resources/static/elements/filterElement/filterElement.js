 FilterElement(container_id, getDataCall, callBack, callObject, customElements, filters, type, showFilterBtn, showNewBtn, newBtnLink) {

	this.container = $(container_id);
	this.getDataCall = getDataCall;
	this.callBack = callBack;
	this.callObject = callObject;

	this.resSet = {};
	this.possibleStateNames = customElements.stateNames;
	this.possibleSortBy = customElements.sortBy;
	this.filters = filters;

	this.type = type;
	this.showFilterBtn = showFilterBtn;
	this.showNewBtn = showNewBtn;
	this.newBtnLink = newBtnLink;

	this.filters_expanded = false;

	this.container.load("/elements/filterElement/filterElement.html",this.init.bind(this));
};

//Inheritance
FilterElement.prototype.init = function() {

	switch(this.type) {
		case "cbtions":
			$("#filter_inputs_cbtion",this.container).show();
			$('#filter_goal_input', this.container).keydown(this.inputEnterKey.bind(this));
			$('#filter_contributor_input', this.container).keydown(this.inputEnterKey.bind(this));
			$('#filter_assignee_input', this.container).keydown(this.inputEnterKey.bind(this));

			$('#filter_goal_input',this.container).autocomplete({
				serviceUrl: '/1/goals/suggestions',
				minChars: 0,
				params: {projectName: ""},
				maxHeight: 100
			});

			$('#filter_contributor_input',this.container).autocomplete({
				serviceUrl: '/1/users/suggestions',
				minChars: 0,
				maxHeight: 100
			});

			$('#filter_assignee_input',this.container).autocomplete({
				serviceUrl: '/1/users/suggestions',
				minChars: 0,
				maxHeight: 100
			});

			break;

		case "decisions":
			$("#filter_inputs_decision",this.container).show();
			break;

		case "goals":
			break;

		case "projects":
			break;

		case "activities":
			$("#filter_creators",this.container).hide();
			break;

		default:
			break;

	}

	if(this.showFilterBtn) {
		$("#filter_btn", this.container).show();
		$("#filter_btn", this.container).click(this.filterClick.bind(this));
		$("#filter_update", this.container).click(this.filterClick.bind(this));
	}

	if(this.showNewBtn) {
		$("#new_btn_div", this.container).show();
		$("#new_btn", this.container).attr("href",this.newBtnLink);
	}

	$("#next_page", this.container).click(this.nextPageClick.bind(this));
	$("#back_page", this.container).click(this.backPageClick.bind(this));

	$('#filter_creator_input', this.container).keydown(this.inputEnterKey.bind(this));
	$('#filter_keyword_input', this.container).keydown(this.inputEnterKey.bind(this));

	$('#filter_creator_input').autocomplete({
	  serviceUrl: '/1/users/suggestions',
	  minChars: 0,
		maxHeight: 100
	});

	for(ix in this.possibleSortBy) {
		$('#sort_by_sel', this.container).append("<option value="+this.possibleSortBy[ix].value+">"+this.possibleSortBy[ix].text+"</option>");
	}

	$('#sort_by_sel', this.container).change(this.getFiltersAndUpdateData.bind(this));

	this.updateFilterContents();

}

FilterElement.prototype.inputEnterKey = function (e) {
    if(e.keyCode == 13){
     	this.filterClick();
    }
}

FilterElement.prototype.filterClick = function() {

	if (!this.filters_expanded) {
		// if the filters are not expanded, expand and update the first time
		$("#filter_bar_p", this.container).text("update results");
		$("#filter_contents", this.container).show();
		this.filters_expanded = true;
		this.updateFilterContents();

	} else {
		// if filters are expanded, collapse and update the cbtions list
		$("#filter_bar_p", this.container).text("filter");
		$("#filter_contents", this.container).hide();
		this.filters_expanded = false;
		this.getFiltersAndUpdateData();
	}
}

FilterElement.prototype.updateFilterContents = function() {
	// Cbtion state
	$("#filter_states", this.container).empty();
	for(var ix = 0; ix < this.possibleStateNames.length; ix++) {
		this.stateFilterDraw(this.possibleStateNames[ix])
	}

	if(this.filters.keyw != "") $("#filter_keyword_input", this.container).val(this.filters.keyw);
	if(this.filters.creatorUsernames.length > 0) [$("#filter_creator_input", this.container).val(this.filters.creatorUsernames)];

	switch(this.type) {
		case "cbtions":
			if(this.filters.goalTag != "") $("#filter_goal_input", this.container).val(this.filters.goalTag);
			if(this.filters.goalSubgoalsFlag != null) $("#filter_subgoals_input").attr('checked', this.filters.goalSubgoalsFlag);
			if(this.filters.contributorUsername != "") $("#filter_contributor_input", this.container).val(this.filters.contributorUsername);
			if(this.filters.assigneeUsername != "") $("#filter_assignee_input", this.container).val(this.filters.assigneeUsername);
			break;

		case "decisions":
			if(this.filters.showInternalDecisions != "") $("#filter_automatic_decisions").attr('checked', this.filters.showInternalDecisions);
			break;
	}

	switch(this.type) {
		case "cbtions":
		case "decisions":
		case "goals":
			// Filter by project
			GLOBAL.serverComm.projectNamesListGet(this.projectListReceivedCallback,this);
			break

		case "projects":
			break
	}
}

FilterElement.prototype.getSelectedProjects = function() {
	var projects = $("#filter_projects", this.container).find("input");
	var npf = projects.length;

	var selectedProjectNames = [];

	for (var i = 0; i < npf; i++) {
		if ($(projects[i]).is(":checked")) {
			selectedProjectNames.push($(projects[i]).attr(
			"project_name"));
		}
	}

	return(selectedProjectNames);
}

FilterElement.prototype.getFiltersAndUpdateData = function() {

	this.filters.projectNames = this.getSelectedProjects();
	this.filters.stateNames = [];

	var statefilter = $("#filter_states", this.container).find("input");
	var nsf = statefilter.length;

	for (var i = 0; i < nsf; i++) {
		if ($(statefilter[i]).is(":checked")) {
			this.filters.stateNames.push($(statefilter[i]).attr(
			"state_id"));
		}
	}

	this.filters.keyw = $("#filter_keyword_input", this.container).val();
	if($("#filter_creator_input", this.container).val() != "") {
		this.filters.creatorUsernames = [$("#filter_creator_input", this.container).val()];
	} else {
		this.filters.creatorUsernames = [];
	}

	switch(this.type) {
		case "cbtions":
			$('#filter_goal_input',this.container).autocomplete().clear();

			var selectedProjects = this.getSelectedProjects();
			if(selectedProjects.length == 1)	$('#filter_goal_input',this.container).autocomplete().setOptions({params: {projectName: selectedProjects[0] }});
			else $('#filter_goal_input',this.container).autocomplete().setOptions({params: {projectName: "" }});


			if(($("#filter_goal_input", this.container).val() != "") && (selectedProjects.length != 1)) {
				showOutput("to filter by goal select only one project");
				return;
			}

			this.filters.goalTag = $("#filter_goal_input", this.container).val();
			this.filters.goalSubgoalsFlag = $("#filter_subgoals_input").is(":checked");
			if($("#filter_contributor_input", this.container).val() != "") {
				/* make sure ACCEPTED is selected as state otherwise it does not makes sense */
				if(this.filters.stateNames.indexOf("ACCEPTED") == -1) {
					this.filters.stateNames.push("ACCEPTED");
				}
			}
			this.filters.contributorUsername = $("#filter_contributor_input", this.container).val();

			if($("#filter_assignee_input", this.container).val() != "") {
				/* make sure ACCEPTED is selected as state otherwise it does not makes sense */
				if(this.filters.stateNames.indexOf("ASSIGNED") == -1) {
					this.filters.stateNames.push("ASSIGNED");
				}
			}
			this.filters.assigneeUsername = $("#filter_assignee_input", this.container).val();
			break;

		case "decisions":
			this.filters.showInternalDecisions = $("#filter_automatic_decisions").is(":checked");
			break;
	}

	this.filters.sortBy = $('#sort_by_sel', this.container).val();
	this.updateData();
}

FilterElement.prototype.updateData = function() {
	$("#loading_gif",this.container).show();
	this.getDataCall.call([],this.filters, this.dataReceivedCallback, this);
}

FilterElement.prototype.dataReceivedCallback = function(data) {
	$("#loading_gif",this.container).hide();
	this.callBack.call(this.callObject,data);
}


FilterElement.prototype.projectListReceivedCallback = function(projectList) {
	np = projectList.length;

	$("#filter_projects", this.container).empty();

	for (var i = 0; i < np; i++) {
		var pname = projectList[i];
		$("#filter_projects", this.container).append(
				"<div class=checkbox>" +
				  "<label><input type=checkbox id="+pname+"_input_fe project_name="+pname+">"+pname+"</label>"+
				"</div>");

		if(this.filters.projectNames) {
			if (this.filters.projectNames.includes(pname)) {
				$("#" + pname + "_input_fe").attr('checked', true);
			}
		}
	}
}

FilterElement.prototype.nextPageClick = function() {
	this.filters.page = this.filters.page+1;

	if(this.resSet) {
		var page_max = Math.ceil(this.resSet[2]/this.filters.nperpage);
		if(this.filters.page > page_max) {
			showOutput("no more data available","DarkGreen");
			this.filters.page = page_max;
		}
	}

	this.updateResSet();
	this.updateData();
}

FilterElement.prototype.backPageClick = function() {
	this.filters.page = this.filters.page-1;
	if (this.filters.page < 1) this.filters.page = 1;

	this.updateResSet();
	this.updateData();
}

FilterElement.prototype.updateResSet = function() {
	$("#page_set", this.container).html(this.resSet[0]+"-"+this.resSet[1]+" of "+this.resSet[2]);
}


FilterElement.prototype.stateFilterDraw = function(state_name) {
	$("#filter_states", this.container).append(
			"<div class=checkbox>" +
			  "<label><input type=checkbox id="+state_name+"_input_fe state_id="+state_name+">"+state_name+"</label>"+
			"</div>");

	if (this.filters.stateNames.includes(state_name)) {
			$("#" + state_name + "_input_fe").attr('checked', true);
	}

}
