package org.collectiveone.modules.c1.data;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.collectiveone.modules.uprcl.entities.Perspective;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "links")
public class Link {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	@JsonIgnore
	private UUID id;
	
	private String platform;
	
	@ManyToOne
	private Perspective parent;
	
	@ManyToOne
	private Perspective perspective;
	
	@ManyToOne
	private Perspective before;
	
	@ManyToOne
	private Perspective after;
	
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Perspective getPerspective() {
		return perspective;
	}

	public void setPerspective(Perspective perspective) {
		this.perspective = perspective;
	}
	
	public Perspective getParent() {
		return parent;
	}

	public void setParent(Perspective parent) {
		this.parent = parent;
	}
	
	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public Perspective getBefore() {
		return before;
	}

	public void setBefore(Perspective before) {
		this.before = before;
	}

	public Perspective getAfter() {
		return after;
	}

	public void setAfter(Perspective after) {
		this.after = after;
	}
	
}
