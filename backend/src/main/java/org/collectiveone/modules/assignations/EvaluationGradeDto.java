package org.collectiveone.modules.assignations;

import org.collectiveone.modules.users.AppUserDto;

public class EvaluationGradeDto {
	
	private String id;
	private AppUserDto evaluatorUser;
	private AppUserDto receiverUser;
	private String type;
	private String state;
	private double percent;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public AppUserDto getEvaluatorUser() {
		return evaluatorUser;
	}
	public void setEvaluatorUser(AppUserDto evaluatorUser) {
		this.evaluatorUser = evaluatorUser;
	}
	public AppUserDto getReceiverUser() {
		return receiverUser;
	}
	public void setReceiverUser(AppUserDto receiver) {
		this.receiverUser = receiver;
	}
	public String getType() {
		return type;
	}
	public void setType(String evaluationGradeType) {
		this.type = evaluationGradeType;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public double getPercent() {
		return percent;
	}
	public void setPercent(double percent) {
		this.percent = percent;
	}
}
