package org.collectiveone.modules.model.dto;

import java.util.ArrayList;
import java.util.List;

import org.collectiveone.modules.model.ModelElementState;
import org.collectiveone.modules.model.ModelSection;

public class ModelSectionDto {

	private String id;
	private String initiativeId;
	private Boolean isSubsection;
	private String parentSectionId;
	private String parentSectionTitle;
	private String viewId;
	private String title;
	private String description;
	private Boolean subElementsLoaded;
	private Integer nSubsections;
	private Integer nCards;
	
	private List<ModelCardWrapperDto> cardsWrappers = new ArrayList<ModelCardWrapperDto>();
	private List<ModelSectionDto> subsections = new ArrayList<ModelSectionDto>();
	
	private List<ModelSectionDto> inSections = new ArrayList<ModelSectionDto>();
	private List<ModelViewDto> inViews = new ArrayList<ModelViewDto>();
	
	
	public ModelSection toEntity(ModelSection section, ModelSectionDto sectionDto) {
		
		if (section == null) section = new ModelSection();
		
		section.setTitle(sectionDto.getTitle());
		section.setDescription(sectionDto.getDescription());
		section.setElementState(ModelElementState.ACTIVE);
		
		return section;
	}
	
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
	public String getParentSectionTitle() {
		return parentSectionTitle;
	}
	public void setParentSectionTitle(String parentSectionTitle) {
		this.parentSectionTitle = parentSectionTitle;
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
	public List<ModelCardWrapperDto> getCardsWrappers() {
		return cardsWrappers;
	}
	public void setCardsWrappers(List<ModelCardWrapperDto> cardsWrappers) {
		this.cardsWrappers = cardsWrappers;
	}
	public List<ModelSectionDto> getSubsections() {
		return subsections;
	}
	public void setSubsections(List<ModelSectionDto> subsections) {
		this.subsections = subsections;
	}
	public Boolean getSubElementsLoaded() {
		return subElementsLoaded;
	}
	public void setSubElementsLoaded(Boolean subElementsLoaded) {
		this.subElementsLoaded = subElementsLoaded;
	}
	public Integer getnSubsections() {
		return nSubsections;
	}
	public void setnSubsections(Integer nSubsections) {
		this.nSubsections = nSubsections;
	}
	public Integer getnCards() {
		return nCards;
	}
	public void setnCards(Integer nCards) {
		this.nCards = nCards;
	}
	public List<ModelSectionDto> getInSections() {
		return inSections;
	}
	public void setInSections(List<ModelSectionDto> inSections) {
		this.inSections = inSections;
	}
	public List<ModelViewDto> getInViews() {
		return inViews;
	}
	public void setInViews(List<ModelViewDto> inViews) {
		this.inViews = inViews;
	}
	
	
}
