package org.collectiveone.repositories;

import org.collectiveone.model.Thesis;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class ThesisDao extends BaseDao {

	public ThesisDao() {
		super();
	}

	public Thesis get(Long id) {
		return (Thesis) super.get(id,Thesis.class);
	}
	
	public Thesis getOfUserInDec(Long decId, Long userId) {
		Session session = sessionFactory.getCurrentSession();

		return (Thesis) session.createCriteria(Thesis.class)
				.add(Restrictions.eq("decision.id", decId))
				.add(Restrictions.eq("author.id", userId))
				.uniqueResult();
	}

}
