package org.collectiveone.model.basic;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.collectiveone.model.enums.TransactionType;
import org.collectiveone.web.dto.TransferDto;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "token_transactions")
public class TokenTransaction {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private TokenType tokenType;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "transaction_type")
	private TransactionType transactionType;
	
	@Column(name = "value")
	private double value;
	
	@Column(name = "from_holder_id")
	private UUID fromHolderId;
	
	@Column(name = "to_holder_id")
	private UUID toHolderId;
	
	@Column(name = "date")
	private Timestamp date;

	public TokenTransaction(UUID tokenType, UUID from, UUID to, double _value) {
		fromHolderId = from;
		toHolderId = to;
		value = _value;
		
		date = new Timestamp(System.currentTimeMillis());
		transactionType = TransactionType.TRANSFER;
	}
	
	public TokenTransaction(UUID tokenType, UUID to, double _value) {
		toHolderId = to;
		value = _value;
		
		date = new Timestamp(System.currentTimeMillis());
		transactionType = TransactionType.MINT;
	}
	
	public TransferDto toDto() {
		TransferDto dto = new TransferDto();
		
		dto.setAssetId(tokenType.getId().toString());
		dto.setAssetName(tokenType.getName());
		dto.setSenderId(fromHolderId.toString());
		dto.setReceiverId(toHolderId.toString());
		dto.setValue(value);
		
		return dto;		
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
	
	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public UUID getFromHolderId() {
		return fromHolderId;
	}

	public void setFromHolderId(UUID fromHolderId) {
		this.fromHolderId = fromHolderId;
	}

	public UUID getToHolderId() {
		return toHolderId;
	}

	public void setToHolderId(UUID toHolderId) {
		this.toHolderId = toHolderId;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}
	
}
