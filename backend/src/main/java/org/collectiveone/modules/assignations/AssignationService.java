package org.collectiveone.modules.assignations;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.activity.ActivityService;
import org.collectiveone.modules.assignations.evaluationlogic.PeerReviewedAssignation;
import org.collectiveone.modules.assignations.evaluationlogic.PeerReviewedAssignationState;
import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.initiatives.InitiativeDto;
import org.collectiveone.modules.initiatives.InitiativeRepositoryIf;
import org.collectiveone.modules.initiatives.InitiativeService;
import org.collectiveone.modules.tokens.MemberTransfer;
import org.collectiveone.modules.tokens.MemberTransferRepositoryIf;
import org.collectiveone.modules.tokens.TokenService;
import org.collectiveone.modules.tokens.TokenTransferService;
import org.collectiveone.modules.tokens.TokenType;
import org.collectiveone.modules.users.AppUserRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AssignationService {
	
	private long DAYS_TO_MS = 24L*60L*60L*1000L;
	
	@Autowired
	private ActivityService activityService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private InitiativeService initiativeService;
	
	@Autowired
	private TokenTransferService tokenTransferService;
	
	
	@Autowired
	private AppUserRepositoryIf appUserRepository;
	
	@Autowired
	private InitiativeRepositoryIf initiativeRepository;
	
	@Autowired
	private AssignationRepositoryIf assignationRepository;
	
	@Autowired
	private ReceiverRepositoryIf receiverRepository;
	
	@Autowired
	private EvaluatorRepositoryIf evaluatorRepository;
	
	@Autowired
	private EvaluationGradeRepositoryIf evaluationGradeRepository;
	
	@Autowired
	private BillRepositoryIf billRepository;
	
	@Autowired
	private AssignationConfigRepositoryIf assignationConfigRepository;
	
	@Autowired
	private MemberTransferRepositoryIf memberTransferRepository;
	
	
		
	public PostResult createAssignation(UUID initiativeId, AssignationDto assignationDto, UUID creatorId) {
		Initiative initiative = initiativeRepository.findById(initiativeId);
	
		Assignation assignation = new Assignation();
		
		assignation.setType(AssignationType.valueOf(assignationDto.getType()));
		assignation.setMotive(assignationDto.getMotive());
		assignation.setNotes(assignationDto.getNotes());
		assignation.setInitiative(initiative);
		assignation.setState(AssignationState.OPEN);
		assignation.setCreator(appUserRepository.findByC1Id(creatorId));
		assignation.setCreationDate(new Timestamp(System.currentTimeMillis()));
		
		AssignationConfig config = new AssignationConfig();
		config.setSelfBiasVisible(Boolean.valueOf(assignationDto.getConfig().getSelfBiasVisible()));
		config.setEvaluationsVisible(Boolean.valueOf(assignationDto.getConfig().getEvaluationsVisible()));
		config.setMinClosureDate(new Timestamp(System.currentTimeMillis()));
		config.setMaxClosureDate(new Timestamp(System.currentTimeMillis() + assignationDto.getConfig().getMaxDuration()*DAYS_TO_MS));
		
		config = assignationConfigRepository.save(config);
		
		assignation.setConfig(config);
		assignation = assignationRepository.save(assignation);
		
		for(ReceiverDto receiverDto : assignationDto.getReceivers()) {
			Receiver receiver = new Receiver();
			
			receiver.setUser(appUserRepository.findByC1Id(UUID.fromString(receiverDto.getUser().getC1Id())));
			receiver.setAssignation(assignation);
			receiver.setAssignedPercent(receiverDto.getPercent());
			receiver.setState(ReceiverState.PENDING);
			
			if (receiverDto.getIsDonor()) {
				receiver.setType(ReceiverType.DONOR);	
			} else {
				receiver.setType(ReceiverType.NORMAL);
			}
						
			receiver = receiverRepository.save(receiver);
			
			assignation.getReceivers().add(receiver);
		}
		
		for(BillDto billDto : assignationDto.getAssets()) {
			TokenType tokenType = tokenService.getTokenType(UUID.fromString(billDto.getAssetId()));
			Bill bill = new Bill();
			
			bill.setAssignation(assignation);
			bill.setTokenType(tokenType);
			bill.setValue(billDto.getValue());
			bill = billRepository.save(bill);
			
			assignation.getBills().add(bill);
		}
		
		switch (assignation.getType()) {
		
		case DIRECT:
			for(Bill bill : assignation.getBills()) {
				for(Receiver receiver : assignation.getReceivers()) {
					double value = bill.getValue() * receiver.getAssignedPercent() / 100.0;
					PostResult result = tokenTransferService.transferFromInitiativeToUser(assignation.getInitiative().getId(), receiver.getUser().getC1Id(), bill.getTokenType().getId(), value);
					
					if (result.getResult().equals("success")) {
						receiver.setState(ReceiverState.RECEIVED);
						receiver.setTransfer(memberTransferRepository.findById(UUID.fromString(result.getElementId())));
						receiverRepository.save(receiver);
					}
				}
			}
			assignation.setState(AssignationState.DONE);
			assignationRepository.save(assignation);
			
			activityService.directAssignationCreated(assignation, appUserRepository.findByC1Id(creatorId));
			
			break;
		
		case PEER_REVIEWED: 
			for(EvaluatorDto evaluatorDto : assignationDto.getEvaluators()) {
				Evaluator evaluator = new Evaluator();
				
				evaluator.setUser(appUserRepository.findByC1Id(UUID.fromString(evaluatorDto.getUser().getC1Id())));
				evaluator.setAssignation(assignation);
				evaluator.setState(EvaluatorState.PENDING);
				/* Weight logic TBD */
				evaluator.setWeight(1.0);
				evaluator = evaluatorRepository.save(evaluator);
				
				/* Create the grades of all evaluators already */
				for(Receiver receiver : assignation.getReceivers()) {
					EvaluationGrade grade = new EvaluationGrade();
					
					grade.setAssignation(assignation);
					grade.setEvaluator(evaluator);
					grade.setReceiver(receiver);
					grade.setPercent(0.0);
					grade.setType(EvaluationGradeType.SET);
					grade.setState(EvaluationGradeState.PENDING);
					grade = evaluationGradeRepository.save(grade);
					
					evaluator.getGrades().add(grade);
				}
				
				assignation.getEvaluators().add(evaluator);
			}
			
			activityService.peerReviewedAssignationCreated(assignation, appUserRepository.findByC1Id(creatorId));
			
			break;
		}
		
		return new PostResult("success", "success", "");
	}
	
	@Transactional
	private PostResult evaluateAssignation(UUID evaluatorUserId, UUID assignationId, EvaluationDto evaluationsDto) {
		
		Assignation assignation = assignationRepository.findById(assignationId);
		Evaluator evaluator = evaluatorRepository.findByAssignationIdAndUser_C1Id(assignation.getId(), evaluatorUserId);
		
		if (evaluator != null) {
			for(EvaluationGradeDto evaluationGradeDto : evaluationsDto.getEvaluationGrades()) {
				UUID receiverUserId = UUID.fromString(evaluationGradeDto.getReceiverUser().getC1Id());
				EvaluationGrade grade = evaluationGradeRepository.findByAssignation_IdAndReceiver_User_C1IdAndEvaluator_User_C1Id(assignation.getId(), receiverUserId, evaluatorUserId);
						
				grade.setPercent(evaluationGradeDto.getPercent());
				grade.setType(EvaluationGradeType.valueOf(evaluationGradeDto.getType()));
				grade.setState(EvaluationGradeState.DONE);
				
				evaluationGradeRepository.save(grade);			
			}
			
			evaluator.setState(EvaluatorState.DONE);
			evaluatorRepository.save(evaluator);
			
			return new PostResult("success", "evaluation saved", evaluator.getId().toString());
		} else {
			return new PostResult("error", "evaluator not found", "");
		}
		
	}

	@Transactional
	public void updateAssignationState(UUID assignationId) {
		
		Assignation assignation = assignationRepository.findById(assignationId);
		
		if (assignation.getType() == AssignationType.PEER_REVIEWED) {
			PeerReviewedAssignation peerReviewedAssignation = new PeerReviewedAssignation();
			peerReviewedAssignation.setAssignation(assignation);
			peerReviewedAssignation.updateState();
			
			if(peerReviewedAssignation.getState() == PeerReviewedAssignationState.CLOSED) {
				/* percents already updated by peerReviewAssignation,
				 * so just change state, transfer funds and save */
				
				/* transfer the assets to the receivers */
				/* normalize once again just to be sure...*/
				
				boolean valid = true;
				double sumOfPercents = 0.0;
				for(Receiver receiver : assignation.getReceivers()) {
					sumOfPercents += receiver.getAssignedPercent();
					if (Double.isNaN(receiver.getAssignedPercent())) {
						valid = false;
					}
				}
				
				if (sumOfPercents == 0) {
					valid = false;
				}
				
				if (Math.abs(100.0 - sumOfPercents) >= 1E-6) {
					/* must sum 100 */
					valid = false;
				}
				
				if (valid) {
					boolean errorSending = false;
					for(Bill bill : assignation.getBills()) {
						for(Receiver receiver : assignation.getReceivers()) {
							double value = bill.getValue() * receiver.getAssignedPercent() / 100.0;
							PostResult result = tokenTransferService.transferFromInitiativeToUser(assignation.getInitiative().getId(), receiver.getUser().getC1Id(), bill.getTokenType().getId(), value);
							
							if (result.getResult().equals("success")) {
								receiver.setState(ReceiverState.RECEIVED);
								receiver.setTransfer(memberTransferRepository.findById(UUID.fromString(result.getElementId())));
								receiverRepository.save(receiver);
								
							} else {
								errorSending = true;
							}
						}
					}
					
					if (!errorSending) {
						assignation.setState(AssignationState.DONE);
						activityService.peerReviewedAssignationDone(assignation);
					} else {
						assignation.setState(AssignationState.ERROR);
					}
					
					
				} else {
					assignation.setState(AssignationState.ERROR);
				}
				
				assignationRepository.save(assignation);
			}
		}
	}
	
	@Transactional
	public AssignationDto getPeerReviewedAssignation(UUID initiativeId, UUID assignationId, UUID evaluatorAppUserId, Boolean addAllEvaluations) {
		Assignation assignation = assignationRepository.findById(assignationId);
		AssignationDto assignationDto = assignation.toDto();
		
		/* add the evaluations of logged user */
		Evaluator evaluator = evaluatorRepository.findByAssignationIdAndUser_C1Id(assignation.getId(), evaluatorAppUserId);
		
		assignationDto.setEvaluationsPending(evaluatorRepository.countByAssignationIdAndState(assignation.getId(), EvaluatorState.PENDING));
		
		if (evaluator != null) {
			EvaluationDto evaluation = new EvaluationDto();
			evaluation.setId(evaluator.getId().toString());
			evaluation.setEvaluationState(evaluator.getState().toString());
			
			for (EvaluationGrade grade : evaluator.getGrades()) {
				evaluation.getEvaluationGrades().add(grade.toDto());
			}
			
			assignationDto.setThisEvaluation(evaluation);
		}
		
		/* add individual biases */
		if (assignation.getConfig().getSelfBiasVisible()) {
			if (assignation.getState() == AssignationState.DONE) {
				for (Receiver receiver : assignation.getReceivers()) {
					EvaluationGrade selfEvaluation = 
							evaluationGradeRepository.findByAssignation_IdAndReceiver_User_C1IdAndEvaluator_User_C1Id(
									assignation.getId(), receiver.getUser().getC1Id(), receiver.getUser().getC1Id());
					
					if (selfEvaluation != null) {
						/* fill the receivers dtos selfBias */
						for (ReceiverDto receiverDto : assignationDto.getReceivers()) {
							if (receiverDto.getUser().getC1Id().equals(receiver.getUser().getC1Id().toString())) {
								if (selfEvaluation.getType() == EvaluationGradeType.SET) {
									receiverDto.setSelfBias(selfEvaluation.getPercent() - receiverDto.getEvaluatedPercent());
								} else {
									receiverDto.setSelfBias(999.9);
								}
							}
						}
					}
				}
			}
		}
		
		/* add all the evaluations */
		if (addAllEvaluations) {
			if (assignation.getConfig().getEvaluationsVisible()) {
				if (evaluator != null) {
					if (assignation.getState() == AssignationState.DONE) {
						for (Evaluator thisEvaluator : assignation.getEvaluators()) {
							assignationDto.getEvaluators().add(thisEvaluator.toDto());
						}	
					}
				}
			}
		}
		
		return assignationDto;
	}
	
	@Transactional
	public GetResult<InitiativeAssignationsDto> getAssignationsResult(UUID initiativeId, UUID evaluatorAppUserId) {
		return new GetResult<InitiativeAssignationsDto>("success", "success", getAssignations(initiativeId, evaluatorAppUserId));
	}
	
	@Transactional
	public List<Assignation> getOpenAssignations(UUID initiativeId) {
		return  assignationRepository.findByInitiativeIdAndState(initiativeId, AssignationState.OPEN);
	}
	
	@Transactional
	public UUID getInitiativeIdOf(UUID assignationId) {
		Assignation assignation = assignationRepository.findById(assignationId);
		if (assignation != null) {
			return assignation.getInitiative().getId();
		} else {
			return null;
		}
	}
	
	@Transactional
	public GetResult<AssignationDto> getAssignationDto(UUID assignationId, UUID userId, Boolean addAllEvaluations) {
		
		Assignation assignation = assignationRepository.findById(assignationId);
		AssignationDto assignationDto = null;
		
		if(assignation.getType() == AssignationType.PEER_REVIEWED) {
			assignationDto = getPeerReviewedAssignation(assignation.getInitiative().getId(), assignation.getId(), userId, addAllEvaluations);
		} else {
			assignationDto = assignation.toDto();
		}
		
		return new GetResult<AssignationDto>("success", "assignation retreived", assignationDto);
	}
	
	@Transactional
	public InitiativeAssignationsDto getAssignations(UUID initiativeId, UUID evaluatorAppUserId) {
		Initiative initiative = initiativeRepository.findById(initiativeId);
		List<Assignation> assignations = assignationRepository.findByInitiativeId(initiativeId);
		
		InitiativeAssignationsDto initiativeAssignations = new InitiativeAssignationsDto();
		initiativeAssignations.setInitiativeId(initiative.getId().toString());
		initiativeAssignations.setInitiativeName(initiative.getMeta().getName());
		
		for(Assignation assignation : assignations) {
			initiativeAssignations.getAssignations().add(getAssignationDto(assignation.getId(), evaluatorAppUserId, false).getData());
		}
		
		/* add the members of all sub-initiatives too */
		for (InitiativeDto subInitiative : initiativeService.getSubinitiativesTree(initiative.getId(), null)) {
			/* recursively call with subinitiatives */
			initiativeAssignations.getSubinitiativesAssignations().add(getAssignations(UUID.fromString(subInitiative.getId()), evaluatorAppUserId));
		}
		
		return initiativeAssignations;
	}
	
	@Transactional
	public PostResult revertAssignation(UUID assignationId, UUID userId) {
		Assignation assignation = assignationRepository.findById(assignationId);
		
		if(assignation.getState() == AssignationState.DONE) {
			for (Receiver receiver : assignation.getReceivers()) {
				/* reset approvals */
				receiver.setRevertApproval(false);
				receiverRepository.save(receiver);
			}
			
			assignation.setState(AssignationState.REVERT_ORDERED);
			assignationRepository.save(assignation);
			
			activityService.assignationRevertOrdered(assignation, appUserRepository.findByC1Id(userId));
			
			return new PostResult("success", "revert of assignation ordered", "");
		} else {
			return new PostResult("error", "assignation not done, cannot be reverted", "");
		}
	}
	
	@Transactional
	public PostResult approveRevertAssignation(UUID userId, UUID assignationId, Boolean value) {
		Assignation assignation = assignationRepository.findById(assignationId);
		
		if(assignation.getState() == AssignationState.REVERT_ORDERED) {
			Receiver receiver = receiverRepository.findByAssignation_IdAndUser_C1Id(assignationId, userId);
			
			if (receiver != null) {
				if (value) {
					receiver.setRevertApproval(true);
					receiverRepository.save(receiver);
				} else {
					/* a single receiver that rejects will cancel the revert */
					assignation.setState(AssignationState.DONE);
					activityService.assignationRevertCancelled(assignation);
				}
			}
			
			return new PostResult("success", "revert of assignation approved by this receiver", "");
		} else {
			return new PostResult("error", "assignation not in revert, cannot be approved", "");
		}
	}
	
	public void checkRevertStatus(UUID assignationId) {
		Assignation assignation = assignationRepository.findById(assignationId);
		
		if(assignation.getState() == AssignationState.REVERT_ORDERED) {
			boolean missingApprovals = false;
			for (Receiver receiver : assignation.getReceivers()) {
				if (!receiver.getRevertApproval()) {
					missingApprovals = true;
				} 
			}
			
			if (!missingApprovals) {
				for (Receiver receiver : assignation.getReceivers()) {
					MemberTransfer transfer = receiver.getTransfer();
					tokenTransferService.revertTransferFromInitiativeToUser(transfer.getId());
				}
				
				assignation.setState(AssignationState.REVERTED);
				assignationRepository.save(assignation);
				
				activityService.assignationReverted(assignation);
			}
		}
	}
	
	
	@Transactional
	public PostResult deleteAssignation(UUID assignationId, UUID userId) {
		Assignation assignation = assignationRepository.findById(assignationId);
		
		if(assignation.getState() == AssignationState.OPEN) {
			assignation.setState(AssignationState.DELETED);
			assignationRepository.save(assignation);
			
			activityService.assignationDeleted(assignation, appUserRepository.findByC1Id(userId));
			
			return new PostResult("success", "assignation deleted", "");
		} else {
			return new PostResult("error", "assignation not open, cannot be deleted", "");
		}
	}
	
	
	/** Non-transactional to evaluate and update assignation state in different transactions */
	public PostResult evaluateAndUpdateAssignation(UUID evaluatorAppUserId, UUID assignationId, EvaluationDto evaluationDto) {
		PostResult result = evaluateAssignation(evaluatorAppUserId, assignationId, evaluationDto);
		updateAssignationState(assignationId);
		
		return result;
	}
	
	@Scheduled(fixedDelay = 3600000)
	@Transactional
	public void periodicCheckToCloseOpenAssignations() {
		
		List<Assignation> assignations = assignationRepository.findByTypeAndState(AssignationType.PEER_REVIEWED, AssignationState.OPEN);
		
		for (Assignation assignation : assignations) {
			updateAssignationState(assignation.getId());
		}
	}
}
