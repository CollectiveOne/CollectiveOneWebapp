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
	
	@ManyToOne
	private Commit commit;
	
	@Enumerated(EnumType.STRING)
	private StageAction action;
	
	@ManyToOne
	private Card card;
	
	public StageCard() {
		super();
	}
	
	public StageCard(Commit _commit, StageAction _action, Card _card) {
		commit = _commit;
		action = _action;
		card = _card;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Commit getCommit() {
		return commit;
	}

	public void setCommit(Commit commit) {
		this.commit = commit;
	}

	public StageAction getAction() {
		return action;
	}

	public void setAction(StageAction action) {
		this.action = action;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public Card getCardContent() {
		return card;
	}

	public void setCardContent(Card cardContent) {
		this.card = cardContent;
	}
	
}
