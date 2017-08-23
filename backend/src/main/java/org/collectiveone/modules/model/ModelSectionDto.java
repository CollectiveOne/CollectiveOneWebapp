package org.collectiveone.modules.model;

import java.util.ArrayList;
import java.util.List;

public class ModelSectionDto {

	private String id;
	private String initiativeId;
	private Boolean isSubsection;
	private String parentSectionId;
	private String viewId;
	private String title;
	private String description;
	private List<ModelCardDto> cards = new ArrayList<ModelCardDto>();
	private List<ModelSectionDto> subsections = new ArrayList<ModelSectionDto>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInitiativeId() {
		return initiativeId;
	}
	public void setInitiativeId(String initiativeId) {
		this.initiativeId = initiativeId;
	}
	public Boolean getIsSubsection() {
		return isSubsection;
	}
	public void setIsSubsection(Boolean isSubsection) {
		this.isSubsection = isSubsection;
	}
	public String getParentSectionId() {
		return parentSectionId;
	}
	public void setParentSectionId(String parentSectionId) {
		this.parentSectionId = parentSectionId;
	}
	public String getViewId() {
		return viewId;
	}
	public void setViewId(String viewId) {
		this.viewId = viewId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<ModelCardDto> getCards() {
		return cards;
	}
	public void setCards(List<ModelCardDto> cards) {
		this.cards = cards;
	}
	public List<ModelSectionDto> getSubsections() {
		return subsections;
	}
	public void setSubsections(List<ModelSectionDto> subsections) {
		this.subsections = subsections;
	}
	
}
