package org.collectiveone.modules.model.dto;

import java.util.ArrayList;
import java.util.List;

public class ModelCardWrapperDto {

	private String id;
	private ModelCardDto card;
	private Long creationDate; 
	private String initiativeId;
	private Integer nLikes;
	private Boolean userLiked;
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
