package org.collectiveone.modules.initiatives;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface InitiativeMetaRepositoryIf extends CrudRepository<InitiativeMeta, UUID> {

}
