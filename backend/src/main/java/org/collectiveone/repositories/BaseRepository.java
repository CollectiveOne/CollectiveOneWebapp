package org.collectiveone.repositories;

import java.util.List;

import org.collectiveone.web.dto.Filters;
import org.collectiveone.web.dto.ObjectListRes;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
class BaseRepository {

	@Autowired
	protected SessionFactory sessionFactory;
	
	public BaseRepository() {
	}

	public Long save(Object object) {
		Session session = sessionFactory.getCurrentSession();
		Long id = (Long) session.save(object);
		return id;
	}
	
	<T> Long getN(Class<T> clazz) {
		Session session = sessionFactory.getCurrentSession();

		Long count = (Long) session.createCriteria(clazz)
				.setProjection(Projections.rowCount()).uniqueResult();

		return count;
	}

	<T> Object get(Long id, Class<T> clazz) {
		Session session = sessionFactory.getCurrentSession();
		T object = session.get(clazz,id);
		return object;
	}

	<T> List<T> getAll(Integer max, Class<T> clazz) {
		Session session = sessionFactory.getCurrentSession();

		@SuppressWarnings("unchecked")
		List<T> res = (List<T>) session.createCriteria(clazz)
		.list();

		return res;
	}

	<T> Criteria applyGeneralFilters(Filters filters, List<String> enabledProjects, Class<T> clazz) {
		return applyGeneralFilters(filters, enabledProjects, clazz, true);
	}
	
	<T> Criteria applyGeneralFilters(Filters filters, List<String> enabledProjects, Class<T> clazz, boolean projectFilter) {

		Session session = sessionFactory.getCurrentSession();
		Criteria q = session.createCriteria(clazz);

		/* ------------------ */
		/* Project filter */
		/* ------------------ */
		if(projectFilter) {
			boolean allEnabledProjects = false;
			if(filters.getProjectNames() != null) {
				if(filters.getProjectNames().size() > 0) {
					/* apply filter per project if they are provided */
					allEnabledProjects = false;
				} else {
					/* otherwise filter using all enabled projects */
					allEnabledProjects = true;
				}
			} else {
				/* otherwise filter using all enabled projects */
				allEnabledProjects = true;
			}
			
			q.createAlias("project", "proj");
			List<String> projectNames; 
			
			if(allEnabledProjects) {
				projectNames = enabledProjects;
			} else {
				projectNames = filters.getProjectNames();
			}
			
			Disjunction projDisj = Restrictions.disjunction();
			for(String projectName:projectNames) {
				projDisj.add( Restrictions.eq("proj.name", projectName));
			}
			q.add(projDisj);
		}
		
		
		/* ------------------ */
		/* filter by creator  */
		/* ------------------ */
		
		if(filters.getCreatorUsernames() != null) {
			if(filters.getCreatorUsernames().size() > 0) {
				q.createAlias("creator", "crea");
				List<String> creatorUsernames = filters.getCreatorUsernames();
				Disjunction userDisj = Restrictions.disjunction();
				for(String creatorUsername:creatorUsernames) {
					if(creatorUsername.length() > 0) {
						userDisj.add( Restrictions.eq("crea.username", creatorUsername));
					}
				}
				q.add(userDisj);	
			}
		}

		/* ------------------ */
		/* filter by keyword  */
		/* ------------------ */
		
		if(filters.getKeyw() != null) {
			String keyw = filters.getKeyw();
			if(keyw.length() > 0) {
				Criterion keywInDescRestr = null;
				Criterion keyWordCrit = null; 

				String clazzName = clazz.getSimpleName();
				
				if(clazzName.equals("Cbtion")) {
					keywInDescRestr = Restrictions.ilike("description",keyw, MatchMode.ANYWHERE);
					keyWordCrit = Restrictions.or(keywInDescRestr,Restrictions.ilike("title",keyw, MatchMode.ANYWHERE));
				}
				
				if(clazzName.equals("Decision")) {
					keywInDescRestr = Restrictions.ilike("description",keyw, MatchMode.ANYWHERE);
					keyWordCrit = keywInDescRestr;
				} 
				
				if(clazzName.equals("Goal")) {
					keywInDescRestr = Restrictions.ilike("description",keyw, MatchMode.ANYWHERE);
					keyWordCrit = Restrictions.or(keywInDescRestr,Restrictions.ilike("goalTag",keyw, MatchMode.ANYWHERE));
				}
				
				if(clazzName.equals("Activity")) { 
					keyWordCrit = Restrictions.ilike("event",keyw, MatchMode.ANYWHERE);
				}
				
				if(clazzName.equals("Project")) {
					keywInDescRestr = Restrictions.ilike("description",keyw, MatchMode.ANYWHERE);
					keyWordCrit = Restrictions.or(keywInDescRestr,Restrictions.ilike("name",keyw, MatchMode.ANYWHERE));
				}

				q.add(keyWordCrit);
			}
		}
		
		return q;

	}

	<T> ObjectListRes<T> getObjectsAndResSet(Criteria q, Filters filter, Class<T> clazz) {

		Long count = (Long) q.setProjection(Projections.rowCount()).uniqueResult();

		// Remove the projection
		q.setProjection(null);
		q.setResultTransformer(Criteria.ROOT_ENTITY);

		Order order = null;
		
		if(filter.getSortBy() != null) {
			if(filter.getSortBy().equals("CREATIONDATEDESC")) order = Order.desc("creationDate");
			if(filter.getSortBy().equals("CREATIONDATEASC")) order = Order.asc("creationDate");
			if(filter.getSortBy().equals("RELEVANCEDESC")) order = Order.desc("relevance");
			if(filter.getSortBy().equals("RELEVANCEASC")) order = Order.asc("relevance");
			
			q.addOrder(order);
		} 
		
		@SuppressWarnings("unchecked")
		List<T> objects = (List<T>) q.setFirstResult((filter.getPage()-1)*filter.getNperpage()).setMaxResults(filter.getNperpage()).list();

		int lastRes = filter.getPage()*filter.getNperpage();
		if(count < filter.getPage()*filter.getNperpage()) lastRes = (int) (count + 0);

		ObjectListRes<T> res = new ObjectListRes<T>();
		res.setObjects(objects);

		int[] resSet = {(filter.getPage()-1)*filter.getNperpage()+1,lastRes,(int) (count + 0)};
		res.setResSet(resSet);

		return res;
	}

}
