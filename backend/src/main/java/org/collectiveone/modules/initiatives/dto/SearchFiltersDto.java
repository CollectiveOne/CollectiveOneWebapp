package org.collectiveone.modules.initiatives.dto;

import java.util.ArrayList;
import java.util.List;

public class SearchFiltersDto {
	
	private String query;
	private List<InitiativeTagDto> tags = new ArrayList<InitiativeTagDto>();

	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public List<InitiativeTagDto> getTags() {
		return tags;
	}
	public void setTags(List<InitiativeTagDto> tags) {
		this.tags = tags;
	}
	
}
