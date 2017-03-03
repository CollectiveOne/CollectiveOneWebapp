package org.collectiveone.repositories;

import java.util.List;

import org.collectiveone.model.Comment;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

@Repository
public class CommentRepository extends BaseRepository {

	public CommentRepository() {
		super();
	}

	public Comment get(Long id) {
		return (Comment) super.get(id,Comment.class);
	}

	public List<Comment> getAll(Integer max) {
		return (List<Comment>) super.getAll(max,Comment.class);
	}
	

	public int countPromotersDiff(Long commentId) {
		return countPromoters(commentId, true) - countPromoters(commentId, false); 
	}
	
	
	public int countPromoters(Long commentId, boolean promoteUp) {
		
		Session session = sessionFactory.getCurrentSession();

		Query query = session.createQuery(
				"SELECT COUNT(*) "
						+" FROM Comment com "
						+ "JOIN com.promoters prom "
						+ "WHERE com.id = :cId "
						+ "AND prom.promoteUp = :puId "
				);
		
		query.setParameter("cId", commentId);
		query.setParameter("puId", promoteUp);
		
		Long count = (Long) query.uniqueResult();
		
		return (int)(count + 0);
	}

	public List<Comment> getRepliesSorted(Long commentId) {
		Session session = sessionFactory.getCurrentSession();

		Query query = session.createQuery(
				"SELECT reps "
						+" FROM Comment com "
						+ "JOIN com.replies reps "
						+ "WHERE com.id = :cId "
						+ "ORDER BY reps.relevance DESC"
				);
		
		query.setParameter("cId", commentId);
				
		@SuppressWarnings("unchecked")
		List<Comment> res = (List<Comment>) query.list();
		
		return res;
	}

}
