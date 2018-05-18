package org.collectiveone.modules.model.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.model.ModelCardWrapperAddition;
import org.collectiveone.modules.model.ModelCardWrapperScope;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ModelCardWrapperAdditionRepositoryIf extends CrudRepository<ModelCardWrapperAddition, UUID> {
	
	@Query("SELECT crdWrpAdd FROM ModelCardWrapperAddition crdWrpAdd "
			+ "WHERE crdWrpAdd.section.id = ?1 "
			+ "AND crdWrpAdd.scope = ?2 "
			+ "AND (crdWrpAdd.cardWrapper.status != 'DELETED' OR crdWrpAdd.cardWrapper.status IS NULL)")
	List<ModelCardWrapperAddition> findOfInSection(UUID sectionId, ModelCardWrapperScope scope);
	
	@Query("SELECT crdWrpAdd FROM ModelCardWrapperAddition crdWrpAdd "
			+ "WHERE crdWrpAdd.cardWrapper.creator.c1Id = ?1 "
			+ "AND crdWrpAdd.section.id = ?2 "
			+ "AND crdWrpAdd.scope = ?3 "
			+ "AND (crdWrpAdd.cardWrapper.status != 'DELETED' OR crdWrpAdd.cardWrapper.status IS NULL)")
	List<ModelCardWrapperAddition> findOfUserInSection(UUID userId, UUID sectionId, ModelCardWrapperScope scope);
	
	@Query("SELECT crdWrpAdd FROM ModelCardWrapperAddition crdWrpAdd "
			+ "WHERE crdWrpAdd.cardWrapper.id= ?2 "
			+ "AND crdWrpAdd.section.id = ?1 "
			+ "AND crdWrpAdd.scope = ?3 "
			+ "AND (crdWrpAdd.cardWrapper.status != 'DELETED' OR crdWrpAdd.cardWrapper.status IS NULL)")
	ModelCardWrapperAddition findBySectionAndCardWrapperId(UUID sectionId, UUID cardWrapperId, ModelCardWrapperScope scope);
	
	@Query("SELECT crdWrpAdd FROM ModelCardWrapperAddition crdWrpAdd "
			+ "WHERE crdWrpAdd.cardWrapper.id= ?2 "
			+ "AND crdWrpAdd.section.id = ?1 "
			+ "AND crdWrpAdd.scope != 'COMMON' "
			+ "AND (crdWrpAdd.cardWrapper.status != 'DELETED' OR crdWrpAdd.cardWrapper.status IS NULL)")
	ModelCardWrapperAddition findBySectionAndCardWrapperIdNotCommon(UUID sectionId, UUID cardWrapperId);
	
}
