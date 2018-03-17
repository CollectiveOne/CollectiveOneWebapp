package org.collectiveone.modules.conversations;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.collectiveone.modules.users.AppUser;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

@Entity
@Table( name = "messages" )
public class Message {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private MessageThread thread;
	
	@ManyToOne
	private AppUser author;
	
	@Column(name = "timestamp")
	private Timestamp timestamp;	
	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	@Column(name = "text")
	private String text;
	
	@OneToOne
	private MessageThread subThread;
	
	
	public MessageDto toDto() {
		MessageDto dto = new MessageDto();
		dto.setId(id.toString());
		dto.setAuthor(author.toDtoLight());
		dto.setText(text);
		if (thread != null) dto.setThreadId(thread.getId().toString());
		return dto;
	}
	

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public MessageThread getThread() {
		return thread;
	}

	public void setThread(MessageThread thread) {
		this.thread = thread;
	}

	public AppUser getAuthor() {
		return author;
	}

	public void setAuthor(AppUser author) {
		this.author = author;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public MessageThread getSubThread() {
		return subThread;
	}

	public void setSubThread(MessageThread subThread) {
		this.subThread = subThread;
	}
	

}
