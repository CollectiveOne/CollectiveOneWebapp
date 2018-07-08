package org.collectiveone.modules.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.collectiveone.modules.assignations.dto.InitiativeMetaDto;
import org.collectiveone.modules.assignations.dto.ModelSectionMetaDto;
import org.collectiveone.modules.files.FileStored;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

@Entity
@Table( name = "model_section_meta" )
public class ModelSectionMeta {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@Column(name = "name", length = 42)
	private String name;
	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	@Column(name = "driver")
	private String driver;
	
	@ManyToOne
	private FileStored imageFile;
	
	@Column(name = "creation_date")
	private Timestamp creationDate;
	
	@Column(name = "color", length = 7)
	private String color;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "visibility")
	private ModelSectionVisibility visibility;
	
	@Column(name = "model_enabled")
	private Boolean modelEnabled;
	
	@ManyToMany
	private List<ModelSectionTag> tags = new ArrayList<ModelSectionTag>();
	
	
	public ModelSectionMetaDto toDto() {
		ModelSectionMetaDto dto = new ModelSectionMetaDto();
		
		dto.setName(name);
		dto.setCreationDate(creationDate);
		dto.setDriver(driver);
		dto.setColor(color);
		dto.setModelEnabled(modelEnabled);
		if (visibility != null) dto.setVisibility(visibility.toString());
		if (imageFile != null) dto.setImageFile(imageFile.toDto());
		
		for (ModelSectionTag tag : tags) {
			dto.getTags().add(tag.toDto());
		}
		
		return dto;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}
	
	public FileStored getImageFile() {
		return imageFile;
	}

	public void setImageFile(FileStored imageFile) {
		this.imageFile = imageFile;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	public ModelSectionVisibility getVisibility() {
		return visibility;
	}

	public void setVisibility(ModelSectionVisibility visibility) {
		this.visibility = visibility;
	}

	public Boolean getModelEnabled() {
		return modelEnabled;
	}

	public void setModelEnabled(Boolean modelEnabled) {
		this.modelEnabled = modelEnabled;
	}
	public List<ModelSectionTag> getTags() {
		return tags;
	}
	public void setTags(List<ModelSectionTag> tags) {
		this.tags = tags;
	}
	
}
