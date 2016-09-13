package coproject.cpweb.utils.db.daos;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Service;

import coproject.cpweb.utils.db.entities.Activity;
import coproject.cpweb.utils.db.services.Filters;
import coproject.cpweb.utils.db.services.ObjectListRes;


@Service
public class ActivityDao extends BaseDao {

	public Activity get(Integer id) {
		return (Activity) super.get(id,Activity.class);
	}

	public List<Activity> getAll(Integer max) {
		return (List<Activity>) super.getAll(max,Activity.class);
	}
	
	public ObjectListRes<Activity> get(Filters filters) {
		Criteria q = applyGeneralFilters(filters, Activity.class);
		return getObjectsAndResSet(q, filters, Activity.class);
	}
	
}
