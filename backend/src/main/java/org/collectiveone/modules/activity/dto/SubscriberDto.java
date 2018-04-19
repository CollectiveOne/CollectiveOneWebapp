package org.collectiveone.modules.activity.dto;

import org.collectiveone.modules.users.AppUserDto;

public class SubscriberDto {
	
	private String id;
	private AppUserDto user;
	private String elementId;
	private String elementType;
	
	private String inheritConfig;
	
	private String inAppConfig;
	private String pushConfig;
	private String emailsNowConfig;
	private String emailsSummaryConfig;
	private String emailsSummaryPeriodConfig;	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public AppUserDto getUser() {
		return user;
	}
	public void setUser(AppUserDto user) {
		this.user = user;
	}
	public String getElementId() {
		return elementId;
	}
	public void setElementId(String elementId) {
		this.elementId = elementId;
	}
	public String getElementType() {
		return elementType;
	}
	public void setElementType(String elementType) {
		this.elementType = elementType;
	}
	public String getInheritConfig() {
		return inheritConfig;
	}
	public void setInheritConfig(String inheritConfig) {
		this.inheritConfig = inheritConfig;
	}
	public String getInAppConfig() {
		return inAppConfig;
	}
	public void setInAppConfig(String inAppConfig) {
		this.inAppConfig = inAppConfig;
	}
	public String getPushConfig() {
		return pushConfig;
	}
	public void setPushConfig(String pushConfig) {
		this.pushConfig = pushConfig;
	}
	public String getEmailsNowConfig() {
		return emailsNowConfig;
	}
	public void setEmailsNowConfig(String emailsNowConfig) {
		this.emailsNowConfig = emailsNowConfig;
	}
	public String getEmailsSummaryConfig() {
		return emailsSummaryConfig;
	}
	public void setEmailsSummaryConfig(String emailsSummaryConfig) {
		this.emailsSummaryConfig = emailsSummaryConfig;
	}
	public String getEmailsSummaryPeriodConfig() {
		return emailsSummaryPeriodConfig;
	}
	public void setEmailsSummaryPeriodConfig(String emailsSummaryPeriodConfig) {
		this.emailsSummaryPeriodConfig = emailsSummaryPeriodConfig;
	}
	
	
}
