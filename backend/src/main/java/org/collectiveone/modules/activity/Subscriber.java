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
import org.collectiveone.modules.activity.enums.SubscriberEmailNowConfig;
import org.collectiveone.modules.activity.enums.SubscriberEmailSummaryConfig;
import org.collectiveone.modules.activity.enums.SubscriberEmailSummaryPeriodConfig;
import org.collectiveone.modules.activity.enums.SubscriberInAppConfig;
import org.collectiveone.modules.activity.enums.SubscriberInheritConfig;
import org.collectiveone.modules.activity.enums.SubscriberPushConfig;
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
	@Column(name = "inherit_config")
	private SubscriberInheritConfig inheritConfig;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "in_app_config")
	private SubscriberInAppConfig inAppConfig;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "push_config")
	private SubscriberPushConfig pushConfig;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "emails_now_config")
	private SubscriberEmailNowConfig emailNowConfig;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "emails_summary_config")
	private SubscriberEmailSummaryConfig emailSummaryConfig;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "emails_summary_period_config")
	private SubscriberEmailSummaryPeriodConfig emailSummaryPeriodConfig;
	
	
	@OneToMany(mappedBy = "subscriber", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Notification> notifications = new ArrayList<Notification>();
	
	
	public SubscriberDto toDto() {
		SubscriberDto dto = new SubscriberDto();
		dto.setId(id.toString());
		dto.setElementId(elementId.toString());
		dto.setUser(user.toDtoLight());
		
		dto.setInheritConfig(inheritConfig.toString());
		
		dto.setInAppConfig(inAppConfig.toString());
		dto.setPushConfig(pushConfig.toString());
		dto.setEmailsNowConfig(emailNowConfig.toString());
		dto.setEmailsSummaryConfig(emailSummaryConfig.toString());
		
		return dto;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Subscriber other = (Subscriber) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
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

	public SubscriberInheritConfig getInheritConfig() {
		return inheritConfig;
	}

	public void setInheritConfig(SubscriberInheritConfig inheritConfig) {
		this.inheritConfig = inheritConfig;
	}

	public SubscriberEmailNowConfig getEmailNowConfig() {
		return emailNowConfig;
	}

	public void setEmailNowConfig(SubscriberEmailNowConfig emailNowConfig) {
		this.emailNowConfig = emailNowConfig;
	}

	public SubscriberEmailSummaryConfig getEmailSummaryConfig() {
		return emailSummaryConfig;
	}

	public void setEmailSummaryConfig(SubscriberEmailSummaryConfig emailSummaryConfig) {
		this.emailSummaryConfig = emailSummaryConfig;
	}

	public SubscriberInAppConfig getInAppConfig() {
		return inAppConfig;
	}

	public void setInAppConfig(SubscriberInAppConfig inAppConfig) {
		this.inAppConfig = inAppConfig;
	}

	public SubscriberPushConfig getPushConfig() {
		return pushConfig;
	}

	public void setPushConfig(SubscriberPushConfig pushConfig) {
		this.pushConfig = pushConfig;
	}

	public List<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}

	public SubscriberEmailSummaryPeriodConfig getEmailSummaryPeriodConfig() {
		return emailSummaryPeriodConfig;
	}

	public void setEmailSummaryPeriodConfig(SubscriberEmailSummaryPeriodConfig emailSummaryPeriodConfig) {
		this.emailSummaryPeriodConfig = emailSummaryPeriodConfig;
	}	
	
	
}
