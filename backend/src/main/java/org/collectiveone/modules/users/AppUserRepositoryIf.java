package org.collectiveone.modules.users;

import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AppUserRepositoryIf extends CrudRepository<AppUser, UUID> {
	
	@Query("SELECT user FROM AppUser user "
			+ "WHERE user.nanny.type = 'AUTH0' "
			+ "AND user.nanny.extId = :auth0Id")
	public AppUser findByAuth0Id(@Param("auth0Id") String auth0Id);
	
}
