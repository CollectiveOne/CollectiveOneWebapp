package org.collectiveone.modules.c1.views;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.collectiveone.modules.uprcl.entities.Context;
import org.collectiveone.modules.users.AppUser;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "views")
public class View {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private Context context;
	
	@ManyToOne
	private AppUser user;
	
	@Enumerated(EnumType.STRING)
	private ContextViewType contextViewType; 
	
	@Enumerated(EnumType.STRING)
	private ElementViewType elementViewType;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public AppUser getUser() {
		return user;
	}

	public void setUser(AppUser user) {
		this.user = user;
	}

	public ContextViewType getContextViewType() {
		return contextViewType;
	}

	public void setContextViewType(ContextViewType contextViewType) {
		this.contextViewType = contextViewType;
	}

	public ElementViewType getElementViewType() {
		return elementViewType;
	}

	public void setElementViewType(ElementViewType elementViewType) {
		this.elementViewType = elementViewType;
	}
	
}
