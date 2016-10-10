package coproject.cpweb.utils.db.daos;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
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
		
		/* Only filter subgoals if showing accepted goals, non accepted goals are not formal goals */
		if(filters.getStateNames().size() > 0) {
			if(!filters.getStateNames().contains("ACCEPTED")) {
				only_parent_goals = false;
			}
		}
		
		if(only_parent_goals) {
			q.add(Restrictions.isNull("parent"));
		}
		
		return getObjectsAndResSet(q, filters, Goal.class);
		
	}
	
	public List<Goal> getSubgoalsIteratively(int goalId) {
		List<Goal> subgoals = getSubgoals(goalId);
		int nsubgoals = subgoals.size();
		for(int ix = 0; ix < nsubgoals ; ix++) {
			Goal subgoal = subgoals.get(ix);
			/* reentrance */ 
			List<Goal> subsubgoals = getSubgoalsIteratively(subgoal.getId());
			for(Goal subsubgoal : subsubgoals) {
				subgoals.add(subsubgoal);
			}
		}
		
		return subgoals;
	}
	
	public List<Goal> getSubgoals(int goalId) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Goal.class,"go");
		
		query.createAlias("go.parent","pa")
			.add(Restrictions.eq("pa.id", goalId));
		
		@SuppressWarnings("unchecked")
		List<Goal> res = (List<Goal>) query.list();
		
		return res;
	}
	
	public List<Goal> getAllParents(int goalId) {
		
		List<Goal> parents = new ArrayList<Goal>();
		
		Goal goal = get(goalId);
		Goal parent = goal.getParent();
		int count = 0;
		
		while((parent != null) && (count < 20)) {
			parents.add(parent);
			parent = parent.getParent();
			count++;
		}
		return parents;
	}
	
}
