package org.collectiveone.repositories;

import org.collectiveone.model.Activity;
import org.collectiveone.web.dto.Filters;
import org.collectiveone.web.dto.ObjectListRes;
import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class ActivityRepository extends BaseRepository {

	@Autowired
	private ProjectRepository projectDao;
	
	public ActivityRepository() {
		super();
	}

	public ObjectListRes<Activity> get(Filters filters) {
		Criteria q = applyGeneralFilters(filters, projectDao.getNamesEnabled(), Activity.class);
		return getObjectsAndResSet(q, filters, Activity.class);
	}
	
}
