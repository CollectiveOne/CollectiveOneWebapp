package org.collectiveone.modules.activity;

import org.collectiveone.modules.initiatives.InitiativeDto;
import org.collectiveone.modules.tokens.TokenMintDto;
import org.collectiveone.modules.users.AppUserDto;

public class ActivityDto {

	private String type;
	private AppUserDto triggerUser;
	private InitiativeDto initiative;
	private InitiativeDto subInitiative;
	private String oldName;
	private String oldDriver;
	private TokenMintDto mint;
	
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
	
}
