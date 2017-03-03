package org.collectiveone.services;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.collectiveone.model.Activity;
import org.collectiveone.model.ActivityType;
import org.collectiveone.model.Bid;
import org.collectiveone.model.Cbtion;
import org.collectiveone.model.CbtionState;
import org.collectiveone.model.Comment;
import org.collectiveone.model.Decision;
import org.collectiveone.model.DecisionRealm;
import org.collectiveone.model.DecisionState;
import org.collectiveone.model.DecisionType;
import org.collectiveone.model.Goal;
import org.collectiveone.model.Project;
import org.collectiveone.model.Promoter;
import org.collectiveone.web.dto.CbtionDto;
import org.collectiveone.web.dto.CbtionDtoListRes;
import org.collectiveone.web.dto.CommentDto;
import org.collectiveone.web.dto.Filters;
import org.collectiveone.web.dto.ObjectListRes;
import org.collectiveone.web.dto.ReviewDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CbtionServiceImp extends BaseService {

	@Transactional
	public Long create(CbtionDto cbtionDto) throws IOException {
		Cbtion cbtion = new Cbtion();
		Project project = projectRepository.get(cbtionDto.getProjectName());
		projectRepository.save(project);
		Goal goal = goalRepository.get(cbtionDto.getGoalTag(), project.getName());

		cbtion.setCreationDate(new Timestamp(System.currentTimeMillis()));
		cbtion.setCreator(userRepository.get(cbtionDto.getCreatorUsername()));
		cbtion.setDescription(cbtionDto.getDescription());
		cbtion.setProject(project);
		cbtion.setProduct(cbtionDto.getProduct());
		cbtion.setSuggestedBid(cbtionDto.getSuggestedBid());
		cbtion.setState(CbtionState.PROPOSED);
		cbtion.setTitle(cbtionDto.getTitle());
		cbtion.setGoal(goal);

		Long id = cbtionRepository.save(cbtion);

		DecisionRealm realm = decisionRealmRepository.getFromGoalId(goal.getId());
		decisionRealmRepository.save(realm);

		Decision open = new Decision();

		open.setCreator(userRepository.get("collectiveone"));
		open.setCreationDate(new Timestamp(System.currentTimeMillis()));
		open.setDecisionRealm(realm);
		open.setDescription("open contribution '"+cbtion.getTitle()+"'");
		open.setFromState(CbtionState.PROPOSED.toString());		
		open.setToState(CbtionState.OPEN.toString());
		open.setProject(project);
		open.setGoal(goal);
		open.setState(DecisionState.IDLE);
		open.setVerdictHours(36);
		open.setType(DecisionType.CBTION);
		open.setAffectedCbtion(cbtion);

		cbtion.setOpenDec(open);
		decisionRepository.save(open);

		Activity act = new Activity("created", 
				new Timestamp(System.currentTimeMillis()),
				cbtion.getProject());

		act.setCbtion(cbtion);
		act.setType(ActivityType.CBTION);
		activityService.saveAndNotify(act);

		return id;
	}
	
	@Transactional
	public Long edit(CbtionDto cbtionDto) {
		Cbtion cbtion = cbtionRepository.get(cbtionDto.getId());
		
		if(cbtion != null) {
			switch(cbtion.getState()) {
			case PROPOSED:
				cbtion.setTitle(cbtionDto.getTitle());
				cbtion.setDescription(cbtionDto.getDescription());
				cbtion.setProduct(cbtionDto.getProduct());
				cbtion.setSuggestedBid(cbtionDto.getSuggestedBid());
				cbtionRepository.save(cbtion);
				
				break;
			
			default:
				break;
			}
			

			return cbtion.getId();
			
		} else {
			return (long) 0;
		}
		
	}

	@Transactional
	public CbtionDto getDto(Long cbtionId) {
		Cbtion cbtion = cbtionRepository.get(cbtionId);
		CbtionDto cbtionDto = cbtion.toDto(goalService.getParentGoalsTags(cbtion.getGoal()), getNSubComments(cbtionId));
		return cbtionDto;
	}

	@Transactional
	public CbtionDtoListRes getFilteredDto(Filters filters) {
		ObjectListRes<Cbtion> cbtionsRes = cbtionRepository.get(filters);

		CbtionDtoListRes cbtionsDtosRes = new CbtionDtoListRes();

		cbtionsDtosRes.setResSet(cbtionsRes.getResSet());
		cbtionsDtosRes.setCbtionsDtos(new ArrayList<CbtionDto>());

		for(Cbtion cbtion : cbtionsRes.getObjects()) {
			cbtionsDtosRes.getCbtionsDtos().add(cbtion.toDto(goalService.getParentGoalsTags(cbtion.getGoal())));
		}

		return cbtionsDtosRes;
	}

	@Transactional 
	void updateStateAll() throws IOException {
		/* Update state of all not closed bids */
		List<Cbtion> cbtionsProposed = cbtionRepository.getWithStates(Arrays.asList(CbtionState.PROPOSED, CbtionState.OPEN));
		for(Cbtion cbtion : cbtionsProposed) {
			updateState(cbtion.getId());
		}	
	}

	@Transactional
	private void updateState(Long cbtionId) throws IOException {
		Cbtion cbtion = cbtionRepository.get(cbtionId);
		
		Activity act = new Activity();
		act.setProject(cbtion.getProject());
		act.setCreationDate(new Timestamp(System.currentTimeMillis()));
		act.setType(ActivityType.CBTION);
		act.setCbtion(cbtion);


		switch(cbtion.getState()) {
		
		case PROPOSED:
			Decision open = cbtion.getOpenDec();
			
			if(open != null) {
				switch(open.getState()){
				
					case OPEN: 
						break;
		
					case CLOSED_DENIED :
						cbtion.setState(CbtionState.NOTOPENED);
						cbtionRepository.save(cbtion);
	
						act.setEvent("proposal refused");
						activityService.saveAndNotify(act);
						break;
		
					case CLOSED_ACCEPTED: 
						cbtion.setState(CbtionState.OPEN);
						
						DecisionRealm realm = decisionRealmRepository.getFromGoalId(cbtion.getGoal().getId());
						decisionRealmRepository.save(realm);
		
						Decision delete = new Decision();
		
						delete.setCreator(userRepository.get("collectiveone"));
						delete.setCreationDate(new Timestamp(System.currentTimeMillis()));
						delete.setDecisionRealm(realm);
						delete.setDescription("delete contribution '"+cbtion.getTitle()+"'");
						delete.setFromState(CbtionState.OPEN.toString());		
						delete.setToState(CbtionState.DELETED.toString());
						delete.setProject(cbtion.getProject());
						delete.setGoal(cbtion.getGoal());
						delete.setState(DecisionState.IDLE);
						delete.setVerdictHours(36);
						delete.setType(DecisionType.CBTION);
						delete.setAffectedCbtion(cbtion);
						
						decisionRepository.save(delete);
						
						cbtion.setDeleteDec(delete);
						cbtionRepository.save(cbtion);
	
						act.setEvent("opened for bidding");
						activityService.saveAndNotify(act);
						
						break;
		
					default:
						break;
				}	
			}
			
			break;
			
		case OPEN:
			Decision delete = cbtion.getDeleteDec();
			
			if(delete != null) {
				switch(delete.getState()) {
					case OPEN: 
						break;
						
					case CLOSED_DENIED:
						break;
						
					case CLOSED_ACCEPTED:
						cbtionRepository.remove(cbtion.getId());
						
						act.setEvent("deleted");
						activityService.saveAndNotify(act);
						
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


	@Transactional
	public void promote(Long cbtionId, Long userId, boolean promoteUp) {
		Cbtion cbtion = cbtionRepository.get(cbtionId);
		cbtionRepository.save(cbtion);

		Promoter promoter = promoterDao.getOfCbtion(cbtionId, userId);

		/* countPromotersDiff will doesn't include the current transaction so this logic 
		 * is used to solve the problem*/
		int currentRelevance = cbtionRepository.countPromotersDiff(cbtionId);
		int newRelevance = currentRelevance;
		
		if(promoter == null) {
			promoter = new Promoter();
			promoter.setUser(userRepository.get(userId));
			cbtion.getPromoters().add(promoter);
			if(promoteUp) {
				newRelevance = newRelevance + 1;
			} else {
				newRelevance = newRelevance - 1;
			}
		} else {
			if(promoter.getPromoteUp() !=  promoteUp) {
				if(promoteUp) {
					newRelevance = newRelevance + 2;
				} else {
					newRelevance = newRelevance - 2;
				}
			}
		}

		promoter.setCreationDate(new Timestamp(System.currentTimeMillis()));
		promoter.setPromoteUp(promoteUp);
		promoterDao.save(promoter);
		
		cbtion.setRelevance(newRelevance);
	}
	
	@Transactional
	public CommentDto getCommentDto(Long commentId) {
		return commentDao.get(commentId).toDto();
	}
	
	@Transactional
	private int getNSubComments(Long cbtionId) {
		return cbtionRepository.countSubComments(cbtionId);
	}

	@Transactional
	public List<CommentDto> getCommentsDtos(Long cbtionId) {
		List<Comment> comments = cbtionRepository.getCommentsSorted(cbtionId);
		List<CommentDto> commentsDtos = new ArrayList<CommentDto>();
		for (Comment comment : comments) {
			commentsDtos.add(comment.toDto());
		}
		return commentsDtos;
	}

	@Transactional
	public List<ReviewDto> getReviewsDtos(Long cbtionId) {
		Bid bid = cbtionRepository.getAcceptedBid(cbtionId);
		return bidService.getReviewsDtos(bid.getId());
	}

}
