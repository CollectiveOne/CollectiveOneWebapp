package org.collectiveone.repositories;

import java.util.List;

import org.collectiveone.model.CbtionState;
import org.collectiveone.model.Project;
import org.collectiveone.model.User;
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

	public List<User> getContributors(Long projectId) {

		Session session = sessionFactory.getCurrentSession();

		Query query = session.createQuery(
				"SELECT DISTINCT cbt.contributor "
						+ "FROM Cbtion cbt "
						+ "WHERE cbt.project.id = :pId "
						+ "AND cbt.state = :cSt "
				);

		query.setParameter("pId", projectId);
		query.setParameter("cSt", CbtionState.ACCEPTED);

		@SuppressWarnings("unchecked")
		List<User> res = (List<User>) query.list();
		
		return res;
	}

}
