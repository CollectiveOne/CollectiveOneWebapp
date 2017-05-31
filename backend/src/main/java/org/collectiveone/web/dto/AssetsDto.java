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

	private List<TransferDto> transferredToSubinitiatives = new ArrayList<TransferDto>();
	private List<TransferDto> transferredToUsers = new ArrayList<TransferDto>();

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
	public List<TransferDto> getTransferredToSubinitiatives() {
		return transferredToSubinitiatives;
	}
	public void setTransferredToSubinitiatives(List<TransferDto> transferredToSubinitiatives) {
		this.transferredToSubinitiatives = transferredToSubinitiatives;
	}
	public List<TransferDto> getTransferredToUsers() {
		return transferredToUsers;
	}
	public void setTransferredToUsers(List<TransferDto> transferredToUsers) {
		this.transferredToUsers = transferredToUsers;
	}
		
}
