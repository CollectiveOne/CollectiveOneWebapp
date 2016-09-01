package coproject.cpweb.utils.db.daos;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseDao {
	
	@Autowired
	SessionFactory sessionFactory;
	
	public int save(Object object) {
		Session session = sessionFactory.getCurrentSession();
		int id = (Integer) session.save(object);
		return id;
	}
	
	public void delete(Object object) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(object);
	}
	
	public <T> int getN(Class<T> clazz) {
		Session session = sessionFactory.getCurrentSession();
		
		Long count = (Long) session.createCriteria(clazz)
				.setProjection(Projections.rowCount()).uniqueResult();
		
		return (int)(count + 0);
	}
	
	public <T> Object get(Integer id, Class<T> clazz) {
		Session session = sessionFactory.getCurrentSession();
		T object = session.get(clazz,id);
		return object;
	}
	
	public <T> List<T> getAll(Integer max, Class<T> clazz) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<T> res = (List<T>) session.createCriteria(clazz)
				.list();
		
		return res;
	}
	
	public <T> List<T> get(Object refObject, Class<T> clazz) {
		
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<T> res = (List<T>) session.createCriteria(clazz)
							.add(Example.create(refObject))
							.list();
		
		return res;
	}

}
