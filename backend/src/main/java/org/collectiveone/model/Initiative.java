package org.collectiveone.model;

import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.collectiveone.web.dto.InitiativeDto;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

@Entity
@Table( name = "initiatives" )
public class Initiative {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@Column(name = "enabled")
	private boolean enabled;
	
	@Column(name = "name")
	private String name;
	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	@Column(name = "driver")
	private String driver;
	
	@Column(name = "creation_date")
	private Timestamp creationDate;
	
	@ManyToOne
	private AppUser creator;
	
	@ManyToMany
	@JoinTable(name = "initiatives_contributors")
	private Set<AppUser> contributors = new LinkedHashSet<AppUser>();
	
	@OneToOne
	private TokenType tokenType;
	
	public InitiativeDto toDto() {
		InitiativeDto dto = new InitiativeDto();
		
		dto.setId(id.toString());
		dto.setName(name);
		dto.setCreatorC1Id(creator.getC1Id().toString());
		dto.setCreatorNickname(creator.getNickname());
		dto.setCreationDate(creationDate);
		dto.setDriver(driver);
		if(tokenType != null) {
			dto.setTokenId(tokenType.getId().toString());
			dto.setTokenName(tokenType.getName());
		}
		
		return dto;
	}


	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public Timestamp getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}
	public AppUser getCreator() {
		return creator;
	}
	public void setCreator(AppUser creator) {
		this.creator = creator;
	}
	public Set<AppUser> getContributors() {
		return contributors;
	}
	public void setContributors(Set<AppUser> contributors) {
		this.contributors = contributors;
	}
	public TokenType getTokenType() {
		return tokenType;
	}
	public void setTokenType(TokenType tokenType) {
		this.tokenType = tokenType;
	}
	
	
}
