package org.collectiveone.modules.model.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.model.ModelCardWrapper;
import org.collectiveone.modules.model.ModelCardWrapperAddition;
import org.collectiveone.modules.model.ModelScope;
import org.collectiveone.modules.model.ModelSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ModelCardWrapperAdditionRepositoryIf extends CrudRepository<ModelCardWrapperAddition, UUID> {
	
	ModelCardWrapperAddition findById(UUID cardWrapperAdditionId);
	
	@Query("SELECT sec FROM ModelCardWrapperAddition crdWrpAdd "
			+ "JOIN crdWrpAdd.section sec "
			+ "WHERE crdWrpAdd.cardWrapper.id = ?1 "
			+ "AND (crdWrpAdd.status != 'DELETED' OR crdWrpAdd.status IS NULL)")
	List<ModelSection> findParentSections(UUID cardWrapperId);
	
	@Query("SELECT crdWrpAdd FROM ModelCardWrapperAddition crdWrpAdd "
			+ "WHERE crdWrpAdd.cardWrapper.id = ?1 "
			+ "AND (crdWrpAdd.status != 'DELETED' OR crdWrpAdd.status IS NULL) "
			+ "ORDER BY crdWrpAdd.cardWrapper.creationDate ASC")
	List<ModelCardWrapperAddition> findOfCardWrapper(UUID cardWrapperId);
	
	@Query("SELECT crdWrpAdd FROM ModelCardWrapperAddition crdWrpAdd "
			+ "WHERE crdWrpAdd.section.id = ?1 "
			+ "AND crdWrpAdd.scope = ?2 "
			+ "AND (crdWrpAdd.status != 'DELETED' OR crdWrpAdd.status IS NULL) "
			+ "ORDER BY crdWrpAdd.cardWrapper.creationDate ASC")
	List<ModelCardWrapperAddition> findInSectionWithScope(UUID sectionId, ModelScope scope);
	
	@Query("SELECT crdWrpAdd FROM ModelCardWrapperAddition crdWrpAdd "
			+ "WHERE crdWrpAdd.section.id = ?1 "
			+ "AND crdWrpAdd.scope = 'COMMON' "
			+ "AND (crdWrpAdd.status != 'DELETED' OR crdWrpAdd.status IS NULL) "
			+ "ORDER BY crdWrpAdd.cardWrapper.creationDate ASC")
	List<ModelCardWrapperAddition> findCommonInSection(UUID sectionId);
	
	@Query("SELECT crdWrpAdd FROM ModelCardWrapperAddition crdWrpAdd "
			+ "WHERE crdWrpAdd.adder.c1Id = ?1 "
			+ "AND crdWrpAdd.section.id = ?2 "
			+ "AND crdWrpAdd.scope = ?3 "
			+ "AND (crdWrpAdd.status != 'DELETED' OR crdWrpAdd.status IS NULL)"
			+ "ORDER BY crdWrpAdd.cardWrapper.creationDate ASC")
	List<ModelCardWrapperAddition> findOfUserInSection(UUID userId, UUID sectionId, ModelScope scope);
	
	@Query("SELECT crdWrpAdd FROM ModelCardWrapperAddition crdWrpAdd "
			+ "JOIN crdWrpAdd.cardWrapper crdWrp "
			+ "WHERE crdWrp.id= ?2 "
			+ "AND crdWrpAdd.section.id = ?1 "
			+ "AND (crdWrpAdd.scope != 'PRIVATE' OR crdWrpAdd.adder.c1Id = ?3) "
			+ "AND (crdWrpAdd.status != 'DELETED' OR crdWrpAdd.status IS NULL) "
			+ "ORDER BY crdWrpAdd.cardWrapper.creationDate ASC")
	ModelCardWrapperAddition findBySectionAndCardWrapperVisibleToUser(UUID sectionId, UUID cardWrapperId, UUID adderId);
	
	@Query("SELECT crdWrpAdd FROM ModelCardWrapperAddition crdWrpAdd "
			+ "JOIN crdWrpAdd.cardWrapper crdWrp "
			+ "WHERE crdWrp.id= ?2 "
			+ "AND crdWrpAdd.section.id = ?1 "
			+ "AND crdWrpAdd.adder.c1Id = ?3 "
			+ "AND crdWrpAdd.status = 'DELETED' "
			+ "ORDER BY crdWrpAdd.cardWrapper.creationDate ASC")
	List<ModelCardWrapperAddition> findDeletedBySectionAndCardWrapperAndAdder(UUID sectionId, UUID cardWrapperId, UUID adderId);
	
	@Query("SELECT crdWrpAdd FROM ModelCardWrapperAddition crdWrpAdd "
			+ "WHERE crdWrpAdd.cardWrapper.id= ?2 "
			+ "AND crdWrpAdd.section.id = ?1 "
			+ "AND crdWrpAdd.scope = ?3 "
			+ "AND (crdWrpAdd.status != 'DELETED' OR crdWrpAdd.status IS NULL)")
	ModelCardWrapperAddition findBySectionAndCardWrapperIdAndScope(UUID sectionId, UUID cardWrapperId, ModelScope scope);
	
	@Query("SELECT crdWrpAdd FROM ModelCardWrapperAddition crdWrpAdd "
			+ "WHERE "
			+ "crdWrpAdd.section.id = ?1 "
			+ "AND crdWrpAdd.scope = ?2 "
			+ "AND (crdWrpAdd.status != 'DELETED' OR crdWrpAdd.status IS NULL) "
			+ "AND crdWrpAdd.beforeElement IS NULL "
			+ "ORDER BY crdWrpAdd.cardWrapper.creationDate ASC")
	List<ModelCardWrapperAddition> findLastBySectionAndScope(UUID sectionId, ModelScope scope);	
	
	@Query("SELECT crdWrpAdd FROM ModelCardWrapperAddition crdWrpAdd "
			+ "WHERE "
			+ "crdWrpAdd.section.id = ?1 "
			+ "AND crdWrpAdd.adder.c1Id = ?2 "
			+ "AND crdWrpAdd.scope = ?3 "
			+ "AND (crdWrpAdd.status != 'DELETED' OR crdWrpAdd.status IS NULL) "
			+ "AND crdWrpAdd.beforeElement IS NULL "
			+ "ORDER BY crdWrpAdd.cardWrapper.creationDate ASC")
	List<ModelCardWrapperAddition> findLastBySectionAndAdderAndScope(UUID sectionId, UUID adderId, ModelScope scope);	
	
	@Query("SELECT DISTINCT crdWrp FROM ModelCardWrapperAddition crdWrpAdd "
			+ "JOIN crdWrpAdd.cardWrapper crdWrp "
			+ "JOIN crdWrp.card crd "
			+ "WHERE crdWrpAdd.section.id IN ?1 "
			+ "AND (LOWER(crd.title) LIKE ?2 OR LOWER(crd.text) LIKE ?2 OR LOWER(crdWrp.creator.profile.nickname) LIKE ?2) "
			+ "AND (crdWrpAdd.status != 'DELETED' OR crdWrpAdd.status IS NULL) "
			+ "AND (crdWrpAdd.scope != 'PRIVATE' OR crdWrpAdd.adder.c1Id = ?3)")
	Page<ModelCardWrapper> searchInSectionsByQuery(List<UUID> sectionIds, String query, UUID requestById, Pageable page);
	
	@Query("SELECT crdWrp FROM ModelCardWrapperAddition crdWrpAdd "
			+ "JOIN crdWrpAdd.section sec "
			+ "JOIN crdWrpAdd.cardWrapper crdWrp "
			+ "JOIN crdWrp.card crd "
			+ "WHERE sec.initiative.id IN ?1 "
			+ "AND (LOWER(crd.title) LIKE ?2 OR LOWER(crd.text) LIKE ?2 OR LOWER(crdWrp.creator.profile.nickname) LIKE ?2) "
			+ "AND (crdWrpAdd.status != 'DELETED' OR crdWrpAdd.status IS NULL) "
			+ "AND (crdWrpAdd.scope != 'PRIVATE' OR crdWrpAdd.adder.c1Id = ?3)")
	Page<ModelCardWrapper> searchInInitiativesByQuery(List<UUID> initiativeIds, String query, UUID requestById, Pageable page);
	
	@Query("SELECT DISTINCT crdWrp.id FROM ModelCardWrapperAddition crdWrpAdd "
			+ "JOIN crdWrpAdd.cardWrapper crdWrp "
			+ "JOIN crdWrpAdd.section sec "
			+ "WHERE sec.id IN ?1 "
			+ "AND crdWrpAdd.scope != 'PRIVATE' "
			+ "AND (crdWrpAdd.status != 'DELETED' OR crdWrpAdd.status IS NULL)")
	public List<UUID> findAllCardWrapperIdsOfSections(List<UUID> sectionId);
	
	@Query("SELECT COUNT(crdWrpAdd) FROM ModelCardWrapperAddition crdWrpAdd "
			+ "JOIN crdWrpAdd.cardWrapper crdWrp "
			+ "WHERE crdWrp.id = ?1 "
			+ "AND (crdWrpAdd.scope != 'PRIVATE' OR crdWrpAdd.adder.c1Id = ?2)")
	public Integer countCardWrapperAdditionsAccessibleTo(UUID cardWrapperId, UUID userId);
	
	default Boolean cardWrapperUserHaveAccess(UUID cardWrapperId, UUID userId) {
		Integer res = countCardWrapperAdditionsAccessibleTo(cardWrapperId, userId);
		return res == null ? false : res > 0;
	}
	
}
