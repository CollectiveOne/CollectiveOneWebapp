package coproject.cpweb.utils.db.daos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coproject.cpweb.utils.db.entities.Decision;
import coproject.cpweb.utils.db.entities.DecisionState;
import coproject.cpweb.utils.db.entities.Thesis;

@Service
public class DecisionDao {

	@Autowired
	SessionFactory sessionFactory;

	public void save(Decision dec) {
		Session session = sessionFactory.getCurrentSession();
		session.save(dec);
	}

	public Decision get(int id) {
		Session session = sessionFactory.getCurrentSession();
		Decision dec = session.get(Decision.class,id);
		return dec;
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

}
