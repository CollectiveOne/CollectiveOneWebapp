package coproject.cpweb.utils.db.daos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import coproject.cpweb.utils.db.entities.Decision;
import coproject.cpweb.utils.db.entities.DecisionState;
import coproject.cpweb.utils.db.entities.Thesis;
import coproject.cpweb.utils.db.services.Filters;
import coproject.cpweb.utils.db.services.ObjectListRes;

@Service
public class DecisionDao extends BaseDao {

	public Decision get(int id) {
		return (Decision) super.get(id,Decision.class);
	}
	
	public List<Decision> getWithStates(List<DecisionState> states) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Decision.class);
		
		Disjunction stateDisj = Restrictions.disjunction();
		for(DecisionState state:states) {	
			stateDisj.add( Restrictions.eq("state", state));
		}
		
		query.add(stateDisj);
		
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
	
	public ObjectListRes<Decision> get(Filters filters) {
		
		Criteria q = applyGeneralFilters(filters, Decision.class);
		
		/* State names are entity specific and I was not able to put these
		 * disjunction in a common function*/
		
		List<String> stateNames = filters.getStateNames();
		Disjunction stateDisj = Restrictions.disjunction();
		for(String stateName:stateNames) {	
			stateDisj.add( Restrictions.eq("state", DecisionState.valueOf(stateName)));
		}
		
		q.add(stateDisj);
		
		return getObjectsAndResSet(q, filters, Decision.class);
	}

}
