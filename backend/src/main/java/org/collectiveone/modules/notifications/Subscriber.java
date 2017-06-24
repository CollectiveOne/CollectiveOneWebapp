package org.collectiveone.modules.notifications;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.collectiveone.modules.users.AppUser;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table( name = "subscriptors" )
public class Subscriber {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@Column(name = "type")
	private SubscriptionElementType type;
	
	@Column(name = "elementId")
	private UUID elementId;
	
	@ManyToOne
	private AppUser user;
	
	@Column(name = "state")
	private SubscriberState state;
	
	
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
	
	public UUID getElementId() {
		return elementId;
	}

	public void setElementId(UUID elementId) {
		this.elementId = elementId;
	}

	public AppUser getUser() {
		return user;
	}

	public void setUser(AppUser user) {
		this.user = user;
	}

	public SubscriberState getState() {
		return state;
	}

	public void setState(SubscriberState state) {
		this.state = state;
	}
	
}
