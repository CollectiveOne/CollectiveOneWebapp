package org.collectiveone.modules.governance;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.collectiveone.modules.users.AppUser;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity( name = "decision_makers")
public class DecisionMaker {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private Governance governance;
	
	@ManyToOne 
	private AppUser user;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private DecisionMakerRole role;

	public DecisionMakerDto toDto() {
		DecisionMakerDto dto = new DecisionMakerDto();
		
		dto.setId(id.toString());
		dto.setUser(user.toDto());
		dto.setRole(role.toString());
	
		return dto;
	}
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public AppUser getUser() {
		return user;
	}

	public void setUser(AppUser user) {
		this.user = user;
	}

	public Governance getGovernance() {
		return governance;
	}

	public void setGovernance(Governance governance) {
		this.governance = governance;
	}

	public DecisionMakerRole getRole() {
		return role;
	}

	public void setRole(DecisionMakerRole role) {
		this.role = role;
	}
	
	
}
