package org.collectiveone.web.dto;

public class UsernameAndPps {
	private String username;
	private double pps;
	
	public UsernameAndPps() {
		
	}
	
	public UsernameAndPps(String username, double pps) {
		this.username = username;
		this.pps = pps;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public double getPps() {
		return pps;
	}
	public void setPps(double pps) {
		this.pps = pps;
	}
}
