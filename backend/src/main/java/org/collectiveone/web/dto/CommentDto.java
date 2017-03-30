package org.collectiveone.web.dto;

import java.util.ArrayList;
import java.util.List;

public class CommentDto {
	private Long id;
	private String creatorUsername;
	private long creationDate;
	private String content;
	private Long parentId;
	private int nreplies;
	private List<CommentDto> replies = new ArrayList<CommentDto>();
	private Long cbtionId;
	private Integer relevance;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCreatorUsername() {
		return creatorUsername;
	}
	public void setCreatorUsername(String creatorUsername) {
		this.creatorUsername = creatorUsername;
	}
	public long getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public int getNreplies() {
		return nreplies;
	}
	public void setNreplies(int nreplies) {
		this.nreplies = nreplies;
	}
	public List<CommentDto> getReplies() {
		return replies;
	}
	public void setReplies(List<CommentDto> replies) {
		this.replies = replies;
	}
	public Long getCbtionId() {
		return cbtionId;
	}
	public void setCbtionId(Long cbtionId) {
		this.cbtionId = cbtionId;
	}
	public Integer getRelevance() {
		return relevance;
	}
	public void setRelevance(Integer relevance) {
		this.relevance = relevance;
	}
	
	
}
