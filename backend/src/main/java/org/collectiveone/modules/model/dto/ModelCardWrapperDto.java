package org.collectiveone.modules.model.dto;

import java.util.ArrayList;
import java.util.List;

public class ModelCardWrapperDto {

	private String id;
	private ModelCardDto card;
	private Boolean stateControl;
	private Boolean userLiked;
	private String state;
	private String initiativeId;
	private Long targetDate; 
	private List<ModelCardDto> oldVersions;
	private List<ModelSectionDto> inSections = new ArrayList<ModelSectionDto>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ModelCardDto getCard() {
		return card;
	}
	public void setCard(ModelCardDto card) {
		this.card = card;
	}
	public Boolean getStateControl() {
		return stateControl;
	}
	public void setStateControl(Boolean stateControl) {
		this.stateControl = stateControl;
	}
	public Boolean getUserLiked() {
		return userLiked;
	}
	public void setUserLiked(Boolean userLiked) {
		this.userLiked = userLiked;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getInitiativeId() {
		return initiativeId;
	}
	public void setInitiativeId(String initiativeId) {
		this.initiativeId = initiativeId;
	}
	public Long getTargetDate() {
		return targetDate;
	}
	public void setTargetDate(Long targetDate) {
		this.targetDate = targetDate;
	}
	public List<ModelCardDto> getOldVersions() {
		return oldVersions;
	}
	public void setOldVersions(List<ModelCardDto> oldVersions) {
		this.oldVersions = oldVersions;
	}
	public List<ModelSectionDto> getInSections() {
		return inSections;
	}
	public void setInSections(List<ModelSectionDto> inSections) {
		this.inSections = inSections;
	}
	
}
