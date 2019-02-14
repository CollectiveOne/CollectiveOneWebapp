package org.collectiveone.modules.contexts.dto;

import java.util.ArrayList;
import java.util.List;

import org.collectiveone.modules.contexts.entities.enums.CommitStatus;

public class PerspectiveDto {
	
	private String id;
	private String headId;
	private CommitStatus commitStatus; 
	private ContextMetadataDto metadata;
	private List<CardDto> cards = new ArrayList<CardDto>();
	private List<PerspectiveDto> subcontexts = new ArrayList<PerspectiveDto>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHeadId() {
		return headId;
	}
	public void setHeadId(String headId) {
		this.headId = headId;
	}
	public CommitStatus getCommitStatus() {
		return commitStatus;
	}
	public void setCommitStatus(CommitStatus commitStatus) {
		this.commitStatus = commitStatus;
	}
	public ContextMetadataDto getMetadata() {
		return metadata;
	}
	public void setMetadata(ContextMetadataDto metadata) {
		this.metadata = metadata;
	}
	public List<CardDto> getCards() {
		return cards;
	}
	public void setCards(List<CardDto> cards) {
		this.cards = cards;
	}
	public List<PerspectiveDto> getSubcontexts() {
		return subcontexts;
	}
	public void setSubcontexts(List<PerspectiveDto> subcontexts) {
		this.subcontexts = subcontexts;
	}
	
}
