function FilterElement(container_id, getDataCall, callBack, callObject, states, filters) {
	
	this.container = $(container_id);
	this.getDataCall = getDataCall;
	this.callBack = callBack;
	this.callObject = callObject;
	
	this.states = states;
	this.resSet = {};
	this.filters = filters || {
			projects : [],
			creators : [],
			states: [],
			keyw : [],
			page : 1,
			nPerPage : 15
	};

	this.filters_expanded = false;
	
	this.container.load("../elements/FilterElement/FilterElement.html",this.init.bind(this));
};

//Inheritance
FilterElement.prototype.init = function() {
	// Apply JQ effects
	$("#filter_btn", this.container).click(this.filterClick.bind(this));
	$("#next_page", this.container).click(this.nextPageClick.bind(this));
	$("#back_page", this.container).click(this.backPageClick.bind(this));
}

FilterElement.prototype.filterClick = function() {

	if (!this.filters_expanded) {
		// if the filters are not expanded, expand and update the first time
		this.filters_expanded = true;
		$("#filter_contents", this.container).slideDown();
		$("#filter_bar_p", this.container).text("update results");

		// Cbtion state
		$("#filter_states", this.container).empty();
		for(var ix = 0; ix < this.states.length; ix++) {
			this.stateFilterDraw(this.states[ix])
		}

		// All Projects
		GLOBAL.serverComm.projectListGet(this.projectListReceivedCallback,this);
		
	} else {
		// if filters are expanded, collapse and update the cbtions list
		this.filters_expanded = false;
		this.filters.projects = [];
		
		var projects = $("#filter_projects", this.container).find("input");
		var npf = projects.length;

		for (var i = 0; i < npf; i++) {
			if ($(projects[i]).is(":checked")) {
				this.filters.projects.push($(projects[i]).attr(
				"project_name"));
			}
		}
		
		this.filters.states = [];
		
		var statefilter = $("#filter_states", this.container).find("input");
		var nsf = statefilter.length;

		for (var i = 0; i < nsf; i++) {
			if ($(statefilter[i]).is(":checked")) {
				this.filters.states.push($(statefilter[i]).attr(
				"state_id"));
			}
		}
		
		$("#filter_contents", this.container).slideUp();
		$("#filter_bar_p", this.container).text("filter");

		this.updateData();
	}

	
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
		if (this.filters.projects.includes(pname)) {
			$("#" + pname + "_input_fe").attr('checked', true);
		}
	}
}

FilterElement.prototype.nextPageClick = function() {
	this.filters.page = this.filters.page+1;

	if(this.resSet) {
		var page_max = Math.ceil(this.resSet[2]/this.filters.nPerPage);
		if(this.filters.page > page_max) {
			showOutput("no more data available","green");
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
	
	if (this.filters.states.includes(state_name)) {
		$("#" + state_name + "_input_fe").attr('checked', true);
	}

}