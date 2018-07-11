package org.collectiveone.modules.model.dto;

import org.collectiveone.modules.model.enums.SemaphoreState;
import org.collectiveone.modules.users.AppUserDto;

public class ElementSemaphoreDto {
	private String id;
	private String elementId;
	private AppUserDto author;
	private SemaphoreState state;
	
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
	public SemaphoreState getState() {
		return state;
	}
	public void setState(SemaphoreState state) {
		this.state = state;
	}
	
	
}
