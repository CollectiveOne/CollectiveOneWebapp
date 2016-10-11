function Page(container_id) {
	this.container = $(container_id);
	this.projectsRecivedCallBackFun = null;
	this.projectsRecivedCallBackObj = null;
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

Page.prototype.append_project_selector = function(id,callBackFunc, callBackObj) {
	this.container.append($('<div class=select_div id=project_selector_div>'));
	
	$('#project_selector_div').append($('<p class=field_name_p>Project</p>'));
	$('#project_selector_div').append($('<select class=select id='+id+'>'));
	
	this.projectsRecivedCallBackFun = callBackFunc;
	this.projectsRecivedCallBackObj = callBackObj;
	
	GLOBAL.serverComm.projectListGet(this.projectListReceivedCallback,this);
}

Page.prototype.projectListReceivedCallback = function(projectList) {
	$("#project_select").empty();
	
	for(var ix in projectList) {
		$("#project_select").append($("<option value="+projectList[ix]+">"+projectList[ix]+"</option>"));
	} 

}

Page.prototype.append_text_area = function(id,txt,val) {
	this.container.append($('<div class=field id='+id+'>'));
	
	$('#'+id).append($('<p class=field_name_p>'+txt+'</p>'));
	$('#'+id).append($('<textarea class=textarea_in id='+id+'_in value="'+val+'">'));
}



