package org.collectiveone.modules.contexts.dto;

import java.util.ArrayList;
import java.util.List;

public class SubsectionsHolderDto {
	
	private List<ModelSectionDto> subsectionsPrivate = new ArrayList<ModelSectionDto>();
	private List<ModelSectionDto> subsectionsShared = new ArrayList<ModelSectionDto>();
	private List<ModelSectionDto> subsectionsCommon = new ArrayList<ModelSectionDto>();
	
	
	public List<ModelSectionDto> getSubsectionsPrivate() {
		return subsectionsPrivate;
	}
	public void setSubsectionsPrivate(List<ModelSectionDto> subsectionsPrivate) {
		this.subsectionsPrivate = subsectionsPrivate;
	}
	public List<ModelSectionDto> getSubsectionsShared() {
		return subsectionsShared;
	}
	public void setSubsectionsShared(List<ModelSectionDto> subsectionsShared) {
		this.subsectionsShared = subsectionsShared;
	}
	public List<ModelSectionDto> getSubsectionsCommon() {
		return subsectionsCommon;
	}
	public void setSubsectionsCommon(List<ModelSectionDto> subsectionsCommon) {
		this.subsectionsCommon = subsectionsCommon;
	}
		
}
