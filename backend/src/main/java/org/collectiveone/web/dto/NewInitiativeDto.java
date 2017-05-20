package org.collectiveone.web.dto;

import java.util.List;

public class NewInitiativeDto {
	
	private boolean asSubinitiative;
	private String parentInitiativeId;
	private String name;
	private String driver;
	private List<AppUserWithRoleDto> contributors;
	private AssetsDto ownTokens;
	private List<TransferDto> otherAssetsTransfers;
	
	
	public boolean getAsSubinitiative() {
		return asSubinitiative;
	}
	public void setAsSubinitiative(boolean asSubinitiative) {
		this.asSubinitiative = asSubinitiative;
	}
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
	public AssetsDto getOwnTokens() {
		return ownTokens;
	}
	public void setOwnTokens(AssetsDto ownTokens) {
		this.ownTokens = ownTokens;
	}
	public List<TransferDto> getOtherAssetsTransfers() {
		return otherAssetsTransfers;
	}
	public void setOtherAssetsTransfers(List<TransferDto> otherAssetsTransfers) {
		this.otherAssetsTransfers = otherAssetsTransfers;
	}
	
}
