package org.collectiveone.modules.contexts.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.collectiveone.modules.contexts.cards.Card;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "stages_cards")
public class StageCard {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@Enumerated(EnumType.STRING)
	private StageAction action;
	
	@ManyToOne
	private CardInP cardInP;
	
	/* in case action is EDIT, the new content is stored here */
	@ManyToOne
	private Card newVersion;
	
	/* in case action is EDIT, keep a reference to the previous version  */
	@ManyToOne
	private Card oldVersion;
	
	public StageCard() {
		super();
	}
	
	
	public StageCard(Commit _commit, StageAction _action, CardInP _cardInP) {
		action = _action;
		cardInP = _cardInP;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public StageAction getAction() {
		return action;
	}

	public void setAction(StageAction action) {
		this.action = action;
	}

	public CardInP getCardInP() {
		return cardInP;
	}

	public void setCardInP(CardInP cardInP) {
		this.cardInP = cardInP;
	}
	
	public Card getNewVersion() {
		return newVersion;
	}

	public void setNewVersion(Card newVersion) {
		this.newVersion = newVersion;
	}

	public Card getOldVersion() {
		return oldVersion;
	}

	public void setOldVersion(Card oldVersion) {
		this.oldVersion = oldVersion;
	}
	
	
	
}
