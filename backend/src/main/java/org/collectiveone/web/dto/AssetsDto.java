package org.collectiveone.web.dto;

import java.util.ArrayList;
import java.util.List;

public class AssetsDto {
	private String assetId;
	private String assetName;
	private double totalExistingTokens;
	private String initiativeId;
	private String initiativeName;
	private double ownedByThisInitiative;
	private List<AssetsDto> ownedBySubinitiatives = new ArrayList<AssetsDto>();
	
	public String getAssetId() {
		return assetId;
	}
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	public String getAssetName() {
		return assetName;
	}
	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}
	public double getTotalExistingTokens() {
		return totalExistingTokens;
	}
	public void setTotalExistingTokens(double totalExistingTokens) {
		this.totalExistingTokens = totalExistingTokens;
	}
	public String getInitiativeId() {
		return initiativeId;
	}
	public void setInitiativeId(String initiativeId) {
		this.initiativeId = initiativeId;
	}
	public String getInitiativeName() {
		return initiativeName;
	}
	public void setInitiativeName(String initiativeName) {
		this.initiativeName = initiativeName;
	}
	public double getOwnedByThisInitiative() {
		return ownedByThisInitiative;
	}
	public void setOwnedByThisInitiative(double ownedByThisInitiative) {
		this.ownedByThisInitiative = ownedByThisInitiative;
	}
	public List<AssetsDto> getOwnedBySubinitiatives() {
		return ownedBySubinitiatives;
	}
	public void setOwnedBySubinitiatives(List<AssetsDto> ownedBySubinitiatives) {
		this.ownedBySubinitiatives = ownedBySubinitiatives;
	}
}
