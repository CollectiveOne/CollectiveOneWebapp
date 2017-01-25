package org.collectiveone.repositories;

import java.sql.Timestamp;
import java.util.List;

import org.collectiveone.model.Bid;
import org.collectiveone.model.BidState;
import org.collectiveone.model.CbtionState;
import org.collectiveone.model.DecisionState;
import org.collectiveone.model.User;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class BidDao extends BaseDao {
	
	public BidDao() {
		super();
	}
	
	public Bid get(Long id) {
		return (Bid) super.get(id,Bid.class);
	}
	
	public List<Bid> getAll(Integer max) {
		return (List<Bid>) super.getAll(max,Bid.class);
	}
	
	public void remove(Long id) {
		Bid bid = get(id);
		
		bid.setState(BidState.DELETED);
		bid.setDeleteDate(new Timestamp(System.currentTimeMillis()));
		
		if(bid.getAssign() != null)	bid.getAssign().setState(DecisionState.CLOSED_EXTERNALLY);
		if(bid.getAccept() != null)	bid.getAccept().setState(DecisionState.CLOSED_EXTERNALLY);
		
		save(bid);
	}
	
	public List<Bid> getNotClosed() {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Bid.class);
		query.add(Restrictions.or(
				Restrictions.eq("state", BidState.OFFERED),
				Restrictions.eq("state", BidState.ASSIGNED)));
		
		@SuppressWarnings("unchecked")
		List<Bid> res = (List<Bid>) query.list();
		
		return res;
	}
	
	public Bid getOfCbtionAndUser(Long cbtionId, Long userId) {
		
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Bid.class);
		query.add(Restrictions.and(
				Restrictions.eq("cbtion.id", cbtionId),
				Restrictions.eq("creator.id", userId)));
		
		Bid bid = (Bid) query.uniqueResult();
		
		return bid;
	}
	
	public List<Bid> getOfCbtion(Long cbtionId) {
		
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Bid.class);
		query.add(Restrictions.eq("cbtion.id", cbtionId));
		
		@SuppressWarnings("unchecked")
		List<Bid> bids = (List<Bid>) query.list();
		
		return bids;
	}
	
	public List<Bid> getOfUser(Long userId) {
		
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Bid.class);
		query.add(Restrictions.eq("creator.id", userId));
		
		@SuppressWarnings("unchecked")
		List<Bid> bids = (List<Bid>) query.list();
		
		return bids;
	}
	
	public User getReviewer(Long bidId, Long creatorId) {
		Session session = sessionFactory.getCurrentSession();

		Query query = session.createQuery(
				"SELECT review.creator "
						+" FROM Bid bid "
						+ "JOIN bid.reviews review "
						+ "WHERE bid.id = :bId "
						+ "AND review.creator.id = :cId"
				);
		
		query.setParameter("bId", bidId);
		query.setParameter("cId", creatorId);
		
		return (User) query.uniqueResult();
	}
	
	public List<Bid> getNotPaid() {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Bid.class,"bd");
		query
			.createAlias("bd.cbtion", "cb")
			.add(Restrictions.eq("cb.state", CbtionState.ASSIGNED))
			.add(Restrictions.eq("state", BidState.ACCEPTED));
				
		
		@SuppressWarnings("unchecked")
		List<Bid> res = (List<Bid>) query.list();
		
		return res;
	}
	
}
