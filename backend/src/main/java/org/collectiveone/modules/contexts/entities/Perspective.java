package org.collectiveone.modules.contexts.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "perspectives")
public class Perspective {

	@Id
	@Column(name = "id", updatable = false, nullable = false, unique = true)
	private String id;
	
	private String name;
	
	private String creator;
	
	@Column
	private Timestamp timestamp;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Context context;
	
	/* non hashed content */
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Commit head;
	
	/* non _prtcl */
	
	/* working commits are uncommitted changes, there cab be zero or more per user and perspective. */
	@OneToMany
	private List<Commit> workingCommits = new ArrayList<Commit>();

		
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public Commit getHead() {
		return head;
	}

	public void setHead(Commit head) {
		this.head = head;
	}

	public List<Commit> getWorkingCommits() {
		return workingCommits;
	}

	public void setWorkingCommits(List<Commit> workingCommits) {
		this.workingCommits = workingCommits;
	}
	
}
