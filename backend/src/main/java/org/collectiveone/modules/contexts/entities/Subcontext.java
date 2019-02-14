package org.collectiveone.modules.contexts.entities;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.collectiveone.modules.contexts.dto.SubcontextDto;
import org.collectiveone.modules.users.AppUser;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "subcontexts")
public class Subcontext {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Perspective onPerspective;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Perspective perspective;
	
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
				"subperspective id: " + perspective.getId().toString();
				
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
		Subcontext other = (Subcontext) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public SubcontextDto toDto() {
		SubcontextDto dto = new SubcontextDto();
		
		dto.setId(id.toString());
		dto.setAdder(adder.toDtoLight());
		dto.setOnPerspectiveId(onPerspective.getId().toString());
		dto.setPerspectiveId(perspective.getId().toString());
		
		return dto;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Perspective getPerspective() {
		return perspective;
	}

	public void setPerspective(Perspective perspective) {
		this.perspective = perspective;
	}

	public Perspective getOnPerspective() {
		return onPerspective;
	}

	public void setOnPerspective(Perspective onPerspective) {
		this.onPerspective = onPerspective;
	}

	public Subcontext getBeforeElement() {
		return beforeElement;
	}

	public void setBeforeElement(Subcontext beforeElement) {
		this.beforeElement = beforeElement;
	}

	public Subcontext getAfterElement() {
		return afterElement;
	}

	public void setAfterElement(Subcontext afterElement) {
		this.afterElement = afterElement;
	}

	public AppUser getAdder() {
		return adder;
	}

	public void setAdder(AppUser adder) {
		this.adder = adder;
	}
	
}
