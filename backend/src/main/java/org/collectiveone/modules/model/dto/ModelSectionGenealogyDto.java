package org.collectiveone.modules.model.dto;

import java.util.ArrayList;
import java.util.List;

public class ModelSectionGenealogyDto {

	private ModelSectionDto section;
	private List<ModelSectionGenealogyDto> parents = new ArrayList<ModelSectionGenealogyDto>();
	
	public ModelSectionDto getSection() {
		return section;
	}
	public void setSection(ModelSectionDto section) {
		this.section = section;
	}
	public List<ModelSectionGenealogyDto> getParents() {
		return parents;
	}
	public void setParents(List<ModelSectionGenealogyDto> parents) {
		this.parents = parents;
	}
	
}
