package org.collectiveone.modules.model.dto;

import java.util.List;

public class ModelCardWrapperDto {

	private String id;
	private ModelCardDto card;
	private String state;
	private Long targetDate; 
	private List<ModelCardDto> oldVersions;
	
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
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
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
	
}
