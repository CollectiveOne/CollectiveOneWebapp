package org.collectiveone.modules.initiatives.repositories;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.initiatives.InitiativeRelationshipType;
import org.collectiveone.modules.initiatives.InitiativeVisibility;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface InitiativeRepositoryIf extends CrudRepository<Initiative, UUID>, InitiativeRepositoryCustomIf {

	Initiative findById(UUID id);
	
	@Query("SELECT init FROM Initiative init JOIN init.members mem WHERE mem.user.c1Id = ?1 AND init.status='ENABLED'")
	List<Initiative> findOfMember(UUID memberId);
	
	@Query("SELECT rels.ofInitiative from InitiativeRelationship rels WHERE rels.initiative.id = ?1 AND rels.type = ?2")
	Initiative findOfInitiativesWithRelationship(UUID initiativeId, InitiativeRelationshipType type);
	
	@Query("SELECT rels.initiative from InitiativeRelationship rels WHERE rels.ofInitiative.id = ?1 AND rels.type = ?2")
	List<Initiative> findInitiativesWithRelationship(UUID initiativeId, InitiativeRelationshipType type);
	
	@Query("SELECT init FROM Initiative init WHERE lower (init.meta.name) LIKE %?1% AND init.status='ENABLED'")
	List<Initiative> searchByName(String q);
	
	@Query("SELECT init FROM Initiative init JOIN init.meta mta JOIN mta.tags tgs WHERE tgs.id IN ?1 AND mta.visibility = ?2 AND init.status='ENABLED' ORDER BY mta.creationDate DESC")
	List<Initiative> searchByTagIdAndVisibility(Collection<UUID> ids, InitiativeVisibility visibility);
	
	@Query("SELECT init FROM Initiative init JOIN init.meta mta WHERE mta.visibility = ?1 AND init.status='ENABLED' ORDER BY mta.creationDate DESC")
	List<Initiative> findByVisibility(InitiativeVisibility visibility);
	
	Initiative findByTokenTypes_Id(UUID tokenTypeId);
	
	@Query("SELECT mta.visibility FROM Initiative init JOIN init.meta mta WHERE init.id = ?1")
	InitiativeVisibility getVisiblity(UUID initiativeId);
	
	@Query("SELECT init.id FROM Initiative init WHERE init.topModelSubsection.id = ?1")
	UUID findByTopLevelSectionId(UUID sectionId);
	
	@Query("SELECT init.topModelSubsection.section.id FROM Initiative init WHERE init.id = ?1")
	UUID findTopModelSectionIdById(UUID initiativeId);
	
}
