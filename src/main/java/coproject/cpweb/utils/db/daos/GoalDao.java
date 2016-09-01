package coproject.cpweb.utils.db.daos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import coproject.cpweb.utils.db.entities.Goal;
import coproject.cpweb.utils.db.entities.GoalState;
import coproject.cpweb.utils.db.services.GoalFilters;
import coproject.cpweb.utils.db.services.GoalListRes;

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
	
	public List<String> getSuggestions(String query) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<String> res = (List<String>) session.createCriteria(Goal.class)
				.add(Restrictions.eq("state", GoalState.ACCEPTED))
				.add(Restrictions.like("goalTag", "%"+query+"%"))
				.setProjection(Projections.property("goalTag"))
				.list();
		
		return res;
	}
	
	public GoalListRes get(GoalFilters filters, int page, int nPerPage) {
		// TODO: repeated code used to list Cbtions, Goals and Decisions. DRY.
		List<String> projectNames = filters.projectNames;
		List<String> stateNames = filters.stateNames;
		
		Session session = sessionFactory.getCurrentSession();
		Criteria q = session.createCriteria(Goal.class).createAlias("project", "proj");
		
		Disjunction projDisj = Restrictions.disjunction();
		for(String projectName:projectNames) {
			projDisj.add( Restrictions.eq("proj.name", projectName));
		}
		
		Disjunction stateDisj = Restrictions.disjunction();
		for(String stateName:stateNames) {	
			stateDisj.add( Restrictions.eq("state", GoalState.valueOf(stateName)));
		}
		q.add(Restrictions.and(projDisj,stateDisj));
		
		Long count = (Long) q.setProjection(Projections.rowCount()).uniqueResult();
		
		// Remove the projection
		q.setProjection(null);
		q.setResultTransformer(Criteria.ROOT_ENTITY);
		
		@SuppressWarnings("unchecked")
		List<Goal> goals = (List<Goal>) q.setFirstResult((page-1)*nPerPage).setMaxResults(nPerPage).list();
		
		int lastRes = page*nPerPage;
		if(count < page*nPerPage) lastRes = (int) (count + 0);
		
		GoalListRes res = new GoalListRes();
		res.setGoals(goals);
		
		int[] resSet = {(page-1)*nPerPage+1,lastRes,(int) (count + 0)};
		res.setResSet(resSet);
		
		return res;
	}
	
}
