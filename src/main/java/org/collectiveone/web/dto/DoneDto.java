package org.collectiveone.web.dto;

public class DoneDto {
	private Long bidId;
	private double newPps;
	private String description;
	private String username;
	
	public Long getBidId() {
		return bidId;
	}
	public void setBidId(Long bidId) {
		this.bidId = bidId;
	}
	public double getNewPps() {
		return newPps;
	}
	public void setNewPps(double newPps) {
		this.newPps = newPps;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
