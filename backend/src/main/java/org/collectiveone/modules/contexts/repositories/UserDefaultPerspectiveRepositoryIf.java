package org.collectiveone.modules.contexts.repositories;

import java.util.UUID;

import org.collectiveone.modules.contexts.entities.UserDefaultPerspective;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserDefaultPerspectiveRepositoryIf extends CrudRepository<UserDefaultPerspective, UUID> {
	
	@Query("SELECT defaultPerspective.perspective.id "
			+ "FROM UserDefaultPerspective defaultPerspective "
			+ "WHERE defaultPerspective.context.id = :contextId "
			+ "AND defaultPerspective.user.id = :userId")
	UUID findDefaultPerspetiveIdByContextIdAndUserId(
			@Param("contextId") UUID contextId,
			@Param("userId") UUID userId);
	
}
