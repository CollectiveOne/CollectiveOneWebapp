function SessionData(containerId) {
	this.container = $(containerId);
	this.userLogged = null;
};

SessionData.prototype = {
		
	init: function(callbackFun,callbackObj) {
		
		this.updateUserLogged(callbackFun,callbackObj);
		
		$("#login_expand").click(function() {
			$("#user_div").toggle();
		});
	},
	
	updateUserLogged: function(callbackFun,callbackObj) {
		GLOBAL.serverComm.userLoggedGet(callbackFun,callbackObj);
	},
	
	draw: function() {
		if(this.userLogged == null) {
			this.drawNotLogged();
		} else {
			this.drawLogged();
		}
	},
	
	drawNotLogged: function () {
		this.container.empty();
		this.container.load("Common/SessionData/sessionNotLogged.html",this.userNotLoggedLoaded.bind(this));
	},
	
	userNotLoggedLoaded: function() {
		$("#submit_login",this.container).click(this.userLogin.bind(this));
	},
	
	userLogin: function() {
		GLOBAL.serverComm.userLogin(this.userLoginCallback,this);
	},
	
	userSignup: function() {
		window.location = 'UserNewPage.action';
	},
	
	userLoginCallback: function(userLoggedDto) {
		if (userLoggedDto != null) {
			showOutput(userLoggedDto.username+" logged in","green");
			this.userLogged = userLoggedDto;
			this.draw();
		} else {
			showOutput("login error");
		}
	},
	
	drawLogged: function() {
		$("#login_expand").html(this.userLogged.username);
		
		this.container.hide();
		this.container.load("Common/SessionData/sessionLogged.html",this.userLoggedLoaded.bind(this));
	},	
	
	userLoggedLoaded: function() {
		$("#my_profile",this.container).click(function(e) {
		     e.preventDefault(); 
		     window.location = "UserPage.action?username="+GLOBAL.sessionData.userLogged.username;
		 });
		$("#logout",this.container).click(this.userLogout.bind(this));
	},
	
	userLogout: function() {
		GLOBAL.serverComm.userLogout(this.userLogoutCallback.bind(this),this);
	},
	
	userLogoutCallback: function() {
		$("#user_div").hide();
		$("#login_expand").html("login");
		
		this.updateUserLogged();
	}, 
}
