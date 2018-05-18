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
	
	@Query("SELECT crdWrp FROM ModelSection section JOIN section.cardsWrappers crdWrp WHERE crdWrp.id = ?1 AND section.id = ?2")
	public ModelCardWrapper findByIdAndSectionId(UUID modelCardWrapperId, UUID sectionId);

	@Query("SELECT section FROM ModelSection section "
			+ "JOIN section.cardsWrappers crds "
			+ "WHERE crds.id = ?1")
	public List<ModelSection> findParentSections(UUID modelCardWrapperId);
	
	@Query("SELECT DISTINCT crdWrp, crdWrpA, crdWrpS FROM ModelSection sec "
			+ "JOIN sec.cardsWrappers crdWrp JOIN crdWrp.card crd "
			+ "JOIN sec.cardsWrappersAdditionsPrivate crdWrpAP JOIN crdWrpAP.cardWrapper crdWrpA JOIN crdWrpA.card crdA "
			+ "JOIN sec.cardsWrappersAdditionsShared  crdWrpAS JOIN crdWrpAS.cardWrapper crdWrpS JOIN crdWrpS.card crdS "
			+ "WHERE (LOWER(crd.title) LIKE ?2 OR LOWER(crd.text) LIKE ?2 OR LOWER(crdWrp.creator.profile.nickname) LIKE ?2) "
			+ "OR    (LOWER(crdA.title) LIKE ?2 OR LOWER(crdA.text) LIKE ?2 OR LOWER(crdWrpA.creator.profile.nickname) LIKE ?2) "
			+ "OR    (LOWER(crdS.title) LIKE ?2 OR LOWER(crdS.text) LIKE ?2 OR LOWER(crdWrpS.creator.profile.nickname) LIKE ?2) "
			+ "AND sec.id IN ?1 "
			+ "AND (crdWrp.status != 'DELETED' OR crdWrp.status IS NULL) "
			+ "AND (crdWrpA.status != 'DELETED' OR crdWrpA.status IS NULL) "
			+ "AND (crdWrpS.status != 'DELETED' OR crdWrpS.status IS NULL) ")
	public Page<ModelCardWrapper> searchInSectionsByQuery(List<UUID> sectionIds, String query, Pageable page);
	
	@Query("SELECT DISTINCT crdWrp FROM ModelCardWrapper crdWrp JOIN crdWrp.card crd "
			+ "WHERE (LOWER(crd.title) LIKE ?2 OR LOWER(crd.text) LIKE ?2 OR LOWER(crdWrp.creator.profile.nickname) LIKE ?2) "
			+ "AND crdWrp.initiative.id IN ?1 "
			+ "AND (crdWrp.status != 'DELETED' OR crdWrp.status IS NULL) ORDER BY crdWrp.lastEdited")
	public Page<ModelCardWrapper> searchInInitiativesByQuery(List<UUID> initiativeIds, String query, Pageable page);
	
	@Query("SELECT crdWrp FROM ModelCardWrapper crdWrp WHERE crdWrp.creationDate IS NULL OR crdWrp.creator IS NULL")
	public List<ModelCardWrapper> findWithNullCreation();
	
	@Query("SELECT crdWrp FROM ModelCardWrapper crdWrp WHERE crdWrp.lastEdited IS NULL OR crdWrp.editors IS EMPTY")
	public List<ModelCardWrapper> findWithNullLastEdited();
	
	@Query("SELECT edt FROM ModelCardWrapper crdWrp JOIN crdWrp.editors edt WHERE crdWrp.id = ?1 AND edt.id = ?2")
	public AppUser findEditor(UUID cardWrapperId, UUID editorId);
	
	@Query("SELECT DISTINCT crdWrp.id FROM ModelSection sec "
			+ "JOIN sec.cardsWrappers crdWrp "
			+ "WHERE sec.id IN ?1 "
			+ "AND (crdWrp.status != 'DELETED' OR crdWrp.status IS NULL)")
	public List<UUID> findAllCardsIdsOfSections(List<UUID> sectionId);
	
	
}
