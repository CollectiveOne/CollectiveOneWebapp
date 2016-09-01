package coproject.cpweb.utils.db.daos;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import coproject.cpweb.utils.db.entities.Thesis;

@Service
public class ThesisDao extends BaseDao {

	public Thesis get(Integer id) {
		return (Thesis) super.get(id,Thesis.class);
	}
	
	public Thesis getOfUserInDec(int decId, int userId) {
		Session session = sessionFactory.getCurrentSession();

		return (Thesis) session.createCriteria(Thesis.class)
				.add(Restrictions.eq("decision.id", decId))
				.add(Restrictions.eq("author.id", userId))
				.uniqueResult();
	}

}
