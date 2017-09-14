package org.collectiveone.modules.initiatives.repositories;

import java.util.List;

import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.initiatives.dto.SearchFiltersDto;

public interface InitiativeRepositoryCustomIf {
	
	public List<Initiative> customSearch(SearchFiltersDto filters);
	
}
