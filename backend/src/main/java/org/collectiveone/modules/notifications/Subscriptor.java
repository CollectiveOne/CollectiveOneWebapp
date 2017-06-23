package org.collectiveone.modules.notifications;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.collectiveone.modules.users.AppUser;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

public class Subscriptor {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@Column(name = "type")
	private SubscriptionElementType type;
	
	@ManyToOne
	private AppUser user;
	
	@Column(name = "state")
	private SubscriptionState state;
	
	

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public SubscriptionElementType getType() {
		return type;
	}

	public void setType(SubscriptionElementType type) {
		this.type = type;
	}

	public AppUser getUser() {
		return user;
	}

	public void setUser(AppUser user) {
		this.user = user;
	}

	public SubscriptionState getState() {
		return state;
	}

	public void setState(SubscriptionState state) {
		this.state = state;
	}
	
}
