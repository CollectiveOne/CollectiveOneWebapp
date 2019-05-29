package org.collectiveone.modules.c1.views;

public class ViewDto {
	
	private String elementId;
	private String inElementId;
	private ElementViewType elementViewType;
	
	public ViewDto() {
		super();
	}
	public ViewDto(String elementId, String inElementId, ElementViewType elementViewType) {
		super();
		this.elementId = elementId;
		this.inElementId = inElementId;
		this.elementViewType = elementViewType;
	}
	public String getElementId() {
		return elementId;
	}
	public void setElementId(String elementId) {
		this.elementId = elementId;
	}
	public String getInElementId() {
		return inElementId;
	}
	public void setInElementId(String inElementId) {
		this.inElementId = inElementId;
	}
	public ElementViewType getElementViewType() {
		return elementViewType;
	}
	public void setElementViewType(ElementViewType elementViewType) {
		this.elementViewType = elementViewType;
	}
	
}
