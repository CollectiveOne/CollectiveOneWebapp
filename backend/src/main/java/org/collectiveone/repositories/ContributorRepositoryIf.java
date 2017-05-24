package org.collectiveone.repositories;

import java.util.UUID;

import org.collectiveone.model.extensions.Contributor;
import org.springframework.data.repository.CrudRepository;

public interface ContributorRepositoryIf extends CrudRepository<Contributor, UUID> {
	
	Contributor findById(UUID id);
}
