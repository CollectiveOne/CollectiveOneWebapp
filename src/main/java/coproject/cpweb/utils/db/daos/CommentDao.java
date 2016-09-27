package coproject.cpweb.utils.db.daos;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import coproject.cpweb.utils.db.entities.Comment;

@Service
public class CommentDao extends BaseDao {

	public Comment get(Integer id) {
		return (Comment) super.get(id,Comment.class);
	}

	public List<Comment> getAll(Integer max) {
		return (List<Comment>) super.getAll(max,Comment.class);
	}
	

	public int countPromotersDiff(int commentId) {
		return countPromoters(commentId, true) - countPromoters(commentId, false); 
	}
	
	
	public int countPromoters(int commentId, boolean promoteUp) {
		
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

	public List<Comment> getRepliesSorted(int commentId) {
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
