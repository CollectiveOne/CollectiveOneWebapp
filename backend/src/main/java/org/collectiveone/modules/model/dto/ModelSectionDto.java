package org.collectiveone.modules.model.dto;

import java.util.ArrayList;
import java.util.List;

import org.collectiveone.modules.model.ModelScope;
import org.collectiveone.modules.model.ModelSection;

public class ModelSectionDto {

	private String id;
	private String initiativeId;
	private Boolean isTopModelSection;
	private Boolean isSubsection;
	private ModelScope scope;
	private String parentSectionId;
	private String parentSectionTitle;
	private String title;
	private String description;
	private ModelScope newScope;
	private Boolean subElementsLoaded;
	
	private List<ModelCardWrapperDto> cardsWrappersPrivate = new ArrayList<ModelCardWrapperDto>();
	private List<ModelCardWrapperDto> cardsWrappersShared = new ArrayList<ModelCardWrapperDto>();
	private List<ModelCardWrapperDto> cardsWrappersCommon = new ArrayList<ModelCardWrapperDto>();
	
	private List<ModelSectionDto> subsectionsCommon = new ArrayList<ModelSectionDto>();
	private List<ModelSectionDto> subsectionsShared = new ArrayList<ModelSectionDto>();
	private List<ModelSectionDto> subsectionsPrivate = new ArrayList<ModelSectionDto>();
	
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
	public ModelScope getScope() {
		return scope;
	}
	public void setScope(ModelScope scope) {
		this.scope = scope;
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
	public ModelScope getNewScope() {
		return newScope;
	}
	public void setNewScope(ModelScope newScope) {
		this.newScope = newScope;
	}
	public List<ModelCardWrapperDto> getCardsWrappersCommon() {
		return cardsWrappersCommon;
	}
	public void setCardsWrappersCommon(List<ModelCardWrapperDto> cardsWrappersCommon) {
		this.cardsWrappersCommon = cardsWrappersCommon;
	}
	public List<ModelSectionDto> getSubsectionsCommon() {
		return subsectionsCommon;
	}
	public void setSubsectionsCommon(List<ModelSectionDto> subsectionsCommon) {
		this.subsectionsCommon = subsectionsCommon;
	}
	public List<ModelSectionDto> getSubsectionsShared() {
		return subsectionsShared;
	}
	public void setSubsectionsShared(List<ModelSectionDto> subsectionsShared) {
		this.subsectionsShared = subsectionsShared;
	}
	public List<ModelSectionDto> getSubsectionsPrivate() {
		return subsectionsPrivate;
	}
	public void setSubsectionsPrivate(List<ModelSectionDto> subsectionsPrivate) {
		this.subsectionsPrivate = subsectionsPrivate;
	}
	public Boolean getSubElementsLoaded() {
		return subElementsLoaded;
	}
	public void setSubElementsLoaded(Boolean subElementsLoaded) {
		this.subElementsLoaded = subElementsLoaded;
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
	public List<ModelCardWrapperDto> getCardsWrappersShared() {
		return cardsWrappersShared;
	}
	public void setCardsWrappersShared(List<ModelCardWrapperDto> cardsWrappersShared) {
		this.cardsWrappersShared = cardsWrappersShared;
	}
	
	
}
