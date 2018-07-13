package org.collectiveone.modules.model;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.collectiveone.modules.model.dto.InModelSectionDto;
import org.collectiveone.modules.model.enums.ElementGovernanceType;
import org.collectiveone.modules.model.enums.SimpleConsentState;
import org.collectiveone.modules.model.enums.Status;
import org.collectiveone.modules.users.AppUser;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "model_cards_wrapper_additions")
public class ModelCardWrapperAddition implements OrderedElement {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private ModelSection section;
	
	@Enumerated(EnumType.STRING)
	private ModelScope scope;
	
	@ManyToOne
	private ModelCardWrapper cardWrapper;
	
	/* double-linked list determines the order */
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private ModelCardWrapperAddition beforeElement;
	
	/* double-linked list determines the order */
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private ModelCardWrapperAddition afterElement;
	
	@ManyToOne
	private AppUser adder;
	
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@Enumerated(EnumType.STRING)
	private ElementGovernanceType governanceType;
	
	@Enumerated(EnumType.STRING)
	private SimpleConsentState simpleConsentState;	
	
	
	
	public InModelSectionDto toInModelSectionDto() {
		InModelSectionDto dto = new InModelSectionDto();
		dto.setId(section.getId().toString());
		dto.setTitle(section.getTitle());
		dto.setDescription(section.getDescription());
		dto.setInitiativeId(section.getInitiative().getId().toString());
		dto.setScope(scope.toString());
		
		return dto;
	}
	
	@Override
	public String toString() {
		return "id: " + id.toString() + 
				"- section id: " + section.getId().toString() +
				"- section title: " + section.getTitle() +
				"- cardWrapper id: " + cardWrapper.getId().toString() +
				"- scope : " + scope.toString() +
				"- status : " + (status != null ? status.toString() : "null");
	}
	
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

	public ModelScope getScope() {
		return scope;
	}

	public void setScope(ModelScope scope) {
		this.scope = scope;
	}
	
	public OrderedElement getBeforeElement() {
		return beforeElement;
	}

	public void setBeforeElement(OrderedElement beforeElement) {
		this.beforeElement = (ModelCardWrapperAddition) beforeElement;
	}

	public OrderedElement getAfterElement() {
		return afterElement;
	}

	public void setAfterElement(OrderedElement afterElement) {
		this.afterElement = (ModelCardWrapperAddition) afterElement;
	}
	
	public ModelCardWrapperAddition getBeforeCardWrapperAddition() {
		return beforeElement;
	}
	
	public ModelCardWrapperAddition getAfterCardWrapperAddition() {
		return afterElement;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public AppUser getAdder() {
		return adder;
	}

	public void setAdder(AppUser adder) {
		this.adder = adder;
	}

	public ElementGovernanceType getGovernanceType() {
		return governanceType;
	}

	public void setGovernanceType(ElementGovernanceType governanceType) {
		this.governanceType = governanceType;
	}

	public SimpleConsentState getSimpleConsentState() {
		return simpleConsentState;
	}

	public void setSimpleConsentState(SimpleConsentState simpleConsentState) {
		this.simpleConsentState = simpleConsentState;
	}
	
	
	
}
