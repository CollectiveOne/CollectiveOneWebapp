package org.collectiveone.modules.model.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.model.ModelView;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ModelViewRepositoryIf extends CrudRepository<ModelView, UUID> {
	
	public ModelView findById(UUID viewId);
	
	@Query("SELECT sec.id FROM ModelView vw JOIN vw.sections sec WHERE vw.id = ?1")
	public List<UUID> getSectionIds(UUID viewId);
	
}
