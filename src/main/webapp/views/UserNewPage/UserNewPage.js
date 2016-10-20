$(document).ready(function() {

	GLOBAL = new Object();
	GLOBAL.userNewPage = new UserNewPage("#content_pane");
	
	CopDocReadyCommon(GLOBAL.userNewPage.draw,GLOBAL.userNewPage);
	
	GLOBAL.userNewPage.init();
	
});

function UserNewPage(container_id) {
	// Parent constructor
	Page.call(this,container_id);
};

// Inheritance
UserNewPage.prototype = Page.prototype;

UserNewPage.prototype.init = function() {
	$('#create_btn').click(this.userNew.bind(this));
}
UserNewPage.prototype.draw = function() {

	if (GLOBAL.sessionData.userLogged) {
		this.container.empty();
		this.container
				.append($("<p>user already logged in</p>"));
		
	} else {
		
	}
}

UserNewPage.prototype.userNew = function() {
	if($("#password_in", this.container).val() == $("#password_check_in", this.container).val()) {
		var data = {	
			username : $("#username_in", this.container).val(),
			password : $("#password_in", this.container).val()
		}
		
		GLOBAL.serverComm.userNew(data,this.userNewCallback,this);
	} else {
		showOutput(" passwords do not match", "DarkRed")
	}
}

UserNewPage.prototype.userNewCallback = function(userDto) {
	window.location = 'CbtionListPage.action';
}
