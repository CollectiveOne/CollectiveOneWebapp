package org.collectiveone.modules.c1.views;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ViewRepositoryIf extends JpaRepository<View, UUID> {
	
	public View findByElementIdAndUserDid(byte[] elementId, String userDid);
	
	public View findByElementIdAndInElementIdAndUserDid(byte[] elementId, byte[] inElementId, String userDid);
	
	@Query("SELECT vw FROM View vw "
			+ "WHERE vw.elementId = :elementId")
	public List<View> findOfElementId(
			@Param("elementId") byte[] elementId, 
			Pageable page);
	
	default View findOldestOfElementId(byte[] elementId) {
		List<View> views = findOfElementId(elementId, PageRequest.of(0, 1, Sort.Direction.DESC, "vw.timestamp"));
		return views != null ? views.get(0) : null;
	}
	
}
