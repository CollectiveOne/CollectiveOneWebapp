package org.collectiveone.web.controllers.rest;

import java.io.IOException;
import java.util.List;

import org.collectiveone.services.BidServiceImp;
import org.collectiveone.services.ReviewServiceImp;
import org.collectiveone.web.dto.BidDto;
import org.collectiveone.web.dto.BidNewDto;
import org.collectiveone.web.dto.DoneDto;
import org.collectiveone.web.dto.ReviewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/1")
public class BidsController {  // NO_UCD (unused code)
	
	@Autowired
	BidServiceImp bidService;
	
	@Autowired
	ReviewServiceImp reviewService;
	
	@RequestMapping(value="/bid/{id}", method = RequestMethod.GET)
	public @ResponseBody BidDto get(@PathVariable Long id) {
		return bidService.getDto(id);
	}
	
	@RequestMapping(value="/cbtion/{cbtionId}/bids", method = RequestMethod.GET)
	public @ResponseBody List<BidDto> getList(@PathVariable("cbtionId") Long cbtionId) {
		List<BidDto> bidDtos = bidService.getOfCbtionDto(cbtionId);
		return bidDtos;
	}
	
	
	@RequestMapping(value="/bid", method = RequestMethod.POST)
	public @ResponseBody boolean newBid(@RequestBody BidNewDto bidNewDto) throws IOException {
		// Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		bidNewDto.setCreatorUsername("");
		Long id = bidService.create(bidNewDto);
		
		bidNewDto.setId(id);
		if(bidNewDto.getOffer()) {
			bidService.fromConsideringToOffered(bidNewDto);
		}
		return true;
	}
	
	/* 	TODO: all bid editions could go under PUT /bid and then internal logic would
	 	understand which action to execute */
	
	@RequestMapping(value="/bid/offer", method = RequestMethod.PUT)
	public @ResponseBody boolean offer(@RequestBody BidNewDto bidNewDto) {
		// Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// if(auth.isAuthenticated()) {
			bidNewDto.setCreatorUsername("");
			bidService.fromConsideringToOffered(bidNewDto);	
		// }
		return true;
	}
	
	@RequestMapping(value="/bid/done", method = RequestMethod.PUT)
	public @ResponseBody boolean markDone(@RequestBody DoneDto doneDto) throws IOException {
		// Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		doneDto.setUsername("");
		bidService.markDone(doneDto);
		return true;
	}
	
	@RequestMapping(value="/bid/{id}/reviews", method = RequestMethod.GET)
	public @ResponseBody List<ReviewDto> getReviews(@PathVariable Long id) {
		return bidService.getReviewsDtos(id);
	}
	
	@RequestMapping(value="/review", method = RequestMethod.POST)
	public @ResponseBody boolean newReview(@RequestBody ReviewDto reviewDto) {
		// Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// if(auth.isAuthenticated()) {
			reviewDto.setCreatorUsername("");
			reviewService.bidCreate(reviewDto);
			return true;
		// }
		// return false;
	}
	
	
}
