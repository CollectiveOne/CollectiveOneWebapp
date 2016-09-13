function FilterElement(container_id, getDataCall, callBack, callObject, customElements, filters) {
	
	this.container = $(container_id);
	this.getDataCall = getDataCall;
	this.callBack = callBack;
	this.callObject = callObject;
	
	this.resSet = {};
	this.possibleStateNames = customElements.stateNames;
	this.possibleSortBy = customElements.sortBy;
	this.filters = filters;

	this.filters_expanded = false;
	
	this.container.load("../elements/FilterElement/FilterElement.html",this.init.bind(this));
};

//Inheritance
FilterElement.prototype.init = function() {
	// Apply JQ effects
	$("#filter_btn", this.container).click(this.filterClick.bind(this));
	$("#next_page", this.container).click(this.nextPageClick.bind(this));
	$("#back_page", this.container).click(this.backPageClick.bind(this));

	$('#filter_creator_input', this.container).keydown(this.inputEnterKey.bind(this));
	$('#filter_keyword_input', this.container).keydown(this.inputEnterKey.bind(this));
	
	$('#filter_creator_input').autocomplete({
	    serviceUrl: '../json/UsernameGetSuggestions.action',
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

	// All Projects
	GLOBAL.serverComm.projectListGet(this.projectListReceivedCallback,this);
}

FilterElement.prototype.getFiltersAndUpdateData = function() {
	this.filters.projectNames = [];

	var projects = $("#filter_projects", this.container).find("input");
	var npf = projects.length;

	for (var i = 0; i < npf; i++) {
		if ($(projects[i]).is(":checked")) {
			this.filters.projectNames.push($(projects[i]).attr(
			"project_name"));
		}
	}

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
	this.filters.creatorUsernames = [$("#filter_creator_input", this.container).val()];
	this.filters.sortBy = $('#sort_by_sel', this.container).attr("value");

	this.updateData();
}

FilterElement.prototype.updateData = function() {
	this.getDataCall.call([],this.filters, this.callBack, this.callObject);
}

FilterElement.prototype.projectListReceivedCallback = function(projectList) {
	np = projectList.length;

	$("#filter_projects", this.container).empty();

	for (var i = 0; i < np; i++) {
		var pname = projectList[i];
		$("#filter_projects", this.container).append(
				$("<input type='checkbox' class=filter_checkbox id=" + pname
						+ "_input_fe project_name=" + pname + ">" +
								"<div class=checkbox_p_div>"+"" +
										"<p>" + pname + "</p>" +
												"</div>"));
		if (this.filters.projectNames.includes(pname)) {
			$("#" + pname + "_input_fe").attr('checked', true);
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
	$("#page_set", this.container).html("<p>"+this.resSet[0]+"-"+this.resSet[1]+" of "+this.resSet[2]+"</p>");
}


FilterElement.prototype.stateFilterDraw = function(state_name) {
	$("#filter_states", this.container).append(
			$("<input type='checkbox' class=filter_checkbox id="+state_name+
					"_input_fe state_id="+state_name+"><div class=checkbox_p_div>" +
							"<p>"+state_name+"</p>" +
									"</div>"));
	
	if (this.filters.stateNames.includes(state_name)) {
		$("#" + state_name + "_input_fe").attr('checked', true);
	}

}