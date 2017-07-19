package org.collectiveone.modules.users;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface AppUserRepositoryIf extends CrudRepository<AppUser, UUID> {
	
	AppUser findByC1Id(UUID id);
	
	AppUser findByAuth0Id(String auth0Id);
	
	List<AppUser> findTop10ByNicknameLikeIgnoreCase(String q);
}
