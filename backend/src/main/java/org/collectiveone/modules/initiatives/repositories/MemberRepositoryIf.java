package org.collectiveone.modules.initiatives.repositories;

import java.util.UUID;

import org.collectiveone.modules.initiatives.Member;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepositoryIf extends CrudRepository<Member, UUID> {

	Member findByInitiative_IdAndUser_C1Id(UUID initiativeId, UUID userId);
	
	@Query("SELECT mem.id FROM Initiative init JOIN init.members mem WHERE init.id = ?1 AND mem.user.c1Id = ?2")
	UUID findMemberId(UUID initiativeId, UUID userId);
	
}
