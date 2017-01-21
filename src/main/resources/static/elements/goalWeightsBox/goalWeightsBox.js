function GoalWeightsBox(container_id,goalWeightsData) {
	// Parent constructor
	this.container = $(container_id);
	this.goalWeightsData = goalWeightsData;
};

GoalWeightsBox.prototype.update = function() {
	GLOBAL.serverComm.goalWeightsGet(this.goalWeightsData.projectName, this.goalWeightsData.goalTag, this.dataReceivedCallback, this);
}

GoalWeightsBox.prototype.dataReceivedCallback = function(data) {
	this.goalWeightsData = data;
	this.draw();	
}

GoalWeightsBox.prototype.draw = function() {
	this.container.load("/elements/goalWeightsBox/goalWeightsBox.html",this.htmlLoaded.bind(this));
}

GoalWeightsBox.prototype.htmlLoaded = function() {
	if(isUserLogged()) {
		$("#left_div", this.container).show();
		$("#total_weight", this.container).html(this.goalWeightsData.totalWeight);
		$("#user_max_weight", this.container).html(this.goalWeightsData.userWeightsDto.maxWeight);
		$("#user_actual_weight", this.container).html(this.goalWeightsData.userWeightsDto.actualWeight);
		$("#touch_btn", this.container).click(this.touchClicked.bind(this));
		$("#release_btn", this.container).click(this.releaseClicked.bind(this));
	}
	
	this.drawGraph();	
}

GoalWeightsBox.prototype.touchClicked = function() {
	GLOBAL.serverComm.goalRealmTouch(true, this.goalWeightsData.projectName, this.goalWeightsData.goalTag, this.afterClickCallback, this);
}

GoalWeightsBox.prototype.releaseClicked = function() {
	GLOBAL.serverComm.goalRealmTouch(false, this.goalWeightsData.projectName, this.goalWeightsData.goalTag, this.afterClickCallback, this);
}

GoalWeightsBox.prototype.afterClickCallback = function() {
	this.update();
}

GoalWeightsBox.prototype.drawGraph = function() {
	
	var votersDtos = this.goalWeightsData.votersDtos;
	
	var chartData = []; 
	var chartLabels = [];
	var chartColors = [];
	
	for(var ix in votersDtos) {
		chartData.push(votersDtos[ix].actualWeight);
		chartLabels.push(votersDtos[ix].username);
		chartColors.push("#5bc0de");
	}
	
	var ctx =  $("#weights_chart");
	var ppsChart = new Chart(ctx, {
	    type: 'bar',
	    data: {	datasets: [{
	    			data: chartData,
	    			backgroundColor: chartColors
	    		}],
	    		labels: chartLabels 
	    },
	    options: {
	    		legend: {
	    			display: false
	    		},
	    		scales: {
					yAxes: [{
						ticks: {
							beginAtZero:true
						}
					}]
	    		}
	    }
	});
	
	$("#weights_chart").click( 
		function(evt){
		    var activePoints = ppsChart.getElementsAtEvent(evt);
		    var clickedElementindex = activePoints[0]["_index"];
		    var username = ppsChart.data.labels[clickedElementindex]; 
		    window.location.href = "/views/userPageR/"+username;
	});
}

