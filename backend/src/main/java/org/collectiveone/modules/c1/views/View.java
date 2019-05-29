package org.collectiveone.modules.c1.views;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.collectiveone.modules.ipld.IpldService;
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
	
	private byte[] elementId;
	
	private byte[] inElementId;
	
	@ManyToOne
	private AppUser user;
	
	@Enumerated(EnumType.STRING)
	private ElementViewType elementViewType;
	
	private Timestamp timestamp;
	
	
	public ViewDto toDto() {
		ViewDto dto = new ViewDto();
		
		dto.setElementId(IpldService.decode(elementId));
		dto.setElementViewType(elementViewType);
		
		return dto;
	}
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
	
	public byte[] getInElementId() {
		return inElementId;
	}

	public void setInElementId(byte[] inElementId) {
		this.inElementId = inElementId;
	}

	public byte[] getElementId() {
		return elementId;
	}

	public void setElementId(byte[] elementId) {
		this.elementId = elementId;
	}

	public AppUser getUser() {
		return user;
	}

	public void setUser(AppUser user) {
		this.user = user;
	}

	public ElementViewType getElementViewType() {
		return elementViewType;
	}

	public void setElementViewType(ElementViewType elementViewType) {
		this.elementViewType = elementViewType;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
}
