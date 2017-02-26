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
				url : '/rest/cbtions/getList',
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
				type : 'POST',
				url : '/rest/cbtions/get/'+cbtionId,
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
				type : 'POST',
				url : '/rest/cbtions/edit',
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
			//TODO: update
			var data = {
					'elementId' : cbtionId,
					'promoteUp' : promoteUp
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '/rest/cbtions/promote',
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

		goalNew : function(goalData,callbackFunction,callbackObj) {
			var datastr = JSON.stringify(goalData);
			$.ajax({
				type : 'POST',
				url : '/rest/goals/new',
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
		
		goalGet : function(goalTag,projectName,callbackFunction,callbackObj) {

			var data = {
					'goalTag' : goalTag,
					'projectName' : projectName
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '/rest/goals/get',
				data : datastr,
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

			var data = {
				'goalTag' : goalTag,
				'projectName' : projectName
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '/rest/goals/exist',
				data : datastr,
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
				url : '/rest/goals/getList',
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
				type : 'POST',
				url : '/rest/goals/getOfProject/'+projectName,
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

		goalProposeParent : function(goalId,parentTag,callbackFunction,callbackObj) {
			var data = {
				'goalId' : goalId,
				'parentTag' : parentTag
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '/rest/goals/proposeParent',
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
		
		goalWeightsGet: function(projectName,goalTag,callbackFunction,callbackObj) {
			$.ajax({
				type : 'POST',
				url : '/rest/goals/getWeightData/'+projectName+'/'+goalTag,
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
		
		goalRealmTouch:  function(touchFlag, projectName,goalTag,callbackFunction,callbackObj) {
			
			var data = {
				'touchFlag' : touchFlag,
				'projectName' : projectName,
				'goalTag': goalTag
			};
			var datastr = JSON.stringify(data);
				
			$.ajax({
				type : 'POST',
				url : '/rest/goals/touch',
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

		goalProposeDetach: function(goalId, increaseBudget,callbackFunction,callbackObj) {
			
			var data = {
				'goalId' : goalId,
				'increaseBudget' : increaseBudget
			};
			var datastr = JSON.stringify(data);
				
			$.ajax({
				type : 'POST',
				url : '/rest/goals/proposeDetach',
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

		goalProposeReattach: function(goalId,callbackFunction,callbackObj) {
			
			var data = {
				'goalId' : goalId,
				'increaseBudget' : 0.0
			};
			var datastr = JSON.stringify(data);
				
			$.ajax({
				type : 'POST',
				url : '/rest/goals/proposeReattach',
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

		goalProposeIncreaseBudget: function(goalId, increaseBudget,callbackFunction,callbackObj) {
			
			var data = {
				'goalId' : goalId,
				'increaseBudget' : increaseBudget
			};
			var datastr = JSON.stringify(data);
				
			$.ajax({
				type : 'POST',
				url : '/rest/goals/proposeIncreaseBudget',
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

		userGet : function(username,callbackFunction,callbackObj) {
			$.ajax({
				type : 'POST',
				url : '/rest/users/get/'+username,
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
				type : 'POST',
				url : '/rest/users/updateProfile',
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

		userGetProjectsContributed : function(username,callbackFunction,callbackObj) {
			$.ajax({
				type : 'POST',
				url : '/rest/users/getProjectsContributed/'+username,
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
			var data = {
				'username' : username,
				'projectName' : projectName
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '/rest/users/ppsInProjectGet',
				data : datastr,
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
				type : 'POST',
				url : '/rest/session/activeProjectsGet',
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
				type : 'POST',
				url : '/rest/session/activeProjectChange',
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
				type : 'POST',
				url : '/rest/projects/get/'+projectName,
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
				url : '/rest/projects/getNamesList',
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
				url : '/rest/projects/getList',
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
				type : 'POST',
				url : '/rest/projects/getContributors/'+projectName,
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
				type : 'POST',
				url : '/rest/projects/star/'+projectId,
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
				type : 'POST',
				url : '/rest/projects/unStar/'+projectId,
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
				type : 'POST',
				url : '/rest/projects/watch/'+projectId,
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
				type : 'POST',
				url : '/rest/projects/unWatch/'+projectId,
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
				type : 'POST',
				url : '/rest/bids/getOfCbtion/'+cbtion_id,
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
				type : 'POST',
				url : '/rest/bids/get/'+bidId,
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
				url : '/rest/bids/new',
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
				type : 'POST',
				url : '/rest/bids/offer',
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
				type : 'POST',
				url : '/rest/bids/markDone',
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
				type : 'POST',
				url : '/rest/decisions/get/'+decisionId,
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
				url : '/rest/decisions/vote',
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
				type : 'POST',
				url : '/rest/decisions/getVote/'+decId,
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
				url : '/rest/decisions/getList',
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
				type : 'POST',
				url : '/rest/decisions/getArgument/'+argumentId,
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
				type : 'POST',
				url : '/rest/decisions/getArguments/'+decisionId,
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
				url : '/rest/decisions/newArgument',
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
				url : '/rest/decisions/argumentIsBacked/'+argumentId,
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

			var data = {
				'argId' : argumentId,
				'back' : backFlag
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '/rest/decisions/argumentBack',
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

		
		reviewNew : function(reviewDto,callbackFunction,callbackObj) {
			var datastr = JSON.stringify(reviewDto);
			$.ajax({
				type : 'POST',
				url : '/rest/bids/newReview',
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
				type : 'POST',
				url : '/rest/bids/getReviews/'+bidId,
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
				type : 'POST',
				url : '/rest/cbtions/getReviews/'+cbtionId,
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
				type : 'POST',
				url : '/rest/cbtions/getComment/'+commentId,
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
				type : 'POST',
				url : '/rest/cbtions/getComments/'+cbtionId,
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
				url : '/rest/cbtions/commentNew',
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
				type : 'POST',
				url : '/rest/cbtions/commentGetReplies/'+commentId,
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
			//TODO: update
			var data = {
				'elementId' : commentId,
				'promoteUp' : promoteUp
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '/rest/cbtions/commentPromote',
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

		activityListGet : function(filters,callbackFunction,callbackObj) {
			var datastr = JSON.stringify(filters);
			$.ajax({
				type : 'POST',
				url : '/rest/activity/getList',
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
