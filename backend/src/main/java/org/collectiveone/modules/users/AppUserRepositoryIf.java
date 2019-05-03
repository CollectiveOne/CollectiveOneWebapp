package org.collectiveone.modules.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppUserRepositoryIf extends JpaRepository<AppUser, String> {
	
	@Query("SELECT user FROM AppUser user "
			+ "WHERE user.nanny.type = 'AUTH0' "
			+ "AND user.nanny.extId = :auth0Id")
	public AppUser findByAuth0Id(@Param("auth0Id") String auth0Id);
	
	@Query("SELECT user.context.id FROM AppUser user "
			+ "WHERE user.did = :userDid")
	public String getContextId(
			@Param("userDid") String userDid);
	
}
