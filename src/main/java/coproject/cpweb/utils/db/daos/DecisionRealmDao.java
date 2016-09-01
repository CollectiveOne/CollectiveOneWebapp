package coproject.cpweb.utils.db.daos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import coproject.cpweb.utils.db.entities.DecisionRealm;

@Service
public class DecisionRealmDao extends BaseDao {

	public DecisionRealm get(int id) {
		return (DecisionRealm) super.get(id,DecisionRealm.class);
	}
	
	public List<DecisionRealm> getAll(Integer max) {
		return (List<DecisionRealm>) super.getAll(max,DecisionRealm.class);
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
}
