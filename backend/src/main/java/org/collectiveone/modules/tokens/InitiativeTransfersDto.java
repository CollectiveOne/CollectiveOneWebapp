package org.collectiveone.modules.tokens;

import java.util.ArrayList;
import java.util.List;

public class InitiativeTransfersDto {
	
	private String initiativeId;
	private String initiativeName;
	private List<TransferDto> transfers = new ArrayList<TransferDto>();
	private List<InitiativeTransfersDto> subinitiativesTransfers = new ArrayList<InitiativeTransfersDto>();
	
	
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
	public List<TransferDto> getTransfers() {
		return transfers;
	}
	public void setTransfers(List<TransferDto> transfers) {
		this.transfers = transfers;
	}
	public List<InitiativeTransfersDto> getSubinitiativesTransfers() {
		return subinitiativesTransfers;
	}
	public void setSubinitiativesTransfers(List<InitiativeTransfersDto> subinitiativesTransfers) {
		this.subinitiativesTransfers = subinitiativesTransfers;
	}

}
