package org.collectiveone.web.dto;

public class AutocompleteDto {
	private String anchor;
	private String label;
	
	public AutocompleteDto(String anchor, String label) {
		this.anchor = anchor;
		this.label = label;
	}
	public String getAnchor() {
		return anchor;
	}
	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
}
