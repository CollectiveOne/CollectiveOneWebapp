package org.collectiveone.modules.initiatives.repositories;

import java.util.UUID;

import org.collectiveone.modules.initiatives.Member;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepositoryIf extends CrudRepository<Member, UUID> {

	Member findByInitiative_IdAndUser_C1Id(UUID initiativeId, UUID userId);
	
}
