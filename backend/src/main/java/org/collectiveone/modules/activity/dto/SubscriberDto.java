package org.collectiveone.modules.activity.dto;

import org.collectiveone.modules.contexts.dto.ModelSectionDto;
import org.collectiveone.modules.initiatives.dto.InitiativeDto;
import org.collectiveone.modules.users.AppUserDto;

public class SubscriberDto {
	
	private String id;
	private AppUserDto user;
	private String elementId;
	private String elementType;
	private ModelSectionDto section;
	private InitiativeDto initiative;
	
	private String inheritConfig;
	private SubscriberDto applicableSubscriber;
	
	private String inAppConfig;
	private String pushConfig;
	private String emailNowConfig;
	private String emailSummaryConfig;
	private String emailSummaryPeriodConfig;	
	
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
	public ModelSectionDto getSection() {
		return section;
	}
	public void setSection(ModelSectionDto section) {
		this.section = section;
	}
	public InitiativeDto getInitiative() {
		return initiative;
	}
	public void setInitiative(InitiativeDto initiative) {
		this.initiative = initiative;
	}
	public String getInheritConfig() {
		return inheritConfig;
	}
	public void setInheritConfig(String inheritConfig) {
		this.inheritConfig = inheritConfig;
	}
	public SubscriberDto getApplicableSubscriber() {
		return applicableSubscriber;
	}
	public void setApplicableSubscriber(SubscriberDto applicableSubscriber) {
		this.applicableSubscriber = applicableSubscriber;
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
	public String getEmailNowConfig() {
		return emailNowConfig;
	}
	public void setEmailNowConfig(String emailNowConfig) {
		this.emailNowConfig = emailNowConfig;
	}
	public String getEmailSummaryConfig() {
		return emailSummaryConfig;
	}
	public void setEmailSummaryConfig(String emailSummaryConfig) {
		this.emailSummaryConfig = emailSummaryConfig;
	}
	public String getEmailSummaryPeriodConfig() {
		return emailSummaryPeriodConfig;
	}
	public void setEmailSummaryPeriodConfig(String emailSummaryPeriodConfig) {
		this.emailSummaryPeriodConfig = emailSummaryPeriodConfig;
	}
	
}
