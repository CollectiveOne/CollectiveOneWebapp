package org.collectiveone.modules.model.dto;

import java.util.ArrayList;
import java.util.List;

public class ModelSectionLinkedDto {

	private ModelSectionDto section;
	private List<ModelSectionLinkedDto> parents = new ArrayList<ModelSectionLinkedDto>();
	private List<ModelSectionLinkedDto> children = new ArrayList<ModelSectionLinkedDto>();
	
	public ModelSectionDto getSection() {
		return section;
	}
	public void setSection(ModelSectionDto section) {
		this.section = section;
	}
	public List<ModelSectionLinkedDto> getParents() {
		return parents;
	}
	public void setParents(List<ModelSectionLinkedDto> parents) {
		this.parents = parents;
	}
	public List<ModelSectionLinkedDto> getChildren() {
		return children;
	}
	public void setChildren(List<ModelSectionLinkedDto> children) {
		this.children = children;
	}
	
	
}
