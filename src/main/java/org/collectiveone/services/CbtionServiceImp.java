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
import org.collectiveone.model.User;
import org.collectiveone.web.controllers.rest.ResStatus;
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
	public void cbtionSave(Cbtion cbtion) {
		cbtionDao.save(cbtion);
	}
	
	@Transactional
	public void cbtionUpdate(Cbtion cbtion) {
		cbtionDao.update(cbtion);
	}

	@Transactional
	public Long cbtionCreate(CbtionDto cbtionDto) throws IOException {
		Cbtion cbtion = new Cbtion();
		Project project = projectDao.get(cbtionDto.getProjectName());
		projectDao.save(project);
		Goal goal = goalDao.get(cbtionDto.getGoalTag(), project.getName());

		cbtion.setCreationDate(new Timestamp(System.currentTimeMillis()));
		cbtion.setCreator(userDao.get(cbtionDto.getCreatorUsername()));
		cbtion.setDescription(cbtionDto.getDescription());
		cbtion.setProject(project);
		cbtion.setProduct(cbtionDto.getProduct());
		cbtion.setSuggestedBid(cbtionDto.getSuggestedBid());
		cbtion.setState(CbtionState.PROPOSED);
		cbtion.setTitle(cbtionDto.getTitle());
		cbtion.setGoal(goal);

		Long id = cbtionDao.save(cbtion);

		DecisionRealm realm = decisionRealmDao.getFromGoalId(goal.getId());
		decisionRealmDao.save(realm);

		Decision open = new Decision();

		open.setCreator(userDao.get("collectiveone"));
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
		decisionDao.save(open);

		Activity act = new Activity("created", 
				new Timestamp(System.currentTimeMillis()),
				cbtion.getProject());

		act.setCbtion(cbtion);
		act.setType(ActivityType.CBTION);
		activityService.activitySaveAndNotify(act);

		return id;
	}
	
	@Transactional
	public Long cbtionEdit(CbtionDto cbtionDto) {
		Cbtion cbtion = cbtionDao.get(cbtionDto.getId());
		
		if(cbtion != null) {
			switch(cbtion.getState()) {
			case PROPOSED:
				cbtion.setTitle(cbtionDto.getTitle());
				cbtion.setDescription(cbtionDto.getDescription());
				cbtion.setProduct(cbtionDto.getProduct());
				cbtion.setSuggestedBid(cbtionDto.getSuggestedBid());
				cbtionDao.save(cbtion);
				
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
	public Cbtion cbtionGet(Long id) {
		return cbtionDao.get(id);
	}

	@Transactional
	public List<Cbtion> cbtionGet(Cbtion refCbtion) {
		return cbtionDao.get(refCbtion);
	}
	
	@Transactional
	public User cbtionGetCreator(Long id) {
		return cbtionDao.get(id).getCreator();
	}

	@Transactional
	public CbtionDto cbtionGetDto(Long cbtionId) {
		Cbtion cbtion = cbtionDao.get(cbtionId);
		CbtionDto cbtionDto = cbtion.toDto(goalService.goalGetParentGoalsTags(cbtion.getGoal()), cbtionGetNSubComments(cbtionId));
		return cbtionDto;
	}

	@Transactional
	public List<CbtionDto> cbtionGetDto(Cbtion refCbtion) {
		List<Cbtion> cbtions = cbtionDao.get(refCbtion);
		List<CbtionDto> cbtionDtos = new ArrayList<CbtionDto>();
		for(Cbtion cbtion : cbtions) {
			cbtionDtos.add(cbtion.toDto());
		}
		return cbtionDtos;
	}

	@Transactional
	public CbtionDtoListRes cbtionDtoGetFiltered(Filters filters) {
		ObjectListRes<Cbtion> cbtionsRes = cbtionDao.get(filters);

		CbtionDtoListRes cbtionsDtosRes = new CbtionDtoListRes();

		cbtionsDtosRes.setResSet(cbtionsRes.getResSet());
		cbtionsDtosRes.setCbtionsDtos(new ArrayList<CbtionDto>());

		for(Cbtion cbtion : cbtionsRes.getObjects()) {
			cbtionsDtosRes.getCbtionsDtos().add(cbtion.toDto(goalService.goalGetParentGoalsTags(cbtion.getGoal())));
		}

		return cbtionsDtosRes;
	}

	@Transactional
	public void cbtionsUpdateState() throws IOException {
		/* Update state of all not closed bids */
		List<Cbtion> cbtionsProposed = cbtionDao.getWithStates(Arrays.asList(CbtionState.PROPOSED, CbtionState.OPEN));
		for(Cbtion cbtion : cbtionsProposed) {
			cbtionUpdateState(cbtion.getId());
		}	
	}

	@Transactional
	public void cbtionUpdateState(Long cbtionId) throws IOException {
		Cbtion cbtion = cbtionDao.get(cbtionId);
		
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
						cbtionDao.save(cbtion);
	
						act.setEvent("proposal refused");
						activityService.activitySaveAndNotify(act);
						break;
		
					case CLOSED_ACCEPTED: 
						cbtion.setState(CbtionState.OPEN);
						
						DecisionRealm realm = decisionRealmDao.getFromGoalId(cbtion.getGoal().getId());
						decisionRealmDao.save(realm);
		
						Decision delete = new Decision();
		
						delete.setCreator(userDao.get("collectiveone"));
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
						
						decisionDao.save(delete);
						
						cbtion.setDeleteDec(delete);
						cbtionDao.save(cbtion);
	
						act.setEvent("opened for bidding");
						activityService.activitySaveAndNotify(act);
						
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
						cbtionDao.remove(cbtion.getId());
						
						act.setEvent("deleted");
						activityService.activitySaveAndNotify(act);
						
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
	public ResStatus cbtionPromote(Long cbtionId, Long userId, boolean promoteUp) {
		ResStatus resStatus = new ResStatus();

		Cbtion cbtion = cbtionDao.get(cbtionId);
		cbtionDao.save(cbtion);

		Promoter promoter = promoterDao.getOfCbtion(cbtionId, userId);

		/* countPromotersDiff will doesn't include the current transaction so this logic 
		 * is used to solve the problem*/
		int currentRelevance = cbtionDao.countPromotersDiff(cbtionId);
		int newRelevance = currentRelevance;
		
		if(promoter == null) {
			promoter = new Promoter();
			promoter.setUser(userDao.get(userId));
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

		return resStatus;		
	}
	
	@Transactional
	public CommentDto cbtionGetCommentDto(Long commentId) {
		return commentDao.get(commentId).toDto();
	}
	
	@Transactional
	public int cbtionGetNSubComments(Long cbtionId) {
		return cbtionDao.countSubComments(cbtionId);
	}

	@Transactional
	public List<CommentDto> cbtionGetCommentsDtos(Long cbtionId) {
		List<Comment> comments = cbtionDao.getCommentsSorted(cbtionId);
		List<CommentDto> commentsDtos = new ArrayList<CommentDto>();
		for (Comment comment : comments) {
			commentsDtos.add(comment.toDto());
		}
		return commentsDtos;
	}

	@Transactional
	public List<ReviewDto> cbtionGetReviewsDtos(Long cbtionId) {
		Bid bid = cbtionDao.getAcceptedBid(cbtionId);
		return bidService.bidGetReviewsDtos(bid.getId());
	}

}
