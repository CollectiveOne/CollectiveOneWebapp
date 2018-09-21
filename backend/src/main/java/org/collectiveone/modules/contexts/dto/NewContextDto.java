package org.collectiveone.modules.contexts.dto;

public class NewContextDto {

	private String title;
	private String description;
	
	public NewContextDto() {
		super();
	}
	
	public NewContextDto(String title, String description) {
		super();
		this.title = title;
		this.description = description;
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
	
}
