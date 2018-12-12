package org.collectiveone.modules.users;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface AppUserProfileRepositoryIf extends CrudRepository<AppUserProfile, UUID> {
	AppUserProfile findByUserId(UUID c1Id);
	
	AppUserProfile findByUsername(String username);
}
