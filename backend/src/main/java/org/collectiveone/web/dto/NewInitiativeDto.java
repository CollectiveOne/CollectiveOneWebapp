package org.collectiveone.web.dto;

import java.util.List;

public class NewInitiativeDto {
	
	private String parentInitiativeId;
	private String name;
	private String driver;
	private List<AppUserWithRoleDto> contributors;
	private double nTokens;
	private String tokenName;
	
	
	public String getParentInitiativeId() {
		return parentInitiativeId;
	}
	public void setParentInitiativeId(String parentInitiativeId) {
		this.parentInitiativeId = parentInitiativeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public List<AppUserWithRoleDto> getContributors() {
		return contributors;
	}
	public void setContributors(List<AppUserWithRoleDto> contributors) {
		this.contributors = contributors;
	}
	public double getnTokens() {
		return nTokens;
	}
	public void setnTokens(double nTokens) {
		this.nTokens = nTokens;
	}
	public String getTokenName() {
		return tokenName;
	}
	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}
	
}
