package org.collectiveone.modules.model.dto;

import java.util.ArrayList;
import java.util.List;

public class ModelSectionMembersDto {

	private String modelSectionId;

	public String getModelsectionid()
	{
		return this.modelSectionId;
	}

	public void setModelsectionid(String modelSectionId)
	{
		this.modelSectionId = modelSectionId;
	}

	private String modelSectionName;

	public String getModelsectionname()
	{
		return this.modelSectionName;
	}

	public void setModelsectionname(String modelSectionName)
	{
		this.modelSectionName = modelSectionName;
	}

	private List<MemberDto> members = new ArrayList<MemberDto>();

	public List<MemberDto> getMembers()
	{
		return this.members;
	}

	public void setMembers(List<MemberDto> members)
	{
		this.members = members;
	}

	private List<ModelSectionMembersDto> subModelSectionMembers = new ArrayList<ModelSectionMembersDto>();

	public List<ModelSectionMembersDto> getSubmodelsectionmembers()
	{
		return this.subModelSectionMembers;
	}

	public void setSubmodelsectionmembers(List<ModelSectionMembersDto> subModelSectionMembers)
	{
		this.subModelSectionMembers = subModelSectionMembers;
	}

	
	
	
	
}
