package org.collectiveone.web.dto;

import java.util.List;

public class ProjectContributorsDto {
	private List<UsernameAndPps> usernamesAndPps;
	private double ppsTot;
	
	public List<UsernameAndPps> getUsernamesAndPps() {
		return usernamesAndPps;
	}
	public void setUsernamesAndPps(List<UsernameAndPps> usernamesAndPps) {
		this.usernamesAndPps = usernamesAndPps;
	}
	public double getPpsTot() {
		return ppsTot;
	}
	public void setPpsTot(double ppsTot) {
		this.ppsTot = ppsTot;
	}
	
}
