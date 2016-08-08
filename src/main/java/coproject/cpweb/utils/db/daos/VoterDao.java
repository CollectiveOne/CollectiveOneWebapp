package coproject.cpweb.utils.db.daos;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coproject.cpweb.utils.db.entities.DecisionRealm;
import coproject.cpweb.utils.db.entities.User;
import coproject.cpweb.utils.db.entities.Voter;

@Service
public class VoterDao {

	@Autowired
	SessionFactory sessionFactory;

	public void save(Voter voter) {
		Session session = sessionFactory.getCurrentSession();
		session.save(voter);
	}
	
	public Voter get(int id) {
		Session session = sessionFactory.getCurrentSession();
		Voter voter = session.get(Voter.class,id);
		return voter;
	}
	
	public void updateOrAdd(int realmId, int voterUserId, double addWeight) {
		
		Session session = sessionFactory.getCurrentSession();
		DecisionRealm realm = session.get(DecisionRealm.class,realmId);
		
		/* use DB to get the voter instead on looping on the realm voters */
		Voter voter = getFromUserAndRealm(realmId,voterUserId);
		
		if(voter == null) {
			/* if voter is not in the realm, then add him */
			voter = new Voter();
			voter.setWeight(0.0);
			voter.setVoterUser(session.get(User.class,voterUserId));
			realm.getVoters().add(voter);
		}
		
		voter.setWeight(voter.getWeight() + addWeight);
		
		session.save(realm);
	}
	
	public Voter getFromUserAndRealm(int realmId, int voterUserId) {
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
