package org.collectiveone.modules.activity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table( name = "notifications" )
public class Notification {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private Activity activity;
	
	@ManyToOne
	private Subscriber subscriber;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "state")
	private NotificationState state;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "email_state")
	private NotificationEmailState emailState;

	
	public NotificationDto toDto() {
		NotificationDto dto = new NotificationDto();
		
		dto.setId(id.toString());
		dto.setActivity(activity.toDto());
		dto.setState(state.toString());
		dto.setSubscriberUser(subscriber.getUser().toDto());
		dto.setSubscriberState(subscriber.getState().toString());
		
		return dto;
	}
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public Subscriber getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}

	public NotificationState getState() {
		return state;
	}

	public void setState(NotificationState state) {
		this.state = state;
	}

	public NotificationEmailState getEmailState() {
		return emailState;
	}

	public void setEmailState(NotificationEmailState emailState) {
		this.emailState = emailState;
	}
	
	
	
}
