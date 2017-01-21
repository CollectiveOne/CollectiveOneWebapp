package org.collectiveone.web.dto;

public class VoterDto {
	private String username;
	private double maxWeight;
	private double actualWeight;
	private double scale;
	
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
	public double getScale() {
		return scale;
	}
	public void setScale(double scale) {
		this.scale = scale;
	}
}
