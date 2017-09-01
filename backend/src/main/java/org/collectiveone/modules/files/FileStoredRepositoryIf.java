package org.collectiveone.modules.files;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface FileStoredRepositoryIf extends CrudRepository<FileStored, UUID> {
	
	public FileStored findById(UUID fileStoredId);
	
}
