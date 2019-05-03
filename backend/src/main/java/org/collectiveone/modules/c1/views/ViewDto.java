package org.collectiveone.modules.c1.views;

public class ViewDto {
	
	private ContextViewType contextViewType; 
	private ElementViewType elementViewType;
	
	public ContextViewType getContextViewType() {
		return contextViewType;
	}
	public void setContextViewType(ContextViewType contextViewType) {
		this.contextViewType = contextViewType;
	}
	public ElementViewType getElementViewType() {
		return elementViewType;
	}
	public void setElementViewType(ElementViewType elementViewType) {
		this.elementViewType = elementViewType;
	}
	
}
