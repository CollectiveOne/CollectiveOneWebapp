package org.collectiveone.model.support;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.collectiveone.model.basic.AppUser;
import org.collectiveone.model.basic.DecisionRealm;
import org.collectiveone.model.enums.DecisionMakerRole;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity( name = "decision_makers")
public class DecisionMaker {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "c1Id", updatable = false, nullable = false)
	private UUID c1Id;
	
	@ManyToOne
	private DecisionRealm realm;
	
	@ManyToOne 
	private AppUser user;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private DecisionMakerRole role;

	
	public UUID getC1Id() {
		return c1Id;
	}

	public void setC1Id(UUID c1Id) {
		this.c1Id = c1Id;
	}

	public DecisionRealm getRealm() {
		return realm;
	}

	public void setRealm(DecisionRealm realm) {
		this.realm = realm;
	}
	
	public DecisionMakerRole getRole() {
		return role;
	}

	public void setRole(DecisionMakerRole role) {
		this.role = role;
	}
	
	
}
