package org.collectiveone.modules.tokens;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.collectiveone.modules.tokens.dto.TokenMintDto;
import org.collectiveone.modules.users.AppUser;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "token_mints")
public class TokenBurn {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private TokenType token;
	
	@Column(name = "motive", length = 55)
	private String motive;
	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	@Column(name = "notes")
	private String notes;
	
	@Column(name = "value")
	private double value;
	
	@ManyToOne
	private AppUser orderedBy;
	
	public TokenMintDto toDto() {
		TokenMintDto dto = new TokenMintDto();
		
		dto.setOrderedBy(orderedBy.toDtoLight());
		dto.setValue(value);
		dto.setTokenId(token.getId().toString());
		dto.setTokenName(token.getName());
		dto.setMotive(motive);
		dto.setNotes(notes);
		
		return dto;
	}
	
	

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public TokenType getToken() {
		return token;
	}

	public void setToken(TokenType token) {
		this.token = token;
	}

	public String getMotive() {
		return motive;
	}

	public void setMotive(String motive) {
		this.motive = motive;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public AppUser getOrderedBy() {
		return orderedBy;
	}

	public void setOrderedBy(AppUser orderedBy) {
		this.orderedBy = orderedBy;
	}

}
