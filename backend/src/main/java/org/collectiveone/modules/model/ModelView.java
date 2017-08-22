package org.collectiveone.modules.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.collectiveone.modules.initiatives.Initiative;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "model_views")
public class ModelView {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private Initiative initiative;
	
	@Column(name = "title", length = 30)
	private String title;
	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	@Column(name = "description")
	private String description;
	
	@OneToMany
	@OrderColumn(name = "sections_order")
	private List<ModelSection> sections = new ArrayList<ModelSection>();
	
	
	public ModelViewDto toDto(Integer level) {
		ModelViewDto viewDto = new ModelViewDto();
		
		viewDto.setId(id.toString());
		viewDto.setTitle(title);
		viewDto.setDescription(description);
		
		if (level >= 1) {
			for (ModelSection section : sections) {
				viewDto.getSections().add(section.toDto(level - 1));
			}
		}
		
		return viewDto;
	}

	public UUID getId() {
		return id;
	}
	
	public Initiative getInitiative() {
		return initiative;
	}

	public void setInitiative(Initiative initiative) {
		this.initiative = initiative;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ModelSection> getSections() {
		return sections;
	}

	public void setSections(List<ModelSection> sections) {
		this.sections = sections;
	}
	
	
}
