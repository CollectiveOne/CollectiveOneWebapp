package coproject.cpweb.utils.db.daos;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import coproject.cpweb.utils.db.entities.Promoter;

@Service
public class PromoterDao extends BaseDao {

	public Promoter get(Integer id) {
		return (Promoter) super.get(id,Promoter.class);
	}

	public List<Promoter> getAll(Integer max) {
		return (List<Promoter>) super.getAll(max,Promoter.class);
	}

	public Promoter getOfCbtion(int cbtionId, int userId) {
		Session session = sessionFactory.getCurrentSession();

		Query query = session.createQuery(
				" SELECT prom "
						+ "FROM Cbtion cbt "
						+ "JOIN cbt.promoters prom "
						+ "WHERE cbt.id = :cId "
						+ "AND prom.user.id = :uId"
				);

		query.setParameter("cId", cbtionId);
		query.setParameter("uId", userId);

		return (Promoter) query.uniqueResult();
	}

}
