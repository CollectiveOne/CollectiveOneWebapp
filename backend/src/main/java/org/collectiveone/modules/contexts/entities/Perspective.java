package org.collectiveone.modules.contexts.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "perspectives")
public class Perspective {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	private String name;
	
	@ManyToOne
	private Context context;
	
	@ManyToOne
	private Commit head;
	
	/* working commits are uncommitted changes, there is one per user and perspective. */
	@OneToMany
	private List<Commit> workingCommits = new ArrayList<Commit>();
	
	
	public Perspective(String name, Context context, Commit head, List<Commit> workingCommits) {
		super();
		this.name = name;
		this.context = context;
		this.head = head;
		this.workingCommits = workingCommits;
	}
	
	public Perspective() {
		super();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
