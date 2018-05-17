package org.collectiveone.modules.model.dto;

import java.util.ArrayList;
import java.util.List;

import org.collectiveone.modules.model.ModelSection;

public class ModelSectionDto {

	private String id;
	private String initiativeId;
	private Boolean isTopModelSection;
	private Boolean isSubsection;
	private String parentSectionId;
	private String parentSectionTitle;
	private String title;
	private String description;
	private Boolean subElementsLoaded;
	private Integer nSubsections;
	private Integer nCards;
	
	private List<ModelCardWrapperDto> cardsWrappersPrivate = new ArrayList<ModelCardWrapperDto>();
	private List<ModelCardWrapperDto> cardsWrappersPersonal = new ArrayList<ModelCardWrapperDto>();
	private List<ModelCardWrapperDto> cardsWrappers = new ArrayList<ModelCardWrapperDto>();
	private List<ModelSectionDto> subsections = new ArrayList<ModelSectionDto>();
	private List<ModelSectionDto> inSections = new ArrayList<ModelSectionDto>();
	
	
	public ModelSection toEntity(ModelSection section, ModelSectionDto sectionDto) {
		
		if (section == null) section = new ModelSection();
		
		section.setTitle(sectionDto.getTitle());
		section.setDescription(sectionDto.getDescription());
		
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
	public Boolean getIsTopModelSection() {
		return isTopModelSection;
	}
	public void setIsTopModelSection(Boolean isTopModelSection) {
		this.isTopModelSection = isTopModelSection;
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
	public List<ModelCardWrapperDto> getCardsWrappersPrivate() {
		return cardsWrappersPrivate;
	}
	public void setCardsWrappersPrivate(List<ModelCardWrapperDto> cardsWrappersPrivate) {
		this.cardsWrappersPrivate = cardsWrappersPrivate;
	}
	public List<ModelCardWrapperDto> getCardsWrappersPersonal() {
		return cardsWrappersPersonal;
	}
	public void setCardsWrappersPersonal(List<ModelCardWrapperDto> cardsWrappersPersonal) {
		this.cardsWrappersPersonal = cardsWrappersPersonal;
	}
	
	
}
