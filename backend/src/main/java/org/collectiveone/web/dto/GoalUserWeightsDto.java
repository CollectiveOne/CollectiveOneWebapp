package org.collectiveone.web.dto;

public class GoalUserWeightsDto {
	
	private String username;
	private double maxWeight;
	private double actualWeight;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public double getMaxWeight() {
		return maxWeight;
	}
	public void setMaxWeight(double maxWeight) {
		this.maxWeight = maxWeight;
	}
	public double getActualWeight() {
		return actualWeight;
	}
	public void setActualWeight(double actualWeight) {
		this.actualWeight = actualWeight;
	}
	
}