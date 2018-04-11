package org.collectiveone.modules.model.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.model.ModelCardWrapper;
import org.collectiveone.modules.model.ModelSection;
import org.collectiveone.modules.users.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ModelCardWrapperRepositoryIf extends CrudRepository<ModelCardWrapper, UUID> {
	
	public ModelCardWrapper findById(UUID modelCardWrapperId);

	@Query("SELECT section from ModelSection section JOIN section.cardsWrappers crds WHERE crds.id = ?1")
	public List<ModelSection> findParentSections(UUID modelCardWrapperId);
	
	@Query("SELECT crdWrp FROM ModelSection sec JOIN sec.cardsWrappers crdWrp JOIN crdWrp.card crd "
			+ "WHERE (LOWER(crd.title) LIKE ?2 OR LOWER(crd.text) LIKE ?2 OR LOWER(crdWrp.creator.profile.nickname) LIKE ?2) "
			+ "AND sec.id IN ?1")
	public Page<ModelCardWrapper> searchInSectionByQuery(List<UUID> sectionIds, String query, Pageable page);
	
	@Query("SELECT crdWrp from ModelCardWrapper crdWrp WHERE crdWrp.creationDate IS NULL OR crdWrp.creator IS NULL")
	public List<ModelCardWrapper> findWithNullCreation();
	
	@Query("SELECT crdWrp from ModelCardWrapper crdWrp WHERE crdWrp.lastEdited IS NULL OR crdWrp.editors IS EMPTY")
	public List<ModelCardWrapper> findWithNullLastEdited();
	
	@Query("SELECT edt from ModelCardWrapper crdWrp JOIN crdWrp.editors edt WHERE crdWrp.id = ?1 AND edt.id = ?2")
	public AppUser findEditor(UUID cardWrapperId, UUID editorId);
	
}
