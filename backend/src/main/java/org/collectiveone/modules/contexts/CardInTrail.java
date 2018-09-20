package org.collectiveone.modules.contexts;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.collectiveone.modules.contexts.cards.CardWrapper;
import org.collectiveone.modules.users.AppUser;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "cards_in_contexts")
public class CardInTrail {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private Trail trail;
	
	@ManyToOne
	private CardWrapper cardWrapper;
	
	/* double-linked list determines the order */
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private CardInTrail beforeElement;
	
	/* double-linked list determines the order */
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private CardInTrail afterElement;
	
	@ManyToOne
	private AppUser adder;
	
	
	@Override
	public String toString() {
		return "id: " + id.toString() + 
				"- context id: " + trail.getContext().getId().toString() +
				"- context title: " + trail.getContext().getTitle() +
				"- cardWrapper id: " + cardWrapper.getId().toString();
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
		CardInTrail other = (CardInTrail) obj;
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

	public Trail getTrail() {
		return trail;
	}

	public void setTrail(Trail trail) {
		this.trail = trail;
	}

	public CardWrapper getCardWrapper() {
		return cardWrapper;
	}

	public void setCardWrapper(CardWrapper cardWrapper) {
		this.cardWrapper = cardWrapper;
	}

	public CardInTrail getBeforeElement() {
		return beforeElement;
	}

	public void setBeforeElement(CardInTrail beforeElement) {
		this.beforeElement = beforeElement;
	}

	public CardInTrail getAfterElement() {
		return afterElement;
	}

	public void setAfterElement(CardInTrail afterElement) {
		this.afterElement = afterElement;
	}

	public AppUser getAdder() {
		return adder;
	}

	public void setAdder(AppUser adder) {
		this.adder = adder;
	}

	
	
}
