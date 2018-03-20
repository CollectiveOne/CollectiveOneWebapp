package org.collectiveone.modules.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.collectiveone.modules.conversations.MessageThread;
import org.collectiveone.modules.governance.CardLike;
import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.model.dto.ModelCardWrapperDto;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "model_cards_wrapper")
public class ModelCardWrapper {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private Initiative initiative;
	
	@OneToOne(cascade = CascadeType.ALL)
	private ModelCard card;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ModelCard> oldVersions = new ArrayList<ModelCard>();
	
	@OneToOne
	private MessageThread messageThread;	
	
	@OneToMany(mappedBy="cardWrapper")
	private List<CardLike> likes;
	
	
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
		
		ModelCardWrapper other = (ModelCardWrapper) obj;
		return id.equals(other.getId());
	}
	
	public ModelCardWrapperDto toDto() {
		ModelCardWrapperDto cardWrapperDto = new ModelCardWrapperDto();
		
		cardWrapperDto.setId(id.toString());
		cardWrapperDto.setCard(card.toDto());
		cardWrapperDto.setInitiativeId(initiative.getId().toString());
		
		return cardWrapperDto;
	}
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
	
	public Initiative getInitiative() {
		return initiative;
	}

	public void setInitiative(Initiative initiative) {
		this.initiative = initiative;
	}
	
	public ModelCard getCard() {
		return card;
	}

	public void setCard(ModelCard card) {
		this.card = card;
	}

	public List<ModelCard> getOldVersions() {
		return oldVersions;
	}

	public void setOldVersions(List<ModelCard> oldVersions) {
		this.oldVersions = oldVersions;
	}
	
	public MessageThread getMessageThread() {
		return messageThread;
	}

	public void setMessageThread(MessageThread messageThread) {
		this.messageThread = messageThread;
	}

	public List<CardLike> getLikes() {
		return likes;
	}

	public void setLikes(List<CardLike> likes) {
		this.likes = likes;
	}
	
	
}
