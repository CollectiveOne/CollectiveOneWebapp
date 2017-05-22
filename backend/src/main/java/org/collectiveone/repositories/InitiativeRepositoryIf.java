package org.collectiveone.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.model.basic.Initiative;
import org.collectiveone.model.enums.InitiativeRelationshipType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface InitiativeRepositoryIf extends CrudRepository<Initiative, UUID> {

	Initiative findById(UUID id);
	
	@Query("SELECT init FROM Initiative init JOIN init.contributors ctr WHERE ctr.c1Id = ?1")
	List<Initiative> findOfContributor(UUID contributorId);
	
	@Query("SELECT init FROM Initiative init "
			+ "JOIN init.contributors ctr "
			+ "WHERE ctr.c1Id = ?1 "
			+ "AND init.id NOT IN "
			+ "(SELECT childInit.id FROM Initiative childInit "
			+ "JOIN childInit.relationships childRels "
			+ "WHERE childRels.type = 'IS_DETACHED_SUB')" )
	List<Initiative> findSuperInitiativesOfContributor(UUID contributorId);
		
	
	@Query("SELECT rels.initiative from InitiativeRelationship rels WHERE rels.ofInitiative.id = ?1 AND rels.type = ?2")
	List<Initiative> findInitiativesWithRelationship(UUID initiativeId, InitiativeRelationshipType type);
	
	@Query("SELECT init FROM Initiative init WHERE init.name LIKE %?1%")
	List<Initiative> searchBy(String q);
	
}
