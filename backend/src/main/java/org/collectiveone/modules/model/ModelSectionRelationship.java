package org.collectiveone.modules.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table( name = "initiatives_relationships" )
public class ModelSectionRelationship {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private ModelSection modelSection;

	public ModelSection getModelsection()
	{
		return this.modelSection;
	}

	public void setModelsection(ModelSection modelSection)
	{
		this.modelSection = modelSection;
	}

	
	@ManyToOne
	private ModelSection ofModelSection;

	public ModelSection getOfmodelsection()
	{
		return this.ofModelSection;
	}

	public void setOfmodelsection(ModelSection ofModelSection)
	{
		this.ofModelSection = ofModelSection;
	}

	
	@Enumerated(EnumType.STRING)
	private ModelSectionRelationshipType type;

	public ModelSectionRelationshipType getType()
	{
		return this.type;
	}

	public void setType(ModelSectionRelationshipType type)
	{
		this.type = type;
	}

	
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}


	
}
