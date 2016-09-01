package coproject.cpweb.utils.db.daos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import coproject.cpweb.utils.db.entities.Bid;
import coproject.cpweb.utils.db.entities.BidState;

@Service
public class BidDao extends BaseDao {
	
	public Bid get(Integer id) {
		return (Bid) super.get(id,Bid.class);
	}
	
	public List<Bid> getAll(Integer max) {
		return (List<Bid>) super.getAll(max,Bid.class);
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
	
	public Bid getOfCbtionAndUser(int cbtionId, int userId) {
		
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Bid.class);
		query.add(Restrictions.and(
				Restrictions.eq("cbtion.id", cbtionId),
				Restrictions.eq("creator.id", userId)));
		
		Bid bid = (Bid) query.uniqueResult();
		
		return bid;
	}
	
	public List<Bid> getOfCbtion(int cbtionId) {
		
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Bid.class);
		query.add(Restrictions.eq("cbtion.id", cbtionId));
		
		@SuppressWarnings("unchecked")
		List<Bid> bids = (List<Bid>) query.list();
		
		return bids;
	}
	
	public List<Bid> getOfUser(int userId) {
		
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Bid.class);
		query.add(Restrictions.eq("creator.id", userId));
		
		@SuppressWarnings("unchecked")
		List<Bid> bids = (List<Bid>) query.list();
		
		return bids;
	}
	
}
