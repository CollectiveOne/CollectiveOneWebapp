package org.collectiveone.modules.initiatives.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.initiatives.InitiativeRelationshipType;
import org.collectiveone.modules.initiatives.model.Initiative;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface InitiativeRepositoryIf extends CrudRepository<Initiative, UUID> {

	Initiative findById(UUID id);
	
	@Query("SELECT init FROM Initiative init JOIN init.members mem WHERE mem.user.c1Id = ?1")
	List<Initiative> findOfMember(UUID memberId);
	
	@Query("SELECT init FROM Initiative init "
			+ "JOIN init.members mem "
			+ "WHERE mem.user.c1Id = ?1 "
			+ "AND init.id NOT IN "
			+ "(SELECT childInit.id FROM Initiative childInit "
			+ "JOIN childInit.relationships childRels "
			+ "WHERE childRels.type = 'IS_DETACHED_SUB')" )
	List<Initiative> findSuperInitiativesOfMember(UUID memberId);
		
	
	@Query("SELECT rels.initiative from InitiativeRelationship rels WHERE rels.ofInitiative.id = ?1 AND rels.type = ?2")
	List<Initiative> findInitiativesWithRelationship(UUID initiativeId, InitiativeRelationshipType type);
	
	@Query("SELECT init FROM Initiative init WHERE init.name LIKE %?1%")
	List<Initiative> searchBy(String q);
	
}
