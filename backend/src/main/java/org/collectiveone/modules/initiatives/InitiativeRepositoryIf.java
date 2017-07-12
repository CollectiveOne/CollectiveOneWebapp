package org.collectiveone.modules.initiatives;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface InitiativeRepositoryIf extends CrudRepository<Initiative, UUID> {

	Initiative findById(UUID id);
	
	@Query("SELECT init FROM Initiative init JOIN init.members mem WHERE mem.user.c1Id = ?1")
	List<Initiative> findOfMember(UUID memberId);
	
	@Query("SELECT rels.ofInitiative from InitiativeRelationship rels WHERE rels.initiative.id = ?1 AND rels.type = ?2")
	Initiative findOfInitiativesWithRelationship(UUID initiativeId, InitiativeRelationshipType type);
		
	
	@Query("SELECT rels.initiative from InitiativeRelationship rels WHERE rels.ofInitiative.id = ?1 AND rels.type = ?2")
	List<Initiative> findInitiativesWithRelationship(UUID initiativeId, InitiativeRelationshipType type);
	
	@Query("SELECT init FROM Initiative init WHERE lower (init.name) LIKE %?1%")
	List<Initiative> searchBy(String q);
	
	Initiative findByTokenType_Id(UUID tokenTypeId);
	
}
