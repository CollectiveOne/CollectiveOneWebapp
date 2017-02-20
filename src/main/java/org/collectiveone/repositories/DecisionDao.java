package org.collectiveone.repositories;

import java.util.List;

import org.collectiveone.model.Decision;
import org.collectiveone.model.DecisionState;
import org.collectiveone.model.Thesis;
import org.collectiveone.web.dto.Filters;
import org.collectiveone.web.dto.ObjectListRes;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DecisionDao extends BaseDao {

	@Autowired
	private ProjectDao projectDao;
	
	public DecisionDao() {
		super();
	}

	public Decision get(Long id) {
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

	public Thesis getThesisCasted(Long decId, Long thesisAuthorId) {
		
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
		
		Criteria q = applyGeneralFilters(filters, projectDao.getListEnabled(), Decision.class);
		
		/* State names are entity specific and I was not able to put these
		 * disjunction in a common function*/
		
		List<String> stateNames = filters.getStateNames();
		Disjunction stateDisj = Restrictions.disjunction();
		for(String stateName:stateNames) {	
			stateDisj.add( Restrictions.eq("state", DecisionState.valueOf(stateName)));
		}
		
		q.add(stateDisj);
		
		/* Decisions created by the platform can be filtered */
		if(!filters.getShowInternalDecisions()) {
			q.createAlias("creator", "crea")
				.add(Restrictions.ne("crea.username", "collectiveone"));
		}
		
		return getObjectsAndResSet(q, filters, Decision.class);
	}

}
