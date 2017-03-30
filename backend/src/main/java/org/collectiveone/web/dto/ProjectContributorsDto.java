package org.collectiveone.web.dto;

import java.util.List;

public class ProjectContributorsDto {
	private List<UsernameAndData> usernamesAndData;
	private double ppsTot;
	
	public List<UsernameAndData> getUsernamesAndData() {
		return usernamesAndData;
	}
	public void setUsernamesAndData(List<UsernameAndData> usernamesAndData) {
		this.usernamesAndData = usernamesAndData;
	}
	public double getPpsTot() {
		return ppsTot;
	}
	public void setPpsTot(double ppsTot) {
		this.ppsTot = ppsTot;
	}
	
}
