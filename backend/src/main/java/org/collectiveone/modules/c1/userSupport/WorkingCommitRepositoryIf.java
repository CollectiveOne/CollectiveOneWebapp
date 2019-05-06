package org.collectiveone.modules.c1.userSupport;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WorkingCommitRepositoryIf extends JpaRepository<WorkingCommit, UUID> {
	
	@Query("SELECT workComm FROM WorkingCommit workComm "
			+ "WHERE workComm.perspective.id = :perspectiveId "
			+ "AND workComm.user.did = :userDid")
	public WorkingCommit findByUserIdAndPerspectiveId(
			@Param("userDid") String userDid, 
			@Param("perspectiveId") String perspectiveId);
	
}
