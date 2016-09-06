package coproject.cpweb.utils.db.daos;

import java.util.List;

import org.springframework.stereotype.Service;

import coproject.cpweb.utils.db.entities.Review;

@Service
public class ReviewDao extends BaseDao {

	public Review get(Integer id) {
		return (Review) super.get(id,Review.class);
	}

	public List<Review> getAll(Integer max) {
		return (List<Review>) super.getAll(max,Review.class);
	}


}
