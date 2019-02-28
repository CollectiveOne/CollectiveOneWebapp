package org.collectiveone.modules.users;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AppUserRepositoryIf extends CrudRepository<AppUser, UUID> {
	
	AppUser findByC1Id(UUID id);
	
	@Query("SELECT us FROM AppUser us JOIN us.auth0Ids auth WHERE auth = ?1")
	AppUser findByAuth0Id(String auth0Id);
	
	AppUser findByEmail(String email);	
	
	List<AppUser> findTop10ByProfile_NicknameLikeIgnoreCase(String q);
	
	List<AppUser> findByOnlineStatus(UserOnlineStatus status);
	
	@Query("SELECT user.onlineStatus FROM AppUser user WHERE user.c1Id = :userId")
	UserOnlineStatus getOnlineStatus(@Param("userId") UUID userId);
	
	default Boolean isOnline(UUID userId, String endpoint) {
		return getOnlineStatus(userId) == UserOnlineStatus.ONLINE;
	} 
	
	@Modifying
    @Query("UPDATE AppUser user SET user.onlineStatus = ?1 WHERE user.lastSeen < ?2")
    void setStatusForUsersLastSeenBefore(UserOnlineStatus status, Timestamp editedBefore);
}
