package org.collectiveone.modules.initiatives;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.collectiveone.modules.governance.Governance;
import org.collectiveone.modules.tokens.TokenType;
import org.collectiveone.modules.users.AppUser;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.SortNatural;

@Entity
@Table( name = "initiatives" )
public class Initiative {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@Column(name = "status")
	private InitiativeStatus status;
	
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
	
	@OneToOne
	private InitiativeMeta meta;	
	
	
	public InitiativeDto toDto() {
		InitiativeDto dto = new InitiativeDto();
		
		dto.setId(id.toString());
		dto.setCreator(creator.toDto());
		dto.setMeta(meta.toDto());
		
		if(tokenType != null) dto.setOwnAssetsId(tokenType.getId().toString());
		
		return dto;
	}
	
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public InitiativeStatus getStatus() {
		return status;
	}
	public void setStatus(InitiativeStatus status) {
		this.status = status;
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
	public InitiativeMeta getMeta() {
		return meta;
	}
	public void setMeta(InitiativeMeta meta) {
		this.meta = meta;
	}
	
}
