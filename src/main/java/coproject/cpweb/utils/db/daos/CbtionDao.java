package coproject.cpweb.utils.db.daos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import coproject.cpweb.utils.db.entities.Bid;
import coproject.cpweb.utils.db.entities.BidState;
import coproject.cpweb.utils.db.entities.Cbtion;
import coproject.cpweb.utils.db.entities.CbtionState;
import coproject.cpweb.utils.db.entities.Comment;
import coproject.cpweb.utils.db.entities.Goal;
import coproject.cpweb.utils.db.services.Filters;
import coproject.cpweb.utils.db.services.ObjectListRes;

@Service
public class CbtionDao extends BaseDao {
	
	public Cbtion get(Integer id) {
		return (Cbtion) super.get(id,Cbtion.class);
	}
	
	public List<Cbtion> getAll(Integer max) {
		return (List<Cbtion>) super.getAll(max,Cbtion.class);
	}
	
	public List<Cbtion> get(Cbtion refCbtion) {
		return (List<Cbtion>) super.get(refCbtion,Cbtion.class);
	}
	
	public List<Cbtion> getWithState(CbtionState state) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Cbtion.class);
		query.add(Restrictions.eq("state", state));
		
		@SuppressWarnings("unchecked")
		List<Cbtion> res = (List<Cbtion>) query.list();
		
		return res;
	}
	
	public ObjectListRes<Cbtion> get(Filters filters) {
		
		Criteria q = applyGeneralFilters(filters, Cbtion.class);
		
		/* State names are entity specific and I was not able to put these
		 * disjunction in a common function*/
		
		List<String> stateNames = filters.getStateNames();
		Disjunction stateDisj = Restrictions.disjunction();
		for(String stateName:stateNames) {	
			stateDisj.add( Restrictions.eq("state", CbtionState.valueOf(stateName)));
		}
		
		q.add(stateDisj);
		
		/* if contributorUsername requested */
		String contributorUsername = filters.getContributorUsername();
		if(contributorUsername != null) {
			if(!contributorUsername.equals("")) {
				q.createAlias("contributor","cont")
					.add(Restrictions.eq("cont.username",contributorUsername));
			}
		}
		
		/* if goalTag requested */
		String goalTag = filters.getGoalTag();
		if(goalTag != null) {
			if(!goalTag.equals("")) {
				
				GoalDao goalDao = new GoalDao();
				goalDao.setSessionFactory(this.getSessionFactory());
				
				Disjunction goalDisj = Restrictions.disjunction();
				Goal goal = goalDao.get(goalTag);
				
				q.createAlias("goal","go");
				goalDisj.add(Restrictions.eq("go.id",goal.getId()));
				
				if(filters.getGoalSubgoalsFlag()) {
					for(Goal subgoal : goal.getSubgoals()) {
						goalDisj.add(Restrictions.eq("go.id",subgoal.getId()));
					}
				}
				
				q.add(goalDisj);
			}
		}
		
		return getObjectsAndResSet(q, filters, Cbtion.class);
	}
	
	public List<Cbtion> getAcceptedOfUserInProject(int userId, int projectId) {
		Session session = sessionFactory.getCurrentSession();

		Query query = session.createQuery(
				"SELECT cbtion "
						+" FROM User user "
						+ "JOIN user.cbtionsAccepted cbtion "
						+ "WHERE user.id = :uId "
						+ "AND cbtion.project.id = :pId "
				);
		
		query.setParameter("uId", userId);
		query.setParameter("pId", projectId);
		
		@SuppressWarnings("unchecked")
		List<Cbtion> res = (List<Cbtion>) query.list();
		
		return res;
	}
	
	public int countPromotersDiff(int cbtionId) {
		return countPromoters(cbtionId, true) - countPromoters(cbtionId, false); 
	}
	
	
	public int countPromoters(int cbtionId, boolean promoteUp) {
		
		Session session = sessionFactory.getCurrentSession();

		Query query = session.createQuery(
				"SELECT COUNT(*) "
						+" FROM Cbtion cbt "
						+ "JOIN cbt.promoters prom "
						+ "WHERE cbt.id = :cId "
						+ "AND prom.promoteUp = :puId "
				);
		
		query.setParameter("cId", cbtionId);
		query.setParameter("puId", promoteUp);
		
		Long count = (Long) query.uniqueResult();
		
		return (int)(count + 0);
	}	
	
	public List<Comment> getCommentsSorted(int cbtionId) {
		Session session = sessionFactory.getCurrentSession();

		Query query = session.createQuery(
				"SELECT coms "
						+" FROM Cbtion cbt "
						+ "JOIN cbt.comments coms "
						+ "WHERE cbt.id = :cId "
						+ "ORDER BY coms.relevance DESC"
				);
		
		query.setParameter("cId", cbtionId);
				
		@SuppressWarnings("unchecked")
		List<Comment> res = (List<Comment>) query.list();
		
		return res;
	}
	
	public Bid getAcceptedBid(int cbtionId) {
		Session session = sessionFactory.getCurrentSession();

		Criteria q = session.createCriteria(Bid.class)
				.createAlias("cbtion", "cbt")
				.add(Restrictions.eq("state",BidState.ACCEPTED))
				.add(Restrictions.eq("cbt.id",cbtionId));
						
		Bid res = (Bid) q.uniqueResult();
		
		return res;
	}
	
}
