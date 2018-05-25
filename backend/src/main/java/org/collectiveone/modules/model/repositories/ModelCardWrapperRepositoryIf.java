package org.collectiveone.modules.model.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.model.ModelCardWrapper;
import org.collectiveone.modules.users.AppUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ModelCardWrapperRepositoryIf extends CrudRepository<ModelCardWrapper, UUID> {
	
	public ModelCardWrapper findById(UUID modelCardWrapperId);
	
	@Query("SELECT crdWrp FROM ModelCardWrapper crdWrp WHERE crdWrp.creationDate IS NULL OR crdWrp.creator IS NULL")
	public List<ModelCardWrapper> findWithNullCreation();
	
	@Query("SELECT crdWrp FROM ModelCardWrapper crdWrp WHERE crdWrp.lastEdited IS NULL OR crdWrp.editors IS EMPTY")
	public List<ModelCardWrapper> findWithNullLastEdited();
	
	@Query("SELECT edt FROM ModelCardWrapper crdWrp JOIN crdWrp.editors edt WHERE crdWrp.id = ?1 AND edt.id = ?2")
	public AppUser findEditor(UUID cardWrapperId, UUID editorId);
	
}
