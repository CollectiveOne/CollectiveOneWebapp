package coproject.cpweb.utils.db.daos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coproject.cpweb.utils.db.entities.Cbtion;
import coproject.cpweb.utils.db.entities.CbtionState;
import coproject.cpweb.utils.db.services.CbtionFilters;
import coproject.cpweb.utils.db.services.CbtionListRes;

@Service
public class CbtionDao {
	
	@Autowired
	SessionFactory sessionFactory;
	
	public int save(Cbtion cbtion) {
		Session session = sessionFactory.getCurrentSession();
		int id = (Integer) session.save(cbtion);
		return id;
	}
	
	public Cbtion get(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		Cbtion cbtion = session.get(Cbtion.class,id);
		return cbtion;
	}
	
	public List<Cbtion> getAll(Integer max) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<Cbtion> res = (List<Cbtion>) session.createCriteria(Cbtion.class)
				.list();
		
		return res;
	}
	
	public List<Cbtion> get(Cbtion refCbtion) {
		
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Cbtion> res = (List<Cbtion>) session.createCriteria(Cbtion.class)
							.add(Example.create(refCbtion))
							.list();
		
		return res;
	}
	
	public CbtionListRes get(CbtionFilters filters, int page, int nPerPage) {
		
		List<String> projectNames = filters.projectNames;
		List<String> stateNames = filters.stateNames;
		
		Session session = sessionFactory.getCurrentSession();
		Criteria q = session.createCriteria(Cbtion.class).createAlias("project", "proj");
		
		Disjunction projDisj = Restrictions.disjunction();
		for(String projectName:projectNames) {
			projDisj.add( Restrictions.eq("proj.name", projectName));
		}
		
		Disjunction stateDisj = Restrictions.disjunction();
		for(String stateName:stateNames) {	
			stateDisj.add( Restrictions.eq("state", CbtionState.valueOf(stateName)));
		}
		q.add(Restrictions.and(projDisj,stateDisj));
		
		Long count = (Long) q.setProjection(Projections.rowCount()).uniqueResult();
		
		// Remove the projection
		q.setProjection(null);
		q.setResultTransformer(Criteria.ROOT_ENTITY);
		
		@SuppressWarnings("unchecked")
		List<Cbtion> cbtions = (List<Cbtion>) q.setFirstResult((page-1)*nPerPage).setMaxResults(nPerPage).list();
		
		int lastRes = page*nPerPage;
		if(count < page*nPerPage) lastRes = (int) (count + 0);
		
		CbtionListRes res = new CbtionListRes();
		res.setCbtions(cbtions);
		
		int[] resSet = {(page-1)*nPerPage+1,lastRes,(int) (count + 0)};
		res.setResSet(resSet);
		
		return res;
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

}
