package org.collectiveone.modules.users.nannies;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "did_nannies")
public class DIDNanny {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	private String did;
	
	@Enumerated(EnumType.STRING)
	private DIDNannyType type;
	
	private String extId;
	
	private byte[] privateKey;

	public DIDNannyDto toDto() {
		DIDNannyDto dto = new DIDNannyDto();
		
		dto.setDid(did);
		dto.setType(type);
		dto.setExtId(extId);
		
		return dto;
	}

	public DIDNannyType getType() {
		return type;
	}

	public void setType(DIDNannyType type) {
		this.type = type;
	}

	public String getExtId() {
		return extId;
	}

	public void setExtId(String extId) {
		this.extId = extId;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public byte[] getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(byte[] privateKey) {
		this.privateKey = privateKey;
	}
	
}
	
