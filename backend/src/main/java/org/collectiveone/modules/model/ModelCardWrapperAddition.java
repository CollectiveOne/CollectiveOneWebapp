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
@Table(name = "model_cards_wrapper_additions")
public class ModelCardWrapperAddition {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private ModelSection section;
	
	@Enumerated(EnumType.STRING)
	private ModelCardWrapperScope scope;
	
	@ManyToOne
	private ModelCardWrapper cardWrapper;
	
	@ManyToOne
	private ModelCardWrapper onCardWrapper;
	
	private Boolean isBefore;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public ModelCardWrapper getCardWrapper() {
		return cardWrapper;
	}

	public void setCardWrapper(ModelCardWrapper cardWrapper) {
		this.cardWrapper = cardWrapper;
	}
	
	public ModelSection getSection() {
		return section;
	}

	public void setSection(ModelSection section) {
		this.section = section;
	}

	public ModelCardWrapperScope getScope() {
		return scope;
	}

	public void setScope(ModelCardWrapperScope scope) {
		this.scope = scope;
	}

	public ModelCardWrapper getOnCardWrapper() {
		return onCardWrapper;
	}

	public void setOnCardWrapper(ModelCardWrapper onCardWrapper) {
		this.onCardWrapper = onCardWrapper;
	}

	public Boolean getIsBefore() {
		return isBefore;
	}

	public void setIsBefore(Boolean isBefore) {
		this.isBefore = isBefore;
	}

}
