package org.collectiveone.modules.assignations;

import org.collectiveone.modules.users.AppUserDto;

public class ReceiverDto {
	
	private String id;
	private AppUserDto user;
	private double evaluatedPercent;
	private double percent;
	private String state;
	private boolean isDonor;
	private double selfBias;
	private boolean revertApproval;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public AppUserDto getUser() {
		return user;
	}
	public void setUser(AppUserDto user) {
		this.user = user;
	}
	public double getEvaluatedPercent() {
		return evaluatedPercent;
	}
	public void setEvaluatedPercent(double evaluatedPercent) {
		this.evaluatedPercent = evaluatedPercent;
	}
	public double getPercent() {
		return percent;
	}
	public void setPercent(double percent) {
		this.percent = percent;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public boolean getIsDonor() {
		return isDonor;
	}
	public void setIsDonor(boolean isDonor) {
		this.isDonor = isDonor;
	}
	public double getSelfBias() {
		return selfBias;
	}
	public void setSelfBias(double selfBias) {
		this.selfBias = selfBias;
	}
	public boolean isRevertApproval() {
		return revertApproval;
	}
	public void setRevertApproval(boolean revertApproval) {
		this.revertApproval = revertApproval;
	}
	
	
}
