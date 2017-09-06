package org.collectiveone.modules.initiatives.repositories;

import java.util.UUID;

import org.collectiveone.modules.initiatives.InitiativeMeta;
import org.springframework.data.repository.CrudRepository;

public interface InitiativeMetaRepositoryIf extends CrudRepository<InitiativeMeta, UUID> {
	
}
