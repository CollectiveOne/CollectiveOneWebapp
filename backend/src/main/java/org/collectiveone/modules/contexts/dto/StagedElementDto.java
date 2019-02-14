package org.collectiveone.modules.contexts.dto;

import org.collectiveone.modules.contexts.entities.enums.StageAction;
import org.collectiveone.modules.contexts.entities.enums.StageStatus;
import org.collectiveone.modules.contexts.entities.enums.StageType;

public class StagedElementDto {

	private String id;
	private StageType type;
	private StageAction action;
	private StageStatus status;
	private ContextMetadataDto contextMetadata;
	private SubcontextDto subcontextDto;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public StageType getType() {
		return type;
	}
	public void setType(StageType type) {
		this.type = type;
	}
	public StageAction getAction() {
		return action;
	}
	public void setAction(StageAction action) {
		this.action = action;
	}
	public StageStatus getStatus() {
		return status;
	}
	public void setStatus(StageStatus status) {
		this.status = status;
	}
	public ContextMetadataDto getContextMetadata() {
		return contextMetadata;
	}
	public void setContextMetadata(ContextMetadataDto contextMetadata) {
		this.contextMetadata = contextMetadata;
	}
	public SubcontextDto getSubcontextDto() {
		return subcontextDto;
	}
	public void setSubcontextDto(SubcontextDto subcontextDto) {
		this.subcontextDto = subcontextDto;
	}
	
}
