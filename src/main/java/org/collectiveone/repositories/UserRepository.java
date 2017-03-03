package org.collectiveone.repositories;

import java.util.List;

import org.collectiveone.model.CbtionState;
import org.collectiveone.model.Project;
import org.collectiveone.model.User;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends BaseRepository {
	
	public UserRepository() {
		super();
	}

	public User get(Long id) {
		return (User) super.get(id,User.class);
	}
	
	public List<User> getAll(Integer max) {
		return (List<User>) super.getAll(max,User.class);
	}
	
	public List<User> getFromRef(User refUser) {
		return (List<User>) super.get(refUser,User.class);
	}
	
	public Long getN() {
		return super.getN(User.class);
	}
	
	public User get(String username) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(User.class);
		query.add(Restrictions.eq("username", username));

		return (User) query.uniqueResult();
	}
	
	public List<Project> getProjectsContributed(Long userId) {
		Session session = sessionFactory.getCurrentSession();

		Query query = session.createQuery(
				"SELECT DISTINCT cbt.project "
						+ "FROM Cbtion cbt "
						+ "WHERE cbt.state = :sT "
						+ "AND cbt.contributor.id = :cId"
				);
		
		query.setParameter("cId", userId);
		query.setParameter("sT", CbtionState.ACCEPTED);
		
		@SuppressWarnings("unchecked")
		List<Project> res = (List<Project>) query.list();
		
		return res;
	}
	
	public List<String> getSuggestions(String query) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<String> res = (List<String>) session.createCriteria(User.class)
				.add(Restrictions.ilike("username", query, MatchMode.ANYWHERE))
				.setProjection(Projections.property("username"))
				.list();
		
		return res;
	}
	
	public List<String> getSuggestionsReferrer(String query) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<String> res = (List<String>) session.createCriteria(User.class)
				.add(Restrictions.ilike("username", query, MatchMode.ANYWHERE))
				.add(Restrictions.eq("isReferrer", true))
				.setProjection(Projections.property("username"))
				.list();
		
		return res;
	}
	
	public boolean isProjectStarred(Long projectId, Long userId) {
		Session session = sessionFactory.getCurrentSession();
		
		Criteria query = session.createCriteria(User.class)
				.add(Restrictions.eq("id", userId))
				.createAlias("projectsStarred","prCr")
				.add(Restrictions.eq("prCr.id", projectId));
				
		User res = (User) query.uniqueResult();
		
		if(res != null) return true;
		else return false;
	}
	
	public boolean isProjectWatched(Long projectId, Long userId) {
		Session session = sessionFactory.getCurrentSession();
		
		Criteria query = session.createCriteria(User.class)
				.add(Restrictions.eq("id", userId))
				.createAlias("projectsWatched","prWt")
				.add(Restrictions.eq("prWt.id", projectId));
				
		User res = (User) query.uniqueResult();
		
		if(res != null) return true;
		else return false;
	}
	
}
