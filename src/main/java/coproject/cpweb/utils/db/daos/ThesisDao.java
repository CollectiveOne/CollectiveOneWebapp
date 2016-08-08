package coproject.cpweb.utils.db.daos;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coproject.cpweb.utils.db.entities.Thesis;

@Service
public class ThesisDao {

	@Autowired
	SessionFactory sessionFactory;

	public void save(Thesis thesis) {
		Session session = sessionFactory.getCurrentSession();
		session.save(thesis);
	}

	public void delete(Thesis thesis) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(thesis);
	}
	
	public Thesis get(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		Thesis vote = session.get(Thesis.class,id);
		return vote;
	}
	
	public Thesis getOfUserInDec(int decId, int userId) {
		Session session = sessionFactory.getCurrentSession();

		return (Thesis) session.createCriteria(Thesis.class)
				.add(Restrictions.eq("decision.id", decId))
				.add(Restrictions.eq("author.id", userId))
				.uniqueResult();
	}

}
