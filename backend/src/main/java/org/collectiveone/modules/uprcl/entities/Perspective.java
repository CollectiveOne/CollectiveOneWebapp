package org.collectiveone.modules.uprcl.entities;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.bitcoinj.core.Base58;
import org.collectiveone.modules.c1.data.LinkToCommit;
import org.collectiveone.modules.uprcl.dtos.PerspectiveDto;
import org.collectiveone.modules.uprcl.support.JacksonViews;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name = "perspectives")
@JsonPropertyOrder({ "creator", "name", "context", "type", "head" })
public class Perspective {

	@Id
	@Column(name = "id", updatable = false, nullable = false, unique = true)
	@JsonIgnore
	private String id;

	@JsonView(JacksonViews.DynamicPerspective.class)
	private String creator;
	
	@JsonView(JacksonViews.DynamicPerspective.class)
	private String name;
	
	@JsonView(JacksonViews.DynamicPerspective.class)
	@ManyToOne(fetch = FetchType.LAZY)
	private Context context;
	
	@JsonView(JacksonViews.StaticPerspective.class)
	@ManyToOne(fetch = FetchType.LAZY)
	private LinkToCommit commitLink;
	
	@JsonView(JacksonViews.DynamicPerspective.class)
	@Enumerated(EnumType.STRING)
	private PerspectiveType type;
	
	
	/* working commits are uncommitted changes, there cab be zero or more per user and perspective. */
	@OneToMany
	@JsonIgnore
	private List<Commit> workingCommits = new ArrayList<Commit>();

	
	public String computeId() {
		try {
			MessageDigest digestInstance = MessageDigest.getInstance("SHA-256");
			ObjectMapper objectMapper = new ObjectMapper();
			
			String json = "";
			
			switch (type) {
			
			case DYNAMIC: 
				json = objectMapper
				.writerWithView(JacksonViews.DynamicPerspective.class)
				.writeValueAsString(this);	
			break;
				
			case STATIC:
				json = objectMapper
				.writerWithView(JacksonViews.StaticPerspective.class)
				.writeValueAsString(this);	
			break;
			
			}
			
			byte[] hash = digestInstance.digest(json.getBytes());
			
			return Base58.encode(hash);	
		} catch (Exception e) {
			// TODO
		}
		return null;
	}
	
	@JsonGetter("context")
    public String getContextId() {
        return context.getId();
    }
	
	@JsonGetter("commit")
    public String getCommitId() {
        return commit.getId();
    }
	
	public PerspectiveDto toDto() throws JsonProcessingException {
		return this.toDto(0);
	}
	
	public PerspectiveDto toDto(Integer levels) throws JsonProcessingException {
		PerspectiveDto dto = new PerspectiveDto();
		
		dto.setId(id);
		dto.setContext(context.toDto());
		dto.setCreator(creator);
		dto.setType(type);
		dto.setName(name);
		if (commit != null) dto.setHead(commit.toDto(levels));
		
		return dto;
	}
	
	@Override
	public String toString() {
		return "   name: " + name + "\n" + 
			   "     id: " + id + "\n" + 
			   "context: " + (context != null ? context.getId() : "null") + "\n" +
			   " commit: " + (commit !=null ? commit.getId() : "null");
	}
	
	public String getId() {
		return id;
	}
	
	public void setId() {
		this.id = this.computeId();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public LinkToCommit getCommitLink() {
		return commitLink;
	}

	public void setCommitLink(LinkToCommit commitLink) {
		this.commitLink = commitLink;
	}

	public PerspectiveType getType() {
		return type;
	}

	public void setType(PerspectiveType type) {
		this.type = type;
	}

	public List<Commit> getWorkingCommits() {
		return workingCommits;
	}

	public void setWorkingCommits(List<Commit> workingCommits) {
		this.workingCommits = workingCommits;
	}

}
