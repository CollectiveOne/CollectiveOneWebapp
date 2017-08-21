package org.collectiveone.modules.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
	
	@OneToOne
	private ModelCard card;
	
	@OneToMany
	private List<ModelCard> oldVersions = new ArrayList<ModelCard>();
	
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
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
	
	
}
