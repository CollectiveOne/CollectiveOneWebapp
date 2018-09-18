package org.collectiveone.modules.contexts;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.collectiveone.modules.contexts.dto.ElementConsentPositionDto;
import org.collectiveone.modules.contexts.enums.ElementConsentPositionColor;
import org.collectiveone.modules.users.AppUser;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table( name = "card_wrapper_addition_positions")
public class ElementConsentPosition {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	private UUID elementId;
	
	@ManyToOne
	private AppUser author;
	
	@Enumerated(EnumType.STRING)
	private ElementConsentPositionColor positionColor;
	
	
	public ElementConsentPositionDto toDto() {
		ElementConsentPositionDto dto = new ElementConsentPositionDto();
		
		dto.setId(id.toString());
		dto.setElementId(elementId.toString());
		dto.setAuthor(author.toDtoLight());
		dto.setPositionColor(positionColor);
		
		return dto;
	} 

	public UUID getId() {
		return id;
	}

	public UUID getElementId() {
		return elementId;
	}

	public void setElementId(UUID elementId) {
		this.elementId = elementId;
	}

	public AppUser getAuthor() {
		return author;
	}

	public void setAuthor(AppUser author) {
		this.author = author;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public ElementConsentPositionColor getPositionColor() {
		return positionColor;
	}

	public void setPositionColor(ElementConsentPositionColor positionColor) {
		this.positionColor = positionColor;
	}
	
	
}
