package org.collectiveone.modules.model.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.model.Member;
import org.collectiveone.modules.users.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepositoryIf extends CrudRepository<Member, UUID> {

	Member findByModelSection_IdAndUser_C1Id(UUID modelSectionId, UUID userId);
	
	@Query("SELECT mem.id FROM ModelSection ms JOIN ms.members mem WHERE ms.id = ?1 AND mem.user.c1Id = ?2")
	UUID findMemberId(UUID modelSectionId, UUID userId);
	
	@Query("SELECT DISTINCT mem.user FROM Member mem WHERE mem.model_section.id IN ?1 AND lower (mem.user.profile.nickname) LIKE %?2%")
	Page<AppUser> findMembersOfInitiatives(List<UUID> modelSectionId, String query, Pageable pageable);
	
}
