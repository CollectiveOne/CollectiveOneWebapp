package org.collectiveone.modules.contexts.dto;

import java.util.ArrayList;
import java.util.List;

public class PerspectiveDto {
	
	private ContextMetadataDto metadata;
	private List<CardDto> cards = new ArrayList<CardDto>();
	private List<PerspectiveDto> subcontexts = new ArrayList<PerspectiveDto>();
	
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
