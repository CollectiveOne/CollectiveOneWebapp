package org.collectiveone.modules.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.collectiveone.modules.model.dto.ModelCardDto;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "model_cards")
public class ModelCard {
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
	private String text;
	
	
	public ModelCardDto toDto() {
		ModelCardDto cardDto = new ModelCardDto();
		
		cardDto.setId(id.toString());
		cardDto.setTitle(title);
		cardDto.setText(text);
		
		return cardDto;
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
