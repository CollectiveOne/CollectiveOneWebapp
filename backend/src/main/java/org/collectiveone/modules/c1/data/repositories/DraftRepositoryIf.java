package org.collectiveone.modules.c1.data.repositories;

import java.util.UUID;

import org.collectiveone.modules.c1.data.entities.Draft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DraftRepositoryIf extends JpaRepository<Draft, UUID> {
	
	@Query("SELECT dft FROM Draft dft "
			+ "WHERE dft.elementId = :elementId "
			+ "AND dft.user.did = :userDid")
	public Draft findByUserIdAndElementId(
			@Param("userDid") String userDid, 
			@Param("elementId") byte[] elementId);
	
}
