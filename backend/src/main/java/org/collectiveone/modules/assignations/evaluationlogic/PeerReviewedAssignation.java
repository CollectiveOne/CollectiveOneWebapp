package org.collectiveone.modules.assignations.evaluationlogic;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.assignations.Assignation;
import org.collectiveone.modules.assignations.EvaluationGrade;
import org.collectiveone.modules.assignations.EvaluationGradeType;
import org.collectiveone.modules.assignations.Evaluator;
import org.collectiveone.modules.assignations.EvaluatorState;
import org.collectiveone.modules.assignations.Receiver;

public class PeerReviewedAssignation {
	private Assignation assignation;
	private PeerReviewedAssignationState state = PeerReviewedAssignationState.OPEN;
	private List<ReceiverData> receiversData = new ArrayList<ReceiverData>();
	
	
	private int indexOfReceiver(String receiverId) {
		for(int ix = 0; ix < assignation.getReceivers().size(); ix++) {
			if(assignation.getReceivers().get(ix).getId().toString().equals(receiverId)) {
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
		
		if(now.getTime() > assignation.getConfig().getMaxClosureDate().getTime()) {
			close();
		} else {
			if(countPendingEvaluators() == 0) {
				if(now.getTime() > assignation.getConfig().getMinClosureDate().getTime()) {
					close();
				}
			}
		}
	}
	
	private void close() {
		/* First, prepare the receiversData array, then, fill it with the evaluations
		 * and their weights, and, finally, combine all evaluations into a final
		 * percent */
		
		addReceiversEvaluations();
		combineEvaluations();
		updateReceivers();
		
		state = PeerReviewedAssignationState.CLOSED;
	}
	
	private void addReceiversEvaluations() {
		
		/* Update percentages  */
		for (Receiver receiver : assignation.getReceivers()) {
			ReceiverData receiverData = new ReceiverData();
			
			receiverData.setReceiverId(receiver.getId().toString());
			
			List<EvaluationGrade> gradesSet = getReceiverGradesSet(receiver.getId()); 
			
			if (gradesSet.size() == 0) {
				/* no body evaluated this receiver? */
			} else {
			
				/* compute the mean of evaluations set */
				double mean = 0.0;
				double totalWeight = 0.0;
				
				for (EvaluationGrade grade : gradesSet) {
					double thisWeight = grade.getEvaluator().getWeight(); 
					totalWeight += thisWeight;
					mean += grade.getPercent() * thisWeight;
				}
				
				mean = mean / totalWeight;
				
				
				/* update the receiver evaluations */
				List<EvaluationGrade> grades = getReceiverGrades(receiver.getId());
				
				for (EvaluationGrade grade : grades) {
					EvaluationData evaluation = new EvaluationData();
					
					evaluation.setEvaluatorId(grade.getEvaluator().getId().toString());
					evaluation.setWeight(grade.getEvaluator().getWeight());
					
					if (grade.getType() == EvaluationGradeType.SET) {
						evaluation.setValue(grade.getPercent());	
					} else {
						evaluation.setValue(mean);
					}
					
					receiverData.getEvaluations().add(evaluation);
				}
			}
			
			receiversData.add(receiverData);
		}
	}
	
	/** Get all the grades of a receiver */
	private List<EvaluationGrade> getReceiverGrades(UUID receiverId) { 
		
		List<EvaluationGrade> grades = new ArrayList<EvaluationGrade>();
		
		for (Evaluator evaluator : this.assignation.getEvaluators()) {
			for (EvaluationGrade grade : evaluator.getGrades()) {
				if (grade.getReceiver().getId() == receiverId) {
					grades.add(grade);
				}
			}
		}
		
		return grades;
	}
	
	/** Get all the grades of a receiver which are SET (not DONT_KNOW) */
	private List<EvaluationGrade> getReceiverGradesSet(UUID receiverId) { 
		
		List<EvaluationGrade> grades = new ArrayList<EvaluationGrade>();
		
		for (Evaluator evaluator : this.assignation.getEvaluators()) {
			for (EvaluationGrade grade : evaluator.getGrades()) {
				if (grade.getReceiver().getId() == receiverId) {
					if (grade.getType() == EvaluationGradeType.SET) {
						grades.add(grade);
					}
				}
			}
		}
		
		return grades;
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
