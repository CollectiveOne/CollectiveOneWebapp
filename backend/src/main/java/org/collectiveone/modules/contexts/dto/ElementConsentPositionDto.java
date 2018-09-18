package org.collectiveone.modules.contexts.dto;

import org.collectiveone.modules.contexts.enums.ElementConsentPositionColor;
import org.collectiveone.modules.users.AppUserDto;

public class ElementConsentPositionDto {
	private String id;
	private String elementId;
	private AppUserDto author;
	private ElementConsentPositionColor positionColor;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getElementId() {
		return elementId;
	}
	public void setElementId(String elementId) {
		this.elementId = elementId;
	}
	public AppUserDto getAuthor() {
		return author;
	}
	public void setAuthor(AppUserDto author) {
		this.author = author;
	}
	public ElementConsentPositionColor getPositionColor() {
		return positionColor;
	}
	public void setPositionColor(ElementConsentPositionColor positionColor) {
		this.positionColor = positionColor;
	}
	
}
