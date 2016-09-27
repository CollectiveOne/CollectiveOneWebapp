package coproject.cpweb.utils.db.entities.dtos;

import java.util.ArrayList;
import java.util.List;

public class CommentDto {
	private int id;
	private String creatorUsername;
	private long creationDate;
	private String content;
	private int parentId;
	private int nreplies;
	private List<CommentDto> replies = new ArrayList<CommentDto>();
	private int cbtionId;
	private Integer relevance;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
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
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
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
	public int getCbtionId() {
		return cbtionId;
	}
	public void setCbtionId(int cbtionId) {
		this.cbtionId = cbtionId;
	}
	public Integer getRelevance() {
		return relevance;
	}
	public void setRelevance(Integer relevance) {
		this.relevance = relevance;
	}
	
	
}
