package org.collectiveone.modules.users;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AppUserRepositoryIf extends CrudRepository<AppUser, UUID> {
	
	AppUser findByC1Id(UUID id);
	
	@Query("SELECT us FROM AppUser us JOIN us.auth0Ids auth WHERE auth = ?1")
	AppUser findByAuth0Id(String auth0Id);
	
	AppUser findByEmail(String email);	
	
	List<AppUser> findTop10ByProfile_NicknameLikeIgnoreCase(String q);
	
	List<AppUser> findByOnlineStatus(UserOnlineStatus status);
	
	@Modifying
    @Query("UPDATE AppUser user SET user.onlineStatus = ?1 WHERE user.lastEdited < ?2")
    void setStatusForUsersLastSeenBefore(UserOnlineStatus status, Long editedBefore);
}
