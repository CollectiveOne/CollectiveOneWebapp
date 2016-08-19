function Page(container_id) {
	this.container = $(container_id);
};

Page.prototype.create_div_jQ = function(div_type,id) {
	return $('<div class='+div_type+'_div id='+div_type+'_div'+id+'>');
}

Page.prototype.create_p_str = function(div_type,id,text) {
	return $('<p class='+div_type+'_p id='+div_type+'_p'+id+'>'+text+'</p>');
}

Page.prototype.create_a_str = function(div_type,id,text,href) {
	return $('<a class='+div_type+'_p id='+div_type+'_p'+id+' href='+href+'>'+text+'</a>');
}

Page.prototype.create_pap_str = function(div_type,id,textprev,texthref,href,textafter) {
	text = textprev + '<a class='+div_type+'_a id='+div_type+'_a'+id+' href='+href+'>'+texthref+'</a>' + textafter;
	return $('<p class='+div_type+'_p id='+div_type+'_p'+id+'>'+text+'</p>');
}

Page.prototype.get_div_jQ = function(div_type,id) {
	return $('#'+div_type+'_div'+id);
}

Page.prototype.append_input = function(id,txt,val,type) {
	type = type || "text";
	this.container.append($('<div class=field id='+id+'>'));
	
	$('#'+id).append($('<p class=field_name_p>'+txt+'</p>'));
	$('#'+id).append($('<input type='+type+' class=field_in id='+id+'_in value="'+val+'">'));
	
}

Page.prototype.append_project_selector = function(id) {
	this.container.append($('<div class=select_div id=project_selector_div>'));
	
	$('#project_selector_div').append($('<p class=field_name_p>Project</p>'));
	$('#project_selector_div').append($('<select class=select id='+id+'>'));
	
	var np = GLOBAL.sessionData.userLogged.projectsFollowing.length;
	$("#project_select").empty();
	
	for(var i=0; i<np ; i++ ) {
		var pname = GLOBAL.sessionData.userLogged.projectsFollowing[i];
		$("#project_select").append($("<option value="+pname+">"+pname+"</option>"));
	} 
}

Page.prototype.append_text_area = function(id,txt,val) {
	this.container.append($('<div class=field id='+id+'>'));
	
	$('#'+id).append($('<p class=field_name_p>'+txt+'</p>'));
	$('#'+id).append($('<textarea class=textarea_in id='+id+'_in value="'+val+'">'));
}

Page.prototype.getTimeStrSince = function(date) {
	/* Server is in timezoneoffset -120 */
	var datems = Date.parse(date) - 120*60*1000
	var nowms = new Date().getTime();
	var dtsec = (nowms - datems)/1000;
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



