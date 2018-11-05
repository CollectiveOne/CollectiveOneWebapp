package org.collectiveone.modules.governance;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface GovernanceRepositoryIf extends CrudRepository<Governance, UUID> {
	
	Optional<Governance> findById(UUID governanceId);
	
	Governance findByInitiative_Id(UUID initiativeId);
	
}
