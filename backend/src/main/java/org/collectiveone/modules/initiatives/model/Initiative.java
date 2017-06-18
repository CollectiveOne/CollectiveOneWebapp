package org.collectiveone.modules.initiatives.model;

import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.collectiveone.modules.governance.model.Governance;
import org.collectiveone.modules.initiatives.dto.InitiativeDto;
import org.collectiveone.modules.tokens.model.TokenType;
import org.collectiveone.modules.users.model.AppUser;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.SortNatural;
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
	
	@OneToOne
	private TokenType tokenType;
	
	@OneToMany(mappedBy = "initiative")
	private Set<InitiativeRelationship> relationships = new LinkedHashSet<InitiativeRelationship>();
	
	@OneToMany(mappedBy = "initiative")
	@SortNatural
	private SortedSet<Member> members = new TreeSet<Member>();
	
	@OneToOne
	private Governance governance;
	
	
	
	public InitiativeDto toDto() {
		InitiativeDto dto = new InitiativeDto();
		
		dto.setId(id.toString());
		dto.setName(name);
		dto.setCreatorC1Id(creator.getC1Id().toString());
		dto.setCreatorNickname(creator.getNickname());
		dto.setCreationDate(creationDate);
		dto.setDriver(driver);
		
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
	public TokenType getTokenType() {
		return tokenType;
	}
	public void setTokenType(TokenType tokenType) {
		this.tokenType = tokenType;
	}
	public Set<InitiativeRelationship> getRelationships() {
		return relationships;
	}
	public void setRelationships(Set<InitiativeRelationship> relationships) {
		this.relationships = relationships;
	}
	public SortedSet<Member> getMembers() {
		return members;
	}
	public void setMembers(SortedSet<Member> members) {
		this.members = members;
	}
	public Governance getGovernance() {
		return governance;
	}
	public void setGovernance(Governance governance) {
		this.governance = governance;
	}
	
	
}
