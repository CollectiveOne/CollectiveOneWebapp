package org.collectiveone.modules.initiatives.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.initiatives.Member;
import org.collectiveone.modules.users.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepositoryIf extends CrudRepository<Member, UUID> {

	Member findByInitiative_IdAndUser_C1Id(UUID initiativeId, UUID userId);
	
	@Query("SELECT mem.id FROM Initiative init JOIN init.members mem WHERE init.id = ?1 AND mem.user.c1Id = ?2")
	UUID findMemberId(UUID initiativeId, UUID userId);
	
	@Query("SELECT DISTINCT mem.user FROM Member mem WHERE mem.initiative.id IN ?1 AND lower (mem.user.profile.nickname) LIKE %?2%")
	Page<AppUser> findMembersOfInitiatives(List<UUID> initiativesIds, String query, Pageable pageable);
	
}
