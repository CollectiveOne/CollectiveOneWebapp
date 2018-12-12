package org.collectiveone.modules.contexts.entities;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.collectiveone.modules.contexts.OrderedElement;
import org.collectiveone.modules.users.AppUser;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "subcontexts")
public class Subcontext implements OrderedElement {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private Perspective perspective;
	
	@ManyToOne
	private Context subcontext;
	
	/* double-linked list determines the order */
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Subcontext beforeElement;
	
	/* double-linked list determines the order */
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Subcontext afterElement;
	
	@ManyToOne
	private AppUser adder;
	
	
	public String toString() {
		return "id: " + id.toString() + " " + 	
				"parent context id: " + (perspective != null ? perspective.getContext().getId().toString() : "null") + " " +
				"subcontext id: " + subcontext.getId().toString();
				
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Perspective getTrail() {
		return perspective;
	}

	public void setTrail(Perspective trail) {
		this.perspective = trail;
	}

	public Context getSubcontext() {
		return subcontext;
	}

	public void setSubcontext(Context subcontext) {
		this.subcontext = subcontext;
	}

	public void setBeforeElement(Subcontext beforeElement) {
		this.beforeElement = beforeElement;
	}

	public void setAfterElement(Subcontext afterElement) {
		this.afterElement = afterElement;
	}

	public OrderedElement getBeforeElement() {
		return beforeElement;
	}
	
	public void setBeforeElement(OrderedElement beforeElement) {
		this.beforeElement = (Subcontext) beforeElement;
	}

	public OrderedElement getAfterElement() {
		return afterElement;
	}

	public void setAfterElement(OrderedElement afterElement) {
		this.afterElement = (Subcontext) afterElement;
	}
	
	public Subcontext getBeforeSubsection() {
		return beforeElement;
	}
	
	public Subcontext getAfterSubsection() {
		return afterElement;
	}
	
	public AppUser getAdder() {
		return adder;
	}

	public void setAdder(AppUser adder) {
		this.adder = adder;
	}
	
}
