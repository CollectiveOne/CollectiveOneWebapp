package org.collectiveone.repositories;

import java.util.List;
import java.util.Set;

import org.collectiveone.model.Contributor;
import org.collectiveone.model.Project;
import org.collectiveone.services.Filters;
import org.collectiveone.services.ObjectListRes;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectDao extends BaseDao {

	public ProjectDao() {
		super();
	}

	public Project get(Long id) {
		return (Project) super.get(id,Project.class);
	}
	
	public List<Project> getAll(Integer max) {
		return (List<Project>) super.getAll(max,Project.class);
	}

	public Project get(String project_name) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Project.class);
		query.add(Restrictions.like("name", project_name));

		@SuppressWarnings("unchecked")
		List<Project> res = (List<Project>) query.list();

		Project project;
		if(res.size() > 0)
			project = res.get(0);
		else
			project = null;

		return project;
	}

	public List<String> getListEnabled() {
		Session session = sessionFactory.getCurrentSession();

		@SuppressWarnings("unchecked")
		List<String> res = (List<String>) session.createCriteria(Project.class)
			.add(Restrictions.eq("enabled", true))
			.setProjection(Projections.property("name"))
			.addOrder(Order.desc("ppsTot"))
			.list();

		return res;
	}

	public List<Project> getFromRef(Project refProject, Integer max) {
		Session session = sessionFactory.getCurrentSession();

		@SuppressWarnings("unchecked")
		List<Project> res = (List<Project>) session.createCriteria(Project.class)
		.add(Example.create(refProject))
		.list();

		return res;
	}

	public Set<Contributor> getContributors(Long projectId) {

		Session session = sessionFactory.getCurrentSession();
		Project project = (Project) session.get(Project.class,projectId);
				
		return project.getContributors();
	}
	
	public Contributor getContributor(Long projectId, Long userId) {
		Session session = sessionFactory.getCurrentSession();

		Query query = session.createQuery(
				"SELECT ctrb "
						+ "FROM Project proj "
						+ "JOIN proj.contributors ctrb "
						+ "WHERE proj.id = :pId "
						+ "AND ctrb.contributorUser.id = :uId "
				);

		query.setParameter("pId", projectId);
		query.setParameter("uId", userId);
		
		Contributor res = (Contributor) query.uniqueResult();
		
		return res;
	}
	
	public ObjectListRes<Project> get(Filters filters) {
		
		Criteria q = applyGeneralFilters(filters, null, Project.class, false);
		
		/* hide disabled projects */
		q.add(Restrictions.eq("enabled",true));
		
		return getObjectsAndResSet(q, filters, Project.class);
		
	}

}
