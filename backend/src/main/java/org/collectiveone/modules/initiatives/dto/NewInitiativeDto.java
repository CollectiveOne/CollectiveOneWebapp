package org.collectiveone.modules.initiatives.dto;

import java.util.List;

import org.collectiveone.modules.tokens.dto.AssetsDto;
import org.collectiveone.modules.tokens.dto.TransferDto;

public class NewInitiativeDto {
	
	private boolean asSubinitiative;
	private String parentInitiativeId;
	private String name;
	private String driver;
	private List<MemberDto> members;
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
	public List<MemberDto> getMembers() {
		return members;
	}
	public void setMembers(List<MemberDto> members) {
		this.members = members;
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
