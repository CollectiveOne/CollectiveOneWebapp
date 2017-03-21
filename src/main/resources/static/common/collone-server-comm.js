function showReqOutput(resStatus) {
	if(resStatus.success) {
		showOutput(resStatus.msg,"DarkGreen")
	} else {
		if(resStatus.msg) showOutput(resStatus.msg,"DarkRed")
		else  showOutput("unknown error","DarkRed")
	}
}

function ServerComm() {

};

ServerComm.prototype = {

		cbtionListGet : function(filters,callbackFunction,callbackObj) {

			var datastr = JSON.stringify(filters);

			$.ajax({
				type : 'POST',
				url : '/1/cbtions',
				data : datastr,
				dataType : "json",
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		cbtionGet : function(cbtionId,callbackFunction,callbackObj) {

			$.ajax({
				type : 'GET',
				url : '/1/cbtion/'+cbtionId,
				data : '',
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);	
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
					showOutput("error getting cbtion");
				}
			});
		},

		cbtionEdit : function(cbtionEditData,callbackFunction,callbackObj) {
			var datastr = JSON.stringify(cbtionEditData);
			$.ajax({
				type : 'PUT',
				url : '/1/cbtion',
				data : datastr,
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		cbtionPromote : function(cbtionId,promoteUp,callbackFunction,callbackObj) {
			$.ajax({
				type : 'PUT',
				url : '/1/cbtion/'+cbtionId+'/promote?up='+promoteUp,
				data : '',
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		goalNew : function(goalData,callbackFunction,callbackObj) {
			var datastr = JSON.stringify(goalData);
			$.ajax({
				type : 'POST',
				url : '/1/goal',
				data : datastr,
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},
		
		goalGetFromTag : function(goalTag,projectName,callbackFunction,callbackObj) {
			$.ajax({
				type : 'GET',
				url : '/1/goal?projectName='+projectName+'&goalTag='+goalTag,
				data : '',
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					showOutput("error getting goal","DarkRed");
				}
			});
		},
		
		goalGet : function(goalId,callbackFunction,callbackObj) {
			$.ajax({
				type : 'GET',
				url : '/1/goal/'+goalId,
				data : '',
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					showOutput("error getting goal","DarkRed");
				}
			});
		},

		goalExist : function(goalTag,projectName,callbackFunction,callbackObj) {

			$.ajax({
				type : 'GET',
				url : '/1/goalExist?projectName='+projectName+'&goalTag='+goalTag,
				data : '',
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					callbackFunction.call(callbackObj,data);
				},
				error : function(jqXHR, textStatus, errorThrown) {
					showOutput("error getting goal","DarkRed");
				}
			});
		},

		goalListGet : function(filters,callbackFunction,callbackObj) {

			var datastr = JSON.stringify(filters);

			$.ajax({
				type : 'POST',
				url : '/1/goals',
				data : datastr,
				dataType : "json",
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		goalsOfProjectGet : function(projectName,callbackFunction,callbackObj) {
			$.ajax({
				type : 'GET',
				url : '/1/project/'+projectName+'/goals',
				data : '',
				dataType : "json",
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		goalProposeParent : function(goalId,callbackFunction,callbackObj) {
			$.ajax({
				type : 'PUT',
				url : '/1/goal/'+goalId+'/proposeParent?parentTag='+parentTag,
				data : '',
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},
		
		goalWeightsGet: function(goalId,callbackFunction,callbackObj) {
			$.ajax({
				type : 'GET',
				url : '/1/goal/'+goalId+'/weights',
				data : '',
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},
		
		goalRealmTouch:  function(touchFlag, goalId, callbackFunction,callbackObj) {
			
			$.ajax({
				type : 'PUT',
				url : '/1/goal/'+goalId+'/touch?touch='+touchFlag,
				data : '',
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},
		
		goalRealmSetWeight: function(goalId, weight, callbackFunction,callbackObj) {
			$.ajax({
				type : 'PUT',
				url : '/1/goal/'+goalId+'/setWeight?weight='+weight,
				data : '',
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		goalProposeDetach: function(goalId, increaseBudget,callbackFunction,callbackObj) {
			
			$.ajax({
				type : 'PUT',
				url : '/1/goal/'+goalId+'/proposeDetach?increaseBudget='+increaseBudget,
				data : '',
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		goalProposeReattach: function(goalId,callbackFunction,callbackObj) {
			
			$.ajax({
				type : 'PUT',
				url : '/1/goal/'+goalId+'/proposeReattach',
				data : '',
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		goalProposeIncreaseBudget: function(goalId, increaseBudget,callbackFunction,callbackObj) {
			$.ajax({
				type : 'PUT',
				url : '/1/goal/'+goalId+'/proposeIncreaseBudget?increaseBudget='+increaseBudget,
				data : '',
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		userGet : function(username,callbackFunction,callbackObj) {
			$.ajax({
				type : 'GET',
				url : '/1/user/'+username,
				data : '',
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		userUpdateProfile : function(username,profile,callbackFunction,callbackObj) {
			var data = {
				'username' : username,
				'profile' : profile
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'PUT',
				url : '/1/user',
				data : datastr,
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},
		
		userMyDataGet : function(username,callbackFunction,callbackObj) {
			$.ajax({
				type : 'GET',
				url : '/1/user/'+username+'/myData',
				data : '',
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		userGetProjectsContributed : function(username,callbackFunction,callbackObj) {
			$.ajax({
				type : 'GET',
				url : '/1/user/'+username+'/projectsContributed',
				data : '',
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					callbackFunction.call(callbackObj,data);
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		userProjectPpsGet : function(username,projectName,callbackFunction,callbackObj) {
			$.ajax({
				type : 'GET',
				url : '/1/user/'+username+'/ppsInProject?projectName='+projectName,
				data : '',
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					callbackFunction.call(callbackObj,data);
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		activeProjectsGet : function(callbackFunction,callbackObj) {
			var data = {
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'GET',
				url : '/1/session/activeProjects',
				data : datastr,
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
					showOutput("error getting user");
				}
			});
		},

		activeProjectChange : function(projectName,active,callbackFunction,callbackObj) {
			var data = {
				"projectName": projectName,
				"active": active
			};

			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'PUT',
				url : '/1/session/activeProjects',
				data : datastr,
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					} else {
						showOutput(data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
					showOutput("error getting user");
				}
			});
		},

		projectGet : function(projectName,callbackFunction,callbackObj) {
			$.ajax({
				type : 'GET',
				url : '/1/project/'+projectName,
				data : '',
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		projectNamesListGet : function(callbackFunction,callbackObj) {
			$.ajax({
				type : 'GET',
				url : '/1/projects/getNamesEnabled',
				data : '',
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data.projectList) {
						callbackFunction.call(callbackObj,data.projectList);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},
		
		projectListGet : function(filters,callbackFunction,callbackObj) {
			var datastr = JSON.stringify(filters);

			$.ajax({
				type : 'POST',
				url : '/1/projects',
				data : datastr,
				dataType : "json",
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		projectContributorsGet : function(projectName,callbackFunction,callbackObj) {
			$.ajax({
				type : 'GET',
				url : '/1/project/'+projectName+'/contributors',
				data : '',
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},
		
		projectStar : function(projectId,callbackFunction,callbackObj) {
			$.ajax({
				type : 'PUT',
				url : '/1/project/'+projectId+'/star',
				data : '',
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},
		
		projectUnStar : function(projectId,callbackFunction,callbackObj) {
			$.ajax({
				type : 'PUT',
				url : '/1/project/'+projectId+'/unStar',
				data : '',
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},
		
		projectWatch : function(projectId,callbackFunction,callbackObj) {
			$.ajax({
				type : 'PUT',
				url : '/1/project/'+projectId+'/watch',
				data : '',
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},
		
		projectUnWatch: function(projectId,callbackFunction,callbackObj) {
			$.ajax({
				type : 'PUT',
				url : '/1/project/'+projectId+'/unWatch',
				data : '',
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},


		bidsOfCbtionGet : function(cbtion_id,callbackFunction,callbackObj) {

			$.ajax({
				type : 'GET',
				url : '/1/cbtion/'+cbtion_id+'/bids',
				data : '',
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
					showOutput("error getting bids");
				}
			});
		},

		bidGet : function(bidId,callbackFunction,callbackObj) {

			$.ajax({
				type : 'GET',
				url : '/1/bid/'+bidId,
				data : '',
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			})
		},

		bidNew : function(bidData,callbackFunction,callbackObj) {

			var datastr = JSON.stringify(bidData);

			$.ajax({
				type : 'POST',
				url : '/1/bid',
				data : datastr,
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			})
		},

		bidOffer : function(bidData,callbackFunction,callbackObj) {

			var datastr = JSON.stringify(bidData);

			$.ajax({
				type : 'PUT',
				url : '/1/bid/offer',
				data : datastr,
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if(data) {
						callbackFunction.call(callbackObj);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
					showOutput("error creating bid");
				}
			})
		},

		bidMarkDone : function(doneData,callbackFunction,callbackObj) {

			var datastr = JSON.stringify(doneData);

			$.ajax({
				type : 'PUT',
				url : '/1/bid/done',
				data : datastr,
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			})
		},


		decisionGet : function(decisionId,callbackFunction,callbackObj) {

			$.ajax({
				type : 'GET',
				url : '/1/decision/'+decisionId,
				data : '',
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					} else {
						showOutput(data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
					showOutput("error getting decision");
				}
			});
		},

		decisionVote : function(vote,decId, callbackFunction, callbackObj) {
			var data = {
					'value'  : vote,
					'decisionId' : decId
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '/1/decision/vote',
				data : datastr,
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		decisionGetVote : function(decId, callbackFunction, callbackObj) {
			
			$.ajax({
				type : 'GET',
				url : '/1/decision/'+decId+'/vote',
				data : '',
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		decisionListGet : function(filters,callbackFunction,callbackObj) {
			var datastr = JSON.stringify(filters);
			$.ajax({
				type : 'POST',
				url : '/1/decisions',
				data : datastr,
				dataType : "json",
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		argumentGet : function(argumentId,callbackFunction,callbackObj) {

			$.ajax({
				type : 'GET',
				url : '/1/argument/'+argumentId,
				data : '',
				dataType : "json",
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		argumentsGetOfDecision : function(decisionId,callbackFunction,callbackObj) {

			$.ajax({
				type : 'GET',
				url : '/1/decision/'+decisionId+'/arguments',
				data : '',
				dataType : "json",
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		argumentNew : function(argDto,callbackFunction,callbackObj) {

			var datastr = JSON.stringify(argDto);

			$.ajax({
				type : 'POST',
				url : '/1/argument',
				data : datastr,
				dataType : "json",
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data); 
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		argumentIsBacked : function(argumentId,callbackFunction,callbackObj) {

			$.ajax({
				type : 'POST',
				url : '/1/argument/'+argumentId+'/isBacked',
				data : '',
				dataType : "json",
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					callbackFunction.call(callbackObj,data);
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		argumentBack : function(argumentId,backFlag,callbackFunction,callbackObj) {

			$.ajax({
				type : 'PUT',
				url : '/1/argument/'+argumentId+'/back?back='+backFlag,
				data : '',
				dataType : "json",
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		
		reviewNew : function(reviewDto,callbackFunction,callbackObj) {
			var datastr = JSON.stringify(reviewDto);
			$.ajax({
				type : 'POST',
				url : '/1/review',
				data : datastr,
				dataType : "json",
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						if (data.res) {
							callbackFunction.call(callbackObj,data); 
						}
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		reviewsGetOfBid : function(bidId,callbackFunction,callbackObj) {
			$.ajax({
				type : 'GET',
				url : '/1/bid/'+bidId+'/reviews',
				data : '',
				dataType : "json",
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},
		
		reviewsGetOfCbtion : function(cbtionId,callbackFunction,callbackObj) {
			$.ajax({
				type : 'GET',
				url : '/1/cbtion/'+cbtionId+'/reviews',
				data : '',
				dataType : "json",
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		commentGet : function(commentId,callbackFunction,callbackObj) {

			$.ajax({
				type : 'GET',
				url : '/1/comment/'+commentId,
				data : '',
				dataType : "json",
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data); 
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		commentsGetOfCbtion : function(cbtionId,callbackFunction,callbackObj) {

			$.ajax({
				type : 'GET',
				url : '/1/cbtion/'+cbtionId+'/comments',
				data : '',
				dataType : "json",
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		commentNew : function(commentDto,callbackFunction,callbackObj) {

			var datastr = JSON.stringify(commentDto);

			$.ajax({
				type : 'POST',
				url : '/1/comment',
				data : datastr,
				dataType : "json",
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data); 
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		commentGetReplies : function(commentId,callbackFunction,callbackObj) {

			$.ajax({
				type : 'GET',
				url : '/1/comment/'+commentId+'/replies',
				data : '',
				dataType : "json",
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},
		
		commentPromote : function(commentId,promoteUp,callbackFunction,callbackObj) {
			$.ajax({
				type : 'PUT',
				url : '/1/comment/'+commentId+'/promote?up='+promoteUp,
				data : '',
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		activityListGet : function(filters,callbackFunction,callbackObj) {
			var datastr = JSON.stringify(filters);
			$.ajax({
				type : 'POST',
				url : '/1/activities',
				data : datastr,
				dataType : "json",
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						callbackFunction.call(callbackObj,data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		}

};
