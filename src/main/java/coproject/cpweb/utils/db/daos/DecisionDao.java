package coproject.cpweb.utils.db.daos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import coproject.cpweb.utils.db.entities.Decision;
import coproject.cpweb.utils.db.entities.DecisionState;
import coproject.cpweb.utils.db.entities.Thesis;
import coproject.cpweb.utils.db.services.DecisionFilters;
import coproject.cpweb.utils.db.services.DecisionListRes;

@Service
public class DecisionDao extends BaseDao {

	public Decision get(int id) {
		return (Decision) super.get(id,Decision.class);
	}
	
	public List<Decision> getWithState(DecisionState state) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Decision.class);
		query.add(Restrictions.eq("state", state));
		
		@SuppressWarnings("unchecked")
		List<Decision> res = (List<Decision>) query.list();
		
		return res;
	}

	public Thesis getThesisCasted(int decId, int thesisAuthorId) {
		
 		Session session = sessionFactory.getCurrentSession();

		Query query = session.createQuery(
				"SELECT thesis "
						+ "FROM Decision dec "
						+ "JOIN dec.thesesCast thesis "
						+ "WHERE dec.id = :dId "
						+ "AND thesis.author.id = :aId"
				);
		
		query.setParameter("dId", decId);
		query.setParameter("aId", thesisAuthorId);

		return (Thesis)query.uniqueResult();
	}
	
public DecisionListRes get(DecisionFilters filters, int page, int nPerPage) {
		// TODO: repeated code used to list Cbtions, Goals and Decisions. DRY.
	
		List<String> projectNames = filters.projectNames;
		List<String> stateNames = filters.stateNames;
		
		Session session = sessionFactory.getCurrentSession();
		Criteria q = session.createCriteria(Decision.class).createAlias("project", "proj");
		
		Disjunction projDisj = Restrictions.disjunction();
		for(String projectName:projectNames) {
			projDisj.add( Restrictions.eq("proj.name", projectName));
		}
		
		Disjunction stateDisj = Restrictions.disjunction();
		for(String stateName:stateNames) {	
			stateDisj.add( Restrictions.eq("state", DecisionState.valueOf(stateName)));
		}
		q.add(Restrictions.and(projDisj,stateDisj));
		
		Long count = (Long) q.setProjection(Projections.rowCount()).uniqueResult();
		
		// Remove the projection
		q.setProjection(null);
		q.setResultTransformer(Criteria.ROOT_ENTITY);
		
		@SuppressWarnings("unchecked")
		List<Decision> decisions = (List<Decision>) q.setFirstResult((page-1)*nPerPage).setMaxResults(nPerPage).list();
		
		int lastRes = page*nPerPage;
		if(count < page*nPerPage) lastRes = (int) (count + 0);
		
		DecisionListRes res = new DecisionListRes();
		res.setDecisions(decisions);
		
		int[] resSet = {(page-1)*nPerPage+1,lastRes,(int) (count + 0)};
		res.setResSet(resSet);
		
		return res;
	}

}
