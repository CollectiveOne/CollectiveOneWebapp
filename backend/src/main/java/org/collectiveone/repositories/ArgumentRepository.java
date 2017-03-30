package org.collectiveone.repositories;

import java.util.List;

import org.collectiveone.model.Argument;
import org.collectiveone.model.ArgumentTendency;
import org.collectiveone.model.User;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class ArgumentRepository extends BaseRepository {

	public ArgumentRepository() {
		super();
	}

	public Argument get(Long id) {
		return (Argument) super.get(id,Argument.class);
	}

	public List<Argument> getOfDecision(Long decId, ArgumentTendency tendency) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Argument.class);
		
		query.add(Restrictions.eq("decision.id",decId))
		.add(Restrictions.eq("tendency", tendency));

		@SuppressWarnings("unchecked")
		List<Argument> res = (List<Argument>) query.list();

		return res;
	}
	
	public User getBacker(Long argId, Long userId) {
		Session session = sessionFactory.getCurrentSession();

		Query query = session.createQuery(
				"SELECT backer "
						+" FROM Argument arg "
						+ "JOIN arg.backers backer "
						+ "WHERE arg.id = :aId "
						+ "AND backer.id = :bId "
				);
		
		query.setParameter("aId", argId);
		query.setParameter("bId", userId);
		
		return (User) query.uniqueResult();
	}
	
	public String back(Long argId, User user) {
		
		String msg;
		
		if(getBacker(argId,user.getId()) == null) {
			// user is not a backer yet
			Argument arg = get(argId);
			arg.getBackers().add(user);
			msg = "argument backed";
		} else {
			msg = "user already backed this";
		}
				
		return msg;
	}
	
	public String unBack(Long argId, User user) {
		
		String msg;
		
		if(getBacker(argId,user.getId()) == null) {
			msg = "argument was not backed";
		} else {
			Argument arg = get(argId);
			arg.getBackers().remove(user);
			save(arg);
			
			msg = "backer removed";
		}
				
		return msg;
	}

}
