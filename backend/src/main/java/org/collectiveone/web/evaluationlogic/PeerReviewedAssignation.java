package org.collectiveone.web.evaluationlogic;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.collectiveone.model.basic.Assignation;
import org.collectiveone.model.enums.EvaluationGradeType;
import org.collectiveone.model.enums.EvaluatorState;
import org.collectiveone.model.support.EvaluationGrade;
import org.collectiveone.model.support.Evaluator;
import org.collectiveone.model.support.Receiver;

public class PeerReviewedAssignation {
	private Assignation assignation;
	private List<ReceiverData> receiversData = new ArrayList<ReceiverData>();
	private PeerReviewedAssignationState state = PeerReviewedAssignationState.OPEN;
	
	
	private int indexOfReceiverData(String receiverId) {
		for(int ix = 0; ix <= receiversData.size(); ix++) {
			if(receiversData.get(ix).getReceiverId().equals(receiverId)) {
				return ix;
			}
		}
		return -1;
	}
	
	private int indexOfReceiver(String receiverId) {
		for(int ix = 0; ix <= assignation.getReceivers().size(); ix++) {
			if(assignation.getReceivers().get(ix).getUser().getC1Id().equals(receiverId)) {
				return ix;
			}
		}
		return -1;
	}
	
	private int countPendingEvaluators() {
		int count = 0;
		for(Evaluator evaluator : assignation.getEvaluators()) {
			if(evaluator.getState() == EvaluatorState.PENDING ) {
				count++;
			}
		}
		return count;
	}
	
	public void updateState() {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		
		if(assignation.getMaxClosureDate().getTime() > now.getTime()) {
			close();
		} else {
			if(countPendingEvaluators() == 0) {
				if(assignation.getMinClosureDate().getTime() > now.getTime()) {
					close();
				}
			}
		}
	}
	
	private void close() {
		/* First, prepare the receivers array, then, fill it with the evaluations
		 * and their weights, and, finally, combine all evaluations into a final
		 * percent */
		
		for (Receiver receiver : assignation.getReceivers()) {
			ReceiverData receiverComp = new ReceiverData();
			receiverComp.setReceiverId(receiver.getUser().getC1Id().toString());
			receiversData.add(receiverComp);
		}
		
		addEvaluations();
		combineEvaluations();
		updateReceivers();
		
		state = PeerReviewedAssignationState.CLOSED;
	}
	
	private void addEvaluations() {
		/* go over all evaluators and add their evaluations to the 
		 * corresponding receiver */
		for (Evaluator evaluator : assignation.getEvaluators()) {
			
			if(evaluator.getState() == EvaluatorState.DONE) {
			
				/* count the percentage of "not-sures" */
				double percentageSet = 0.0;
				int nEvalsSet = 0;
				
				for (EvaluationGrade grade : evaluator.getGrades()) {
					if(grade.getType() == EvaluationGradeType.SET) {
						nEvalsSet++;
						percentageSet += grade.getPercent();
					} 
				}
				
				double percentNotSure = 100.0 - percentageSet;
				int nEvalsNotSure = evaluator.getGrades().size() - nEvalsSet;
				
				/* add the evaluations (with the corresponding weights) to the receiver */
				for (EvaluationGrade grade : evaluator.getGrades()) {
					
					String receiverId = grade.getReceiver().getUser().getC1Id().toString();
					
					String evaluatorId = evaluator.getUser().getC1Id().toString();
					double weight = 0.0;
					double percent = 0.0;
					
					if(grade.getType() == EvaluationGradeType.SET) {
						percent = grade.getPercent();
						weight = evaluator.getWeight();
					} else {
						/* percent for not sure */
						percent = percentNotSure / (double) nEvalsNotSure;
						/* weight for not sure is penalized */
						weight = evaluator.getWeight() / 3.0;
					}
					
					EvaluationData thisEvaluation = 
							new EvaluationData(evaluatorId, percent, weight);
					
					addEvaluation(receiverId, thisEvaluation);
					
				}
			}
		}
	}
	
	private void combineEvaluations() {
		/* combine all evaluations and weights into a single percentage for each receiver */
		
		/* compute the mean percentage of each receiver (not normalized), may not sum 100% */
		double totalPercentage = 0.0;
		for(ReceiverData  receiver : receiversData) {
			double totalWeight = 0.0;
			for(EvaluationData evaluation : receiver.evaluations) {
				totalWeight += evaluation.weight;
			}
			
			receiver.nonNormalizedPercent = 0.0;
			for(EvaluationData evaluation : receiver.evaluations) {
				receiver.nonNormalizedPercent += evaluation.value * evaluation.weight / totalWeight;
			}
			
			totalPercentage += receiver.nonNormalizedPercent;
		}
		
		/* normalize the percentages */
		double scale = totalPercentage / 100.0;
		for(ReceiverData  receiver : receiversData) {
			receiver.normalizedPercent = receiver.nonNormalizedPercent / scale;
		}
	}
	
	private void updateReceivers() {
		/* copy the normalized percents from receiversData into
		 * assignation receivers */
		for(ReceiverData receiverData : receiversData) {
			this.assignation.getReceivers().get(indexOfReceiver(receiverData.receiverId)).setAssignedPercent(receiverData.normalizedPercent);
		}
	}
	
	private void addEvaluation(String receiverId, EvaluationData evaluation) {
		receiversData.get(indexOfReceiverData(receiverId)).evaluations.add(evaluation);
	}
	
	/* getter/setters */
	
	public Assignation getAssignation() {
		return assignation;
	}

	public void setAssignation(Assignation assignation) {
		this.assignation = assignation;
	}

	public List<ReceiverData> getReceiversData() {
		return receiversData;
	}

	public void setReceiversData(List<ReceiverData> receiverComps) {
		this.receiversData = receiverComps;
	}

	public PeerReviewedAssignationState getState() {
		return state;
	}

	public void setState(PeerReviewedAssignationState state) {
		this.state = state;
	}
	
	


}
