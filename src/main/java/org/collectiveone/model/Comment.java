package org.collectiveone.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.collectiveone.web.dto.CommentDto;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "COMMENTS")
public class Comment {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private User creator;
	private Timestamp creationDate;
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String content;
	@ManyToOne
	private Comment parent;
	@OneToMany
	@JoinTable(name = "COMMENTS_ANSWERS")
	private List<Comment> replies = new ArrayList<Comment>();
	@ManyToOne
	private Cbtion cbtion;
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinTable(name = "COMMENTS_PROMOTERS")
	private List<Promoter> promoters = new ArrayList<Promoter>();
	private int relevance;
	
	
	public CommentDto toDto() {
		CommentDto dto = new CommentDto();
		
		dto.setId(id);
		if(creator != null) dto.setCreatorUsername(creator.getUsername());
		if(creationDate != null) dto.setCreationDate(creationDate.getTime());
		if(content != null) dto.setContent(content);
		if(replies != null) dto.setNreplies(replies.size());
		if(cbtion != null) dto.setCbtionId(cbtion.getId());
		dto.setRelevance(relevance);
		
		return dto;
	}

		
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public User getCreator() {
		return creator;
	}
	public void setCreator(User creator) {
		this.creator = creator;
	}
	public Timestamp getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Comment getParent() {
		return parent;
	}
	public void setParent(Comment parent) {
		this.parent = parent;
	}
	public List<Comment> getReplies() {
		return replies;
	}
	public void setReplies(List<Comment> replies) {
		this.replies = replies;
	}
	public Cbtion getCbtion() {
		return cbtion;
	}
	public void setCbtion(Cbtion cbtion) {
		this.cbtion = cbtion;
	}
	public List<Promoter> getPromoters() {
		return promoters;
	}
	public void setPromoters(List<Promoter> promoters) {
		this.promoters = promoters;
	}
	public int getRelevance() {
		return relevance;
	}
	public void setRelevance(int relevance) {
		this.relevance = relevance;
	}
	
}
