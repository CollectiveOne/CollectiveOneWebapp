package coproject.cpweb.utils.db.daos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coproject.cpweb.utils.db.entities.Cbtion;
import coproject.cpweb.utils.db.entities.Project;
import coproject.cpweb.utils.db.entities.User;

@Service
public class ProjectDao {

	@Autowired
	SessionFactory sessionFactory;

	public void save(Project project) {		
		Session session = sessionFactory.getCurrentSession();
		session.save(project);
	}

	public Project get(Integer id) {

		Session session = sessionFactory.getCurrentSession();
		Project project = session.get(Project.class,id);

		return project;
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

	public List<Project> getAll(Integer max) {
		Session session = sessionFactory.getCurrentSession();

		@SuppressWarnings("unchecked")
		List<Project> res = (List<Project>) session.createCriteria(Project.class)
		.list();

		return res;
	}

	public List<String> getList() {
		Session session = sessionFactory.getCurrentSession();

		@SuppressWarnings("unchecked")
		List<String> res = (List<String>) session.createCriteria(Project.class)
		.setProjection(Projections.property("name"))
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

	public void addContributor(int contributorId, int projectId) {

		Session session = sessionFactory.getCurrentSession();

		Query query = session.createQuery(
				"SELECT pj.contributors "
						+ "FROM Project pj "
						+ "JOIN pj.contributors cn "
						+ "WHERE pj.id = :pId "
						+ "AND cn.id = :cId"
				);

		query.setParameter("pId", projectId);
		query.setParameter("cId", contributorId);

		@SuppressWarnings("unchecked")
		List<User> res = (List<User>) query.list();

		if(res.size() == 0) {
			UserDao userDAO = new UserDao();
			userDAO.sessionFactory = sessionFactory;

			Project project = get(projectId);
			User contributor = userDAO.get(contributorId);
			project.getContributors().add(contributor);

			save(project);
		}
	}
	
	public double projectGetPpsTot(int projectId) {
		Session session = sessionFactory.getCurrentSession();
		
		Project project = session.get(Project.class,projectId);
		
		double ppsTot = 0.0;
		for(Cbtion cbtion : project.getCbtionsAccepted()) {
			ppsTot += cbtion.getAssignedPpoints();
		}
		return ppsTot;
	}
}
