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
	private ModelCardWrapperAddition onCardWrapperAddition;
	
	private Boolean isBefore;
	
	@Enumerated(EnumType.STRING)
	private Status status;
	
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
		ModelCardWrapperAddition other = (ModelCardWrapperAddition) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

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

	public ModelCardWrapperAddition getOnCardWrapperAddition() {
		return onCardWrapperAddition;
	}

	public void setOnCardWrapperAddition(ModelCardWrapperAddition onCardWrapperAddition) {
		this.onCardWrapperAddition = onCardWrapperAddition;
	}

	public Boolean getIsBefore() {
		return isBefore;
	}

	public void setIsBefore(Boolean isBefore) {
		this.isBefore = isBefore;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
}
