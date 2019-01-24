package org.collectiveone.modules.contexts.repositories;

import java.util.UUID;

import org.collectiveone.modules.contexts.entities.UserActivePerspective;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserActivePerspectiveRepositoryIf extends CrudRepository<UserActivePerspective, UUID> {
	
	@Query("SELECT activePerspective.perspective.id "
			+ "FROM UserActivePerspective activePerspective "
			+ "WHERE activePerspective.context.id = :contextId "
			+ "AND activePerspective.user.id = :userId")
	UUID findDefaultPerspetiveIdByContextIdAndUserId(
			@Param("contextId") UUID contextId,
			@Param("userId") UUID userId);
	
}
