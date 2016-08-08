package coproject.cpweb.utils.db.daos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coproject.cpweb.utils.db.entities.DecisionRealm;

@Service
public class DecisionRealmDao {

	@Autowired
	SessionFactory sessionFactory;

	public void save(DecisionRealm realm) {
		Session session = sessionFactory.getCurrentSession();
		session.save(realm);
	}
	
	public DecisionRealm get(int id) {
		Session session = sessionFactory.getCurrentSession();
		DecisionRealm realm = session.get(DecisionRealm.class,id);
		return realm;
	}
	
	public int getIdFromProjectId(int projectId) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(DecisionRealm.class);
		query.add(Restrictions.eq("project.id", projectId)).setProjection(Projections.id());
		return (Integer) query.uniqueResult();
	}
	
	public DecisionRealm getFromProjectId(int projectId) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(DecisionRealm.class);
		query.add(Restrictions.eq("project.id", projectId));
		return (DecisionRealm) query.uniqueResult();
	}
	
	public List<DecisionRealm> getAll(Integer max) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<DecisionRealm> res = (List<DecisionRealm>) session.createCriteria(DecisionRealm.class)
				.list();
		
		return res;
	}

}
