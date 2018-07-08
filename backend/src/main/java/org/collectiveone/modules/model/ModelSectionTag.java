package org.collectiveone.modules.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.collectiveone.modules.initiatives.dto.InitiativeTagDto;
import org.collectiveone.modules.model.dto.ModelSectionTagDto;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

@Entity
@Table( name = "model_section_tags" )
public class ModelSectionTag {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@Column(name = "tag_text", length = 25, unique = true)
	private String tagText;
	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	@Column(name = "tag_description")
	private String description;

	
	public ModelSectionTagDto toDto() {
		ModelSectionTagDto dto = new ModelSectionTagDto();
		
		dto.setId(id.toString());
		dto.setTagText(tagText);
		dto.setDescription(description);
		
		return dto;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		ModelSectionTag other = (ModelSectionTag) obj;
		return id.equals(other.getId());
	}
	
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getTagText() {
		return tagText;
	}

	public void setTagText(String tagText) {
		this.tagText = tagText;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
