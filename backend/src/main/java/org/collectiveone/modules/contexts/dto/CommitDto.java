package org.collectiveone.modules.contexts.dto;

public class CommitDto {

	private String id;
	private String message;
	
	public CommitDto() {
		super();
	}
	
	public CommitDto(String message) {
		super();
		this.message = message;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
