package org.collectiveone.modules.files;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.collectiveone.modules.users.AppUser;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table( name = "files" )
public class FileStored {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private AppUser uploadedBy;
	
	@Column(name="bucket")
	private String bucket;
	
	@Column(name="key")
	private String key;
	
	@Column(name="url")
	private String url;
	
	@Column(name="last_updated")
	private Timestamp lastUpdated;
	
	public FileStoredDto toDto() {
		FileStoredDto dto = new FileStoredDto();
		
		dto.setId(id.toString());
		dto.setKey(key);
		dto.setBucket(bucket);
		dto.setUrl(url);
		dto.setUploadedById(uploadedBy.getC1Id().toString());
		
		if(lastUpdated != null) dto.setLastUpdated(lastUpdated.getTime());
		
		return dto;
	}
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
	
	public AppUser getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(AppUser uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Timestamp getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Timestamp lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
}
