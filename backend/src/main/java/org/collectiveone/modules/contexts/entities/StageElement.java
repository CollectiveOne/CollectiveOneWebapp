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
@Table(name = "staged_elements")
public class StageElement {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@Enumerated(EnumType.STRING)
	private StageType type;
	
	@Enumerated(EnumType.STRING)
	private StageAction action;
	
	@Enumerated(EnumType.STRING)
	private StageStatus status;
	
	/* Properties in case type = METADATA*/
	@ManyToOne
	private ContextMetadata contextMetadata;
	
	/* Properties in case type = CARD*/
	@ManyToOne
	private CardInP cardInP;
	
	/* in case action is EDIT, the new content is stored here */
	@ManyToOne
	private Card newVersion;
	
	/* in case action is EDIT, keep a reference to the previous version  */
	@ManyToOne
	private Card oldVersion;
	
	/* Properties in case type = SUBCONTEXT*/
	@ManyToOne
	private Subcontext subcontext;
	
	public StageElement() {
		super();
		this.status = StageStatus.PENDING;
	}
	
	public StageElement(StageType type) {
		super();
		this.status = StageStatus.PENDING;
		this.type = type;
	}
	
	public StageElement(StageType type, StageAction action) {
		super();
		this.status = StageStatus.PENDING;
		this.type = type;
		this.action = action;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public StageType getType() {
		return type;
	}

	public void setType(StageType type) {
		this.type = type;
	}

	public StageAction getAction() {
		return action;
	}

	public void setAction(StageAction action) {
		this.action = action;
	}

	public StageStatus getStatus() {
		return status;
	}

	public void setStatus(StageStatus status) {
		this.status = status;
	}

	public ContextMetadata getContextMetadata() {
		return contextMetadata;
	}

	public void setContextMetadata(ContextMetadata contextMetadata) {
		this.contextMetadata = contextMetadata;
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

	public Subcontext getSubcontext() {
		return subcontext;
	}

	public void setSubcontext(Subcontext subcontext) {
		this.subcontext = subcontext;
	}
	
}
