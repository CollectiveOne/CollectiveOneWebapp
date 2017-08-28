package org.collectiveone.modules.activity;

import org.collectiveone.modules.assignations.AssignationDto;
import org.collectiveone.modules.initiatives.InitiativeDto;
import org.collectiveone.modules.tokens.TokenMintDto;
import org.collectiveone.modules.tokens.TransferDto;
import org.collectiveone.modules.users.AppUserDto;

public class ActivityDto {

	private String type;
	private AppUserDto triggerUser;
	private InitiativeDto initiative;
	private InitiativeDto subInitiative;
	private String oldName;
	private String oldDriver;
	private TokenMintDto mint;
	private AssignationDto assignation;
	private TransferDto transfer;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public AppUserDto getTriggerUser() {
		return triggerUser;
	}
	public void setTriggerUser(AppUserDto triggerUser) {
		this.triggerUser = triggerUser;
	}
	public InitiativeDto getInitiative() {
		return initiative;
	}
	public void setInitiative(InitiativeDto initiative) {
		this.initiative = initiative;
	}
	public InitiativeDto getSubInitiative() {
		return subInitiative;
	}
	public void setSubInitiative(InitiativeDto subInitiative) {
		this.subInitiative = subInitiative;
	}
	public String getOldName() {
		return oldName;
	}
	public void setOldName(String oldName) {
		this.oldName = oldName;
	}
	public String getOldDriver() {
		return oldDriver;
	}
	public void setOldDriver(String oldDriver) {
		this.oldDriver = oldDriver;
	}
	public TokenMintDto getMint() {
		return mint;
	}
	public void setMint(TokenMintDto mint) {
		this.mint = mint;
	}
	public AssignationDto getAssignation() {
		return assignation;
	}
	public void setAssignation(AssignationDto assignation) {
		this.assignation = assignation;
	}
	public TransferDto getTransfer() {
		return transfer;
	}
	public void setTransfer(TransferDto transfer) {
		this.transfer = transfer;
	}
	
}
