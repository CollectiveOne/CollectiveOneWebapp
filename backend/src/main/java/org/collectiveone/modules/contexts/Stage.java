package org.collectiveone.modules.contexts;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.collectiveone.modules.contexts.cards.CardContent;
import org.collectiveone.modules.contexts.cards.CardWrapper;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "stages")
public class Stage {

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
	private CardWrapper cardWrapper;
	
	@ManyToOne
	private CardContent cardContent;
	
	public Stage(Commit _commit, StageAction _action, CardWrapper _cardWrapper, CardContent _cardContent) {
		commit = _commit;
		action = _action;
		cardWrapper = _cardWrapper;
		cardContent = _cardContent;
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

	public CardWrapper getCardWrapper() {
		return cardWrapper;
	}

	public void setCardWrapper(CardWrapper cardWrapper) {
		this.cardWrapper = cardWrapper;
	}

	public CardContent getCardContent() {
		return cardContent;
	}

	public void setCardContent(CardContent cardContent) {
		this.cardContent = cardContent;
	}
	
}
