package org.collectiveone.modules.model.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.collectiveone.modules.model.ModelSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ModelSectionRepositoryIf extends CrudRepository<ModelSection, UUID> {

	public Optional<ModelSection> findById(UUID sectionId);
	
	@Query("SELECT section.id FROM ModelSection section WHERE section.id = ?1")
	public UUID getId(UUID sectionId);
	
	default Boolean sectionExists(UUID sectionId) {
		UUID res = getId(sectionId);
		return res != null;
	}
	
	@Query("SELECT DISTINCT sec FROM ModelSubsection subsection " +
			"JOIN subsection.section sec " +
			"WHERE (LOWER(sec.title) LIKE :query OR LOWER(sec.description) LIKE :query) " +
			"AND sec.initiative.id IN :initiativeIds " +
			"AND (subsection.scope != 'PRIVATE' OR subsection.adder.c1Id = :requestBy) " +
			"ORDER BY sec.creationDate DESC")			
	public Page<ModelSection> searchBy(
			@Param("query") String query, 
			@Param("initiativeIds") List<UUID> initiativeId, 
			@Param("requestBy") UUID requestBy,
			Pageable page);

}
