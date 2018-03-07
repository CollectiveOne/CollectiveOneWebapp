package org.collectiveone.modules.initiatives;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.collectiveone.modules.conversations.MessageThread;
import org.collectiveone.modules.governance.Governance;
import org.collectiveone.modules.initiatives.dto.InitiativeDto;
import org.collectiveone.modules.model.ModelSection;
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
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private InitiativeStatus status;
	
	@ManyToOne
	private AppUser creator;
	
	@OneToMany
	private List<TokenType> tokenTypes = new ArrayList<TokenType>();
	
	@OneToMany(mappedBy = "initiative")
	private Set<InitiativeRelationship> relationships = new LinkedHashSet<InitiativeRelationship>();
	
	@OneToMany(mappedBy = "initiative")
	@SortNatural
	private SortedSet<Member> members = new TreeSet<Member>();
	
	@OneToOne
	private Governance governance;
	
	@OneToOne
	private InitiativeMeta meta;	
	
	@OneToOne
	private ModelSection topModelSection;
		
	@OneToOne
	private MessageThread messageThread;
	
	
	public InitiativeDto toDto() {
		InitiativeDto dto = new InitiativeDto();
		
		dto.setId(id.toString());
		dto.setCreator(creator.toDtoLight());
		dto.setStatus(status.toString());
		dto.setMeta(meta.toDto());
		
		if(tokenTypes != null) {
			for (TokenType tokenType : tokenTypes) {
				dto.getOwnAssetsIds().add(tokenType.getId().toString());
			}
		}
		
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
	public List<TokenType> getTokenTypes() {
		return tokenTypes;
	}
	public void setTokenTypes(List<TokenType> tokenTypes) {
		this.tokenTypes = tokenTypes;
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
	public ModelSection getTopModelSection() {
		return topModelSection;
	}
	public void setTopModelSection(ModelSection topModelSection) {
		this.topModelSection = topModelSection;
	}
	public MessageThread getMessageThread() {
		return messageThread;
	}
	public void setMessageThread(MessageThread messageThread) {
		this.messageThread = messageThread;
	}
	
	
}
