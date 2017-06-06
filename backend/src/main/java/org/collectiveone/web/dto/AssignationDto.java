package org.collectiveone.web.dto;

import java.util.ArrayList;
import java.util.List;

public class AssignationDto {
	
	private String type;
	private List<TransferDto> transfers = new ArrayList<TransferDto>();
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<TransferDto> getTransfers() {
		return transfers;
	}
	public void setTransfers(List<TransferDto> transfers) {
		this.transfers = transfers;
	}
	
}
