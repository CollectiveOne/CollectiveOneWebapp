package coproject.cpweb.utils.db.daos;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
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
	
	public List<String> getSuggestions(String query, List<String>  projectNames) {
		Session session = sessionFactory.getCurrentSession();
		
		Criteria q = session.createCriteria(Goal.class,"go")
				.add(Restrictions.eq("state", GoalState.ACCEPTED))
				.add(Restrictions.ilike("goalTag", query, MatchMode.ANYWHERE));
			
		if(projectNames.size() > 0) {
			q.createAlias("go.project", "pr");
			
			Disjunction prDisj = Restrictions.disjunction();
			for(String projectName : projectNames) {
				prDisj.add( Restrictions.eq("pr.name", projectName));
			}
			q.add(prDisj);
		}
		
		@SuppressWarnings("unchecked")
		List<String> res = q.setProjection(Projections.property("goalTag"))
				.list();
		
		return res;
	}
	
	public ObjectListRes<Goal> get(Filters filters) {
		
		Criteria q = applyGeneralFilters(filters, Goal.class);
		
		/* State names are entity specific and I was not able to put these
		 * disjunction in a common function */
		
		List<String> stateNames = filters.getStateNames();
		Disjunction stateDisj = Restrictions.disjunction();
		for(String stateName:stateNames) {	
			stateDisj.add( Restrictions.eq("state", GoalState.valueOf(stateName)));
		}
		
		q.add(stateDisj);
		
		boolean only_parent_goals = true;
		
		/* Only filter subgoals if showing accepted goals, otherwise nothing would be shown */
		if(filters.getStateNames().size() > 0) {
			if(!filters.getStateNames().contains("ACCEPTED")) {
				only_parent_goals = false;
			}
		}
		
		if(only_parent_goals) {
			/* Filter all goals that are subgoals so that results are only parent only goals */
			DetachedCriteria subgoalsIds = DetachedCriteria.forClass(Goal.class,"goalx")
					.createAlias("goalx.subgoals", "subgoalsx")
					.setProjection(Projections.property("subgoalsx.id"));
			
			DetachedCriteria notSubgoals = DetachedCriteria.forClass(Goal.class)
					.add(Subqueries.propertyNotIn("id",subgoalsIds))
					.setProjection(Projections.property("id"));
			
			q.add(Subqueries.propertyIn("id", notSubgoals));
		}
		
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
	
	@SuppressWarnings("unchecked")
	public List<Goal> getAllSubGoals() {
		Session session = sessionFactory.getCurrentSession();

		DetachedCriteria subQuery = DetachedCriteria.forClass(Goal.class,"goalx")
				.createAlias("goalx.subgoals", "subgoalsx")
				.setProjection(Projections.property("subgoalsx.id"));
		
		Criteria query = session.createCriteria(Goal.class)
				.add(Subqueries.propertyNotIn("id",subQuery));
		
		List<Goal> res = (List<Goal>) query.list();
		
		return res;
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
	
	public Goal getParent(int goalId) {
		Session session = sessionFactory.getCurrentSession();

		Query query = session.createQuery(
						"  FROM Goal goalx "
						+ "JOIN goalx.subgoals subgoalx "
						+ "WHERE subgoalx.id = :gId "
				);
		
		query.setParameter("gId", goalId);
		
		return (Goal) query.uniqueResult();
	}
	
	public List<Goal> getAllParents(int goalId) {
		
		List<Goal> parents = new ArrayList<Goal>();
		
		Goal parent = getParent(goalId);
		while(parent != null) {
			parents.add(parent);
			parent = getParent(parent.getId());
		}
		return parents;
	}
	
}
