package org.collectiveone.model.extensions;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.collectiveone.model.basic.Initiative;
import org.collectiveone.model.basic.TokenType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "initiative_transfers")
public class InitiativeTransfer {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private Initiative fromInitiative;
	
	@ManyToOne
	private Initiative toInitiative;
	
	@ManyToOne
	private TokenType tokenType;
	
	private double value;

	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Initiative getFromInitiative() {
		return fromInitiative;
	}

	public void setFromInitiative(Initiative fromInitiative) {
		this.fromInitiative = fromInitiative;
	}

	public Initiative getToInitiative() {
		return toInitiative;
	}

	public void setToInitiative(Initiative toInitiative) {
		this.toInitiative = toInitiative;
	}

	public TokenType getTokenType() {
		return tokenType;
	}

	public void setTokenType(TokenType tokenType) {
		this.tokenType = tokenType;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	
}
