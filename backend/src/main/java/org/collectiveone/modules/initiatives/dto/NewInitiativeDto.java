package org.collectiveone.modules.initiatives.dto;

import java.util.List;

import org.collectiveone.modules.tokens.AssetsDto;
import org.collectiveone.modules.tokens.TransferDto;

public class NewInitiativeDto {
	
	private boolean asSubinitiative;
	private String parentInitiativeId;
	private String name;
	private String driver;
	private String newImageFileId;
	private String color;
	private Boolean modelEnabled;
	private String visibility;
	private List<MemberDto> members;
	private AssetsDto ownTokens;
	private List<TransferDto> assetsTransfers;
	private List<InitiativeTagDto> tags;
	
	
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
	public String getNewImageFileId() {
		return newImageFileId;
	}
	public void setNewImageFileId(String newImageFileId) {
		this.newImageFileId = newImageFileId;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Boolean getModelEnabled() {
		return modelEnabled;
	}
	public void setModelEnabled(Boolean modelEnabled) {
		this.modelEnabled = modelEnabled;
	}
	public String getVisibility() {
		return visibility;
	}
	public void setVisibility(String visibility) {
		this.visibility = visibility;
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
	public List<TransferDto> getAssetsTransfers() {
		return assetsTransfers;
	}
	public void setAssetsTransfers(List<TransferDto> assetsTransfers) {
		this.assetsTransfers = assetsTransfers;
	}
	public List<InitiativeTagDto> getTags() {
		return tags;
	}
	public void setTags(List<InitiativeTagDto> tags) {
		this.tags = tags;
	}
	
}
