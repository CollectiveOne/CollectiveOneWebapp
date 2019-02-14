package org.collectiveone.modules.contexts.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.collectiveone.modules.contexts.cards.Card;
import org.collectiveone.modules.contexts.dto.StagedElementDto;
import org.collectiveone.modules.contexts.entities.enums.StageAction;
import org.collectiveone.modules.contexts.entities.enums.StageStatus;
import org.collectiveone.modules.contexts.entities.enums.StageType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "staged_elements")
public class StagedElement {

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
	@ManyToOne(fetch = FetchType.LAZY)
	private ContextMetadata contextMetadata;
	
	/* Properties in case type = CARD*/
	@ManyToOne(fetch = FetchType.LAZY)
	private CardInP cardInP;
	
	/* in case action is EDIT, the new content is stored here */
	@ManyToOne(fetch = FetchType.LAZY)
	private Card newVersion;
	
	/* in case action is EDIT, keep a reference to the previous version  */
	@ManyToOne(fetch = FetchType.LAZY)
	private Card oldVersion;
	
	/* Properties in case type = SUBCONTEXT*/
	@ManyToOne(fetch = FetchType.LAZY)
	private Subcontext subcontext;
	
	public StagedElement() {
		super();
		this.status = StageStatus.PENDING;
	}
	
	public StagedElement(StageType type) {
		super();
		this.status = StageStatus.PENDING;
		this.type = type;
	}
	
	public StagedElement(StageType type, StageAction action) {
		super();
		this.status = StageStatus.PENDING;
		this.type = type;
		this.action = action;
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
		StagedElement other = (StagedElement) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public StagedElementDto toDto() {
		StagedElementDto dto = new StagedElementDto();
		
		dto.setId(id.toString());
		dto.setAction(action);
		dto.setType(type);
		dto.setStatus(status);
		
		switch (type) {
			case METADATA:
				dto.setContextMetadata(contextMetadata.toDto());
				break;
				
			case SUBCONTEXT:
				dto.setSubcontextDto(subcontext.toDto());
				break;
				
			case CARD:
				break;
		}
		
		return dto;
		
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
