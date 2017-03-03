package org.collectiveone.repositories;

import java.util.ArrayList;
import java.util.List;

import org.collectiveone.model.Goal;
import org.collectiveone.model.GoalAttachState;
import org.collectiveone.model.GoalState;
import org.collectiveone.web.dto.Filters;
import org.collectiveone.web.dto.ObjectListRes;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class GoalRepository extends BaseRepository {
	
	@Autowired
	private ProjectRepository projectDao;
	
	public GoalRepository() {
		super();
	}

	public Goal get(Long id) {
		return (Goal) super.get(id,Goal.class);
	}
	
	public Goal get(String goalTag, String projectName) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Goal.class);
		query.add(Restrictions.eq("goalTag", goalTag))
			.createAlias("project", "prj")
			.add(Restrictions.eq("prj.name", projectName));
		
		return (Goal) query.uniqueResult();
	}
	
	public Goal get(String goalTag, String projectName, GoalState state) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Goal.class);
		query.add(Restrictions.eq("goalTag", goalTag))
			.createAlias("project", "prj")
			.add(Restrictions.eq("prj.name", projectName))
			.add(Restrictions.eq("state", state));
		
		return (Goal) query.uniqueResult();
	}
	
	public List<Goal> getAllOfProject(Long projectId) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Goal.class);
		query
			.createAlias("project", "prj")
			.add(Restrictions.eq("prj.id", projectId));
		
		@SuppressWarnings("unchecked")
		List<Goal> res = (List<Goal>) query.list();
		
		return res;
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
		
		Criteria q = applyGeneralFilters(filters, projectDao.getNamesEnabled(), Goal.class);
		
		/* State names are entity specific and I was not able to put these
		 * disjunction in a common function */
		
		if(filters.getStateNames() != null) {
			List<String> stateNames = filters.getStateNames();
			Disjunction stateDisj = Restrictions.disjunction();
			for(String stateName:stateNames) {	
				stateDisj.add( Restrictions.eq("state", GoalState.valueOf(stateName)));
			}
			
			q.add(stateDisj);
		}
		
		if(filters.getOnlyParents()) {
			q.add(Restrictions.isNull("parent"));
		}
	
		return getObjectsAndResSet(q, filters, Goal.class);
		
	}
	
	public List<Goal> getSuperGoalsOnly(Long projectId) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Goal.class,"go");
		
		query
			.add(Restrictions.eq("project.id", projectId))
			.add(Restrictions.isNull("parent"));
		
		@SuppressWarnings("unchecked")
		List<Goal> res = (List<Goal>) query.list();
		
		return res;
	}
	
	public List<Goal> getSubgoalsIteratively(Long goalId) {
		
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
	
	public List<Goal> getSubgoals(Long goalId) {
		
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Goal.class,"go");
		
		query.createAlias("go.parent","pa")
			.add(Restrictions.eq("pa.id", goalId));
		
		@SuppressWarnings("unchecked")
		List<Goal> res = (List<Goal>) query.list();
		
		return res;
	}
	
	public List<Goal> getSubgoals(Long goalId, List<GoalState> states) {
		
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Goal.class,"go");
		
		if(states != null) {
			Disjunction stateDisj = Restrictions.disjunction();
			for(GoalState state:states) {	
				stateDisj.add( Restrictions.eq("state", state));
			}	
			query.add(stateDisj);
		}
		
		query.createAlias("go.parent","pa")
			.add(Restrictions.eq("pa.id", goalId));
		
		@SuppressWarnings("unchecked")
		List<Goal> res = (List<Goal>) query.list();
		
		return res;
	}
	
	public List<Goal> getAllParents(Long goalId) {
		
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
	
	public Goal getClosestDetachedParent(Long goalId) {
		/* returns the closest parent which is detached or null */
		Goal goal = get(goalId);
		Goal parent = goal.getParent();
		
		if(parent == null) {
			/* goal is a supergoal, doesn not have parents*/
			return null;		
		} else {
			
			boolean lookForDetachedParent = true;
			Goal detachedParent = null;
			
			Goal nextParent = parent;
			int level = 0;
			while(lookForDetachedParent) {
				if(nextParent == null) {
					/* all grand parents are attached up until the supergoal is reached */
					return null;
				} else {
					if(nextParent.getAttachedState() == GoalAttachState.DETACHED) {
						/* found detached parent */
						detachedParent = nextParent;
						lookForDetachedParent = false;
					} else {
						/* look with parent of parent */
						nextParent = nextParent.getParent();
					}
				} 
				
				/* protection against endless loop */
				level++;
				if(level >= 100) {
					lookForDetachedParent = false;
				}
			}
			
			return detachedParent;
		}
	}
	
	public Goal getClosestDetached(Long goalId) {
		/* returns the goal itself or the closest parent which is detached or null */
		Goal goal = get(goalId);
		if(goal.getAttachedState() == GoalAttachState.DETACHED) {
			return goal; 
		} else {
			return getClosestDetachedParent(goalId);
		}
		
	}
	
}
