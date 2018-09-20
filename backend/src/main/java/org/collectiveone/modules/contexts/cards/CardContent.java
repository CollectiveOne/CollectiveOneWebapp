package org.collectiveone.modules.contexts.cards;

import java.util.List;
import java.util.UUID;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.collectiveone.modules.files.FileStored;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "cards_content")
public class CardContent {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
		
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String base;
	
	@ElementCollection
    @CollectionTable(name = "card_number_fields")
	private List<CardNumberField> numberFields;
	
	@ElementCollection
    @CollectionTable(name = "card_number_fields")
	private List<CardStringField> stringFields;
	
	@OneToMany
	private List<FileStored> filesStored;
	
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public List<CardNumberField> getNumberFields() {
		return numberFields;
	}

	public void setNumberFields(List<CardNumberField> numberFields) {
		this.numberFields = numberFields;
	}

	public List<CardStringField> getStringFields() {
		return stringFields;
	}

	public void setStringFields(List<CardStringField> stringFields) {
		this.stringFields = stringFields;
	}

	public List<FileStored> getFilesStored() {
		return filesStored;
	}

	public void setFilesStored(List<FileStored> filesStored) {
		this.filesStored = filesStored;
	}


}
