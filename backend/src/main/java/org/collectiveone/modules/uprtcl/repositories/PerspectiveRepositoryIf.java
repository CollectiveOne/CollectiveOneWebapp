package org.collectiveone.modules.uprtcl.repositories;

import java.util.List;

import org.collectiveone.modules.uprtcl.entities.Perspective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PerspectiveRepositoryIf extends JpaRepository<Perspective, byte[]> {
	
	@Query("SELECT persp.id FROM Perspective persp "
			+ "WHERE persp.contextId = :contextId")
	public byte[] findIdOfOldestOfContext(
			@Param("contextId") byte[] contextId);
	
	public List<Perspective> findByContextId(String contextId);
}
