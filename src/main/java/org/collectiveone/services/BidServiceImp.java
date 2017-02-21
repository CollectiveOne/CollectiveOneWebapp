package org.collectiveone.services;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.collectiveone.model.Activity;
import org.collectiveone.model.ActivityType;
import org.collectiveone.model.Bid;
import org.collectiveone.model.BidDoneState;
import org.collectiveone.model.BidState;
import org.collectiveone.model.Cbtion;
import org.collectiveone.model.CbtionState;
import org.collectiveone.model.Decision;
import org.collectiveone.model.DecisionRealm;
import org.collectiveone.model.DecisionState;
import org.collectiveone.model.DecisionType;
import org.collectiveone.model.Goal;
import org.collectiveone.model.Project;
import org.collectiveone.model.Review;
import org.collectiveone.model.User;
import org.collectiveone.web.dto.BidDto;
import org.collectiveone.web.dto.BidNewDto;
import org.collectiveone.web.dto.DoneDto;
import org.collectiveone.web.dto.ReviewDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BidServiceImp extends BaseService {
	
	@Transactional
	public void bidSave(Bid bid, Long cbtionId) {
		Cbtion cbtion = cbtionDao.get(cbtionId);
		bid.setCbtion(cbtion);
		cbtion.getBids().add(bid);

		bidDao.save(bid);
		cbtionDao.save(cbtion);

	}

	@Transactional
	public Bid bidGet(Long id) {
		return bidDao.get(id);
	}
	
	@Transactional
	public BidDto bidGetDto(Long id) {
		return bidDao.get(id).toDto();
	}

	@Transactional
	public User bidGetCreator(Long id) {
		return bidDao.get(id).getCreator();
	}

	@Transactional
	public void bidSave(Bid bid) {
		bidDao.save(bid);
	}

	@Transactional
	public void bidSaveState(Long bidId, BidState state) {
		Bid bid = bidDao.get(bidId);
		bid.setState(state);
		bidDao.save(bid);
	}

	@Transactional
	public Long bidCreate(BidNewDto bidDtoIn) throws IOException {

		Long cbtionId = bidDtoIn.getCbtionId();
		User user = userDao.get(bidDtoIn.getCreatorUsername());

		Cbtion cbtion = cbtionDao.get(cbtionId);
		Bid bid = bidDao.getOfCbtionAndUser(cbtionId, user.getId());

		if(bid == null) {
			if(cbtion.getState() == CbtionState.OPEN) {
				Project project = cbtion.getProject();
				projectDao.save(project);
	
				bid = new Bid();
				
				bid.setCreator(userDao.get(user.getId()));
				bid.setCreationDate(new Timestamp(System.currentTimeMillis()));
				bid.setDescription(bidDtoIn.getDescription());
				bid.setCbtion(cbtion);
	
				if(bidDtoIn.getOffer()) {
					bid.setState(BidState.OFFERED);
					bid.setPpoints(bidDtoIn.getPpoints());
				} else {
					bid.setState(BidState.CONSIDERING);
				}
				
				bidDao.save(bid);
	
				cbtion.getBids().add(bid);
				cbtionDao.save(cbtion);
	
				Activity act = new Activity();
				act.setCreationDate(new Timestamp(System.currentTimeMillis()));
				act.setProject(project);
				act.setBid(bid);
				act.setType(ActivityType.BID);
				act.setEvent("created");
				activityService.activitySaveAndNotify(act);
				
				return bid.getId();
			} 
		} else {
			return bid.getId();
		}
		return null;
	}
	
	@Transactional
	public void bidFromConsideringToOffered(BidNewDto bidDto) {
		Bid bid = bidDao.get(bidDto.getId());

		Project project = bid.getCbtion().getProject();

		bid.setDescription(bidDto.getDescription());
		bid.setPpoints(bidDto.getPpoints());
		/* Sum one day as delivery is at the end of the date marked as delivery day*/
		bid.setDeliveryDate(new Timestamp(bidDto.getDeliveryDate() + 24*60*60*1000));
		bid.setState(BidState.OFFERED);

		/* prepare assign decision */
		DecisionRealm realm = decisionRealmDao.getFromGoalId(bid.getCbtion().getGoal().getId());

		Decision assign = new Decision();
		assign.setCreator(userDao.get("collectiveone"));
		assign.setCreationDate(new Timestamp(System.currentTimeMillis()));
		assign.setDescription("assign contribution '"+bid.getCbtion().getTitle()+"' to "+bid.getCreator().getUsername());
		assign.setFromState(BidState.OFFERED.toString());
		assign.setToState(BidState.ASSIGNED.toString());
		assign.setState(DecisionState.IDLE);
		/* TODO: Include bid decisions duration logic */
		assign.setVerdictHours(36);
		assign.setDecisionRealm(realm);
		assign.setProject(project);
		assign.setGoal(bid.getCbtion().getGoal());
		assign.setType(DecisionType.BID);
		assign.setAffectedBid(bid);

		bid.setAssign(assign);

		decisionDao.save(assign);
		bidDao.save(bid);

	}

	@Transactional
	public void bidMarkDone(DoneDto doneDto) throws IOException {
		
		Bid bid = bidDao.get(doneDto.getBidId());
		
		if(bid.getCreator().getUsername().equals(doneDto.getUsername())) {
			if(doneDto.getNewPps() <= bid.getPpoints()) {
				bid.setPpoints(doneDto.getNewPps());
				bid.setDoneState(BidDoneState.DONE);
				bid.setDoneDescription(doneDto.getDescription());
				bid.setDoneDate(new Timestamp(System.currentTimeMillis()));
				bidDao.save(bid);	

				Activity act = new Activity();
				act.setCreationDate(new Timestamp(System.currentTimeMillis()));
				act.setProject(bid.getCbtion().getProject());
				act.setBid(bid);
				act.setType(ActivityType.BID);
				act.setEvent("marked done");
				activityService.activitySaveAndNotify(act);
			}
		}
	}

	@Transactional
	public List<BidDto> bidGetOfUserDto(Long userId) {
		List<Bid> bids = bidDao.getOfUser(userId);
		List<BidDto> bidDtos = new ArrayList<BidDto>();
		for (Bid bid : bids) {
			bidDtos.add(bid.toDto());
		}
		return bidDtos;
	}

	@Transactional
	public List<BidDto> bidGetOfCbtionDto(Long cbtionId) {
		List<Bid> bids = bidDao.getOfCbtion(cbtionId);
		List<BidDto> bidDtos = new ArrayList<BidDto>();
		for (Bid bid : bids) {
			bidDtos.add(bid.toDto());
		}
		return bidDtos;
	}

	@Transactional
	public void bidsUpdateState() throws IOException {
		/* Update state of all not closed bids */
		List<Bid> bidsNotClosed = bidDao.getNotClosed();
		for(Bid bid : bidsNotClosed) {
			bidUpdateState(bid.getId());
		}	
	}

	@Transactional
	public List<Bid> bidGetAll() {
		List<Bid> bids = bidDao.getAll(100000);
		return bids;
	}

	@Transactional
	public List<ReviewDto> bidGetReviewsDtos(Long bidId) {
		List<Review> reviews = bidDao.get(bidId).getReviews();
		List<ReviewDto> reviewsDtos = new ArrayList<ReviewDto>();
		for (Review review : reviews) {
			reviewsDtos.add(review.toDto());
		}
		return reviewsDtos;
	}

	@Transactional
	public void bidUpdateState(Long bidId) throws IOException {
		Bid bid = bidDao.get(bidId);
		Cbtion cbtion = bid.getCbtion();

		bidDao.save(bid);
		cbtionDao.save(cbtion);

		Activity act = new Activity();
		act.setCreationDate(new Timestamp(System.currentTimeMillis()));
		act.setBid(bid);
		act.setType(ActivityType.BID);
		act.setProject(cbtion.getProject());

		switch(bid.getState()) {
		case OFFERED:
			Decision assign = bid.getAssign();
			switch(assign.getState()) {
			case OPEN: 
				break;

			case CLOSED_DENIED : 
				bid.setState(BidState.NOT_ASSIGNED);
				act.setEvent("not assigned");
				activityService.activitySaveAndNotify(act);
				break;

			case CLOSED_ACCEPTED: 
				bid.setState(BidState.ASSIGNED);
				act.setEvent("assigned");
				activityService.activitySaveAndNotify(act);
				cbtion.setState(CbtionState.ASSIGNED);

				/* prepare accept decision */
				Decision accept = new Decision();
				accept.setCreator(userDao.get("collectiveone"));
				accept.setCreationDate(new Timestamp(System.currentTimeMillis()));
				accept.setDescription("accept contribution '"+bid.getCbtion().getTitle()+"' as delivered by "+bid.getCreator().getUsername());
				accept.setFromState(BidState.ASSIGNED.toString());
				accept.setToState(BidState.ACCEPTED.toString());
				accept.setState(DecisionState.IDLE);
				/* TODO: Include bid duration logic */
				accept.setVerdictHours(36);
				accept.setDecisionRealm(decisionRealmDao.getFromGoalId(bid.getCbtion().getGoal().getId()));
				accept.setProject(cbtion.getProject());
				accept.setGoal(cbtion.getGoal());
				accept.setType(DecisionType.BID);
				accept.setAffectedBid(bid);

				bid.setAccept(accept);
				decisionDao.save(accept);

				break;

			default:
				break;
			}
			break;

		case ASSIGNED:
			Decision accept = bid.getAccept();
			if(accept != null) {
				switch(accept.getState()) {
				case OPEN: 
					break;

				case CLOSED_DENIED: 
					bid.setState(BidState.NOT_ACCEPTED);
					act.setEvent("not accepted");
					activityService.activitySaveAndNotify(act);
					break;

				case CLOSED_ACCEPTED: 
					/* once a bid is accepted, the cbtion and all other bids on it
					 * are closed */
					
					/* if under detached goal, check there is enough pps in the 
					 * budget and if so, reduce the budget accordingly */
					Goal applicableGoal = goalDao.getClosestDetached(cbtion.getGoal().getId());
					
					boolean goWithAssignment = false;
					if(applicableGoal == null) {
						/* all goals are attached so pps can be created out of thin air */
						goWithAssignment = true;
					} else {
						if(applicableGoal.getCurrentBudget() >= bid.getPpoints()) {
							applicableGoal.setCurrentBudget(applicableGoal.getCurrentBudget() - bid.getPpoints());
							goalDao.save(applicableGoal);
							goWithAssignment = true;
						} else {
							/* not enough pps in the budget */
							goWithAssignment = false;
						}
					}
						
					if(goWithAssignment) {
						/* only mark the bid as accepted when there is enough budget */
						bid.setState(BidState.ACCEPTED); 
						
						/* update cbtion state */
						cbtion.setAssignedPpoints(bid.getPpoints());
						cbtion.setContributor(bid.getCreator());
						cbtion.setState(CbtionState.ACCEPTED);
	
						/* close all other bids and decisions */
						for(Bid otherBid : cbtion.getBids()) {
							if(otherBid.getId() != bid.getId()) {
								if(otherBid.getAssign() != null) otherBid.getAssign().setState(DecisionState.CLOSED_EXTERNALLY);
								if(otherBid.getAccept() != null) otherBid.getAccept().setState(DecisionState.CLOSED_EXTERNALLY);
								otherBid.setState(BidState.SUPERSEEDED);
								bidDao.save(otherBid);
							}
						}
	
						/* -----------------------------------------------------------------------*/ 
						/* updated pps of user in project - This is the only place where PPS are created/updated
						 * and here all the decisions weights update logic is called */
						/* -----------------------------------------------------------------------*/
						contributorService.contributorUpdate(bid.getCbtion().getProject().getId(), bid.getCreator().getId(), cbtion.getAssignedPpoints());
						projectService.projectUpdatePpsTot(bid.getCbtion().getProject().getId(), 0.0);
						voterService.updateVoterInProject(bid.getCbtion().getProject().getId(), bid.getCreator().getId());
						/* -----------------------------------------------------------------------*/
	
						act.setEvent("accepted");
						activityService.activitySaveAndNotify(act);
					}

					break;

				default:
					break;
				}
			}
			break;

		default:
			break;

		}
	}

}
