package org.collectiveone.web.dto;

import java.util.ArrayList;
import java.util.List;

public class TransferDto {
	private String assetId;
	private String assetName;
	private String senderId;
	private String senderName;
	private String receiverId;
	private String receiverName;
	private double value;
	
	private List<TransferDto> subtransfers = new ArrayList<TransferDto>();
	
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
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public List<TransferDto> getSubtransfers() {
		return subtransfers;
	}
	public void setSubtransfers(List<TransferDto> subtransfers) {
		this.subtransfers = subtransfers;
	}
	
}
