package org.collectiveone.modules.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.collectiveone.modules.activity.dto.SubscriberDto;
import org.collectiveone.modules.activity.enums.SubscriberEmailNotificationsState;
import org.collectiveone.modules.activity.enums.SubscriberState;
import org.collectiveone.modules.activity.enums.SubscriptionElementType;
import org.collectiveone.modules.users.AppUser;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table( name = "subscribers" )
public class Subscriber {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private SubscriptionElementType type;
	
	@Column(name = "elementId")
	private UUID elementId;
	
	@ManyToOne
	private AppUser user;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "state")
	private SubscriberState state;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "email_notification_state")
	private SubscriberEmailNotificationsState emailNotificationsState;
	
	@OneToMany(mappedBy = "subscriber", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Notification> notifications = new ArrayList<Notification>();
	
	
	public SubscriberDto toDto() {
		SubscriberDto dto = new SubscriberDto();
		dto.setId(id.toString());
		dto.setElementId(elementId.toString());
		dto.setUser(user.toDtoLight());
		dto.setState(state.toString());
		dto.setEmailNotificationsState(emailNotificationsState.toString());
		
		return dto;
	}
	
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

	public SubscriberEmailNotificationsState getEmailNotificationsState() {
		return emailNotificationsState;
	}

	public void setEmailNotificationsState(SubscriberEmailNotificationsState emailNotificationsState) {
		this.emailNotificationsState = emailNotificationsState;
	}
	
	public List<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}	
	
}
