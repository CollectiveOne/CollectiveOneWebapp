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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/rest/bids")
public class BidsController {
	
	@Autowired
	BidServiceImp bidService;
	
	@Autowired
	ReviewServiceImp reviewService;
	
	@RequestMapping(value="/get/{id}", method = RequestMethod.POST)
	public @ResponseBody BidDto get(@PathVariable Long id) {
		return bidService.bidGetDto(id);
	}
	
	@RequestMapping(value="/getOfCbtion/{cbtionId}", method = RequestMethod.POST)
	public @ResponseBody List<BidDto> getList(@PathVariable("cbtionId") Long cbtionId) {
		List<BidDto> bidDtos = bidService.bidGetOfCbtionDto(cbtionId);
		return bidDtos;
	}
	
	
	@RequestMapping(value="/new", method = RequestMethod.POST)
	public @ResponseBody boolean newBid(@RequestBody BidNewDto bidNewDto) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		bidNewDto.setCreatorUsername(auth.getName());
		Long id = bidService.bidCreate(bidNewDto);
		
		bidNewDto.setId(id);
		if(bidNewDto.getOffer()) {
			bidService.bidFromConsideringToOffered(bidNewDto);
		}
		return true;
	}
	
	@RequestMapping(value="/offer", method = RequestMethod.POST)
	public @ResponseBody boolean offer(@RequestBody BidNewDto bidNewDto) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		bidNewDto.setCreatorUsername(auth.getName());
		bidService.bidFromConsideringToOffered(bidNewDto);
		return true;
	}
	
	@RequestMapping(value="/markDone", method = RequestMethod.POST)
	public @ResponseBody boolean markDone(@RequestBody DoneDto doneDto) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		doneDto.setUsername(auth.getName());
		bidService.bidMarkDone(doneDto);
		return true;
	}
	
	@RequestMapping(value="/getReviews/{id}", method = RequestMethod.POST)
	public @ResponseBody List<ReviewDto> getReviews(@PathVariable Long id) {
		return bidService.bidGetReviewsDtos(id);
	}
	
	@RequestMapping(value="/newReview", method = RequestMethod.POST)
	public @ResponseBody boolean newReview(@RequestBody ReviewDto reviewDto) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth.isAuthenticated()) {
			reviewDto.setCreatorUsername(auth.getName());
			reviewService.reviewBidCreate(reviewDto);
			return true;
		}
		return false;
	}
	
	
}
