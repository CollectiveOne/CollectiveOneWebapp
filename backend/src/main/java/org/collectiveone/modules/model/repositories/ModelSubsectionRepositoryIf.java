package org.collectiveone.modules.model.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.model.ModelScope;
import org.collectiveone.modules.model.ModelSubsection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ModelSubsectionRepositoryIf extends CrudRepository<ModelSubsection, UUID> {
	
	ModelSubsection findById(UUID subsectionId);

	@Query("SELECT subsection FROM ModelSubsection subsection "
			+ "WHERE subsection.section.id = ?1 "
			+ "AND (subsection.status != 'DELETED' OR subsection.status IS NULL)")
	List<ModelSubsection> findOfSection(UUID sectionId);
		
	@Query("SELECT parents.id FROM ModelSubsection subsection "
			+ "JOIN subsection.parentSection parents "
			+ "WHERE subsection.section.id = ?1 "
			+ "AND (subsection.status != 'DELETED' OR subsection.status IS NULL)")
	List<UUID> findParentSectionsIds(UUID sectionId);
	
	@Query("SELECT parents.id FROM ModelSubsection subsection "
			+ "JOIN subsection.parentSection parents "
			+ "WHERE subsection.section.id = ?1 "
			+ "AND ((subsection.scope = 'PRIVATE' AND subsection.adder.c1Id = ?2) "
			+ "OR (subsection.scope = 'SHARED' AND true = ?3) "
			+ "OR (subsection.scope = 'COMMON')) "
			+ "AND (subsection.status != 'DELETED' OR subsection.status IS NULL)")
	List<UUID> findParentSectionsIdsVisibleToUser(UUID sectionId, UUID requestByUserId, Boolean isMemberOfEcosystem);
	
	@Query("SELECT sec.id FROM ModelSubsection subsection "
			+ "JOIN subsection.section sec "
			+ "WHERE subsection.parentSection.id = ?1 "
			+ "AND (subsection.status != 'DELETED' OR subsection.status IS NULL)")
	List<UUID> findSubsectionsIds(UUID sectionId);
		
	@Query("SELECT sec.id FROM ModelSubsection subsection "
			+ "JOIN subsection.section sec "
			+ "WHERE subsection.parentSection.id = ?1 "
			+ "AND ((subsection.scope = 'PRIVATE' AND subsection.adder.c1Id = ?2) "
			+ "OR (subsection.scope = 'SHARED' AND true = ?3) "
			+ "OR (subsection.scope = 'COMMON')) "
			+ "AND (subsection.status != 'DELETED' OR subsection.status IS NULL)")
	List<UUID> findSubsectionsIdsVisibleToUser(UUID sectionId, UUID requestByUserId, Boolean isMemberOfEcosystem);
	
	@Query("SELECT subsection FROM ModelSubsection subsection "
			+ "JOIN subsection.section sec "
			+ "WHERE subsection.parentSection.id = ?1 "
			+ "AND ((subsection.scope = 'PRIVATE' AND subsection.adder.c1Id = ?2) "
			+ "OR (subsection.scope = 'SHARED' AND true = ?3) "
			+ "OR (subsection.scope = 'COMMON')) "
			+ "AND (subsection.status != 'DELETED' OR subsection.status IS NULL)")
	List<ModelSubsection> findSubsectionsVisibleToUser(UUID sectionId, UUID requestByUserId, Boolean isMemberOfEcosystem);
		
	@Query("SELECT subsection FROM ModelSubsection subsection "
			+ "WHERE subsection.parentSection.id = ?1 "
			+ "AND subsection.scope = ?2 "
			+ "AND (subsection.status != 'DELETED' OR subsection.status IS NULL)")
	List<ModelSubsection> findInParentSectionWithScope(UUID parentSectionId, ModelScope scope);
	
	@Query("SELECT subsection FROM ModelSubsection subsection "
			+ "WHERE subsection.adder.c1Id = ?1 "
			+ "AND subsection.parentSection.id = ?2 "
			+ "AND subsection.scope = ?3 "
			+ "AND (subsection.status != 'DELETED' OR subsection.status IS NULL)")
	List<ModelSubsection> findOfUserInParentSection(UUID userId, UUID parentSectionId, ModelScope scope);
	
	@Query("SELECT subsection FROM ModelSubsection subsection "
			+ "JOIN subsection.section sec "
			+ "WHERE sec.id= ?2 "
			+ "AND subsection.parentSection.id = ?1 "
			+ "AND (subsection.scope != 'PRIVATE' OR subsection.adder.c1Id = ?3) "
			+ "AND (subsection.status != 'DELETED' OR subsection.status IS NULL)")
	ModelSubsection findByParentSectionAndSectionVisibleToUser(UUID parentSectionId, UUID sectionId, UUID adderId);
	
	@Query("SELECT subsection FROM ModelSubsection subsection "
			+ "WHERE "
			+ "subsection.parentSection.id = ?1 "
			+ "AND subsection.scope = ?2 "
			+ "AND (subsection.status != 'DELETED' OR subsection.status IS NULL) "
			+ "AND subsection.beforeElement IS NULL")
	List<ModelSubsection> findLastByParentSectionAndScope(UUID parentSectionId, ModelScope scope);	
	
	@Query("SELECT subsection FROM ModelSubsection subsection "
			+ "WHERE "
			+ "subsection.parentSection.id = ?1 "
			+ "AND subsection.adder.c1Id = ?2 "
			+ "AND subsection.scope = ?3 "
			+ "AND (subsection.status != 'DELETED' OR subsection.status IS NULL) "
			+ "AND subsection.beforeElement IS NULL")
	List<ModelSubsection> findLastByParentSectionAndAdderAndScope(UUID sectionId, UUID adderId, ModelScope scope);	
	

	@Query("SELECT subsection FROM ModelSubsection subsection "
			+ "WHERE subsection.section.id= ?2 "
			+ "AND subsection.parentSection.id = ?1 "
			+ "AND subsection.adder.c1Id = ?3 "
			+ "AND subsection.status = 'DELETED'")
	List<ModelSubsection> findDeletedByParentSectionAndSectionAndAdder(UUID parentSectionId, UUID sectionId, UUID adderId);
	
	@Query("SELECT COUNT(subsection) FROM ModelSubsection subsection "
			+ "JOIN subsection.section sec "
			+ "WHERE sec.id = ?1 "
			+ "AND (subsection.scope != 'PRIVATE' OR subsection.adder.c1Id = ?2)")
	public Integer countSubsectionsAccessibleTo(UUID sectionId, UUID userId);
	
	default Boolean subsectionUserHaveAccess(UUID sectionId, UUID userId) {
		Integer res = countSubsectionsAccessibleTo(sectionId, userId);
		return res == null ? false : res > 0;
	}
		
}
