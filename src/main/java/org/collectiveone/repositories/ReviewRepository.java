package org.collectiveone.repositories;

import java.util.List;

import org.collectiveone.model.Review;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewRepository extends BaseRepository {

	public ReviewRepository() {
		super();
	}

	public Review get(Integer id) {
		return (Review) super.get(id,Review.class);
	}

	public List<Review> getAll(Integer max) {
		return (List<Review>) super.getAll(max,Review.class);
	}


}
