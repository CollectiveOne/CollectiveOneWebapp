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
public class ModelSubsection {
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
	private ModelSubsection beforeSubsction;
	
	/* double-linked list determines the order */
	@ManyToOne
	private ModelSubsection afterCSubsection;
	
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

	public ModelSubsection getBeforeSubsction() {
		return beforeSubsction;
	}

	public void setBeforeSubsction(ModelSubsection beforeSubsction) {
		this.beforeSubsction = beforeSubsction;
	}

	public ModelSubsection getAfterCSubsection() {
		return afterCSubsection;
	}

	public void setAfterCSubsection(ModelSubsection afterCSubsection) {
		this.afterCSubsection = afterCSubsection;
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
