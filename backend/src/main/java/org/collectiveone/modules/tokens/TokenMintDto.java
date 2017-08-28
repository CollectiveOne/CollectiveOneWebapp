package org.collectiveone.modules.tokens;

import org.collectiveone.modules.users.AppUserDto;

public class TokenMintDto {

	private String tokenId;
	private String tokenName;
	private double value;
	private AppUserDto orderedBy;
	private String motive;
	private String notes;
	
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	public String getTokenName() {
		return tokenName;
	}
	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public AppUserDto getOrderedBy() {
		return orderedBy;
	}
	public void setOrderedBy(AppUserDto orderedBy) {
		this.orderedBy = orderedBy;
	}
	public String getMotive() {
		return motive;
	}
	public void setMotive(String motive) {
		this.motive = motive;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
		
}
