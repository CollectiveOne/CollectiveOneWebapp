package org.collectiveone.repositories;

import java.util.List;

import org.collectiveone.model.Thesis;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class ThesisRepository extends BaseRepository {

	public ThesisRepository() {
		super();
	}

	public Thesis getOfUserInDec(Long decId, Long userId) {
		Session session = sessionFactory.getCurrentSession();

		return (Thesis) session.createCriteria(Thesis.class)
				.add(Restrictions.eq("decision.id", decId))
				.add(Restrictions.eq("author.id", userId))
				.uniqueResult();
	}
	
	public List<Thesis> getOfUserInRealm(Long realmId, Long userId) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Thesis.class,"thes");
		
		query
			.createAlias("thes.author", "auth")
			.createAlias("thes.decision", "dec")
			.createAlias("dec.decisionRealm", "realm")
			.add(Restrictions.eq("realm.id", realmId))
			.add(Restrictions.eq("auth.id", userId));
		
		@SuppressWarnings("unchecked")
		List<Thesis> res = (List<Thesis>) query.list();
		
		return res;
	}

}
