package org.collectiveone.modules.contexts.dto;

import java.util.ArrayList;
import java.util.List;

import org.collectiveone.modules.contexts.ModelScope;
import org.collectiveone.modules.contexts.enums.ElementConsentPositionColor;
import org.collectiveone.modules.contexts.enums.ElementGovernanceType;
import org.collectiveone.modules.contexts.enums.SimpleConsentState;
import org.collectiveone.modules.users.AppUserDto;

public class ModelCardWrapperDto {

	private String id;
	private String additionId;
	private ModelCardDto card;
	private AppUserDto creator;
	private Long creationDate; 
	private String initiativeId;
	private Integer nLikes;
	private Boolean userLiked;
	private Long lastEdited;
	private List<AppUserDto> editors = new ArrayList<AppUserDto>();
	private List<ModelCardDto> oldVersions = new ArrayList<ModelCardDto>();
	private List<InModelSectionDto> inModelSections = new ArrayList<InModelSectionDto>();
	
	/* for private and shared, position is given relative to another card */
	private ModelScope scope;
	private String beforeElementId;
	private String afterElementId;
	
	/* for governance */
	private ElementGovernanceType newGovernanceType;
	private ElementGovernanceType governanceType;
	private SimpleConsentState simpleConsentState;	
	
	private ElementConsentPositionColor ownPosition;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAdditionId() {
		return additionId;
	}
	public void setAdditionId(String additionId) {
		this.additionId = additionId;
	}
	public ModelCardDto getCard() {
		return card;
	}
	public void setCard(ModelCardDto card) {
		this.card = card;
	}
	public AppUserDto getCreator() {
		return creator;
	}
	public void setCreator(AppUserDto creator) {
		this.creator = creator;
	}
	public List<AppUserDto> getEditors() {
		return editors;
	}
	public void setEditors(List<AppUserDto> editors) {
		this.editors = editors;
	}
	public Long getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Long creationDate) {
		this.creationDate = creationDate;
	}
	public String getInitiativeId() {
		return initiativeId;
	}
	public void setInitiativeId(String initiativeId) {
		this.initiativeId = initiativeId;
	}
	public Integer getnLikes() {
		return nLikes;
	}
	public void setnLikes(Integer nLikes) {
		this.nLikes = nLikes;
	}
	public Boolean getUserLiked() {
		return userLiked;
	}
	public void setUserLiked(Boolean userLiked) {
		this.userLiked = userLiked;
	}
	public Long getLastEdited() {
		return lastEdited;
	}
	public void setLastEdited(Long lastEdited) {
		this.lastEdited = lastEdited;
	}
	public List<ModelCardDto> getOldVersions() {
		return oldVersions;
	}
	public void setOldVersions(List<ModelCardDto> oldVersions) {
		this.oldVersions = oldVersions;
	}
	public List<InModelSectionDto> getInModelSections() {
		return inModelSections;
	}
	public void setInModelSections(List<InModelSectionDto> inModelSections) {
		this.inModelSections = inModelSections;
	}
	public ModelScope getScope() {
		return scope;
	}
	public void setScope(ModelScope scope) {
		this.scope = scope;
	}
	public String getBeforeElementId() {
		return beforeElementId;
	}
	public void setBeforeElementId(String beforeElementId) {
		this.beforeElementId = beforeElementId;
	}
	public String getAfterElementId() {
		return afterElementId;
	}
	public void setAfterElementId(String afterElementId) {
		this.afterElementId = afterElementId;
	}
	public ElementGovernanceType getNewGovernanceType() {
		return newGovernanceType;
	}
	public void setNewGovernanceType(ElementGovernanceType newGovernanceType) {
		this.newGovernanceType = newGovernanceType;
	}
	public ElementGovernanceType getGovernanceType() {
		return governanceType;
	}
	public void setGovernanceType(ElementGovernanceType governanceType) {
		this.governanceType = governanceType;
	}
	public SimpleConsentState getSimpleConsentState() {
		return simpleConsentState;
	}
	public void setSimpleConsentState(SimpleConsentState simpleConsentState) {
		this.simpleConsentState = simpleConsentState;
	}
	public ElementConsentPositionColor getOwnPosition() {
		return ownPosition;
	}
	public void setOwnPosition(ElementConsentPositionColor ownPosition) {
		this.ownPosition = ownPosition;
	}
	
}
