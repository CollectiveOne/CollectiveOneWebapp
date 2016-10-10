package coproject.cpweb.utils.db.daos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import coproject.cpweb.utils.db.entities.DecisionRealm;
import coproject.cpweb.utils.db.entities.User;
import coproject.cpweb.utils.db.entities.Voter;

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
	
	public void updateVoter(int realmId, int voterUserId, double weight) {
		
		Session session = sessionFactory.getCurrentSession();
		DecisionRealm realm = session.get(DecisionRealm.class,realmId);
		
		Voter voter = getVoter(realmId,voterUserId);
		
		if(voter == null) {
			/* if voter is not in the realm, then add him */
			voter = new Voter();
			voter.setWeight(0.0);
			voter.setVoterUser(session.get(User.class,voterUserId));
			realm.getVoters().add(voter);
		}
		
		voter.setWeight(weight);
		
		save(voter);
		save(realm);
	}
	
	public Voter getVoter(int realmId, int voterUserId) {
		Session session = sessionFactory.getCurrentSession();

		Query query = session.createQuery(
				"SELECT voter "
						+ "FROM DecisionRealm realm "
						+ "JOIN realm.voters voter "
						+ "WHERE realm.id = :rId "
						+ "AND voter.voterUser.id = :vuId "
				);
		
		query.setParameter("rId", realmId);
		query.setParameter("vuId", voterUserId);
		
		return (Voter)query.uniqueResult();
	}
}
