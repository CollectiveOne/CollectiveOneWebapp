function CopDocReadyCommon(callbackFun,callbackObj) {
	
	GLOBAL.serverComm = new ServerComm();
	
	GLOBAL.sessionData = new SessionData("#user_div");
	GLOBAL.sessionData.init(callbackFun,callbackObj);

	$("#logo").click(function() {
		window.location = 'CbtionListPage.action';
	});
	
	$("#cbtion_list").click(function() {
		window.location = 'CbtionListPage.action';
	});
	
	$("#goals_list").click(function() {
		window.location = 'GoalListPage.action';
	});
	
	$("#new_cbtion").click(function() {
		window.location = 'CbtionNewPage.action';
	});
	
	$("#new_goal").click(function() {
		window.location = 'GoalNewPage.action';
	});
	
	$("#new_project").click(function() {
		window.location = 'ProjectNewPage.action';
	});
}

function CopSessionReadyCommon(userLogged) {
	//
}

function showOutput(output,color) {
	
	color = color || "darkorange";
	
	if(typeof output == "string"){
		txt = output
	}
	else {
		// output is assumed to have fieldErrors from struts
		txt = '';
		if(output.fieldErrors) {
			for (var property in output.fieldErrors) {
			    if (output.fieldErrors.hasOwnProperty(property)) {
			        txt = txt + " " + property + " " + output.fieldErrors[property];
			    }
			}
		}
	}
	
	$("#output").css("background-color",color);
	$("#output").empty();
	$("#output").append(txt);
	setTimeout(function() {
		$("#output").empty();
	}, 3000)
}

function floatToChar(x,ndec) {
	return parseFloat(Math.round(x*Math.pow(10,ndec))/Math.pow(10,ndec)).toFixed(ndec);
}