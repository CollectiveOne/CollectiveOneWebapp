package org.collectiveone.repositories;

import java.util.List;

import org.collectiveone.model.DecisionRealm;
import org.collectiveone.model.User;
import org.collectiveone.model.Voter;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class DecisionRealmDao extends BaseDao {

	public DecisionRealmDao() {
		super();
	}

	public DecisionRealm get(Long id) {
		return (DecisionRealm) super.get(id,DecisionRealm.class);
	}
	
	public List<DecisionRealm> getAll(Integer max) {
		return (List<DecisionRealm>) super.getAll(max,DecisionRealm.class);
	}
	
	public Long getIdFromGoalId(Long goalId) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(DecisionRealm.class);
		query.add(Restrictions.eq("goal.id", goalId)).setProjection(Projections.id());
		return (Long) query.uniqueResult();
	}
	
	public DecisionRealm getFromGoalId(Long goalId) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(DecisionRealm.class);
		query.add(Restrictions.eq("goal.id", goalId));
		return (DecisionRealm) query.uniqueResult();
	}
	
	public List<DecisionRealm> getAllOfProject(Long projectId) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(DecisionRealm.class);
		query.add(Restrictions.eq("project.id", projectId));
		
		@SuppressWarnings("unchecked")
		List<DecisionRealm> res = (List<DecisionRealm>) query.list();
		return res; 
	}
	
	public void addOrUpdateVoter(Long realmId, Long voterUserId, double maxWeight, double scale) {
		
		Session session = sessionFactory.getCurrentSession();
		DecisionRealm realm = session.get(DecisionRealm.class,realmId);
		
		Voter voter = getVoter(realmId,voterUserId);
		
		if(voter == null) {
			/* if voter is not in the realm, then add him */
			voter = new Voter();
			voter.setVoterUser(session.get(User.class,voterUserId));
			voter.setMaxWeight(maxWeight);
			voter.setScale(scale);
			realm.getVoters().add(voter);
			
		}
		
		voter.setMaxWeight(maxWeight);
		voter.setScale(scale);
		
		save(voter);
		save(realm);
	}
	
	public Voter getVoter(Long realmId, Long voterUserId) {
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
