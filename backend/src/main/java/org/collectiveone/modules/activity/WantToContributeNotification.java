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

import org.collectiveone.modules.activity.enums.NotificationState;
import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.model.ModelSection;
import org.collectiveone.modules.users.AppUser;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table( name = "want_contrib_not" )
public class WantToContributeNotification {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private ModelSection modelSection;

	public ModelSection getModelsection()
	{
		return this.modelSection;
	}

	public void setModelsection(ModelSection modelSection)
	{
		this.modelSection = modelSection;
	}

	
	@ManyToOne
	private AppUser user;
	
	@ManyToOne
	private AppUser admin;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "email_state")
	private NotificationState emailState;
	

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

	public AppUser getAdmin() {
		return admin;
	}

	public void setAdmin(AppUser admin) {
		this.admin = admin;
	}

	public NotificationState getEmailState() {
		return emailState;
	}

	public void setEmailState(NotificationState emailState) {
		this.emailState = emailState;
	}
	
}
