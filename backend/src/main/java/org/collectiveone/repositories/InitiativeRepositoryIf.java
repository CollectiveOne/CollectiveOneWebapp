package org.collectiveone.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.model.Initiative;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface InitiativeRepositoryIf extends CrudRepository<Initiative, UUID> {

	Initiative findById(UUID id);
	
	@Query("SELECT init FROM Initiative init JOIN init.contributors ctr WHERE ctr.c1Id = ?1")
	List<Initiative> findOfContributor(UUID contributorId);
	
	@Query("select init from Initiative init where init.name like %?1%")
	List<Initiative> searchBy(String q);
	
}
