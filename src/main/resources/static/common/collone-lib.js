function docReadyCommon(callbackFun,callbackObj) {
	
	/* update the page every time an active project is selected*/
	GLOBAL.sessionData.activeProjectsController = new ActiveProjectsController("#active_projects",callbackFun,callbackObj);
}


function showOutput(output,color) {
	
	/* show a message and hide*/
	color = color || "darkorange";
	txt = output
	
	$("#output_div").css("background-color",color);
	$("#output_div").show();
	$("#output").empty();
	$("#output").append(txt);
	
	setTimeout(function() {
		$("#output").empty();
		$("#output_div").hide();
	}, 3000)
}

function getLoggedUsername() {
	if(GLOBAL) {
		if(GLOBAL.sessionData) {
			if(GLOBAL.sessionData.userLogged) {
				if(GLOBAL.sessionData.userLogged != "anonymousUser") {
					return GLOBAL.sessionData.userLogged;
				}
			}
		}
	}
	/* null otherwise */
	return null;
}

function isUserLogged() {
	if(getLoggedUsername() != null) {
		return true;
	} else {
		return false;
	}
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

function getGoalPageLink(goalTag,projectName) {
	return "<a href=/views/goalPageR?projectName="+projectName+"&goalTag="+goalTag+"><b>+</b>"+goalTag+"</a>";
}

function getUserPageLink(username) {
	return "<a href=/views/userPageR/"+username+">"+username+"</a>";
}

function getProjectLink(projectName) {
	return "<a href=/views/project/"+projectName+">"+projectName+"</a>";
}

function LimitStrSize(strIn, size) {
	if(!size) size = 70;

	if(strIn.length > size) {
		return strIn.slice(0,size)+" ...";
	} else {
		return strIn;
	}
}









