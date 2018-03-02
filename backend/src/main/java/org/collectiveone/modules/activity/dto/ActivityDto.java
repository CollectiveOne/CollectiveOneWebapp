package org.collectiveone.modules.activity.dto;

import org.collectiveone.modules.assignations.dto.AssignationDto;
import org.collectiveone.modules.conversations.MessageDto;
import org.collectiveone.modules.initiatives.dto.InitiativeDto;
import org.collectiveone.modules.model.dto.ModelCardWrapperDto;
import org.collectiveone.modules.model.dto.ModelSectionDto;
import org.collectiveone.modules.tokens.dto.TokenMintDto;
import org.collectiveone.modules.tokens.dto.TransferDto;
import org.collectiveone.modules.users.AppUserDto;

public class ActivityDto {

	private String id;
	private String type;
	private Long timestamp;
	private AppUserDto triggerUser;
	private InitiativeDto initiative;
	private InitiativeDto subInitiative;
	private String oldName;
	private String oldDriver;
	private TokenMintDto mint;
	private AssignationDto assignation;
	private TransferDto transfer;
	private ModelSectionDto modelSection;
	private ModelCardWrapperDto modelCardWrapper;
	
	private ModelSectionDto onSection;
	private ModelSectionDto fromSection;
	
	private MessageDto message;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
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
	public ModelSectionDto getModelSection() {
		return modelSection;
	}
	public void setModelSection(ModelSectionDto modelSection) {
		this.modelSection = modelSection;
	}
	public ModelCardWrapperDto getModelCardWrapper() {
		return modelCardWrapper;
	}
	public void setModelCardWrapper(ModelCardWrapperDto modelCardWrapper) {
		this.modelCardWrapper = modelCardWrapper;
	}
	public ModelSectionDto getOnSection() {
		return onSection;
	}
	public void setOnSection(ModelSectionDto onSection) {
		this.onSection = onSection;
	}
	public ModelSectionDto getFromSection() {
		return fromSection;
	}
	public void setFromSection(ModelSectionDto fromSection) {
		this.fromSection = fromSection;
	}
	public MessageDto getMessage() {
		return message;
	}
	public void setMessage(MessageDto message) {
		this.message = message;
	}
	
}
