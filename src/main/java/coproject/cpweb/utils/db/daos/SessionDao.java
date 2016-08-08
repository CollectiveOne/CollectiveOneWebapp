package coproject.cpweb.utils.db.daos;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionDao {
	
	@Autowired
	SessionFactory sessionFactory;
	
	public void flush() {
		Session session = sessionFactory.getCurrentSession();
		session.flush();
	}
}
