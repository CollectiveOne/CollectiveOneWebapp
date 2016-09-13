
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
	
	$("#decisions_list").click(function() {
		window.location = 'DecisionListPage.action';
	});
	
	$("#new_cbtion").click(function() {
		window.location = 'CbtionNewPage.action';
	});
	
	$("#new_goal").click(function() {
		window.location = 'GoalNewPage.action';
	});
	
	$("#new_decision").click(function() {
		window.location = 'DecisionNewPage.action';
	});
	
	$("#new_project").click(function() {
		window.location = 'ProjectNewPage.action';
	});
	
	$("#activity_page").click(function() {
		window.location = 'ActivityListPage.action';
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
	
	$("#output_div").css("background-color",color);
	$("#output_div").show();
	$("#output").empty();
	$("#output").append(txt);
	setTimeout(function() {
		$("#output").empty();
		$("#output_div").hide();
	}, 3000)
}

function floatToChar(x,ndec) {
	return parseFloat(Math.round(x*Math.pow(10,ndec))/Math.pow(10,ndec)).toFixed(ndec);
}

function getTimeStrUntil(timestamp) {
	return getNiceTimeStr(-getSecondsSince(timestamp));
}

function getTimeStrSince(timestamp) {
	return getNiceTimeStr(getSecondsSince(timestamp));
}

function getNiceTimeStr(dtsec) {
	var strout;
	if(Math.abs(dtsec) < 60) 
		strout = floatToChar(dtsec,0) +" sec";
	else if(Math.abs(dtsec) < 60*60) 
		strout = floatToChar(dtsec/60,0) +" min";
	else if(Math.abs(dtsec) < 60*60*24) 
		strout = floatToChar(dtsec/(60*60),0) +" hr";
	else	 
		strout = floatToChar(dtsec/(60*60*24),0) +" days";
		
	return strout;
}

function getSecondsSince(timestamp) {
	var nowms = new Date().getTime();
	var dtsec = (nowms - timestamp)/1000;
	return dtsec;
}