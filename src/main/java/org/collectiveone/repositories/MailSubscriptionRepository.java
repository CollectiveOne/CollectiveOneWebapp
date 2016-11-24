package org.collectiveone.repositories;

import java.util.List;

import org.collectiveone.model.MailSubscription;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;


@Repository
public class MailSubscriptionRepository extends BaseDao {

	public MailSubscriptionRepository() {
		super();
	}

	public MailSubscription get(Integer id) {
		return (MailSubscription) super.get(id,MailSubscription.class);
	}
	
	public List<String> getSubscribedAddresses(Long projectId) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(MailSubscription.class);
		
		query
			.createAlias("user", "usr")
			.add(Restrictions.eq("project.id",projectId))
			.setProjection(Projections.property("usr.email"));

		@SuppressWarnings("unchecked")
		List<String> res = (List<String>) query.list();

		return res;
	}
	
}
