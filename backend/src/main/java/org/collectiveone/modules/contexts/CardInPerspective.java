package org.collectiveone.modules.contexts;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.collectiveone.modules.contexts.cards.Card;
import org.collectiveone.modules.users.AppUser;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "cards_in_contexts")
public class CardInPerspective {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private Perspective perspective;
	
	@ManyToOne
	private Card card;
	
	/* double-linked list determines the order */
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private CardInPerspective beforeElement;
	
	/* double-linked list determines the order */
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private CardInPerspective afterElement;
	
	@ManyToOne
	private AppUser adder;
	
	
	@Override
	public String toString() {
		return "id: " + id.toString() + 
				"- context id: " + perspective.getContext().getId().toString() +
				"- cardWrapper id: " + card.getId().toString();
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
		CardInPerspective other = (CardInPerspective) obj;
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

	public Perspective getPerspective() {
		return perspective;
	}

	public void setPerspective(Perspective perspective) {
		this.perspective = perspective;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public CardInPerspective getBeforeElement() {
		return beforeElement;
	}

	public void setBeforeElement(CardInPerspective beforeElement) {
		this.beforeElement = beforeElement;
	}

	public CardInPerspective getAfterElement() {
		return afterElement;
	}

	public void setAfterElement(CardInPerspective afterElement) {
		this.afterElement = afterElement;
	}

	public AppUser getAdder() {
		return adder;
	}

	public void setAdder(AppUser adder) {
		this.adder = adder;
	}

	
	
}
