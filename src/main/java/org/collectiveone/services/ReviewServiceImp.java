package org.collectiveone.services;

import java.sql.Timestamp;

import org.collectiveone.model.Bid;
import org.collectiveone.model.Review;
import org.collectiveone.model.User;
import org.collectiveone.repositories.BidDao;
import org.collectiveone.repositories.ReviewDao;
import org.collectiveone.repositories.UserDao;
import org.collectiveone.web.dto.ReviewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class ReviewServiceImp {
	
	@Autowired
	protected UserDao userDao;
	
	@Autowired
	protected BidDao bidDao;
	
	@Autowired 
	protected ReviewDao reviewDao;
	
	@Transactional
	public String reviewBidCreate(ReviewDto reviewDto) {

		User creator = userDao.get(reviewDto.getCreatorUsername());
		Bid bid = bidDao.get(reviewDto.getBidId());

		if(bidDao.getReviewer(reviewDto.getBidId(), creator.getId()) == null) {
			User reviewee = bid.getCreator();

			Review review = new Review();

			review.setCreationDate(new Timestamp(System.currentTimeMillis()));
			review.setCreator(creator);
			review.setReviewee(reviewee);
			review.setDescription(reviewDto.getDescription());
			review.setRate(reviewDto.getRate());

			bid.getReviews().add(review);

			bidDao.save(bid);
			reviewDao.save(review);

			return "review created";
		} else {
			return "user has already reviewed this bid";
		}


	}

}
