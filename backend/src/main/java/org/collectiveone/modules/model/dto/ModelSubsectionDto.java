package org.collectiveone.modules.model.dto;

import org.collectiveone.modules.model.ModelScope;

public class ModelSubsectionDto {
	
	private ModelSectionDto parentSection;
	private ModelSectionDto section;
	private ModelScope scope;
	
	public ModelSectionDto getParentSection() {
		return parentSection;
	}
	public void setParentSection(ModelSectionDto parentSection) {
		this.parentSection = parentSection;
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
