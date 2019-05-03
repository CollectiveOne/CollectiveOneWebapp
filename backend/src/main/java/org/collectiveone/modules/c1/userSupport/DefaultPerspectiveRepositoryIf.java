package org.collectiveone.modules.c1.userSupport;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DefaultPerspectiveRepositoryIf extends JpaRepository<DefaultPerspective, UUID> {
	
	@Query("SELECT dft FROM DefaultPerspective dft "
			+ "WHERE dft.context.id = :contextId "
			+ "AND dft.user.did = :userDid")
	public String getDefaultOfUser(
			@Param("userDid") String userDid,
			@Param("contextId") String contextId);
	
}
