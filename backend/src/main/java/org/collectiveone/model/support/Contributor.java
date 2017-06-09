package org.collectiveone.model.support;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.collectiveone.model.basic.AppUser;
import org.collectiveone.model.basic.Initiative;
import org.collectiveone.model.enums.ContributorRole;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table( name = "contributors" )
public class Contributor {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private Initiative initiative;
	
	@ManyToOne
	private AppUser user;
	
	@Enumerated(EnumType.STRING)
	private ContributorRole role;
	
	@OneToMany(mappedBy = "contributor")
	private List<ContributorTransfer> tokensTransfers = new ArrayList<ContributorTransfer>();

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

	public AppUser getUser() {
		return user;
	}

	public void setUser(AppUser user) {
		this.user = user;
	}

	public ContributorRole getRole() {
		return role;
	}

	public void setRole(ContributorRole role) {
		this.role = role;
	}

	public List<ContributorTransfer> getTokensTransfers() {
		return tokensTransfers;
	}

	public void setTokensTransfers(List<ContributorTransfer> tokensTransfers) {
		this.tokensTransfers = tokensTransfers;
	}
		
}
