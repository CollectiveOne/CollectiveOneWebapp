package org.collectiveone.modules.activity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.collectiveone.modules.activity.dto.ActivityDto;
import org.collectiveone.modules.activity.enums.ActivityType;
import org.collectiveone.modules.assignations.Assignation;
import org.collectiveone.modules.conversations.Message;
import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.model.ModelCardWrapper;
import org.collectiveone.modules.model.ModelSection;
import org.collectiveone.modules.tokens.InitiativeTransfer;
import org.collectiveone.modules.tokens.TokenMint;
import org.collectiveone.modules.tokens.TokenType;
import org.collectiveone.modules.users.AppUser;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

@Entity
@Table( name = "activity" )
public class Activity {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private ActivityType type;
	
	@OneToMany
	private List<AppUser> mentions = new ArrayList<AppUser>();
	
	@Column(name = "timestamp")
	private Timestamp timestamp;
	
	@OneToMany(mappedBy = "activity")
	private List<Notification> notifications = new ArrayList<Notification>();
	
	/* support columns needed depending on the type of activity */
	@ManyToOne
	private AppUser triggerUser;
	
	@ManyToOne
	private Initiative initiative;
	
	@ManyToOne
	private Initiative subInitiative;
	
	@OneToMany
	private List<InitiativeTransfer> initiativeTransfers = new ArrayList<InitiativeTransfer>();
	
	@Column(name = "old_name", length = 30)
	private String oldName;
	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	@Column(name = "old_driver")
	private String oldDriver;
	
	@OneToOne
	private TokenType tokenType;
	
	@OneToOne
	private TokenMint mint;
	
	@ManyToOne
	private Assignation assignation;
	
	@OneToOne
	private InitiativeTransfer initiativeTransfer;
	
	@ManyToOne
	private ModelSection modelSection;
	
	@ManyToOne
	private ModelCardWrapper modelCardWrapper;
	
	@ManyToOne
	private ModelSection onSection;
	
	@ManyToOne
	private ModelSection fromSection;
	
	
	// -------
	
	@ManyToOne
	private Message message;
	
	
	
	/* Dto */
	public ActivityDto toDto() {
		ActivityDto dto = new ActivityDto();
		
		dto.setId(id.toString());
		dto.setType(type.toString());
		if (timestamp != null) dto.setTimestamp(timestamp.getTime());
		if(triggerUser != null) dto.setTriggerUser(triggerUser.toDtoLight());
		if(initiative != null) dto.setInitiative(initiative.toDto());
		
		if(subInitiative != null) dto.setSubInitiative(subInitiative.toDto());
		
		dto.setOldName(oldName);
		dto.setOldDriver(oldDriver);
		
		if(mint != null) dto.setMint(mint.toDto());
		if(assignation != null) dto.setAssignation(assignation.toDto()); 
		if(initiativeTransfer != null) dto.setTransfer(initiativeTransfer.toDto()); 
		
		if(modelSection != null) dto.setModelSection(modelSection.toDto());
		if(modelCardWrapper != null) dto.setModelCardWrapper(modelCardWrapper.toDto());
		
		if(onSection != null) dto.setOnSection(onSection.toDto());
		if(fromSection != null) dto.setFromSection(fromSection.toDto());
		
		if(message != null) dto.setMessage(message.toDto());
		
		return dto;
	}
	
	/* getters and setters */

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public ActivityType getType() {
		return type;
	}

	public void setType(ActivityType type) {
		this.type = type;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
	public List<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}
	
	public AppUser getTriggerUser() {
		return triggerUser;
	}

	public void setTriggerUser(AppUser triggerUser) {
		this.triggerUser = triggerUser;
	}

	public Initiative getInitiative() {
		return initiative;
	}

	public void setInitiative(Initiative initiative) {
		this.initiative = initiative;
	}

	public Initiative getSubInitiative() {
		return subInitiative;
	}

	public void setSubInitiative(Initiative subInitiative) {
		this.subInitiative = subInitiative;
	}

	public List<InitiativeTransfer> getInitiativeTransfers() {
		return initiativeTransfers;
	}

	public void setInitiativeTransfers(List<InitiativeTransfer> initiativeTransfers) {
		this.initiativeTransfers = initiativeTransfers;
	}

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	public String getOldDriver() {
		return oldDriver;
	}

	public void setOldDriver(String oldDriver) {
		this.oldDriver = oldDriver;
	}

	public TokenType getTokenType() {
		return tokenType;
	}

	public void setTokenType(TokenType tokenType) {
		this.tokenType = tokenType;
	}

	public TokenMint getMint() {
		return mint;
	}

	public void setMint(TokenMint mint) {
		this.mint = mint;
	}

	public Assignation getAssignation() {
		return assignation;
	}

	public void setAssignation(Assignation assignation) {
		this.assignation = assignation;
	}

	public InitiativeTransfer getInitiativeTransfer() {
		return initiativeTransfer;
	}

	public void setInitiativeTransfer(InitiativeTransfer initiativeTransfer) {
		this.initiativeTransfer = initiativeTransfer;
	}

	public ModelSection getModelSection() {
		return modelSection;
	}

	public void setModelSection(ModelSection modelSection) {
		this.modelSection = modelSection;
	}

	public ModelCardWrapper getModelCardWrapper() {
		return modelCardWrapper;
	}

	public void setModelCardWrapper(ModelCardWrapper modelCardWrapper) {
		this.modelCardWrapper = modelCardWrapper;
	}

	public ModelSection getOnSection() {
		return onSection;
	}

	public void setOnSection(ModelSection onSection) {
		this.onSection = onSection;
	}

	public ModelSection getFromSection() {
		return fromSection;
	}

	public void setFromSection(ModelSection fromSection) {
		this.fromSection = fromSection;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public List<AppUser> getMentions() {
		return mentions;
	}

	public void setMentions(List<AppUser> mentions) {
		this.mentions = mentions;
	}
	
}
