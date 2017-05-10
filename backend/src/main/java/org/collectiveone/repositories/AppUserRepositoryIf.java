package org.collectiveone.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.model.AppUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AppUserRepositoryIf extends CrudRepository<AppUser, UUID> {
	AppUser findByC1Id(UUID id);
	AppUser findByAuth0Id(String auth0Id);
	@Query("select us from AppUser us where us.nickname like %?1%")
	List<AppUser> searchByNickname(String q);
}
