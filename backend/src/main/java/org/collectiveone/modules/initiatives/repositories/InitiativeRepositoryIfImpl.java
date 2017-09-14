package org.collectiveone.modules.initiatives.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.initiatives.dto.SearchFiltersDto;

public class InitiativeRepositoryIfImpl implements InitiativeRepositoryCustomIf {

	@PersistenceContext
    private EntityManager entityManager;
	
	public List<Initiative> customSearch(SearchFiltersDto filters) {
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Initiative> criteria = builder.createQuery(Initiative.class); 
		Root<Initiative> initiativeRoot = criteria.from(Initiative.class);
		
		criteria.select(initiativeRoot);
		
		TypedQuery<Initiative> query = entityManager.createQuery(criteria);
		List<Initiative> initiatives = query.getResultList();
		
		for (Initiative init : initiatives) {
			System.out.println(init.getId().toString());
		}
		
		return initiatives;
		
	}
	
}
