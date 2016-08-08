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

import coproject.cpweb.utils.db.entities.Project;
import coproject.cpweb.utils.db.entities.User;

@Service
public class UserDao {
	
	@Autowired
	SessionFactory sessionFactory;
	
	public void save(User user) {
		Session session = sessionFactory.getCurrentSession();
		session.save(user);
	}
	
	public User get(Integer id) {
		
		Session session = sessionFactory.getCurrentSession();
		User user = session.get(User.class,id);
		return user;
	}
	
	public int getN() {
		Session session = sessionFactory.getCurrentSession();
		
		Long count = (Long) session.createCriteria(User.class)
				.setProjection(Projections.rowCount()).uniqueResult();
		
		return (int)(count + 0);
	}
	
	public User get(String username) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(User.class);
		query.add(Restrictions.eq("username", username));
		
		@SuppressWarnings("unchecked")
		List<User> res = (List<User>) query.list();
		
		User user;
		if(res.size() > 0)
			user = res.get(0);
		else
			user = null;
		
		return user;
	}
	
	public List<User> getAll(Integer max) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<User> res = (List<User>) session.createCriteria(User.class)
				.list();
		
		return res;
	}

	public List<User> getFromRef(User refUser, Integer max) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<User> res = (List<User>) session.createCriteria(User.class)
				.add(Example.create(refUser))
				.list();
		
		return res;
	}
	
	public Project getProjectContributed(int userId, int projectId) {
		Session session = sessionFactory.getCurrentSession();

		Query query = session.createQuery(
				"SELECT project "
						+ "FROM User user "
						+ "JOIN user.projectsContributed project "
						+ "WHERE user.id = :uId "
						+ "AND project.id = :pId"
				);
		
		query.setParameter("uId", userId);
		query.setParameter("pId", projectId);
		
		return (Project) query.uniqueResult();
	}
	
	public void addProjectContributed(int userId, int projectId) {
		Session session = sessionFactory.getCurrentSession();
		
		if(getProjectContributed(userId,projectId) == null) {
			User user = get(userId);
			user.getProjectsContributed().add(session.get(Project.class,projectId));
			save(user);
		}
	}
}
