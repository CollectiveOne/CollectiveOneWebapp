package org.collectiveone.modules.conversations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.model.ModelCardWrapper;
import org.collectiveone.modules.model.ModelSection;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table( name = "message_threads" )
public class MessageThread {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@Enumerated(EnumType.STRING)
	@Column(name="context_type")
	private MessageThreadContextType contextType;
	
	@OneToOne(mappedBy = "messageThread")
	ModelCardWrapper modelCardWrapper;
	
	@OneToOne(mappedBy = "messageThread")
	ModelSection modelSection;
	
	@OneToOne(mappedBy = "messageThread")
	Initiative initiative;
	
	@OneToMany(mappedBy="thread")
	private List<Message> messages = new ArrayList<Message>();

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public MessageThreadContextType getContextType() {
		return contextType;
	}

	public void setContextType(MessageThreadContextType contextType) {
		this.contextType = contextType;
	}

	public ModelCardWrapper getModelCardWrapper() {
		return modelCardWrapper;
	}

	public void setModelCardWrapper(ModelCardWrapper modelCardWrapper) {
		this.modelCardWrapper = modelCardWrapper;
	}

	public ModelSection getModelSection() {
		return modelSection;
	}

	public void setModelSection(ModelSection modelSection) {
		this.modelSection = modelSection;
	}

	public Initiative getInitiative() {
		return initiative;
	}

	public void setInitiative(Initiative initiative) {
		this.initiative = initiative;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	
	
	
}
