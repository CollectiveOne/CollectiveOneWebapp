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

			var data = {
					'filters' : filters
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '../json/CbtionListGet',
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

			var data = {
					'id' : cbtionId
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '../json/CbtionGet',
				data : datastr,
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data.cbtionDto) {
						callbackFunction.call(callbackObj,data.cbtionDto);
					} else {
						showOutput(data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
					showOutput("error getting cbtion");
				}
			});
		},

		cbtionNew : function(cbtionData,callbackFunction,callbackObj) {
			//TODO: update
			var data = {
					'cbtionDtoIn' : cbtionData
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '../json/CbtionNew',
				data : datastr,
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data.cbtionDto) {
						callbackFunction.call(callbackObj,data.cbtionDto);
					} else {
						showOutput(data);
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
					'cbtionId' : cbtionId,
					'promoteUp' : promoteUp
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '../json/CbtionPromote',
				data : datastr,
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						showReqOutput(data.resStatus);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		goalNew : function(goalData,callbackFunction,callbackObj) {
			//TODO: update
			var data = {
					'goalDtoIn' : goalData
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '../json/GoalNew',
				data : datastr,
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data.goalDto) {
						callbackFunction.call(callbackObj,data.goalDto);
					} else {
						showOutput(data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		goalListGet : function(filters,callbackFunction,callbackObj) {

			var data = {
					'filters' : filters
			};

			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '../json/GoalListGet',
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

		userGet : function(username,callbackFunction,callbackObj) {

			var data = {
					'username' : username
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '../json/UserGet',
				data : datastr,
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data.userDto) {
						callbackFunction.call(callbackObj,data.userDto);
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

		userNew : function(userData,callbackFunction,callbackObj) {

			var data = {
					'username' : userData.username,
					'password' : userData.password
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '../json/UserNew',
				data : datastr,
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data.res) {
						if(data.res == "user created") {
							showOutput("user_created", "DarkGreen");
							setTimeout(function() {
								callbackFunction.call(callbackObj);
							}, 3000);
						} else {
							showOutput(data.res, "red");
						}
					} else {
						showOutput(data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
					showOutput("error creating user");
				}
			});
		},

		userLogin : function(callbackFunction,callbackObj) {

			var user = {
					username : $('#user_username').val(),
					password : $('#user_password').val()
			};

			var data = {
					'user' : user
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '../json/UserLogin',
				data : datastr,
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					callbackFunction.call(callbackObj,data.userLoggedDto);
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		userLoggedGet : function(callbackFunction,callbackObj) {

			var data = {
					'user' : ''
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '../json/UserLoggedGet',
				data : datastr,
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					GLOBAL.sessionData.userLogged = data.userLoggedDto;
					GLOBAL.sessionData.draw();
					if(callbackFunction) {
						callbackFunction.call(callbackObj);
					}

				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		userLogout: function(callbackFunction,callbackObj) {

			var data = {
					'user' : ''
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '../json/UserLogout',
				data : datastr,
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					callbackFunction.call(callbackObj,data.userLoggedDto);
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		userDataIn : function(username,projectName,callbackFunction,callbackObj) {
			var data = {
					'username' : username,
					'projectName' : projectName
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '../json/UserDataIn',
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

		projectGet : function(projectName,callbackFunction,callbackObj) {

			var data = {
					'projectName' : projectName
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '../json/ProjectGet',
				data : datastr,
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data.projectDto) {
						callbackFunction.call(callbackObj,data.projectDto);
					} else {
						showOutput(data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
					showOutput("error getting cbtion");
				}
			});
		},

		projectListGet : function(callbackFunction,callbackObj) {

			var data = {
					'dum' : ''
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '../json/ProjectListGet',
				data : datastr,
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data.projectList) {
						callbackFunction.call(callbackObj,data.projectList);
					} else {
						showOutput(data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		projectNew : function(projectData,callbackFunction,callbackObj) {

			var data = {
					'projectDto' : projectData
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '../json/ProjectNew',
				data : datastr,
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data.res) {
						showOutput("project created", "DarkGreen");
						GLOBAL.sessionData.userLogged = data.userLoggedDto;
						callbackFunction.call(callbackObj);
					} else {
						showOutput(data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
					showOutput("error creating project");
				}
			});
		},

		bidsOfCbtionGet : function(cbtion_id,callbackFunction,callbackObj) {

			var data = {
					'cbtionId' : cbtion_id
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '../json/BidsOfCbtionGet',
				data : datastr,
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data.bidDtos) {
						callbackFunction.call(callbackObj,data.bidDtos);
					} else {
						showOutput(data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
					showOutput("error getting bids");
				}
			});
		},

		bidGet : function(bidId,callbackFunction,callbackObj) {

			var data = {
					'bidId' : bidId
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '../json/BidGet',
				data : datastr,
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data.bidDto) {
						callbackFunction.call(callbackObj,data.bidDto);
					} else {
						showOutput(data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
					showOutput("error getting bid");
				}
			})
		},

		bidNew : function(bidData,callbackFunction,callbackObj) {

			var data = {
					'bidDto' : bidData
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '../json/BidNew',
				data : datastr,
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data.res) {
						showOutput(data.msg, "DarkGreen");
						setTimeout(function() {
							callbackFunction.call(callbackObj);
						}, 3000);
					} else {
						showOutput(data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
					showOutput("error creating bid");
				}
			})
		},

		decisionGet : function(decisionId,callbackFunction,callbackObj) {

			var data = {
					'id' : decisionId
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '../json/DecisionGet',
				data : datastr,
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data.decisionDto) {
						callbackFunction.call(callbackObj,data.decisionDto);
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

		decisionNew : function(decisionData,callbackFunction,callbackObj) {

			var data = {
					'decisionDto' : decisionData
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '../json/DecisionNew',
				data : datastr,
				dataType : 'json',
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data.res) {
						showOutput(data.msg, "DarkGreen");
						setTimeout(function() {
							callbackFunction.call(callbackObj);
						}, 3000);
					} else {
						showOutput(data);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
					showOutput("error creating decision");
				}
			})
		},

		decisionVote : function(vote,decId, callbackFunction, callbackObj) {
			var data = {
					'vote'  : vote,
					'decId' : decId
			};
			var datastr = JSON.stringify(data);

			if(GLOBAL.sessionData.userLogged) {
				$.ajax({
					type : 'POST',
					url : '../json/DecVote',
					data : datastr,
					dataType : 'json',
					contentType : 'application/json',
					success : function(data, textStatus, jqXHR) {
						if (data.res) {
							showOutput(data.res,"DarkGreen");
							callbackFunction.call(callbackObj);
						} else {
							showOutput(data);
						}
					},
					error : function(jqXHR, textStatus, errorThrown) {
						console.log(errorThrown);
						showOutput("error casting vote");
					}
				})
			} else {
				showOutput("please login to vote on this decision");
			}
		},

		decisionGetVote : function(decId, voterUserId, callbackFunction, callbackObj) {
			var data = {
					'decId' : decId,
					'voterUserId'  : voterUserId
			};
			var datastr = JSON.stringify(data);

			if(GLOBAL.sessionData.userLogged) {
				$.ajax({
					type : 'POST',
					url : '../json/DecGetVote',
					data : datastr,
					dataType : 'json',
					contentType : 'application/json',
					success : function(data, textStatus, jqXHR) {
						if (data.thesisDto) {
							callbackFunction.call(callbackObj,data.thesisDto);
						}
					},
					error : function(jqXHR, textStatus, errorThrown) {
						console.log(errorThrown);
						showOutput("error getting vote");
					}
				})
			} else {
				showOutput("plase login to vote on this decision");
			}
		},

		decisionListGet : function(filters,callbackFunction,callbackObj) {

			var data = {
					'filters' : filters
			};
			
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '../json/DecisionListGet',
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

			var data = {
					'id' : argumentId,
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '../json/ArgumentGet',
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

		argumentsGetOfDecision : function(decisionId,callbackFunction,callbackObj) {

			var data = {
					'decisionId' : decisionId
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '../json/ArgumentsGetOfDecision',
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

		argumentNew : function(argDto,callbackFunction,callbackObj) {

			var data = {
					'argumentDto' : argDto
			};

			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '../json/ArgumentNew',
				data : datastr,
				dataType : "json",
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						if (data.res) {
							showOutput(data.msg,"DarkGreen");
							callbackFunction.call(callbackObj,data); 
						} else {
							showOutput(data.msg,"DarkRed");
						}
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		},

		argumentIsBacked : function(argumentId,userId,callbackFunction,callbackObj) {

			var data = {
					'argId' : argumentId,
					'userId' : userId
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '../json/ArgumentIsBacked',
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

		argumentBack : function(argumentId,callbackFunction,callbackObj) {

			var data = {
					'argId' : argumentId
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '../json/ArgumentBack',
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

		argumentUnBack : function(argumentId,callbackFunction,callbackObj) {

			var data = {
					'argId' : argumentId
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '../json/ArgumentUnBack',
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

		reviewsGetOfBid : function(bidId,callbackFunction,callbackObj) {

			var data = {
					'bidId' : bidId
			};
			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '../json/ReviewsGetOfBid',
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

		reviewNew : function(reviewDto,bidId,callbackFunction,callbackObj) {

			var data = {
					'bidId': bidId,
					'reviewDto' : reviewDto
			};

			var datastr = JSON.stringify(data);

			$.ajax({
				type : 'POST',
				url : '../json/ReviewBidNew',
				data : datastr,
				dataType : "json",
				contentType : 'application/json',
				success : function(data, textStatus, jqXHR) {
					if (data) {
						if (data.res) {
							showOutput(data.msg,"DarkGreen");
							callbackFunction.call(callbackObj,data); 
						} else {
							showOutput(data.msg,"DarkRed");
						}
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
		}


};
