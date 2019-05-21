package org.collectiveone.modules.c1.data.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.collectiveone.modules.c1.data.dtos.DataDto;
import org.collectiveone.modules.c1.data.enums.DataType;
import org.collectiveone.modules.users.AppUser;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "drafts")
public class Draft implements DataIf {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private AppUser user;
	
	private byte[] elementId;
	
	@Enumerated(EnumType.STRING)
	private DataType type;
	
	@ManyToOne
	private TextData textData;
	
	@ManyToOne
	private NodeData nodeData;
	
	public DataDto toDataDto() throws Exception {
			
		DataDto dto = new DataDto();
		
		switch (type) {
		
		case TEXT:
			dto.setJsonData(textData.toDto().getDataJson());
		break;
		
		case NODE:
			dto.setJsonData(nodeData.toDto().getDataJson());
		break;
		
		}
		
		dto.setType(type);
		
		return dto;
	}

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

	public byte[] getElementId() {
		return elementId;
	}

	public void setElementId(byte[] elementId) {
		this.elementId = elementId;
	}

	public DataType getType() {
		return type;
	}

	public void setType(DataType type) {
		this.type = type;
	}

	public TextData getTextData() {
		return textData;
	}

	public void setTextData(TextData textData) {
		this.textData = textData;
	}

	public NodeData getNodeData() {
		return nodeData;
	}

	public void setNodeData(NodeData nodeData) {
		this.nodeData = nodeData;
	}
	
}
