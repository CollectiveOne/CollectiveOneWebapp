package org.collectiveone.modules.model.dto;

import org.collectiveone.modules.model.ModelScope;

public class ModelCardWrapperAdditionDto {
	
	private ModelCardWrapperDto cardWrapper;
	private ModelSectionDto section;
	private ModelScope scope;
	
	public ModelCardWrapperDto getCardWrapper() {
		return cardWrapper;
	}
	public void setCardWrapper(ModelCardWrapperDto cardWrapper) {
		this.cardWrapper = cardWrapper;
	}
	public ModelSectionDto getSection() {
		return section;
	}
	public void setSection(ModelSectionDto section) {
		this.section = section;
	}
	public ModelScope getScope() {
		return scope;
	}
	public void setScope(ModelScope scope) {
		this.scope = scope;
	}
	
}
