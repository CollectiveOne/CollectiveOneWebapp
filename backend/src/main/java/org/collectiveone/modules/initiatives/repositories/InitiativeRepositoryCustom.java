package org.collectiveone.modules.initiatives.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.initiatives.InitiativeMeta;
import org.collectiveone.modules.initiatives.InitiativeTag;
import org.collectiveone.modules.initiatives.dto.SearchFiltersDto;

public class InitiativeRepositoryCustom implements InitiativeRepositoryCustomIf {

	@PersistenceContext
    private EntityManager entityManager;
	
	public List<SearchFiltersDto> searchBy(SearchFiltersDto filters) {
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Initiative> query = builder.createQuery(Initiative.class); 
		Root<Initiative> root = query.from( Initiative.class );
		
		Join<Initiative, InitiativeMeta> initiativeMetaJoin = root.join("meta");
		Join<InitiativeMeta, InitiativeTag> initiativeTagsJoin = initiativeMetaJoin.join("tags");
		
		
		
		
	}
	
}
