package org.collectiveone.modules.files;

public class FileStoredDto {

	private String id;
	private String modelSectionId;

	public String getModelsectionid()
	{
		return this.modelSectionId;
	}

	public void setModelsectionid(String modelSectionId)
	{
		this.modelSectionId = modelSectionId;
	}

	private String modelSectionName;

	public String getModelsectionname()
	{
		return this.modelSectionName;
	}

	public void setModelsectionname(String modelSectionName)
	{
		this.modelSectionName = modelSectionName;
	}

	private String uploadedById;
	private String bucket;
	private String key;
	private String url;
	private Long lastUpdated;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getUploadedById() {
		return uploadedById;
	}
	public void setUploadedById(String uploadedById) {
		this.uploadedById = uploadedById;
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
	public Long getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(Long lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
		
}
