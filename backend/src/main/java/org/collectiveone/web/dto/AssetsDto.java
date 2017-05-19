package org.collectiveone.web.dto;

import java.util.ArrayList;
import java.util.List;

public class AssetsDto {
	private String assetId;
	private String assetName;
	private double totalExistingTokens;
	private String holderId;
	private String holderName;
	private double ownedByThisHolder;

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
	public String getHolderId() {
		return holderId;
	}
	public void setHolderId(String initiativeId) {
		this.holderId = initiativeId;
	}
	public String getHolderName() {
		return holderName;
	}
	public void setHolderName(String initiativeName) {
		this.holderName = initiativeName;
	}
	public double getOwnedByThisHolder() {
		return ownedByThisHolder;
	}
	public void setOwnedByThisHolder(double ownedByThisHolder) {
		this.ownedByThisHolder = ownedByThisHolder;
	}
	public List<AssetsDto> getOwnedBySubinitiatives() {
		return ownedBySubinitiatives;
	}
	public void setOwnedBySubinitiatives(List<AssetsDto> ownedBySubinitiatives) {
		this.ownedBySubinitiatives = ownedBySubinitiatives;
	}
	
}
