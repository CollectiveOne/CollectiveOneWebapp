package org.collectiveone.modules.model.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.model.ModelCardWrapperAddition;
import org.collectiveone.modules.model.ModelCardWrapperScope;
import org.collectiveone.modules.model.ModelSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ModelCardWrapperAdditionRepositoryIf extends CrudRepository<ModelCardWrapperAddition, UUID> {
	
	ModelCardWrapperAddition findById(UUID cardWrapperAdditionId);
	
	@Query("SELECT sec FROM ModelCardWrapperAddition crdWrpAdd "
			+ "JOIN crdWrpAdd.section sec "
			+ "WHERE crdWrpAdd.cardWrapper.id = ?1")
	List<ModelSection> findParentSections(UUID cardWrapperId);
	
	@Query("SELECT crdWrpAdd FROM ModelCardWrapperAddition crdWrpAdd "
			+ "WHERE crdWrpAdd.cardWrapper.id = ?1")
	List<ModelCardWrapperAddition> findOfCardWrapper(UUID cardWrapperId);
	
	@Query("SELECT crdWrpAdd FROM ModelCardWrapperAddition crdWrpAdd "
			+ "WHERE crdWrpAdd.section.id = ?1 "
			+ "AND crdWrpAdd.scope = ?2 "
			+ "AND (crdWrpAdd.status != 'DELETED' OR crdWrpAdd.status IS NULL)")
	List<ModelCardWrapperAddition> findInSectionWithScope(UUID sectionId, ModelCardWrapperScope scope);
	
	@Query("SELECT crdWrpAdd FROM ModelCardWrapperAddition crdWrpAdd "
			+ "WHERE crdWrpAdd.cardWrapper.creator.c1Id = ?1 "
			+ "AND crdWrpAdd.section.id = ?2 "
			+ "AND crdWrpAdd.scope = ?3 "
			+ "AND (crdWrpAdd.status != 'DELETED' OR crdWrpAdd.status IS NULL)")
	List<ModelCardWrapperAddition> findOfUserInSection(UUID userId, UUID sectionId, ModelCardWrapperScope scope);
	
	@Query("SELECT crdWrpAdd FROM ModelCardWrapperAddition crdWrpAdd "
			+ "WHERE crdWrpAdd.cardWrapper.id= ?2 "
			+ "AND crdWrpAdd.section.id = ?1 "
			+ "AND (crdWrpAdd.status != 'DELETED' OR crdWrpAdd.status IS NULL)")
	ModelCardWrapperAddition findBySectionAndCardWrapperId(UUID sectionId, UUID cardWrapperId);
	
	@Query("SELECT crdWrpAdd FROM ModelCardWrapperAddition crdWrpAdd "
			+ "WHERE crdWrpAdd.cardWrapper.id= ?2 "
			+ "AND crdWrpAdd.section.id = ?1 "
			+ "AND crdWrpAdd.scope = ?3 "
			+ "AND (crdWrpAdd.status != 'DELETED' OR crdWrpAdd.status IS NULL)")
	ModelCardWrapperAddition findBySectionAndCardWrapperIdAndScope(UUID sectionId, UUID cardWrapperId, ModelCardWrapperScope scope);
	
	@Query("SELECT crdWrpAdd FROM ModelCardWrapperAddition crdWrpAdd "
			+ "WHERE crdWrpAdd.cardWrapper.id= ?2 "
			+ "AND crdWrpAdd.section.id = ?1 "
			+ "AND crdWrpAdd.scope != 'COMMON' "
			+ "AND (crdWrpAdd.status != 'DELETED' OR crdWrpAdd.status IS NULL)")
	ModelCardWrapperAddition findBySectionAndCardWrapperIdNotCommon(UUID sectionId, UUID cardWrapperId);
	
	@Query("SELECT crdWrpAdd FROM ModelCardWrapperAddition crdWrpAdd "
			+ "JOIN crdWrpAdd.cardWrapper crdWrp "
			+ "WHERE crdWrpAdd.section.id IN ?1")
	Page<ModelCardWrapperAddition> searchInSectionsByQuery(List<UUID> sectionIds, Pageable page);
	
	@Query("SELECT crdWrpAdd FROM ModelCardWrapperAddition crdWrpAdd "
			+ "JOIN crdWrpAdd.section sec "
			+ "JOIN crdWrpAdd.cardWrapper crdWrp "
			+ "WHERE sec.initiative.id IN ?1")
	Page<ModelCardWrapperAddition> searchInInitiativesByQuery(List<UUID> initiativeIds, Pageable page);
	
	@Query("SELECT DISTINCT crdWrp.id FROM ModelCardWrapperAddition crdWrpAdd "
			+ "JOIN crdWrpAdd.cardWrapper crdWrp "
			+ "JOIN crdWrpAdd.section sec "
			+ "WHERE sec.id IN ?1 "
			+ "AND crdWrpAdd.scope != 'PRIVATE'")
	public List<UUID> findAllCardWrapperIdsOfSections(List<UUID> sectionId);
	
}
