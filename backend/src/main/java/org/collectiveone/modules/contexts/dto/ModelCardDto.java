package org.collectiveone.modules.contexts.dto;

import org.collectiveone.modules.contexts.ModelCard;
import org.collectiveone.modules.contexts.ModelScope;
import org.collectiveone.modules.files.FileStored;
import org.collectiveone.modules.files.FileStoredDto;

public class ModelCardDto {

	private String id;
	private String title;
	private String text;
	private String newImageFileId;
	private ModelScope newScope;
	private FileStoredDto imageFile;
	
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
	public ModelScope getNewScope() {
		return newScope;
	}
	public void setNewScope(ModelScope newScope) {
		this.newScope = newScope;
	}
	public FileStoredDto getImageFile() {
		return imageFile;
	}
	public void setImageFile(FileStoredDto imageFile) {
		this.imageFile = imageFile;
	}
	
}
