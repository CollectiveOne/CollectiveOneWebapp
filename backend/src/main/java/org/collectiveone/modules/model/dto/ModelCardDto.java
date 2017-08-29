package org.collectiveone.modules.model.dto;

import org.collectiveone.modules.model.ModelCard;

public class ModelCardDto {

	private String id;
	private String initiativeId;
	private String sectionId;
	private String title;
	private String text;
	private String state;
	private Long targetDate;
	
	public ModelCard toEntity(ModelCard card, ModelCardDto cardDto) {
		
		if (card == null) card = new ModelCard();
		
		card.setTitle(cardDto.getTitle());
		card.setText(cardDto.getText());
				
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
	
}
