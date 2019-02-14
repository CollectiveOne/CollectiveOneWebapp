package org.collectiveone.modules.contexts.dto;

import org.collectiveone.modules.contexts.entities.enums.CommitStatus;

public class CardDto {

	private String base;
	private CommitStatus commitStatus; 
	
	public CardDto() {
		super();
	}

	public CardDto(String base) {
		super();
		this.base = base;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public CommitStatus getCommitStatus() {
		return commitStatus;
	}

	public void setCommitStatus(CommitStatus commitStatus) {
		this.commitStatus = commitStatus;
	}

}
