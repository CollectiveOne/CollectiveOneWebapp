package org.collectiveone.model.basic;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.collectiveone.model.enums.TokenHolderType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "token_holders")
public class TokenHolder {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private TokenType tokenType;
	
	@Column(name = "tokens")
	private double tokens;
	
	@Enumerated(EnumType.STRING)
	private TokenHolderType holderType;
	private UUID holderId;

	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public TokenType getTokenType() {
		return tokenType;
	}

	public void setTokenType(TokenType tokenType) {
		this.tokenType = tokenType;
	}

	public double getTokens() {
		return tokens;
	}

	public void setTokens(double tokens) {
		this.tokens = tokens;
	}

	public UUID getHolderId() {
		return holderId;
	}

	public void setHolderId(UUID holderId) {
		this.holderId = holderId;
	}

	public TokenHolderType getHolderType() {
		return holderType;
	}

	public void setHolderType(TokenHolderType holderType) {
		this.holderType = holderType;
	}
	
}
