package org.collectiveone.services;

import java.sql.Timestamp;

import org.collectiveone.model.Bid;
import org.collectiveone.model.Review;
import org.collectiveone.model.User;
import org.collectiveone.web.dto.ReviewDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewServiceImp extends BaseService {
	
	@Transactional
	public String bidCreate(ReviewDto reviewDto) {

		User creator = userRepository.get(reviewDto.getCreatorUsername());
		Bid bid = bidRepository.get(reviewDto.getBidId());

		if(bidRepository.getReviewer(reviewDto.getBidId(), creator.getId()) == null) {
			User reviewee = bid.getCreator();

			Review review = new Review();

			review.setCreationDate(new Timestamp(System.currentTimeMillis()));
			review.setCreator(creator);
			review.setReviewee(reviewee);
			review.setDescription(reviewDto.getDescription());
			review.setRate(reviewDto.getRate());

			bid.getReviews().add(review);

			bidRepository.save(bid);
			reviewDao.save(review);

			return "review created";
		} else {
			return "user has already reviewed this bid";
		}


	}

}
