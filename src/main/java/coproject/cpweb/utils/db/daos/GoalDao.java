package coproject.cpweb.utils.db.daos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import coproject.cpweb.utils.db.entities.Goal;
import coproject.cpweb.utils.db.entities.GoalState;
import coproject.cpweb.utils.db.services.Filters;
import coproject.cpweb.utils.db.services.ObjectListRes;

@Service
public class GoalDao extends BaseDao {
	
	public Goal get(Integer id) {
		return (Goal) super.get(id,Goal.class);
	}
	
	public Goal get(String goalTag) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Goal.class);
		query.add(Restrictions.eq("goalTag", goalTag));
		
		return (Goal) query.uniqueResult();
	}
	
	public List<Goal> getAll(Integer max) {
		return (List<Goal>) super.getAll(max,Goal.class);
	}
	
	public List<Goal> getNotDeleted() {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Goal.class);
		query.add(Restrictions.or(
				Restrictions.eq("state", GoalState.PROPOSED),
				Restrictions.eq("state", GoalState.ACCEPTED)));
		
		@SuppressWarnings("unchecked")
		List<Goal> res = (List<Goal>) query.list();
		
		return res;
	}
	
	public List<String> getSuggestions(String query, int projectId) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<String> res = (List<String>) session.createCriteria(Goal.class)
				.add(Restrictions.eq("state", GoalState.ACCEPTED))
				.add(Restrictions.eq("project.id", projectId))
				.add(Restrictions.ilike("goalTag", query, MatchMode.ANYWHERE))
				.setProjection(Projections.property("goalTag"))
				.list();
		
		return res;
	}
	
	public ObjectListRes<Goal> get(Filters filters) {
		
		Criteria q = applyGeneralFilters(filters, Goal.class);
		
		/* State names are entity specific and I was not able to put these
		 * disjunction in a common function*/
		
		List<String> stateNames = filters.getStateNames();
		Disjunction stateDisj = Restrictions.disjunction();
		for(String stateName:stateNames) {	
			stateDisj.add( Restrictions.eq("state", GoalState.valueOf(stateName)));
		}
		
		q.add(stateDisj);
		
		return getObjectsAndResSet(q, filters, Goal.class);
	}
	
	public Goal getSubGoal(int goalId, int subGoalId) {
		Session session = sessionFactory.getCurrentSession();

		Query query = session.createQuery(
				"SELECT subgoalx "
						+" FROM Goal goalx "
						+ "JOIN goalx.subgoals subgoalx "
						+ "WHERE goalx.id = :gId "
						+ "AND subgoalx.id = :sId "
				);
		
		query.setParameter("gId", goalId);
		query.setParameter("sId", subGoalId);
		
		return (Goal) query.uniqueResult();
	}
	
	public String addSubGoal(int goalId, int subGoalId) {
		String msg;
		
		if(getSubGoal(goalId,subGoalId) == null) {
			// goal is not a subgoal yet
			Goal goal = get(goalId);
			goal.getSubgoals().add(get(subGoalId));
			msg = "subgoal added";
		} else {
			msg = "subgoal is already set";
		}
		
		return msg;
	}
	
}
