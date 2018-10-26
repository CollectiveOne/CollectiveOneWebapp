package org.collectiveone.modules.conversations;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.collectiveone.modules.model.enums.Status;
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
	private AppUser author;
	
	@Column(name = "timestamp")
	private Timestamp timestamp;	
	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	@Column(name = "text")
	private String text;
	
	@Enumerated(EnumType.STRING)
	private Status status;
	
	
	public MessageDto toDto() {
		MessageDto dto = new MessageDto();
		dto.setId(id.toString());
		dto.setAuthor(author.toDtoLight());
		dto.setText(text);
		if (status != null) {
			dto.setStatus(status.toString());
			if (status == Status.DELETED) {
				dto.setText("");	
			}
		} 
		
		return dto;
	}
	

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
