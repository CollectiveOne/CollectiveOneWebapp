package org.collectiveone.modules.c1.userSupport;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.collectiveone.modules.c1.data.DataType;
import org.collectiveone.modules.c1.data.TextContent;
import org.collectiveone.modules.uprcl.entities.Link;
import org.collectiveone.modules.uprcl.entities.Perspective;
import org.collectiveone.modules.users.AppUser;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.SortNatural;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "working_commits")
public class WorkingCommit {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private AppUser user;
	
	@ManyToOne
	private Perspective perspective;
	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String message;
	

	@Enumerated(EnumType.STRING)
	private DataType type;
	
	@ManyToOne
	private TextContent textContent;
	
	@OneToMany
	@MapKey(name="id")
	@SortNatural
	private SortedMap<String, Link> links = new TreeMap<String, Link>();
	

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public AppUser getUser() {
		return user;
	}

	public void setUser(AppUser user) {
		this.user = user;
	}

	public Perspective getPerspective() {
		return perspective;
	}

	public void setPerspective(Perspective perspective) {
		this.perspective = perspective;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public DataType getType() {
		return type;
	}

	public void setType(DataType type) {
		this.type = type;
	}

	public TextContent getTextContent() {
		return textContent;
	}

	public void setTextContent(TextContent textContent) {
		this.textContent = textContent;
	}

	public SortedMap<String, Link> getLinks() {
		return links;
	}

	public void setLinks(SortedMap<String, Link> links) {
		this.links = links;
	}
	
}
