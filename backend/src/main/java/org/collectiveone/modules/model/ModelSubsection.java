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

import org.collectiveone.modules.users.AppUser;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "model_section_subsections")
public class ModelSubsection implements OrderedElement {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private ModelSection inSection;
	
	@Enumerated(EnumType.STRING)
	private ModelScope scope;
	
	@ManyToOne
	private ModelSection section;
	
	/* double-linked list determines the order */
	@ManyToOne
	private ModelSubsection beforeElement;
	
	/* double-linked list determines the order */
	@ManyToOne
	private ModelSubsection afterElement;
	
	@ManyToOne
	private AppUser adder;
	
	@Enumerated(EnumType.STRING)
	private Status status;
	

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public ModelSection getInSection() {
		return inSection;
	}

	public void setInSection(ModelSection inSection) {
		this.inSection = inSection;
	}

	public ModelScope getScope() {
		return scope;
	}

	public void setScope(ModelScope scope) {
		this.scope = scope;
	}

	public ModelSection getSection() {
		return section;
	}

	public void setSection(ModelSection section) {
		this.section = section;
	}


	public OrderedElement getBeforeElement() {
		return beforeElement;
	}
	
	public void setBeforeElement(OrderedElement beforeElement) {
		this.beforeElement = (ModelSubsection) beforeElement;
	}

	public OrderedElement getAfterElement() {
		return afterElement;
	}

	public void setAfterElement(OrderedElement afterElement) {
		this.afterElement = (ModelSubsection) afterElement;
	}
	
	public ModelSubsection getBeforeCardWrapperAddition() {
		return beforeElement;
	}
	
	public ModelSubsection getAfterCardWrapperAddition() {
		return afterElement;
	}
	
	
	public AppUser getAdder() {
		return adder;
	}

	public void setAdder(AppUser adder) {
		this.adder = adder;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
}
