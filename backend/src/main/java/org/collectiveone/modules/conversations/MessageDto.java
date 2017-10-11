package org.collectiveone.modules.conversations;

import java.sql.Timestamp;

import org.collectiveone.modules.users.AppUser;
import org.collectiveone.modules.users.AppUserDto;

public class MessageDto {
	
	private String id;
	private String text;
	private String threadId;
	private String contextType;
	private String contextElementId;
	private Long timestamp;
	private String authorId;
	private AppUserDto author;
	
	public Message toEntity(MessageDto messageDto, AppUser author) {
		
		Message message = new Message();
		
		message.setTimestamp(new Timestamp(System.currentTimeMillis()));
		message.setAuthor(author);
		message.setText(messageDto.getText());
		
		return message;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getThreadId() {
		return threadId;
	}
	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}
	public String getContextType() {
		return contextType;
	}
	public void setContextType(String contextType) {
		this.contextType = contextType;
	}
	public String getContextElementId() {
		return contextElementId;
	}
	public void setContextElementId(String contextElementId) {
		this.contextElementId = contextElementId;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	public AppUserDto getAuthor() {
		return author;
	}
	public void setAuthor(AppUserDto author) {
		this.author = author;
	}
	
}
