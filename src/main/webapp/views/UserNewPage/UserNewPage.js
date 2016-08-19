$(document).ready(function() {

	GLOBAL = new Object();
	GLOBAL.userNewPage = new UserNewPage("#content_pane");
	
	CopDocReadyCommon(GLOBAL.userNewPage.init,GLOBAL.userNewPage);
	
});

function UserNewPage(container_id) {
	// Parent constructor
	Page.call(this,container_id);
};

// Inheritance
UserNewPage.prototype = Page.prototype;

UserNewPage.prototype.init = function() {
	this.data = {
			username: [], 
			password: []
	};
	
	this.draw();
}
UserNewPage.prototype.draw = function() {

	if (GLOBAL.sessionData.userLogged) {
		this.container.empty();
		this.container
				.append($("<p>user already logged in</p>"));
		
	} else {
		this.container.empty();

		this.append_input('username', 'username', '');
		this.append_input('password', 'password', '','password');
		this.append_input('password_check', 'password_check', '','password');
		
		// append create button
		this.container.append($('<button id="create_btn">Sign up</button>'));

		$('#create_btn').click(this.userNew.bind(this));
	}
}

UserNewPage.prototype.userNew = function() {
	if($("#password_in", this.container).val() == $("#password_check_in", this.container).val()) {
		this.data.username = $("#username_in", this.container).val();
		this.data.password = $("#password_in", this.container).val();
		GLOBAL.serverComm.userNew(this.data,this.userNewCallback,this);
	} else {
		showOutput(" passwords do not match", "red")
	}
}

UserNewPage.prototype.userNewCallback = function(userDto) {
	window.location = 'CbtionListPage.action';
}
