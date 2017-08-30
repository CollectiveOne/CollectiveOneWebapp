package org.collectiveone.modules.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.collectiveone.modules.model.dto.ModelSectionDto;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "model_sections")
public class ModelSection {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@Column(name = "title", length = 30)
	private String title;
	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	@Column(name = "description")
	private String description;
	
	@OneToMany
	@OrderColumn(name = "cards_order")
	private List<ModelCardWrapper> cardsWrappers = new ArrayList<ModelCardWrapper>();
	
	@OneToMany
	@OrderColumn(name = "subsections_order")
	private List<ModelSection> subsections = new ArrayList<ModelSection>();
	
	
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
		
		ModelSection other = (ModelSection) obj;
		return id.equals(other.getId());
	}

	public ModelSectionDto toDtoLight() {
		ModelSectionDto sectionDto = new ModelSectionDto();
		
		sectionDto.setId(id.toString());
		sectionDto.setTitle(title);
		sectionDto.setDescription(description);
		sectionDto.setnSubsections(subsections.size());
		sectionDto.setnCards(cardsWrappers.size());
		
		return sectionDto;
	}
	
	public ModelSectionDto toDto() {
		return toDto(0);
	}
	
	public ModelSectionDto toDto(Integer level) {
		
		ModelSectionDto sectionDto = toDtoLight();
		
		/* if current level is 1 or more, keep going */
		if (level >= 1) {
			sectionDto.setSubElementsLoaded(true);
			
			for (ModelCardWrapper cardWrapper : cardsWrappers) {
				sectionDto.getCardsWrappers().add(cardWrapper.toDto());
			}
			
			for (ModelSection subsection : subsections) {
				sectionDto.getSubsections().add(subsection.toDto(level - 1));
			}
		} else {
			sectionDto.setSubElementsLoaded(false);
			for (ModelSection subsection : subsections) {
				sectionDto.getSubsections().add(subsection.toDtoLight());
			}
		}
		
		return sectionDto;
	}

	public UUID getId() {
		return id;
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

	public List<ModelCardWrapper> getCardsWrappers() {
		return cardsWrappers;
	}

	public void setCardsWrappers(List<ModelCardWrapper> cardsWrappers) {
		this.cardsWrappers = cardsWrappers;
	}

	public List<ModelSection> getSubsections() {
		return subsections;
	}

	public void setSubsections(List<ModelSection> subsections) {
		this.subsections = subsections;
	}
	
	
}
