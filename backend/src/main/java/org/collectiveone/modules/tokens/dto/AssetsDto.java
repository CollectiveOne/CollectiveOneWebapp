package org.collectiveone.modules.tokens.dto;

import java.util.ArrayList;
import java.util.List;

public class AssetsDto {
	private String assetId;
	private String assetName;
	private String status;
	private double totalExistingTokens;
	
	private String holderId;
	private String holderName;
	private double ownedByThisHolder;

	private double totalTransferredToSubinitiatives;
	private double totalTransferredToUsers;
	private double totalPending;
	private double totalUnderThisHolder;
	
	private List<TransferDto> transferredToSubinitiatives = new ArrayList<TransferDto>();
	private List<TransferDto> transferredToUsers = new ArrayList<TransferDto>();
	private List<TransferDto> transfersPending = new ArrayList<TransferDto>();
	

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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public double getTotalTransferredToSubinitiatives() {
		return totalTransferredToSubinitiatives;
	}
	public void setTotalTransferredToSubinitiatives(double totalTransferredToSubinitiatives) {
		this.totalTransferredToSubinitiatives = totalTransferredToSubinitiatives;
	}
	public double getTotalTransferredToUsers() {
		return totalTransferredToUsers;
	}
	public void setTotalTransferredToUsers(double totalTransferredToUsers) {
		this.totalTransferredToUsers = totalTransferredToUsers;
	}
	public double getTotalPending() {
		return totalPending;
	}
	public void setTotalPending(double totalPending) {
		this.totalPending = totalPending;
	}
	public double getTotalUnderThisHolder() {
		return totalUnderThisHolder;
	}
	public void setTotalUnderThisHolder(double totalUnderThisHolder) {
		this.totalUnderThisHolder = totalUnderThisHolder;
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
	public List<TransferDto> getTransfersPending() {
		return transfersPending;
	}
	public void setTransfersPending(List<TransferDto> transfersPending) {
		this.transfersPending = transfersPending;
	}
	
}
