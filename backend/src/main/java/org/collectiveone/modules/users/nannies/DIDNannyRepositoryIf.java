package org.collectiveone.modules.users.nannies;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface DIDNannyRepositoryIf extends CrudRepository<DIDNanny, UUID> {
	
}
