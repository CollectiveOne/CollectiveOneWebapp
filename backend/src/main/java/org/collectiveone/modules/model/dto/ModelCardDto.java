package org.collectiveone.modules.model.dto;

import java.util.ArrayList;
import java.util.List;

import org.collectiveone.modules.files.FileStored;
import org.collectiveone.modules.files.FileStoredDto;
import org.collectiveone.modules.model.ModelCard;

public class ModelCardDto {

	private String id;
	private String initiativeId;
	private String sectionId;
	private String title;
	private String text;
	private String newImageFileId;
	private FileStoredDto imageFile;
	private Integer ixInSection;
	private List<ModelSectionDto> inSections = new ArrayList<ModelSectionDto>();
	
	public ModelCard toEntity(ModelCard card, ModelCardDto cardDto, FileStored imageFile) {
		
		if (card == null) card = new ModelCard();
		
		card.setTitle(cardDto.getTitle());
		card.setText(cardDto.getText());
		if (imageFile != null) card.setImageFile(imageFile);
				
		return card;
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
	public String getSectionId() {
		return sectionId;
	}
	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getNewImageFileId() {
		return newImageFileId;
	}
	public void setNewImageFileId(String newImageFileId) {
		this.newImageFileId = newImageFileId;
	}
	public FileStoredDto getImageFile() {
		return imageFile;
	}
	public void setImageFile(FileStoredDto imageFile) {
		this.imageFile = imageFile;
	}
	public Integer getIxInSection() {
		return ixInSection;
	}
	public void setIxInSection(Integer ixInSection) {
		this.ixInSection = ixInSection;
	}
	public List<ModelSectionDto> getInSections() {
		return inSections;
	}
	public void setInSections(List<ModelSectionDto> inSections) {
		this.inSections = inSections;
	}
	
}
