package org.collectiveone.modules.initiatives;

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
import org.collectiveone.modules.files.FileStored;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

@Entity
@Table( name = "initiatives_meta" )
public class InitiativeMeta {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@Column(name = "name", length = 30)
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
	private InitiativeVisibility visibility;
	
	@Column(name = "model_enabled")
	private Boolean modelEnabled;
	
	@ManyToMany
	private List<InitiativeTag> tags = new ArrayList<InitiativeTag>();
	
	
	public InitiativeMetaDto toDto() {
		InitiativeMetaDto dto = new InitiativeMetaDto();
		
		dto.setName(name);
		dto.setCreationDate(creationDate);
		dto.setDriver(driver);
		dto.setColor(color);
		dto.setModelEnabled(modelEnabled);
		if (visibility != null) dto.setVisibility(visibility.toString());
		if (imageFile != null) dto.setImageFile(imageFile.toDto());
		
		for (InitiativeTag tag : tags) {
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
	
	public InitiativeVisibility getVisibility() {
		return visibility;
	}

	public void setVisibility(InitiativeVisibility visibility) {
		this.visibility = visibility;
	}

	public Boolean getModelEnabled() {
		return modelEnabled;
	}

	public void setModelEnabled(Boolean modelEnabled) {
		this.modelEnabled = modelEnabled;
	}
	public List<InitiativeTag> getTags() {
		return tags;
	}
	public void setTags(List<InitiativeTag> tags) {
		this.tags = tags;
	}
	
}
