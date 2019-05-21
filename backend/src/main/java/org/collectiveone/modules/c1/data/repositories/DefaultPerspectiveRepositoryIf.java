package org.collectiveone.modules.c1.data.repositories;

import java.util.UUID;

import org.collectiveone.modules.c1.data.entities.DefaultPerspective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DefaultPerspectiveRepositoryIf extends JpaRepository<DefaultPerspective, UUID> {
	
	@Query("SELECT dft.perspective.id FROM DefaultPerspective dft "
			+ "WHERE dft.context.id = :contextId "
			+ "AND dft.user.did = :userDid")
	public byte[] getDefaultOfUser(
			@Param("userDid") String userDid,
			@Param("contextId") byte[] contextId);
	
}
