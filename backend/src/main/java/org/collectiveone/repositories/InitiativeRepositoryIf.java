package org.collectiveone.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.model.Initiative;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface InitiativeRepositoryIf extends CrudRepository<Initiative, UUID> {
	
	@Query("SELECT init FROM Initiative init JOIN init.contributors ctr WHERE ctr.id = ?1")
	List<Initiative> findOfContributor(UUID contributorId);
}
