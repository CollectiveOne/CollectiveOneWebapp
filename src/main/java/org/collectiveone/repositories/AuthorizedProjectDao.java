package org.collectiveone.repositories;

import org.collectiveone.model.AuthorizedProject;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;


@Repository
public class AuthorizedProjectDao extends BaseDao {

	public AuthorizedProjectDao() {
		super();
	}

	public AuthorizedProject get(Integer id) {
		return (AuthorizedProject) super.get(id,AuthorizedProject.class);
	}
	
	public AuthorizedProject get(String projectName) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(AuthorizedProject.class);
		query.add(Restrictions.eq("projectName", projectName));
		AuthorizedProject res = (AuthorizedProject) query.uniqueResult();
		return res;
	}

	
	
}
