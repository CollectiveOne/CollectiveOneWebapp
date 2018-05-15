package org.collectiveone.modules.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "model_cards_wrapper")
public class ModelCardWrapperAddition {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private ModelSection section;
	
	@ManyToOne
	private ModelCardWrapper cardWrapper;
	
	@ManyToMany
	private ModelCardWrapper afterCardWrapper;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public ModelSection getSection() {
		return section;
	}

	public void setSection(ModelSection section) {
		this.section = section;
	}

	public ModelCardWrapper getCardWrapper() {
		return cardWrapper;
	}

	public void setCardWrapper(ModelCardWrapper cardWrapper) {
		this.cardWrapper = cardWrapper;
	}

	public ModelCardWrapper getAfterCardWrapper() {
		return afterCardWrapper;
	}

	public void setAfterCardWrapper(ModelCardWrapper afterCardWrapper) {
		this.afterCardWrapper = afterCardWrapper;
	}
		
}
